package com.rebelity.plugins.btprinter;

import static com.rebelity.plugins.btprinter.Define.*;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

@NativePlugin(
        requestCodes={BTPrinter.REQUEST_BLUETOOTH}
)
public class BTPrinter extends Plugin {
    protected static final int REQUEST_BLUETOOTH = 1990; // Unique request code

    private static final String LOG_TAG = "BTPrinter";


    /**
     * Called when the plugin has been connected to the bridge
     * and is ready to start initializing.
     */
    @Override
    public void load() {
        new com.rebelity.plugins.btprinter.utils.SettingUtil();
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void discoverPrinters(PluginCall call) {
        saveCall(call);
        pluginRequestPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH);
    }

    @PluginMethod()
    public void connectPrinter(PluginCall call) {
        String address = call.getString("address");
        Log.d("address", address);

        com.rebelity.plugins.btprinter.utils.BluetoothUtil.disconnectBlueTooth(getContext());

        Boolean isConnected = false;
        if(!com.rebelity.plugins.btprinter.utils.BluetoothUtil.connectBlueToothByAddress(getContext(), address)){
            com.rebelity.plugins.btprinter.utils.BluetoothUtil.isBlueToothPrinter = false;
            isConnected = false;
        }else{
            com.rebelity.plugins.btprinter.utils.BluetoothUtil.isBlueToothPrinter = true;
            isConnected = true;
        }

        JSObject ret = new JSObject();
        ret.put("results", isConnected);
        call.success(ret);
    }

    @PluginMethod()
    public void disconnectPrinter(PluginCall call) {
        com.rebelity.plugins.btprinter.utils.BluetoothUtil.disconnectBlueTooth(getContext());

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void printString(PluginCall call) {
        String contents = call.getString("contents");
        Boolean isBold = call.getBoolean("is_bold");
        Boolean isUnderLine = call.getBoolean("is_underline");

        Log.d("Contents", contents);
        Log.d("isBold", isBold.toString());

        printStringByBluetooth(contents, isBold, isUnderLine);

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void printBarcode(PluginCall call) {
        String barcode = call.getString("barcode");
        int width = call.getInt("width");
        int height = call.getInt("height");
        Log.d("Barcode", barcode);
        Log.d("width", Integer.toString(width));
        Log.d("height", Integer.toString(height));

        printBarcodeByBluetooth(barcode, width, height);

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void printCommand(PluginCall call) {
        String command = call.getString("command");
        Log.d("Command", command);

        switch (command) {
            case PRINT_ALIGN_LEFT:
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.alignLeft());
                break;
            case PRINT_ALIGN_RIGHT:
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.alignRight());
                break;
            case PRINT_ALIGN_CENTER:
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.alignCenter());
                break;
            case PRINT_NEXT_LINE:
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.nextLine(1));
                break;
        }

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void openCashRegister(PluginCall call) {
        com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.openCasheDrawer());

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);


        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            Log.d("Test", "No stored plugin call for permissions request result");
            return;
        }

        for(int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                Log.d("Test", "User denied permission");
                return;
            }
        }

        if (requestCode == REQUEST_BLUETOOTH) {
            // We got the permission!
            loadDevices(savedCall);
        }
    }

    @Override
    protected void handleOnDestroy() {
        com.rebelity.plugins.btprinter.utils.BluetoothUtil.disconnectBlueTooth(getContext());
    }

    void loadDevices(PluginCall call) {
        ArrayList<Map> deviceList = com.rebelity.plugins.btprinter.utils.BluetoothUtil.loadBluetoothDevices();

        JSONArray jsonArray = new JSONArray(deviceList);
        JSObject ret = new JSObject();
        ret.put("results", jsonArray);
        call.success(ret);
    }

    private void printStringByBluetooth(String content, boolean isBold, boolean isUnderLine) {
        try {
            if (isBold) {
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.boldOn());
            } else {
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.boldOff());
            }

            if (isUnderLine) {
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.underlineWithOneDotWidthOn());
            } else {
                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.underlineOff());
            }

            int recode = com.rebelity.plugins.btprinter.utils.SettingUtil.getInstance().getDefaultStringEncodingIndex();

//            if (recode < 17) {
//                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.singleByte());
//                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.setCodeSystemSingle(codeParse(recode)));
//            } else {
//                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.singleByteOff());
//                com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.setCodeSystem(codeParse(recode)));
//            }

            String stringEncoding = com.rebelity.plugins.btprinter.utils.SettingUtil.getInstance().getStringEncoding(recode);

            com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(content.getBytes(stringEncoding));
            //BluetoothUtil.sendData(ESCUtil.nextLine(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
                res = 0x00;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                res = (byte) (value + 1);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                res = (byte) (value + 8);
                break;
            case 12:
                res = 21;
                break;
            case 13:
                res = 33;
                break;
            case 14:
                res = 34;
                break;
            case 15:
                res = 36;
                break;
            case 16:
                res = 37;
                break;
            case 17:
            case 18:
            case 19:
                res = (byte) (value - 17);
                break;
            case 20:
                res = (byte) 0xff;
                break;
            default:
                break;
        }
        return (byte) res;
    }

    private void printBarcodeByBluetooth(String barcode, int width, int height) {
        int encode = com.rebelity.plugins.btprinter.utils.SettingUtil.getInstance().getDefaultBarcodeEncoding();
        int position = com.rebelity.plugins.btprinter.utils.SettingUtil.getInstance().getDefaultHriPosition();

        com.rebelity.plugins.btprinter.utils.BluetoothUtil.sendData(com.rebelity.plugins.btprinter.utils.ESCUtil.getPrintBarCode(barcode, encode, height, width, position));
    }
}
