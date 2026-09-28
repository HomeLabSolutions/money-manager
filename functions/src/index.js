import {initializeApp} from 'firebase-admin/app';
import {getRemoteConfig} from 'firebase-admin/remote-config';
import {defineSecret} from 'firebase-functions/params';
import {HttpsError, onCall} from 'firebase-functions/v2/https';
import {logger} from 'firebase-functions';
import OpenAI from 'openai';
import {defaultPrompt} from './defaultPrompt.js';
import {validateInsightRequest} from './insightRequest.js';

initializeApp();

const openAiKey = defineSecret('OPENAI_API_KEY');
async function loadPrompt() {
  try {
    const template = await getRemoteConfig().getServerTemplate({
      defaultConfig: {insights_base_prompt: defaultPrompt},
    });
    return template.evaluate().getString('insights_base_prompt') || defaultPrompt;
  } catch {
    return defaultPrompt;
  }
}

export const generateInsight = onCall(
  {region: 'us-central1', timeoutSeconds: 60, maxInstances: 2, secrets: [openAiKey]},
  async (request) => {
    if (!request.auth) {
      throw new HttpsError('unauthenticated', 'Sign in to generate an insight');
    }
    const data = validateInsightRequest(request.data);
    const prompt = await loadPrompt();

    try {
      const openai = new OpenAI({apiKey: openAiKey.value()});
      const response = await openai.responses.create({
        model: 'gpt-6-sol',
        instructions: prompt,
        input: JSON.stringify(data),
        reasoning: {effort: 'low'},
        store: false,
        max_output_tokens: 2000,
      });
      const insight = response.output_text?.trim();
      if (!insight) {
        throw new Error('Empty model response');
      }
      return insight;
    } catch (error) {
      logger.error('Insight generation failed', {
        name: error?.name,
        status: error?.status,
        code: error?.code,
        type: error?.type,
        requestId: error?.request_id,
      });
      throw new HttpsError('internal', 'Could not generate an insight');
    }
  },
);
