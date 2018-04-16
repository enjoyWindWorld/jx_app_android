package com.kxw.smarthome.utils;

import android.content.Context;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationAndWeatherUtils {

	private static LocationClient locationClient = null;
	private static final int UPDATE_TIME = 5000;

	static String city = null;

	/*
	 * type: 0表示只定位，不获取天气；1表示定位并更新天气
	 */
	public static void getLocation(Context context,final int type) {

		if(NetUtils.isConnected(context)){
			locationClient = new LocationClient(context);
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
					city = location.getCity().substring(0, location.getCity().indexOf("市"));
					Utils.province = location.getProvince();
					Utils.district = location.getDistrict();
					if(type==1){
						getWeatherInfoFromCity(city);   //全球     6000次每天
					}
//				   getWeatherInfoFromCityName(city);   //全国  无次数限制   11月份已停止更新
//				   getCityInfo(city);
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
	}

	//全球天气 6000次每天    注意是否套餐有变更
	public static void getWeatherInfoFromCity(String city) {
		MyLogger.getInstance().e("get weather city = "+city);
		Parameters para = new Parameters();
		para.put("city", city);
		ApiStoreSDK.execute("http://apis.baidu.com/heweather/pro/weather", 
				ApiStoreSDK.GET, 
				para, 
				new ApiCallBack() {

			@Override
			public void onSuccess(int status, String responseString) {
				MyLogger.getInstance().e("get responseString = "+responseString);
				JsonUtils.getWeather(responseString);
			}

			@Override
			public void onComplete() {
			}

			@Override
			public void onError(int status, String responseString, Exception e) {
			}

		});
	}

	//获取城市编码
	public static void getCityInfo(String cityName) {

		Parameters para = new Parameters();
		para.put("cityname", cityName);
		ApiStoreSDK.execute("http://apis.baidu.com/apistore/weatherservice/cityinfo", 
				ApiStoreSDK.GET, 
				para, 
				new ApiCallBack() {

			@Override
			public void onSuccess(int status, String responseString) {     
				//				MyLogger.getInstance().e(responseString.toString());
				getWeatherInfoFromId(JsonUtils.getCityId(responseString));
			}

			@Override
			public void onComplete() {
			}

			@Override
			public void onError(int status, String responseString, Exception e) {
			}

		});
	}

	//通过城市编码获取天气
	public static void getWeatherInfoFromId(String cityid) {

		Parameters para = new Parameters();
		para.put("cityid", cityid);
		ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free", 
				ApiStoreSDK.GET, 
				para, 
				new ApiCallBack() {

			@Override
			public void onSuccess(int status, String responseString) {    
				JsonUtils.getWeather(responseString);
			}

			@Override
			public void onComplete() {
			}

			@Override
			public void onError(int status, String responseString, Exception e) {
			}

		});
	}

	//国内天气   无限制次数
	public static void getWeatherInfoFromCityName(String cityName) {

		Parameters para = new Parameters();
		para.put("cityname", cityName);
		ApiStoreSDK.execute("http://apis.baidu.com/apistore/weatherservice/cityname", 
				ApiStoreSDK.GET, 
				para, 
				new ApiCallBack() {

			@Override
			public void onSuccess(int status, String responseString) {     
				//				MyLogger.getInstance().e(responseString.toString());
				JsonUtils.getWeatherFromCityName(responseString);
			}

			@Override
			public void onComplete() {
			}

			@Override
			public void onError(int status, String responseString, Exception e) {
			}

		});
	}


	/** 
	 * 获取ip地址 
	 * @return 
	 */  
	/*public static void GetNetIp()  {

		new Thread() {
			public void run() {
				URL infoUrl = null;  
				InputStream inStream = null;  
				try{  
					infoUrl = new URL("http://city.ip138.com/ip2city.asp");  

					HttpURLConnection conn = (HttpURLConnection) infoUrl.openConnection();			
					conn.connect();
					MyLogger.getInstance().e(conn.getContent());
					InputStream is = conn.getInputStream();

					int responseCode = conn.getResponseCode();  
					if(responseCode == HttpURLConnection.HTTP_OK){   
						inStream = conn.getInputStream();   
						BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));  
						StringBuilder strber = new StringBuilder();  
						String line = null;  
						while ((line = reader.readLine()) != null)   
							strber.append(line + "\n");  
						inStream.close();  
						//从反馈的结果中提取出IP地址  
						int start = strber.indexOf("[");  
						int end = strber.indexOf("]", start + 1);  
						String ip = strber.substring(start + 1, end);  
						getWeatherInfoFromIp(ip);
						MyLogger.getInstance().e(ip);  
					}else{
						MyLogger.getInstance().e(responseCode); 
					}
				}  
				catch(MalformedURLException e) {  
					e.printStackTrace();  
				}  
				catch (IOException e) {  
					e.printStackTrace();  
				}  
			}
		} .start();
	}  
	 */

	/*	public static void getWeatherInfoFromIp(String cityip) {

		Parameters para = new Parameters();
		para.put("cityip", cityip);
		ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free", 
				ApiStoreSDK.GET, 
				para, 
				new ApiCallBack() {

			@Override
			public void onSuccess(int status, String responseString) {
				MyLogger.getInstance().e("getWeatherInfoFromIp onSuccess "+responseString.toString());     

				JsonUtils.getWeather(responseString);
			}

			@Override
			public void onComplete() {
			}

			@Override
			public void onError(int status, String responseString, Exception e) {
			}

		});
	}*/



}