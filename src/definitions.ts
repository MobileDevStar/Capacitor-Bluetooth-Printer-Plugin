export interface BTPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  discoverPrinters(): Promise<{results: any[]}>;
  connectPrinter(options: {address: string}): Promise<{results: boolean}>;
  disconnectPrinter(): Promise<{results: boolean}>;
  printString(options: {contents: string, is_bold: boolean, is_underline: boolean}): Promise<{results: boolean}>;
  printBarcode(options: {barcode: string, width: number, height: number}): Promise<{results: boolean}>;
  printCommand(options: {command: string}): Promise<{results: boolean}>;
  openCashRegister(): Promise<{results: boolean}>;
}
