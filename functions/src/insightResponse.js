const MAX_INSIGHT_TEXT_LENGTH = 1000;

export function validateInsightResponse(response) {
  if (response?.status !== 'completed' || typeof response.output_text !== 'string') {
    throw new Error('Incomplete or invalid model response');
  }
  const insight = response.output_text.trim();
  if (!insight || insight.length > MAX_INSIGHT_TEXT_LENGTH) {
    throw new Error('Invalid insight text');
  }
  return insight;
}
