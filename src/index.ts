import { registerPlugin } from '@capacitor/core';

import type { BTPrinterPlugin } from './definitions';

const BTPrinter = registerPlugin<BTPrinterPlugin>('BTPrinter', {
  web: () => import('./web').then(m => new m.BTPrinterWeb()),
});

export * from './definitions';
export { BTPrinter };
