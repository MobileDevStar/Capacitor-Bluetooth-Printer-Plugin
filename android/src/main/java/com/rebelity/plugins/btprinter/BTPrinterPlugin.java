package com.rebelity.plugins.btprinter;

import static com.rebelity.plugins.btprinter.Define.PRINT_ALIGN_CENTER;
import static com.rebelity.plugins.btprinter.Define.PRINT_ALIGN_LEFT;
import static com.rebelity.plugins.btprinter.Define.PRINT_ALIGN_RIGHT;
import static com.rebelity.plugins.btprinter.Define.PRINT_NEXT_LINE;

import android.Manifest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.rebelity.plugins.btprinter.sunmi.SunmiBluetoothUtil;

import org.json.JSONArray;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.S)
@CapacitorPlugin(
        name = "BTPrinter",
        permissions = {
                @Permission(strings = { Manifest.permission.BLUETOOTH }, alias = "bluetooth"),
                @Permission(strings = { Manifest.permission.BLUETOOTH_CONNECT }, alias = "bluetooth_connect"),
                @Permission(strings = { Manifest.permission.INTERNET }, alias = "internet"),
                @Permission(strings = { Manifest.permission.ACCESS_NETWORK_STATE }, alias = "network_state")
        }
    )
public class BTPrinterPlugin extends Plugin {
    protected static final String TAG = "BTPrinter";

    private static final String Innerprinter_Address = "00:11:22:33:44:55";

    private static boolean isSunmiDevice = false;

    /**
     * Called when the plugin has been connected to the bridge
     * and is ready to start initializing.
     */
    @Override
    public void load() {
        new com.rebelity.plugins.btprinter.generic.BluetoothPrinter();
        new com.rebelity.plugins.btprinter.sunmi.SettingUtil();
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");
        Log.d(TAG, "echo: " + value);

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
    }

    @PluginMethod()
    public void discoverPrinters(PluginCall call) {
        loadDevices(call);
    }

    @PluginMethod()
    public void connectPrinter(PluginCall call) {
        String address = call.getString("address");
        Log.d(TAG, "address: " + address);

        Boolean isConnected = false;
        assert address != null;
        isSunmiDevice = address.equalsIgnoreCase(Innerprinter_Address);

        SunmiBluetoothUtil.disconnectBlueTooth(getContext());

        isConnected = SunmiBluetoothUtil.connectBlueToothByAddress(getContext(), address);

        JSObject ret = new JSObject();
        ret.put("results", isConnected);
        call.resolve(ret);
    }

    @PluginMethod()
    public void disconnectPrinter(PluginCall call) {
        SunmiBluetoothUtil.disconnectBlueTooth(getContext());
//        if (isSunmiDevice) {
//            SunmiBluetoothUtil.disconnectBlueTooth(getContext());
//        } else {
//            BluetoothPrinter.getInstance().disconnectBlueTooth();
//        }

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.resolve(ret);
    }

    @PluginMethod()
    public void printString(PluginCall call) {
        String contents = call.getString("contents");
        Boolean isBold = call.getBoolean("is_bold");
        Boolean isUnderLine = call.getBoolean("is_underline");

        printStringBySunmiPrinter(contents, isBold, isUnderLine);

//        if (isSunmiDevice) {
//            printStringBySunmiPrinter(contents, isBold, isUnderLine);
//        } else {
//            BluetoothPrinter.getInstance().printString(contents, isBold, isUnderLine);
//        }

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.resolve(ret);
    }

    @PluginMethod()
    public void printBarcode(PluginCall call) {
        String barcode = call.getString("barcode");
        int width = call.getInt("width");
        int height = call.getInt("height");
        Log.d(TAG, "Barcode: " + barcode);
        Log.d(TAG, "width: " + Integer.toString(width));
        Log.d(TAG, "height: " + Integer.toString(height));

        printBarcodeBySunmiPrinter(barcode, width, height);
//        if (isSunmiDevice) {
//            printBarcodeBySunmiPrinter(barcode, width, height);
//        } else {
//            BluetoothPrinter.getInstance().printBarCode(barcode, height);
//        }

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.resolve(ret);
    }

    @PluginMethod()
    public void printCommand(PluginCall call) {
        String command = call.getString("command");
        Log.d(TAG, "Command: " + command);

        //if (isSunmiDevice) {
        switch (command) {
            case PRINT_ALIGN_LEFT:
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.alignLeft());
                break;
            case PRINT_ALIGN_RIGHT:
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.alignRight());
                break;
            case PRINT_ALIGN_CENTER:
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.alignCenter());
                break;
            case PRINT_NEXT_LINE:
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.nextLine(1));
                break;
        }
//        } else {
//            switch (command) {
//                case PRINT_ALIGN_LEFT:
//                    BluetoothPrinter.getInstance().alignLeft();
//                    break;
//                case PRINT_ALIGN_RIGHT:
//                    BluetoothPrinter.getInstance().alignRight();
//                    break;
//                case PRINT_ALIGN_CENTER:
//                    BluetoothPrinter.getInstance().alignCenter();
//                    break;
//                case PRINT_NEXT_LINE:
//                    BluetoothPrinter.getInstance().nextLine(1);
//                    break;
//            }
//        }


        JSObject ret = new JSObject();
        ret.put("results", true);
        call.resolve(ret);
    }

    @PluginMethod()
    public void openCashRegister(PluginCall call) {
        if (isSunmiDevice) {
            SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.openCasheDrawer());
        } else {
//            BluetoothPrinter.getInstance().openCashDrawer();
            SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.openGenericCasheDrawer());
        }

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.resolve(ret);
    }

    @PluginMethod
    public void getIPAddress(PluginCall call) {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        break;
                    }
                }
                assert ip != null;
                if (ip.length() > 0) {
                    break;
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        Log.e(TAG, "IP ADDRESS: " + ip);

        JSObject ret = new JSObject();
        ret.put("result", ip);
        call.resolve(ret);
    }

    @Override
    protected void handleOnDestroy() {
        SunmiBluetoothUtil.disconnectBlueTooth(getContext());
//        if (isSunmiDevice) {
//            SunmiBluetoothUtil.disconnectBlueTooth(getContext());
//        } else {
//            BluetoothPrinter.getInstance().disconnectBlueTooth();
//        }
    }

    void loadDevices(PluginCall call) {
        ArrayList<Map> deviceList = SunmiBluetoothUtil.loadBluetoothDevices();

        JSONArray jsonArray = new JSONArray(deviceList);
        JSObject ret = new JSObject();
        ret.put("results", jsonArray);
        call.resolve(ret);
    }

    private void printStringBySunmiPrinter(String content, boolean isBold, boolean isUnderLine) {
        try {
            if (isBold) {
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.boldOn());
            } else {
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.boldOff());
            }

            if (isUnderLine) {
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.underlineWithOneDotWidthOn());
            } else {
                SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.underlineOff());
            }


            int recode = com.rebelity.plugins.btprinter.sunmi.SettingUtil.getInstance().getDefaultStringEncodingIndex();

            if (isSunmiDevice) {
                if (recode < 17) {
                    SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.singleByte());
                    SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.setCodeSystemSingle(codeParse(recode)));
                } else {
                    SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.singleByteOff());
                    SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.setCodeSystem(codeParse(recode)));
                }
            }

            String stringEncoding = com.rebelity.plugins.btprinter.sunmi.SettingUtil.getInstance().getStringEncoding(recode);

            SunmiBluetoothUtil.sendData(content.getBytes(stringEncoding));
            //BluetoothUtil.sendData(ESCUtil.nextLine(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
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

    private void printBarcodeBySunmiPrinter(String barcode, int width, int height) {
        int encode = com.rebelity.plugins.btprinter.sunmi.SettingUtil.getInstance().getDefaultBarcodeEncoding();
        int position = com.rebelity.plugins.btprinter.sunmi.SettingUtil.getInstance().getDefaultHriPosition();

        SunmiBluetoothUtil.sendData(com.rebelity.plugins.btprinter.sunmi.ESCUtil.getPrintBarCode(barcode, encode, height, width, position));
    }
}
