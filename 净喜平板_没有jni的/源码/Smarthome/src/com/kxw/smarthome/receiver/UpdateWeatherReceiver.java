/*
 * 天气更新广播
*/
package com.kxw.smarthome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.utils.ConfigUtils;

public class UpdateWeatherReceiver extends BroadcastReceiver {  
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		if(intent.getAction().equals(ConfigUtils.update_weather_alarm)){
			getLocation(MyApplication.getInstances());
		}
	}

	/*
	 * 在Broadcast 中不能使用bindservice，而百度地图jar包中使用的就是bindservice，
	 * 所以通过Handler的Runnable启动
	*/
	public void getLocation(final Context context){

		new Handler().post(new Runnable(){    
			public void run() {    
				MyApplication.getInstance().getLocationAndWeatherUtils().start(null);
			} 
		}); 
	}
}