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
