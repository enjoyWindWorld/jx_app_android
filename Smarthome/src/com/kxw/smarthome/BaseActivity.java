/*
 * 公共的activity，主要用于公共布局中的时间、天气、套餐剩余量等数据的显示更新
 */ 
package com.kxw.smarthome;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.WeatherInfo;
import com.kxw.smarthome.entity.WiFiInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.LocationAndWeatherUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.UseStateToast;
import com.kxw.smarthome.utils.Utils;
import com.kxw.smarthome.utils.WifiUtils;

public class BaseActivity extends Activity implements OnClickListener{

	private TextView current_time;
	private static TextView weather_state;
	private static TextView weather_temperature;
	private TextView use_mode;
	private TextView value_surplus;
	private LinearLayout back_ll,base_view_ll;
	private ImageView wifi_state_iv;
	private Handler handler;
	private static WeatherInfo weatherInfo = null;
	private WifiUtils wifiUtils;
	private BaseData mBaseData;

	private boolean isstop=false;

	private SerialPortUtil mSerialPortUtil;
	private ReadThread mReadThread;
	private TimeThread mTimeThread;
	private Message msg;
	private Handler mHandler;

	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.base_activity);
		context=this;
		baseView();
		mSerialPortUtil =MyApplication.getSerialPortUtil();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				current_time.setText((String) msg.obj);
				if (!NetUtils.isConnected(context)){
					wifi_state_iv.setImageResource(R.drawable.wifi_disconnect_img);
				}else{
					wifi_state_iv.setImageResource(R.drawable.wifi_connect_img);
				}
			}
		};		

		mHandler = new Handler() {  
			@Override  
			public void handleMessage(Message msg) {  
				MyLogger.getInstance().e(msg.toString());
				switch (msg.arg1) {

				case 0:
					showPayInfo();
					break;

				case 1:
					break;

				default:
					break;
				}
				super.handleMessage(msg);  
			}
		};
		mReadThread = new ReadThread();
		mReadThread.start();		

	}

	private void baseView() {
		// TODO Auto-generated method stub
		base_view_ll = (LinearLayout)findViewById(R.id.base_view_ll);
		current_time = (TextView)findViewById(R.id.current_time_tv);
		weather_state = (TextView)findViewById(R.id.weather_state_tv);
		weather_temperature = (TextView)findViewById(R.id.weather_temperature_tv);
		back_ll = (LinearLayout)findViewById(R.id.title_back_ll);
		back_ll.setOnClickListener(this);
		use_mode= (TextView)findViewById(R.id.use_mode_tv);
		value_surplus= (TextView)findViewById(R.id.value_surplus_tv);
		wifi_state_iv = (ImageView) findViewById(R.id.wifi_state_iv);	

	}

	/*
	 * 重点是重写setContentView，让继承者可以继续设置setContentView
	 * 重写setContentView
	 * @param resId
	 */	
	public void setBaseContentView(int layoutResID) {

		View contentView = getLayoutInflater().inflate(layoutResID, null);		
		if (contentView != null) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 
					LayoutParams.MATCH_PARENT);
			if(null != base_view_ll)
				base_view_ll.addView(contentView, params);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		baseView();
		showWeather();
		MyLogger.getInstance().e("isNetworkConnected = "+NetUtils.isConnected(this));
		if (NetUtils.isConnected(this)||Utils.connected) {
			wifi_state_iv.setImageResource(R.drawable.wifi_connect_img);
			if(Utils.city==null||Utils.province==null){
				LocationAndWeatherUtils.getLocation(context,ConfigUtils.get_location_only);
			};

		} else {
			WiFiInfo wiFiInfo=new WiFiInfo();
			wiFiInfo=DBUtils.getFirstData(WiFiInfo.class);
			//			MyLogger.getInstance().e("wiFiInfo  = " + wiFiInfo.toString());
			if(wiFiInfo!=null){
				MyLogger.getInstance().e(wiFiInfo.toString());
				wifiUtils = new WifiUtils(context);
				int netID;
				try {
					netID = wifiUtils.CreateWifi(wiFiInfo);
					MyLogger.getInstance().e("Dialog---  netid = " + netID);
					Utils.connected=wifiUtils.ConnectToNetID(netID);
					MyLogger.getInstance().e(Utils.connected=wifiUtils.ConnectToNetID(netID));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			wifi_state_iv.setImageResource(R.drawable.wifi_disconnect_img);
		}

		mTimeThread = new TimeThread();
		isstop=false;
		//		MyLogger.getInstance().e("mTimeThread =  "+mTimeThread.isInterrupted());
		if(!mTimeThread.isInterrupted()){
			mTimeThread.start();
		}

		showPayInfo();
	}

	private void showPayInfo() {
		// TODO Auto-generated method stub
		mBaseData=mSerialPortUtil.returnBaseData();
		if(mBaseData!=null){
			if(mBaseData.timeSurplus==65535){
				MyLogger.getInstance().e(" by quantity of flow");
				Utils.payment_type = 0;
				use_mode.setText(getString(R.string.title_total_flow_surplus));
				value_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.title_total_flow_surplus_value),
						mBaseData.waterSurplus)));
			}else{
				MyLogger.getInstance().e(" by time");
				Utils.payment_type = 1;
				use_mode.setText(getString(R.string.total_type_month));
				value_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.title_total_day_surplus_value),
						mBaseData.timeSurplus)));
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();	
		isstop=true;
	}

	private class TimeThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isstop) {
				handler.sendMessage(handler.obtainMessage(100,getSystemTime()));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			MyLogger.getInstance().e(" ReadThread start ");
			int times= 0;
			do{
				times++;
				while(mSerialPortUtil.setBaseData()>0 && mSerialPortUtil.getBaseData()>=0){					
					msg= mHandler.obtainMessage();
					msg.arg1=0;
					handler.sendMessage(msg);
					times=3;
					break;
				}				
			}while(times<3);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.base_view_ll:
			MyLogger.getInstance().e("...........");
			//			finish();
			break;

		default:
			break;
		}
	}

	public static String getSystemTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd    EEEE   HH:mm:ss");
		return sdf.format(new Date());
	}

	public static void showWeather(){

		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		try {
			weatherInfo=db.findFirst(WeatherInfo.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		if (weatherInfo != null) {
			Utils.city=weatherInfo.getCity();
			Utils.province=weatherInfo.getProvince();
			MyLogger.getInstance().e("weatherInfo = "+weatherInfo.toString());
			weather_state.setText(weatherInfo.getState());
			weather_temperature.setText(weatherInfo.getTemperature() + "℃");
		}else{
			LocationAndWeatherUtils.getLocation(context,ConfigUtils.get_weather_and_location);
			MyLogger.getInstance().e("weatherInfo = null");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isstop=true;
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (!ToolsUtils.isFastDoubleClick()) {
			Intent mIntent = new Intent("ON_TOUCH_ACTION");                 
			sendBroadcast(mIntent);  
		}	
		return super.dispatchTouchEvent(ev);
	}

}