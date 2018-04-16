package com.kxw.smarthome.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class ToolsUtils{

	/*
	 * 百度地图定位方法，由于定位需要一定的时间所以不能够立马获得位置数据
	 */
/*	private static LocationClient locationClient = null;
	private static final int UPDATE_TIME = 5000;
	static String city = null;
	public static void getLocation(Context context) {
		locationClient = new LocationClient(context);
		MyLogger.getInstance().e("LocationAndWeatherUtils");
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
		option.setProdName("JXSmart"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		// option.setScanSpan(UPDATE_TIME); //设置定时定位的时间间隔。单位毫秒
		option.setAddrType("all"); // 地址类型为“all”否则获取城市地名会返回null
		locationClient.setLocOption(option);

		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				String getAddr = location.getAddrStr();
				MyLogger.getInstance().e(getAddr);
				MyLogger.getInstance().e(location.getCityCode());

				if (getAddr != null) {
					if (getAddr.contains("省") && getAddr.contains("市")) {
						city = getAddr.substring(getAddr.indexOf("省") + 1,getAddr.indexOf("市"));
					} else if (!getAddr.contains("省") && getAddr.contains("市")) {
						city = getAddr.substring(0, getAddr.indexOf("市"));
					}
					Utils.city=city;
				}
			}
			@Override
			public void onReceivePoi(BDLocation location) {}
		});
		if (locationClient == null) {
			return;
		} else {
			locationClient.start();
			locationClient.requestLocation();
		}
	}
*/

	/** 
	 * 验证手机格式 
	 */  
	public static boolean isPhoneNum(String phoneNum) {  
		/* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
		 */  
		String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
		if (phoneNum.isEmpty()){
			return false; 
		}else{
			return phoneNum.matches(telRegex);  
		}
	}

	//避免重复点击
	private static  long lastClickTime;  
	public static  boolean isFastDoubleClick() { 
		long time = System.currentTimeMillis();  
		long lead_time = time - lastClickTime;  
		if ( 0 < lead_time && lead_time < 1000) {   
			return true;     
		}     
		lastClickTime = time;    
		return false;     
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		return info.topActivity.getClassName(); // 完整类名
	}


	public static boolean isShowHint(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		if(info.topActivity.getClassName()!= null && info.topActivity.getClassName().endsWith("com.kxw.smarthome.HintDialogActivity"))
			return true; 
		return false;
	}
}