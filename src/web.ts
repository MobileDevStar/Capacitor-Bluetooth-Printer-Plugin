import { WebPlugin } from '@capacitor/core';

import type { BTPrinterPlugin } from './definitions';

export class BTPrinterWeb extends WebPlugin implements BTPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
