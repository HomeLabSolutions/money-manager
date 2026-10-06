import {HttpsError} from 'firebase-functions/v2/https';

const datePattern = /^\d{4}-\d{2}-\d{2}$/;
const amountPattern = /^-?\d+(\.\d+)?$/;
const MAX_INSIGHT_DAYS = 90;
const MAX_PREVIOUS_INSIGHTS = MAX_INSIGHT_DAYS;
const MILLIS_PER_DAY = 86_400_000;

function parseDate(value) {
  if (typeof value !== 'string' || !datePattern.test(value)) return NaN;
  const time = Date.parse(`${value}T00:00:00Z`);
  return Number.isFinite(time) && new Date(time).toISOString().slice(0, 10) === value ? time : NaN;
}

export function validateInsightRequest(data) {
  if (!data || typeof data !== 'object' || Array.isArray(data)) {
    throw new HttpsError('invalid-argument', 'Missing insight data');
  }
  const {periodStart, periodEnd, language, traces, previousInsights = []} = data;
  const startTime = parseDate(periodStart);
  const endTime = parseDate(periodEnd);
  const periodDays = (endTime - startTime) / MILLIS_PER_DAY + 1;
  const normalizedLanguage = typeof language === 'string' ? language.trim() : '';
  if (Object.keys(data).some((key) => !['periodStart', 'periodEnd', 'language', 'traces', 'previousInsights'].includes(key))) {
    throw new HttpsError('invalid-argument', 'Unexpected insight data');
  }
  if (
    !Number.isFinite(startTime) || !Number.isFinite(endTime) ||
    periodDays < 1 || periodDays > MAX_INSIGHT_DAYS ||
    !normalizedLanguage || normalizedLanguage.length > 35 ||
    !Array.isArray(previousInsights) || previousInsights.length > MAX_PREVIOUS_INSIGHTS ||
    previousInsights.some((insight) => typeof insight !== 'string' || insight.length > 1000) ||
    !traces || typeof traces !== 'object' || Array.isArray(traces) ||
    Object.keys(traces).length === 0 || Object.keys(traces).length > 4000
  ) {
    throw new HttpsError('invalid-argument', 'Invalid insight period or traces');
  }
  let dayCount = 0;
  for (const [category, days] of Object.entries(traces)) {
    if (!category || category.length > 120 || !Array.isArray(days) || days.length === 0) {
      throw new HttpsError('invalid-argument', 'Invalid category trace');
    }
    dayCount += days.length;
    if (dayCount > 4000) {
      throw new HttpsError('invalid-argument', 'Too many daily totals');
    }
    for (const day of days) {
      if (
        !day || typeof day !== 'object' || Array.isArray(day) ||
        Object.keys(day).some((key) => !['date', 'type', 'currency', 'amount'].includes(key)) ||
        !Number.isFinite(parseDate(day.date)) ||
        day.date < periodStart || day.date > periodEnd ||
        !['income', 'expense'].includes(day.type) ||
        typeof day.currency !== 'string' || !/^[A-Z]{3}$/.test(day.currency) ||
        typeof day.amount !== 'string' || day.amount.length > 40 || !amountPattern.test(day.amount)
      ) {
        throw new HttpsError('invalid-argument', 'Invalid daily total');
      }
    }
  }
  if (JSON.stringify(data).length > 500_000) {
    throw new HttpsError('invalid-argument', 'Insight data is too large');
  }
  return {periodStart, periodEnd, language: normalizedLanguage, traces, previousInsights};
}
