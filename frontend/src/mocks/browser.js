// src/mocks/browser.js
import { setupWorker } from 'msw';
import { handlers } from './index';

export const worker = setupWorker(...handlers);
