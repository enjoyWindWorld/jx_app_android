/*
 * 时间校准广播
*/
package com.kxw.smarthome.receiver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.Utils;

public class UpdateTimeReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private TimeThread mTimeThread;
	private String time=null;
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		if(intent.getAction().equals(ConfigUtils.update_time_alarm)){
			
			GetNetTime();
		}
	}
	
	public void GetNetTime()  {

		new Thread() {
			public void run() {
				URL infoUrl = null;  

				try {
					TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00")); // 时区设置  
//					infoUrl = new URL("http://time.tianqi.com/");  
					infoUrl = new URL("https://www.baidu.com/"); 
					HttpURLConnection conn = (HttpURLConnection) infoUrl.openConnection();			
					conn.connect();
					long ld = conn.getDate(); // 取得网站日期时间  
					Date date = new Date(ld); // 转换为标准时间对象
					SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
					time= sdf.format(date);
					MyLogger.getInstance().e(" get time = "+time);
					if(mTimeThread!=null){
						mTimeThread.interrupt();
						mTimeThread=null;
					}	
					mTimeThread = new TimeThread();
					mTimeThread.start();
					MyLogger.getInstance().e(" mTimeThread  start success  ");
					conn.disconnect();
					return;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} .start();
	}
	
	private class TimeThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (true) {
				if(!Utils.inuse){//串口没有使用
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					mSerialPortUtil =MyApplication.getSerialPortUtil();
					do{
						MyLogger.getInstance().e(" witer time = "+time);
//						setResult = mSerialPortUtil.setCurrentTime(time);  //设置时间接口，未实现，是否有必要需要此接口还需确认
						MyLogger.getInstance().e("  setResult = "+setResult);	
						if(setResult<0){
							//faile to sent data	
							MyLogger.getInstance().e(" faile to sent data ");	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getBaseData();
						MyLogger.getInstance().e("getBaseData  returnsResult = "+returnsResult);	
						if(returnsResult>=0){
							MyLogger.getInstance().e("  update time success  ");	
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