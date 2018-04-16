package com.kxw.smarthome.utils;

public class SerialPortHandleUtils {

	public static String cmd_success="9CF5A1AA02DC";
	public static String cmd_failed="9CF5A1550287";
	private static StringBuffer sBuffer=null;


	public static String baseData(int pay_proid){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C00000000246");
		return sBuffer.toString();
	}

	public static String payType(int pay_proid){
		sBuffer=new StringBuffer();
		if(pay_proid==0){
			sBuffer.append("96F0C10000000247");
		}else if(pay_proid==1){
			sBuffer.append("96F0C10100000248");
		}
		return sBuffer.toString();
	}

	public static String waterVolume(int quantity){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C2");
		if(quantity>255){
			sBuffer.append(decToHex(quantity));
		}else{
			sBuffer.append("00");
			sBuffer.append(decToHex(quantity));
		}	
		sBuffer.append("00");
		int check_sum=checksum(sBuffer.toString());
		MyLogger.getInstance().e("check_sum = "+check_sum);
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}	
	
	public static String dueTime(int dueTime){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C3");
		if(dueTime>255){
			sBuffer.append(decToHex(dueTime));
		}else{
			sBuffer.append("00");
			sBuffer.append(decToHex(dueTime));
		}
		sBuffer.append("00");
		int check_sum=checksum(sBuffer.toString());
		MyLogger.getInstance().e("check_sum = "+check_sum);
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}
	
/*	public static String waterVolume(String quantity){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C050");
		sBuffer.append(decToHex(Integer.parseInt(quantity)));
		int check_sum=checksum(sBuffer.toString());
		MyLogger.getInstance().e("check_sum = "+check_sum);
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}	

	public static String dueTime(String dueTime){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C051");
		sBuffer.append(decToHex(Integer.parseInt(dueTime)));
		int check_sum=checksum(sBuffer.toString());
		MyLogger.getInstance().e("check_sum = "+check_sum);
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}
	*/
	
	public static String setWaterswitch(boolean isUsing,int temperature){
		sBuffer=new StringBuffer();
		if(isUsing){//on
			sBuffer.append("96F0C4");
			sBuffer.append(decToHex(temperature));
			sBuffer.append("0000");	
			int check_sum=checksum(sBuffer.toString());
			MyLogger.getInstance().e("check_sum = "+check_sum);
			sBuffer.append(decToHex(check_sum/256));
			sBuffer.append(decToHex(check_sum%256));
		}else {
			sBuffer.append("96F0C5FFFFFF0548");
		}		
		return sBuffer.toString();
	}
	
	public static String life(int no,int life){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C6");
		sBuffer.append(decToHex(no));
		if(life>255){
			sBuffer.append(decToHex(life));
		}else{
			sBuffer.append("00");
			sBuffer.append(decToHex(life));
		}
		int check_sum=checksum(sBuffer.toString());
		MyLogger.getInstance().e("check_sum = "+check_sum);
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}
	
	public static String verControl(boolean turnon){
		sBuffer=new StringBuffer();
		if(turnon){
			sBuffer.append("96F0C7010000024E");
		}else{
			sBuffer.append("96F0C7000000024D");
		}
		return sBuffer.toString();
	}


	public static String currentTime(String currentTime){
		sBuffer=new StringBuffer();
		sBuffer.append("96F0C8AA");
		for(int len=0;len<currentTime.length();len+=2){
			MyLogger.getInstance().e(currentTime.substring(len,len+2));
//			sBuffer.append(currentTime.substring(len,len+2));//10进制
			sBuffer.append(decToHex(Integer.parseInt(currentTime.substring(len,len+2))));//转16进制
			MyLogger.getInstance().e(sBuffer.toString());
		}
		int check_sum=checksum(sBuffer.toString());
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}



	
	
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
	 * 16进制string转Bytes
	 */
	public static int checksum(String hexString){
		byte[] src=hexStringToBytes(hexString);
		int getSum=0;
		for (int i = 0; i < src.length; i++) {  			
			int v = src[i] & 0xFF;
			getSum+=v;
		}
		return getSum;	
	}


	/*
	 * 16进制string转Bytes
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	public static String getTestString(String test){
		sBuffer=new StringBuffer();
		sBuffer.append(test);
		int check_sum=checksum(sBuffer.toString());
		sBuffer.append(decToHex(check_sum/256));
		sBuffer.append(decToHex(check_sum%256));
		return sBuffer.toString();
	}
}