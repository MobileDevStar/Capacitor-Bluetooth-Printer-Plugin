# capacitor-plugin-bluetooth-printer

This is a capacitor plugin for the bluetooth printer

## Install

```bash
npm install capacitor-plugin-bluetooth-printer
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`discoverPrinters()`](#discoverprinters)
* [`connectPrinter(...)`](#connectprinter)
* [`disconnectPrinter()`](#disconnectprinter)
* [`printString(...)`](#printstring)
* [`printBarcode(...)`](#printbarcode)
* [`printCommand(...)`](#printcommand)
* [`openCashRegister()`](#opencashregister)
* [`getIPAddress()`](#getipaddress)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### discoverPrinters()

```typescript
discoverPrinters() => Promise<{ results: any[]; }>
```

**Returns:** <code>Promise&lt;{ results: any[]; }&gt;</code>

--------------------


### connectPrinter(...)

```typescript
connectPrinter(options: { address: string; }) => Promise<{ results: boolean; }>
```

| Param         | Type                              |
| ------------- | --------------------------------- |
| **`options`** | <code>{ address: string; }</code> |

**Returns:** <code>Promise&lt;{ results: boolean; }&gt;</code>

--------------------


### disconnectPrinter()

```typescript
disconnectPrinter() => Promise<{ results: boolean; }>
```

**Returns:** <code>Promise&lt;{ results: boolean; }&gt;</code>

--------------------


### printString(...)

```typescript
printString(options: { contents: string; is_bold: boolean; is_underline: boolean; }) => Promise<{ results: boolean; }>
```

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code>{ contents: string; is_bold: boolean; is_underline: boolean; }</code> |

**Returns:** <code>Promise&lt;{ results: boolean; }&gt;</code>

--------------------


### printBarcode(...)

```typescript
printBarcode(options: { barcode: string; width: number; height: number; }) => Promise<{ results: boolean; }>
```

| Param         | Type                                                             |
| ------------- | ---------------------------------------------------------------- |
| **`options`** | <code>{ barcode: string; width: number; height: number; }</code> |

**Returns:** <code>Promise&lt;{ results: boolean; }&gt;</code>

--------------------


### printCommand(...)

```typescript
printCommand(options: { command: string; }) => Promise<{ results: boolean; }>
```

| Param         | Type                              |
| ------------- | --------------------------------- |
| **`options`** | <code>{ command: string; }</code> |

**Returns:** <code>Promise&lt;{ results: boolean; }&gt;</code>

--------------------


### openCashRegister()

```typescript
openCashRegister() => Promise<{ results: boolean; }>
```

**Returns:** <code>Promise&lt;{ results: boolean; }&gt;</code>

--------------------


### getIPAddress()

```typescript
getIPAddress() => Promise<{ result: string; }>
```

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------

</docgen-api>
