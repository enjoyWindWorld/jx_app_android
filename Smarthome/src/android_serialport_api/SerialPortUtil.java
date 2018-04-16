package android_serialport_api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.WaterStateInfo;
import com.kxw.smarthome.utils.MyLogger;

/**
 * 串口操作类
 */
public class SerialPortUtil {
	private String TAG = SerialPortUtil.class.getSimpleName();
	public SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private String path = "/dev/ttyS1";
	private int baudrate = 4800;
	private static SerialPortUtil portUtil;
	private OnDataReceiveListener onDataReceiveListener = null;
	private boolean isStop = false;
	byte[] mBuffer;

	private BaseData mBaseData;
	private WaterStateInfo mWaterStateInfo;
	
	public interface OnDataReceiveListener {
		public void onDataReceive(byte[] buffer, int size);
	}

	public void setOnDataReceiveListener(
			OnDataReceiveListener dataReceiveListener) {
		onDataReceiveListener = dataReceiveListener;
	}

	public static SerialPortUtil getInstance() {
		if (null == portUtil) {
			portUtil = new SerialPortUtil();
			portUtil.onCreate();
		}
		return portUtil;
	}
	
	/**
	 * 重新初始化串口
	 * @return
	 */
	public SerialPortUtil getNewInstance()
	{
		portUtil = new SerialPortUtil();
		portUtil.onCreate();
		return portUtil;
	}

	/**
	 * 初始化串口信息
	 */
	public void onCreate() {
		try {			
				mSerialPort = new SerialPort(new File(path), baudrate,1);
				mOutputStream = mSerialPort.getOutputStream();
				mInputStream = mSerialPort.getInputStream();
				mBaseData = mSerialPort.mBaseData;
				mWaterStateInfo = mSerialPort.mWaterStateInfo;
				isStop = false;
				//			mReadThread = new ReadThread();			
				//			mReadThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 发送指令到串口
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean sendString(String cmd) {
		boolean result = true;
		MyLogger.getInstance().e("  sendString  = " +cmd);
		byte[] mBuffer = hexStringToBytes(cmd);
		try {
			if (mOutputStream != null) {
				mOutputStream.write(mBuffer);
			} else {
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		MyLogger.getInstance().e("  result  = " +result);
		return result;
	}

	public boolean sendBuffer(byte[] mBuffer) {
		MyLogger.getInstance().e(" sendBuffer is "+ mBuffer);
		boolean result = true;
		try {
			if (mOutputStream != null) {
				mOutputStream.write(mBuffer);
			} else {
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isStop && !isInterrupted()) {
				int size;
				try {
					if (mInputStream == null)
						return;
					byte[] buffer = new byte[512];
					size = mInputStream.read(buffer);
					if (size > 0) {										
						if (null != onDataReceiveListener) {
							onDataReceiveListener.onDataReceive(buffer, size);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	public static String bytesToHexString(byte[] src,int len){  
		StringBuilder stringBuilder = new StringBuilder("");  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		int l = len < src.length ? len : src.length ;
		for (int i = 0; i < l; i++) {  
			int v = src[i] & 0xFF;  
			//			Log.e("bytesToHexString ", "int v is:"+v);
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append("0");  
			}
			stringBuilder.append(hv);  
			//stringBuilder.append(" "); 
		}  
		return stringBuilder.toString();  
	}

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

	/**
	 * 关闭串口
	 */
	public void closeSerialPort() {
		isStop = true;
		if (mReadThread != null) {
			mReadThread.interrupt();
		}
		if (mSerialPort != null) {
			mSerialPort.close();
		}
	}


	public void close() {
		if (mSerialPort != null) {
			MyLogger.getInstance().e("mSerialPort close!");
			mSerialPort.close();
		}
	}

	public BaseData returnBaseData(){		
		return mBaseData;
	}

	public WaterStateInfo returnWaterStateInfo(){		
		return mWaterStateInfo;
	}

	public int setBaseData(){
		MyLogger.getInstance().e(mBaseData);
		return mSerialPort.setBaseData(mBaseData);
	}

	public int setPayType(int type){		
		return mSerialPort.setPayType(type);
	}

	public int setWaterVolume(int volume){		
		return mSerialPort.setWaterVolume(volume);
	}

	public int setDueTime(int day){		
		return mSerialPort.setDueTime(day);
	}

	public int setWaterSwitch(boolean isUsing,int temperature){		
		return mSerialPort.setWaterSwitch(isUsing,temperature);
	}

	public int setFilterLife(int[] life,int len){		
		return mSerialPort.setFilterLife(life,len);
	}

	public int setVerSwitch(boolean turnon){		
		return mSerialPort.setVerSwitch(turnon);
	}

	public int setCurrentTime(String currentTime){		
		return mSerialPort.setCurrentTime(currentTime);
	}

	public int getReturn(){		
		return mSerialPort.getReturn();
	}

	public  FileDescriptor getFileDescriptor(){		
		return mSerialPort.getFileDescriptor();
	}

	public int getBaseData(){		
		return mSerialPort.getBaseData(mBaseData);
	}
	
	public int setUnbind(){		
		return mSerialPort.setUnbind();
	}
	
	public int getWaterState(){		
		return mSerialPort.getWaterState(mWaterStateInfo);
	}
	
	public int setWaterState(){
		MyLogger.getInstance().e(mWaterStateInfo);
		return mSerialPort.setWaterState(mWaterStateInfo);
	}
	
	public int setTemperatureCorrectionPlus(int temperature){		
		return mSerialPort.setTemperatureCorrectionPlus(temperature);
	}
	
	public int setTemperatureCorrectionReduce(int temperature){	
		return mSerialPort.setTemperatureCorrectionReduce(temperature);
	}
	
	public int setFlowCorrectionPlus(int flow){		
		return mSerialPort.setFlowCorrectionPlus(flow);
	}
	
	public int setFlowCorrectionReduce(int flow){		
		return mSerialPort.setFlowCorrectionReduce(flow);
	}
}
