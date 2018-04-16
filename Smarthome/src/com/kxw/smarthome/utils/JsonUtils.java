package com.kxw.smarthome.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kxw.smarthome.BaseActivity;
import com.kxw.smarthome.entity.RequestDataInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.WeatherInfo;

public class JsonUtils{

	private static WeatherInfo weatherInfo=null;

	public static void getWeather(String jsonStr){

		JSONArray result = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			result = jsonObject.getJSONArray("HeWeather data service 3.0");			
			MyLogger.getInstance().i("result =" +result);
			JSONObject data = (JSONObject)result.opt(0);
			String city=data.getJSONObject("basic").getString("city");
			String tmp=data.getJSONObject("now").getString("tmp");
			String state =data.getJSONObject("now").getJSONObject("cond").getString("txt");
			DaoConfig daoConfig=DBUtils.getDaoConfig(); 
			DbManager db = x.getDb(daoConfig);
			db.delete(WeatherInfo.class);			
			weatherInfo = new WeatherInfo();
			weatherInfo.setProvince(Utils.province);
			weatherInfo.setDistrict(Utils.district);
			weatherInfo.setCity(city);
			weatherInfo.setTemperature(tmp);
			weatherInfo.setState(state);
			weatherInfo.setUpdataTime();
			MyLogger.getInstance().e(weatherInfo.toString());
			db.save(weatherInfo);
			BaseActivity.showWeather();
		} catch (JSONException | DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	
	public static void getWeatherFromCityName(String jsonStr){

		JSONObject result = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			result = jsonObject.getJSONObject("retData");			
			Log.i("result", ""+result);
			String tmp=result.getString("temp");
			String state =result.getString("weather");

			DaoConfig daoConfig=DBUtils.getDaoConfig(); 
			DbManager db = x.getDb(daoConfig);
			db.delete(WeatherInfo.class);
			weatherInfo = new WeatherInfo();
			weatherInfo.setTemperature(tmp);
			weatherInfo.setState(state);
			weatherInfo.setUpdataTime();
			db.save(weatherInfo);
		} catch (JSONException | DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};


	// {"errNum":0,"retMsg":"success","retData":{"cityName":"\u6df1\u5733","provinceName":"\u5e7f\u4e1c","cityCode":"101280601","zipCode":"518000","telAreaCode":"0755"}}
	public static String getCityId(String jsonStr){
		String cityCode=null;
		JSONObject result = null;		
		try {
			if (new JSONObject(jsonStr).has("retData")) {					
				result = new JSONObject(new JSONObject(jsonStr).getString("retData"));				
				if(result.has("cityName")){
					cityCode="CN"+result.getString("cityCode");		
					MyLogger.getInstance().e("cityCode ="+cityCode);
				}
			} else {

			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return cityCode;
	};


	public static RequestDataInfo requestState(String jsonStr) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(jsonStr, RequestDataInfo.class);
	}

	public static <T> T jsonToBeanClass(String jsonStr, Class<T> clazz) {
		Gson gson = new GsonBuilder().create();
		T t = gson.fromJson(jsonStr, clazz);
		return t;
	}

	/*	public static <T> ArrayList<T> jsonToBeanList(String data) {
		Gson gson = new GsonBuilder().create();
		Type type = new TypeToken<ArrayList<T>>(){}.getType();
		ArrayList<T> t = gson.fromJson(data, type);
		return t;
	}
	 */
	public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz){
		MyLogger.getInstance().e("jsonToArrayList = "+json);
		Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
		ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
		ArrayList<T> arrayList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjects){
			arrayList.add(new Gson().fromJson(jsonObject, clazz));
		}
		return arrayList;
	}


	public static String userInfoToJson(UserInfo userInfo) {
		Gson gson = new GsonBuilder().create();		
		return gson.toJson(userInfo);
	}

	public static <T> String beanToJson(T t) {
		Gson gson = new GsonBuilder().create();		
		return gson.toJson(t);
	}

	public static String getStoreJson(int pages) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("phoneNum", Utils.phoneNum);
			jObj.accumulate("deviceCode", Utils.pro_no);
			jObj.accumulate("pages", pages);
		} catch (Exception e) {

		}	
		return jObj.toString();
	}

	public static String updateInfoJson() {
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("ver", String.valueOf(DeviceUtils.getAppVersionCode()));
			jObj.accumulate("type", 0);
			return jObj.toString();
		} catch (Exception e) {

		}
		return "";
	}

	public static int result(String jsonStr){
		MyLogger.getInstance().e("jsonStr ="+jsonStr);	
		int result = -1;	
		try {	
			result=new  JSONObject(jsonStr).getInt("result");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	};

	public static String msg(String jsonStr){
		MyLogger.getInstance().e("jsonStr ="+jsonStr);	
		String msg = null;	
		try {	
			msg=new  JSONObject(jsonStr).getString("msg");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return msg;
	};

	public static String EquipmentInfoInfoJson(String UUID) {
		JSONObject jObj = new JSONObject();
		try {
			//			PRF_PP：PP滤芯。            PRF_ACB：颗粒活性炭滤芯。      PRF_RO：RO膜滤芯。
			//			PRF_FFR：活性保鲜滤芯。PRF_WFR：弱碱滤芯。
//			UUID.randomUUID()
			jObj.accumulate("pro_id", UUID);
			jObj.accumulate("pp", "35%");
			jObj.accumulate("cto", "16%");
			jObj.accumulate("ro", "87%");
			jObj.accumulate("t33", "68%");
			jObj.accumulate("wfr", "23%");	
			MyLogger.getInstance().e("EquipmentInfoInfoJson = "+jObj.toString());
			return jObj.toString();
		} catch (Exception e) {

		}
		return "";
	}

}