export interface BTPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
