/*
 * 恢复出厂设置广播
*/
package com.kxw.smarthome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.Utils;


public class ResetDeviceReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private Handler mUnbindHandler;  
	private HandlerThread mUnbindHandlerThread;  
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		// TODO Auto-generated method stub  
		if(intent.getAction().equals(ConfigUtils.reset_device_alarm)){	
			
			mUnbindHandlerThread = new HandlerThread("ResetDeviceReceiver_mUnbindHandlerThread", 5);  
			mUnbindHandlerThread.start();  
			mUnbindHandler = new Handler(mUnbindHandlerThread.getLooper()); 
			mUnbindHandler.post(mUnbindRunnable);
		}	
	}
	
	private Runnable mUnbindRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if(!Utils.inuse){//串口没有使用
					mSerialPortUtil =MyApplication.getSerialPortUtil();
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						setResult = mSerialPortUtil.setUnbind();
						if(setResult<0){
							//faile to sent data	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						if(returnsResult>=0){
							break;
						}else{
							times++;
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
	}; 
}