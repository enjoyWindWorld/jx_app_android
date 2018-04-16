package com.kxw.smarthome.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.kxw.smarthome.entity.FreeWeatherInfo;
import com.kxw.smarthome.entity.WeatherInfo;

public class LocationAndWeatherUtils {

	private static LocationClient locationClient = null;
	private static Context mContext;
	static String city = null;
	private static ILocationResult locationResult;
	
	private LocationClientOption mOption, DIYoption;
	private Object  objLock = new Object();
	private boolean isRegisterListener;
	
	public LocationAndWeatherUtils(Context context)
	{
		mContext = context;
		synchronized (objLock) {
			if(locationClient == null){
				locationClient = new LocationClient(context);
				locationClient.setLocOption(getDefaultLocationClientOption());
				isRegisterListener = registerListener(mListener);
			}
		}
	}
	
	/***
	 * 
	 * @param listener
	 * @return
	 */
	
	public boolean registerListener(BDAbstractLocationListener listener){
		boolean isSuccess = false;
		if(listener != null){
			locationClient.registerLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
	}
	
	public void unregisterListener(BDAbstractLocationListener listener){
		if(listener != null){
			isRegisterListener = false;
			locationClient.unRegisterLocationListener(listener);
		}
	}
	
	/***
	 * 
	 * @param option
	 * @return isSuccessSetOption
	 */
	public boolean setLocationOption(LocationClientOption option){
		boolean isSuccess = false;
		if(option != null){
			if(locationClient.isStarted())
				locationClient.stop();
			DIYoption = option;
			locationClient.setLocOption(option);
		}
		return isSuccess;
	}
	
	public LocationClientOption getOption(){
		return DIYoption;
	}
	
	/***
	 * 
	 * @return DefaultLocationClientOption
	 */
	public LocationClientOption getDefaultLocationClientOption(){
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		    mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		    mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
		    mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
		    mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		    mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死   
		    mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		    mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		    mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		    mOption.setProdName("JXSmart"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		    mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
		 
		}
		return mOption;
	}
	
	public void start(ILocationResult mlocationResult){
		synchronized (objLock) {
			locationResult = mlocationResult;
			if(locationClient != null && locationClient.isStarted())
			{
				locationClient.stop();
			}
			if(locationClient != null && !locationClient.isStarted() && isRegisterListener){
				MyToast.getManager(mContext).show("开始定位");
				locationClient.start();
			}
		}
	}
	public void stop(){
		synchronized (objLock) {
			if(locationClient != null && locationClient.isStarted()){
				unregisterListener(mListener);
				locationClient.stop();
			}
		}
	}
	
	public boolean requestHotSpotState(){
		
		return locationClient.requestHotSpotState();
		
	}
	
	/*****
	 *
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub		
			
			if(locationClient != null && locationClient.isStarted()){
				locationClient.stop();
			}
			
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				// TODO Auto-generated method stub				
				System.out.println("省份==="+location.getProvince());
				System.out.println("城市==="+location.getCity());
				System.out.println("区域==="+location.getDistrict());
				System.out.println("经度==="+location.getLongitude());
				System.out.println("纬度==="+location.getLatitude());
				
				//保存下省份
				SharedPreferencesUtil.saveStringData(
						mContext, "province", location.getProvince());
				//保存下城市
				SharedPreferencesUtil.saveStringData(
						mContext, "city", location.getCity());
				//保存下区域
				SharedPreferencesUtil.saveStringData(
						mContext, "district", location.getDistrict());
				
				//保存下经度
				SharedPreferencesUtil.saveStringData(
						mContext, "longitude", location.getLongitude()+"");
				
				//保存下纬度
				SharedPreferencesUtil.saveStringData(
						mContext, "latitude", location.getLatitude()+"");
				
				city = location.getCity().substring(0, location.getCity().indexOf("市"));
				getFreeWeather(city);
				
				if(locationResult != null)
				{
					locationResult.setResult(location.getProvince(), location.getCity(), location.getDistrict());
				}
				
				MyToast.getManager(mContext).show("定位成功，当前城市："+location.getCity());
				
				mContext.sendBroadcast(new Intent(ConfigUtils.update_value_city_action));
			}
			else
			{
				if(locationResult != null)
				{
					locationResult.failResult();
				}
				
				MyToast.getManager(mContext).show("定位失败");
			}
		}

	};
	
	/**
	 * 新浪的天气api
	 * @param cityName
	 */
	private void getFreeWeather(String cityName) {
		try {
			cityName = URLEncoder.encode(cityName, "utf-8");
			System.out.println("url==="
					+ "http://www.sojson.com/open/api/weather/json.shtml?city="
					+ cityName);
			RequestParams params = new RequestParams(
					"http://www.sojson.com/open/api/weather/json.shtml?city="
							+ cityName);
			x.http().get(params, new CommonCallback<String>() {

				@Override
				public void onCancelled(CancelledException arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(Throwable arg0, boolean arg1) {

				}

				@Override
				public void onFinished() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(String response) {
					try {
						System.out.println("===json===" + response);
						 FreeWeatherInfo freeWeatherInfo = new
						 Gson().fromJson(response, FreeWeatherInfo.class);
						 if("200".equals(freeWeatherInfo.getStatus()))
						 {
						 DaoConfig daoConfig = DBUtils.getDaoConfig();
						 DbManager db = x.getDb(daoConfig);
						 db.delete(WeatherInfo.class);
						 WeatherInfo weatherInfo = new WeatherInfo();
						 String highString = freeWeatherInfo.getData().getForecast().get(0).getHigh().replace("高温", "");
						 weatherInfo.setTemperature(highString);
						 weatherInfo.setState(freeWeatherInfo.getData().getForecast().get(0).getType());
						 weatherInfo.setUpdataTime();
						 db.save(weatherInfo);
						 }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//定位的回调
	public interface ILocationResult{
		
		//定位成功，设置地址
		public void setResult(String p, String c, String d);
		
		//定位失败
		public void failResult();
	}
}