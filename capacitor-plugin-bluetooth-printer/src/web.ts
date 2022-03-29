import { WebPlugin } from '@capacitor/core';
import { BluetoothPrinterPlugin } from './definitions';

export class BTPrinterWeb extends WebPlugin implements BluetoothPrinterPlugin {
  constructor() {
    super({
      name: 'BTPrinter',
      platforms: ['web'],
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async discoverPrinters(): Promise<{ results: any[] }> {
    return {
      results: [{
        name: 'Dummy1',
        address: '123456'
      },{
        name: 'Dummy2',
        address: '456789'
      }]
    };
  }

  async connectPrinter(options: {address: string}): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);
    return {
      results: true
    };
  }

  async disconnectPrinter(): Promise<{ results: boolean }> {
    return {
      results: true
    };
  }

  async printString( options: { contents: string, is_bold: boolean, is_underline: boolean } ): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);
    
    return {
      results: true
    };
  }

  async printBarcode(options: {barcode: string, width: number, height: number }): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);

    return {
      results: true
    };
  }

  async printCommand(options: {command: string}): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);

    return {
      results: true
    };
  }

  async openCashRegister(): Promise<{ results: boolean }> {
    return {
      results: true
    };
  }
}

const BTPrinter = new BTPrinterWeb();

export { BTPrinter };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BTPrinter);
