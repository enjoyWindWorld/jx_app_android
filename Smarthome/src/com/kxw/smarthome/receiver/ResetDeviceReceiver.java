/*
 * 恢复出厂设置广播
*/
package com.kxw.smarthome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.Utils;


public class ResetDeviceReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private UnbindThread mUnbindThread;
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		// TODO Auto-generated method stub  
		if(intent.getAction().equals(ConfigUtils.reset_device_alarm)){	
			if(mUnbindThread!=null){
				mUnbindThread.interrupt();
				mUnbindThread=null;
			}	
			mUnbindThread = new UnbindThread();
			mUnbindThread.start();
		}	
	}
	
	private class UnbindThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (true) {
				if(!Utils.inuse){//串口没有使用
					mSerialPortUtil =MyApplication.getSerialPortUtil();
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						setResult = mSerialPortUtil.setUnbind();
						MyLogger.getInstance().e("  setResult = "+setResult);	
						if(setResult<0){
							//faile to sent data	
							MyLogger.getInstance().e(" faile to sent data ");	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						MyLogger.getInstance().e("getReturn  returnsResult = "+returnsResult);	
						if(returnsResult>=0){
							MyLogger.getInstance().e(" set  reset success  ");	
							break;
						}else{
							times++;
							MyLogger.getInstance().e(" try times =   "+ times);
						}
					}while(times < 3);
					Utils.inuse = false;
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}