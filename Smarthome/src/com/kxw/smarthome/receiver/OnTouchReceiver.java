/*
 * 自动跳转到广告界面广播
*/
package com.kxw.smarthome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.utils.ConfigUtils;


public class OnTouchReceiver extends BroadcastReceiver {  

	@Override  
	public void onReceive(Context context, Intent intent){  
		// TODO Auto-generated method stub  
		if(intent.getAction().equals(ConfigUtils.on_touch_action)){	
			MyApplication.getInstance().setTimer();
		}	
	}
}