import assert from 'node:assert/strict';
import {test} from 'node:test';
import {validateInsightResponse} from './insightResponse.js';

test('returns trimmed completed insight', () => {
  assert.equal(validateInsightResponse({status: 'completed', output_text: ' Food spending rose. '}),
    'Food spending rose.');
});

test('rejects incomplete responses even when partial text is present', () => {
  assert.throws(() => validateInsightResponse({status: 'incomplete', output_text: 'Food spending rose'}));
});

test('rejects missing, empty and non-string text', () => {
  for (const output_text of [undefined, null, 1, '', '  ']) {
    assert.throws(() => validateInsightResponse({status: 'completed', output_text}));
  }
});

test('matches the Android insight length limit', () => {
  assert.equal(validateInsightResponse({status: 'completed', output_text: 'a'.repeat(1000)}).length, 1000);
  assert.throws(() => validateInsightResponse({status: 'completed', output_text: 'a'.repeat(1001)}));
});
