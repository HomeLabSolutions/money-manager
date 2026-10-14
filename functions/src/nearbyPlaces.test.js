import assert from 'node:assert/strict';
import {test} from 'node:test';
import {createNearbyPlacesHandler, nearbyPlacesUrl, parseNearbyPlacesResponse, validateNearbyPlacesRequest} from './nearbyPlaces.js';

const location = {latitude: 51.5, longitude: -0.1, language: 'en'};
const feature = (id, name, distance) => ({properties: {place_id: id, name, distance, formatted: 'High Street'}});

test('validates coordinates and rejects unexpected input', () => {
  assert.deepEqual(validateNearbyPlacesRequest(location), location);
  for (const data of [null, [], {...location, latitude: '51.5'}, {...location, latitude: NaN},
    {...location, latitude: 91}, {...location, longitude: -181}, {...location, language: 'ru-RU'},
    {...location, radius: 500}]) {
    assert.throws(() => validateNearbyPlacesRequest(data), (error) => error.code === 'invalid-argument');
  }
});

test('fixes radius and limit server-side and uses longitude first for proximity ranking', () => {
  const url = nearbyPlacesUrl(location, 'test-key');
  assert.equal(url.origin, 'https://api.geoapify.com');
  assert.equal(url.pathname, '/v2/places');
  assert.equal(url.searchParams.get('filter'), 'circle:-0.1,51.5,100');
  assert.equal(url.searchParams.get('bias'), 'proximity:-0.1,51.5');
  assert.equal(url.searchParams.get('limit'), '20');
  assert.equal(url.searchParams.get('categories'), 'catering,commercial');
  assert.equal(url.searchParams.get('conditions'), 'named');
  assert.equal(url.searchParams.get('apiKey'), 'test-key');
});

test('returns named places in distance order within 100 meters and removes duplicates', () => {
  const response = parseNearbyPlacesResponse({features: [feature('far', 'Shop', 90),
    feature('near', ' Cafe ', 10.5), feature('near', 'Cafe', 20), feature('outside', 'Shop', 101),
    feature('empty', ' ', 5), feature('', 'Cafe', 0), feature('invalid', 'Cafe', NaN)]}, location);
  assert.deepEqual(response.places.map((place) => [place.id, place.name, place.distanceMeters]),
    [['near', 'Cafe', 10.5], ['far', 'Shop', 90]]);
});

test('calculates distance when upstream omits it and caps the result at 20', () => {
  const features = Array.from({length: 25}, (_, index) => feature(String(index), 'Shop', index));
  features.push({properties: {place_id: 'same-point', name: 'Cafe', lat: 51.5, lon: -0.1}});
  const response = parseNearbyPlacesResponse({features}, location);
  assert.equal(response.places.length, 20);
  assert.equal(response.places.find((place) => place.id === 'same-point').distanceMeters, 0);
});

test('requires authentication before calling Geoapify', async () => {
  const handler = createNearbyPlacesHandler(() => { throw new Error('Must not read key'); });
  await assert.rejects(handler({data: location}), (error) => error.code === 'unauthenticated');
});

test('calls Geoapify once and accepts an empty result', async () => {
  let calls = 0;
  const handler = createNearbyPlacesHandler(() => 'test-key', async (url, options) => {
    calls++;
    assert.equal(url.searchParams.get('lang'), 'en');
    assert.ok(options.signal instanceof AbortSignal);
    return {ok: true, json: async () => ({features: []})};
  });
  assert.deepEqual(await handler({auth: {uid: 'user'}, data: location}), {places: []});
  assert.equal(calls, 1);
});

test('upstream failures never expose the key or request URL', async () => {
  const handler = createNearbyPlacesHandler(() => 'private-key', async () => {
    throw new Error('https://api.geoapify.com/?apiKey=private-key');
  });
  await assert.rejects(handler({auth: {uid: 'user'}, data: location}), (error) => {
    assert.equal(error.code, 'unavailable');
    assert.ok(!error.message.includes('private-key'));
    return true;
  });
});

test('malformed responses and unsuccessful HTTP status become unavailable', async () => {
  for (const response of [{ok: false}, {ok: true, json: async () => ({error: 'Bad data'})}]) {
    const handler = createNearbyPlacesHandler(() => 'test-key', async () => response);
    await assert.rejects(handler({auth: {uid: 'user'}, data: location}), (error) => error.code === 'unavailable');
  }
});
