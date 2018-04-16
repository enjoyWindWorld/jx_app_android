package com.kxw.smarthome.utils;


/*
 * 数据处理方法
 */
public class DataProcessingUtils{

	/*
	 * 10进制转16进制，高位在前低位在后
	 */
	public static String decToHex(int dec) {
		String hex = "";
		while(dec != 0) {
			String h = Integer.toString(dec & 0xff, 16);
			if((h.length() & 0x01) == 1)
				h = '0' + h;
			hex = h + hex;
			dec = dec >> 8;
		}
		return hex;
	}

	/*
	 * 10进制转16进制，高位在前低位在后

	public static String hex(int dec) {		
	    String hex = "";
	    while(dec != 0) {
	         hex = Integer.toString((dec&0xff00)>>8);
	    }
	    return hex;
	}*/


	/*
	 * 将串口返回的数据转成string
	 */
	public static String bytesToHexString(byte[] src,int len){  
		StringBuilder stringBuilder = new StringBuilder("");  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		int l = len < src.length ? len : src.length ;
		for (int i = 0; i < l; i++) {  
			int v = src[i] & 0xFF;  
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append("0");  
			}  
			stringBuilder.append(hv);  
		}  
		return stringBuilder.toString();  
	}


	private static String decryption;
	private static String encryption;

	/*
	 * des解密方法
	 */
	public static String decode(String string){
		try {
			decryption=Des.decryptDES(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decryption;
	}

	/*
	 * des加密方法
	 */
	public static String encrypt(String string){
		try {
			encryption=Des.encryptDES(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryption;		
	}
}