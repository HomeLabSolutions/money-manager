import {HttpsError} from 'firebase-functions/v2/https';

const RADIUS_METERS = 150;
const RESULT_LIMIT = 20;
const EARTH_RADIUS_METERS = 6_371_000;

export function validateNearbyPlacesRequest(data) {
  if (!data || typeof data !== 'object' || Array.isArray(data) ||
      Object.keys(data).some((key) => !['latitude', 'longitude', 'language'].includes(key))) {
    throw new HttpsError('invalid-argument', 'Invalid nearby places request');
  }
  const {latitude, longitude, language = 'en'} = data;
  if (!Number.isFinite(latitude) || latitude < -90 || latitude > 90 ||
      !Number.isFinite(longitude) || longitude < -180 || longitude > 180 ||
      typeof language !== 'string' || !/^[a-z]{2,3}$/.test(language)) {
    throw new HttpsError('invalid-argument', 'Invalid coordinates or language');
  }
  return {latitude, longitude, language};
}

export function nearbyPlacesUrl(location, apiKey) {
  const {latitude, longitude, language} = location;
  const url = new URL('https://api.geoapify.com/v2/places');
  url.search = new URLSearchParams({
    categories: 'catering,commercial',
    conditions: 'named',
    filter: `circle:${longitude},${latitude},${RADIUS_METERS}`,
    bias: `proximity:${longitude},${latitude}`,
    limit: String(RESULT_LIMIT),
    lang: language,
    apiKey,
  }).toString();
  return url;
}

function distanceFrom(location, properties) {
  if (Number.isFinite(properties.distance) && properties.distance >= 0) return properties.distance;
  if (!Number.isFinite(properties.lat) || !Number.isFinite(properties.lon)) return NaN;
  const radians = (degrees) => degrees * Math.PI / 180;
  const latitudeDelta = radians(properties.lat - location.latitude);
  const longitudeDelta = radians(properties.lon - location.longitude);
  const a = Math.sin(latitudeDelta / 2) ** 2 +
    Math.cos(radians(location.latitude)) * Math.cos(radians(properties.lat)) *
    Math.sin(longitudeDelta / 2) ** 2;
  return 2 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(Math.min(1, a)));
}

export function parseNearbyPlacesResponse(data, location) {
  if (!data || !Array.isArray(data.features)) throw new Error('Invalid places response');
  const places = new Map();
  for (const feature of data.features) {
    const properties = feature?.properties;
    if (!properties || typeof properties.place_id !== 'string' || !properties.place_id.trim() ||
        typeof properties.name !== 'string' || !properties.name.trim()) continue;
    const distance = distanceFrom(location, properties);
    if (!Number.isFinite(distance) || distance < 0 || distance > RADIUS_METERS) continue;
    const place = {
      id: properties.place_id,
      name: properties.name.trim(),
      address: typeof properties.formatted === 'string' ? properties.formatted : '',
      distanceMeters: distance,
    };
    if (!places.has(place.id) || places.get(place.id).distanceMeters > distance) places.set(place.id, place);
  }
  return {places: [...places.values()].sort((a, b) => a.distanceMeters - b.distanceMeters).slice(0, RESULT_LIMIT)};
}

export function createNearbyPlacesHandler(apiKeyProvider, fetchImpl = fetch) {
  return async (request) => {
    if (!request.auth) throw new HttpsError('unauthenticated', 'Sign in to find nearby places');
    const location = validateNearbyPlacesRequest(request.data);
    try {
      const response = await fetchImpl(nearbyPlacesUrl(location, apiKeyProvider()), {
        signal: AbortSignal.timeout(10_000),
      });
      if (!response.ok) throw new Error('Places service unavailable');
      return parseNearbyPlacesResponse(await response.json(), location);
    } catch {
      // Never include the upstream URL: it contains the server API key and coordinates.
      throw new HttpsError('unavailable', 'Could not find nearby places');
    }
  };
}
