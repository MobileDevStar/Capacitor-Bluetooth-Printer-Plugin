package com.rebelity.plugins.btprinter.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

//commands
public class ESCUtil {

	public static final byte ESC = 0x1B;// Escape
	public static final byte FS =  0x1C;// Text delimiter
	public static final byte GS =  0x1D;// Group separator
	public static final byte DLE = 0x10;// data link escape
	public static final byte EOT = 0x04;// End of transmission
	public static final byte ENQ = 0x05;// Enquiry character
	public static final byte SP =  0x20;// Spaces
	public static final byte HT =  0x09;// Horizontal list
	public static final byte LF =  0x0A;//Print and wrap (horizontal orientation)
	public static final byte CR =  0x0D;// Home key
	public static final byte FF =  0x0C;// Carriage control (print and return to the standard mode (in page mode))
	public static final byte CAN = 0x18;// Canceled (cancel print data in page mode)

	public static final byte LINE_FEED = 0x0A;
	public static final byte[] CODIFICATION = new byte[] { 0x1b, 0x4D, 0x01 };

	public static final byte[] ESC_ALIGN_LEFT = { 0x1B, 0x61, 0x00 };
	public static final byte[] ESC_ALIGN_RIGHT = { 0x1B, 0x61, 0x02 };
	public static final byte[] ESC_ALIGN_CENTER = { 0x1B, 0x61, 0x01 };

	public static final byte[] CHAR_SIZE_00 = { 0x1B, 0x21, 0x00 };// Normal size
	public static final byte[] CHAR_SIZE_01 = { 0x1B, 0x21, 0x01 };// Reduzided width
	public static final byte[] CHAR_SIZE_08 = { 0x1B, 0x21, 0x08 };// bold normal size
	public static final byte[] CHAR_SIZE_10 = { 0x1B, 0x21, 0x10 };// Double height size
	public static final byte[] CHAR_SIZE_11 = { 0x1B, 0x21, 0x11 };// Reduzided Double height size
	public static final byte[] CHAR_SIZE_20 = { 0x1B, 0x21, 0x20 };// Double width size
	public static final byte[] CHAR_SIZE_30 = { 0x1B, 0x21, 0x30 };
	public static final byte[] CHAR_SIZE_31 = { 0x1B, 0x21, 0x31 };
	public static final byte[] CHAR_SIZE_51 = { 0x1B, 0x21, 0x51 };
	public static final byte[] CHAR_SIZE_61 = { 0x1B, 0x21, 0x61 };

	public static final byte[] UNDERL_OFF = { 0x1b, 0x2d, 0x00 }; // Underline font OFF
	public static final byte[] UNDERL_ON = { 0x1b, 0x2d, 0x01 }; // Underline font 1-dot ON
	public static final byte[] UNDERL2_ON = { 0x1b, 0x2d, 0x02 }; // Underline font 2-dot ON
	public static final byte[] BOLD_OFF = { 0x1b, 0x45, 0x00 }; // Bold font OFF
	public static final byte[] BOLD_ON = { 0x1b, 0x45, 0x01 }; // Bold font ON
	public static final byte[] FONT_A = { 0x1b, 0x4d, 0x00 }; // Font type A
	public static final byte[] FONT_B = { 0x1b, 0x4d, 0x01 }; // Font type B

	public static final byte[] BARCODE_UPC_A = { 0x1D, 0x6B, 0x00 };
	public static final byte[] BARCODE_UPC_E = { 0x1D, 0x6B, 0x01 };
	public static final byte[] BARCODE_EAN13 = { 0x1D, 0x6B, 0x02 };
	public static final byte[] BARCODE_EAN8 = { 0x1D, 0x6B, 0x03 };
	public static final byte[] BARCODE_CODE39 = { 0x1D, 0x6B, 0x04 };
	public static final byte[] BARCODE_ITF = { 0x1D, 0x6B, 0x05 };
	public static final byte[] BARCODE_CODABAR = { 0x1D, 0x6B, 0x06 };

	public static final byte[] OPEN_CASH_DRAWER = { 0x1B, 0x70, 0x00, 0x19, (byte)0xFA };

	/**
	 * print barcode
	 */
	public static byte[] getPrintBarCode(String data, int symbology, int height, int width, int textposition){
		if(symbology < 0 || symbology > 10){
			return new byte[]{LF};
		}

		if(width < 2 || width > 6){
			width = 2;
		}

		if(textposition <0 || textposition > 3){
			textposition = 0;
		}

		if(height < 1 || height>255){
			height = 162;
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try{
			buffer.write(new byte[]{0x1D,0x66,0x01,0x1D,0x48,(byte)textposition,
					0x1D,0x77,(byte)width,0x1D,0x68,(byte)height,0x0A});

			buffer.write(new byte[]{0x1D,0x6B,(byte)(symbology)});
			buffer.write(data.getBytes());
			buffer.write(0x00);

		}catch(Exception e){
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}

	/**
	 * 跳指定行数
	 */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LINE_FEED;
        }

        return result;
    }

    // ------------------------style set-----------------------------
	//设置默认行间距
	public static byte[] setDefaultLineSpace(){
		return new byte[]{0x1B, 0x32};
	}

	//设置行间距
	public static byte[] setLineSpace(int height){
    	return new byte[]{0x1B, 0x33, (byte) height};
	}

	// ------------------------underline-----------------------------
	//设置下划线1点
	public static byte[] underlineWithOneDotWidthOn() {
		return UNDERL_ON;
	}

	//设置下划线2点
	public static byte[] underlineWithTwoDotWidthOn() {
		return UNDERL2_ON;
	}

	//取消下划线
	public static byte[] underlineOff() {
		return UNDERL_OFF;
	}

	// ------------------------bold-----------------------------
	/**
	 * 字体加粗
	 */
	public static byte[] boldOn() {
		return BOLD_ON;
	}

	/**
	 * 取消字体加粗
	 */
	public static byte[] boldOff() {
		return BOLD_OFF;
	}

	// ------------------------character-----------------------------
	/*
	*单字节模式开启
	 */
	public static byte[] singleByte(){
		byte[] result = new byte[2];
		result[0] = FS;
		result[1] = 0x2E;
		return result;
	}

	/*
	*单字节模式关闭
 	*/
	public static byte[] singleByteOff(){
		byte[] result = new byte[2];
		result[0] = FS;
		result[1] = 0x26;
		return result;
	}

	/**
	 * 设置单字节字符集
	 */
	public static byte[] setCodeSystemSingle(byte charset){
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 0x74;
		result[2] = charset;
		return result;
	}

	/**
	 * 设置多字节字符集
	 */
	public static byte[] setCodeSystem(byte charset){
		byte[] result = new byte[3];
		result[0] = FS;
		result[1] = 0x43;
		result[2] = charset;
		return result;
	}

	// ------------------------Align-----------------------------

	/**
	 * 居左
	 */
	public static byte[] alignLeft() {
		return ESC_ALIGN_LEFT;
	}

	/**
	 * 居中对齐
	 */
	public static byte[] alignCenter() {
		return ESC_ALIGN_CENTER;
	}

	/**
	 * 居右
	 */
	public static byte[] alignRight() {
		return ESC_ALIGN_RIGHT;
	}

	//切刀
	public static byte[] cutter() {
		byte[] data = new byte[]{0x1d, 0x56, 0x01};
		return data;
	}

	//走纸到黑标
	public static byte[] gogogo(){
		byte[] data = new byte[]{0x1C, 0x28, 0x4C, 0x02, 0x00, 0x42, 0x31};
		return data;
	}

	// open cash drawer
	public static byte[] openCasheDrawer(){
		return OPEN_CASH_DRAWER;
	}

	////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////          private                /////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	private static byte[] setQRCodeSize(int modulesize){
		//二维码块大小设置指令
		byte[] dtmp = new byte[8];
		dtmp[0] = GS;
		dtmp[1] = 0x28;
		dtmp[2] = 0x6B;
		dtmp[3] = 0x03;
		dtmp[4] = 0x00;
		dtmp[5] = 0x31;
		dtmp[6] = 0x43;
		dtmp[7] = (byte)modulesize;
		return dtmp;
	}

	private static byte[] setQRCodeErrorLevel(int errorlevel){
		//二维码纠错等级设置指令
		byte[] dtmp = new byte[8];
		dtmp[0] = GS;
		dtmp[1] = 0x28;
		dtmp[2] = 0x6B;
		dtmp[3] = 0x03;
		dtmp[4] = 0x00;
		dtmp[5] = 0x31;
		dtmp[6] = 0x45;
		dtmp[7] = (byte)(48+errorlevel);
		return dtmp;
	}


	private static byte[] getBytesForPrintQRCode(boolean single){
		//打印已存入数据的二维码
		byte[] dtmp;
		if(single){		//同一行只打印一个QRCode， 后面加换行
			dtmp = new byte[9];
			dtmp[8] = 0x0A;
		}else{
			dtmp = new byte[8];
		}
		dtmp[0] = 0x1D;
		dtmp[1] = 0x28;
		dtmp[2] = 0x6B;
		dtmp[3] = 0x03;
		dtmp[4] = 0x00;
		dtmp[5] = 0x31;
		dtmp[6] = 0x51;
		dtmp[7] = 0x30;
		return dtmp;
	}

	private static byte[] getQCodeBytes(String code) {
		//二维码存入指令
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			byte[] d = code.getBytes("GB18030");
			int len = d.length + 3;
			if (len > 7092) len = 7092;
			buffer.write((byte) 0x1D);
			buffer.write((byte) 0x28);
			buffer.write((byte) 0x6B);
			buffer.write((byte) len);
			buffer.write((byte) (len >> 8));
			buffer.write((byte) 0x31);
			buffer.write((byte) 0x50);
			buffer.write((byte) 0x30);
			for (int i = 0; i < d.length && i < len; i++) {
				buffer.write(d[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}
}