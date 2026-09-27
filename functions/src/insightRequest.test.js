import assert from 'node:assert/strict';
import {test} from 'node:test';
import {validateInsightRequest} from './insightRequest.js';

const validRequest = {
  periodStart: '2025-09-27',
  periodEnd: '2026-09-27',
  language: 'en-US',
  traces: {
    Cafe: [{date: '2026-09-27', type: 'expense', currency: 'USD', amount: '400'}],
  },
};

test('accepts category traces', () => {
  assert.deepEqual(validateInsightRequest(validRequest), validRequest);
});

test('rejects malformed amounts', () => {
  const traces = {Cafe: [{...validRequest.traces.Cafe[0], amount: 'NaN'}]};
  assert.throws(() => validateInsightRequest({...validRequest, traces}));
});

test('rejects individual transaction details', () => {
  const withNotes = {Cafe: [{...validRequest.traces.Cafe[0], description: 'Private'}]};
  assert.throws(() => validateInsightRequest({...validRequest, traces: withNotes}));
});
