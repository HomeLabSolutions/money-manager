import assert from 'node:assert/strict';
import {test} from 'node:test';
import {validateInsightRequest} from './insightRequest.js';

const validRequest = {
  periodStart: '2026-06-30',
  periodEnd: '2026-09-27',
  language: 'en-US',
  traces: {
    Cafe: [{date: '2026-09-27', type: 'expense', currency: 'USD', amount: '400'}],
  },
};

test('accepts category traces', () => {
  assert.deepEqual(validateInsightRequest(validRequest), {...validRequest, previousInsights: []});
});

test('accepts prior insight texts without adding transaction details', () => {
  const request = {...validRequest, previousInsights: ['Food spending rose in July.']};
  assert.deepEqual(validateInsightRequest(request), request);
});

test('rejects periods longer than ninety calendar dates', () => {
  assert.throws(() => validateInsightRequest({...validRequest, periodStart: '2026-06-29'}));
});

test('rejects more than ninety prior insights', () => {
  assert.throws(() => validateInsightRequest({...validRequest, previousInsights: Array(91).fill('Earlier insight')}));
});

test('rejects malformed amounts', () => {
  const traces = {Cafe: [{...validRequest.traces.Cafe[0], amount: 'NaN'}]};
  assert.throws(() => validateInsightRequest({...validRequest, traces}));
});

test('rejects individual transaction details', () => {
  const withNotes = {Cafe: [{...validRequest.traces.Cafe[0], description: 'Private'}]};
  assert.throws(() => validateInsightRequest({...validRequest, traces: withNotes}));
});

test('rejects non-string period dates without coercing them', () => {
  const date = {toString() { throw new Error('Must not coerce'); }};
  for (const field of ['periodStart', 'periodEnd']) {
    assert.throws(() => validateInsightRequest({...validRequest, [field]: date}),
      (error) => error.code === 'invalid-argument');
  }
});

test('rejects impossible calendar dates in periods and daily totals', () => {
  const request = {...validRequest, periodStart: '2026-02-01', periodEnd: '2026-03-01',
    traces: {Cafe: [{...validRequest.traces.Cafe[0], date: '2026-02-30'}]}};
  assert.throws(() => validateInsightRequest({...request, periodStart: '2026-02-30'}));
  assert.throws(() => validateInsightRequest({...request, periodEnd: '2026-02-30'}));
  assert.throws(() => validateInsightRequest(request));
});

test('accepts leap day and a single-day period', () => {
  const request = {...validRequest, periodStart: '2024-02-29', periodEnd: '2024-02-29',
    traces: {Cafe: [{...validRequest.traces.Cafe[0], date: '2024-02-29'}]}};
  assert.deepEqual(validateInsightRequest(request), {...request, previousInsights: []});
});

test('rejects reversed periods', () => {
  assert.throws(() => validateInsightRequest({...validRequest, periodStart: '2026-09-28'}));
});

test('normalizes language and rejects empty or non-string language', () => {
  assert.equal(validateInsightRequest({...validRequest, language: ' en-US '}).language, 'en-US');
  for (const language of ['', '  ', null, 1]) {
    assert.throws(() => validateInsightRequest({...validRequest, language}));
  }
});
