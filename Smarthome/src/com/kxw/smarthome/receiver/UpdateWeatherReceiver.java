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
import com.kxw.smarthome.utils.LocationAndWeatherUtils;
import com.kxw.smarthome.utils.MyLogger;

public class UpdateWeatherReceiver extends BroadcastReceiver {  

	@Override  
	public void onReceive(Context context, Intent intent){  
		MyLogger.getInstance().e("UpdateWeatherReceiver Action = "+intent.getAction());
		if(intent.getAction().equals(ConfigUtils.update_weather_alarm)){
			//			LocationAndWeatherUtils.getWeatherInfoFromCity(Utils.city);
			getLocation(MyApplication.getInstances());
			//			LocationAndWeatherUtils.getLocation(context,ConfigUtils.get_weather_and_location);
		}
	}

	/*
	 * 在Broadcast 中不能使用bindservice，而百度地图jar包中使用的就是bindservice，
	 * 所以通过Handler的Runnable启动
	*/
	public void getLocation(final Context context){

		new Handler().post(new Runnable(){    
			public void run() {    
				//execute the task    
				MyLogger.getInstance().e("handler post  ");
				LocationAndWeatherUtils.getLocation(context,ConfigUtils.get_weather_and_location);
			} 
		}); 
	}
}