/*
 * 公共的activity，主要用于公共布局中的时间、天气、套餐剩余量等数据的显示更新
 */ 
package com.kxw.smarthome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.entity.WeatherInfo;
import com.kxw.smarthome.entity.WiFiInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.LocationAndWeatherUtils.ILocationResult;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.Utils;
import com.kxw.smarthome.utils.WifiUtils;

public class BaseActivity extends Activity implements OnClickListener{

	private TextView current_time;
	protected static TextView current_city;
	protected static TextView select_city_tv;
	private static TextView weather_state;
	private static TextView weather_temperature;
	private TextView use_mode;
	private TextView value_surplus;
	protected LinearLayout back_ll, base_view_ll, refresh_ll;
	private ImageView wifi_state_iv;
	private Handler handler;
	private static WeatherInfo weatherInfo = null;
	private WifiUtils wifiUtils;
	private boolean isstop=false;

	private SerialPortUtil mSerialPortUtil;
	private BaseData baseData;
	private ReadThread mReadThread;
	private TimeThread mTimeThread;
	private Message msg;
	private Handler mHandler;

	private static Context context;
	private VerificationData verificationData;
	
    Handler wifiHandler  = new Handler();
    Runnable updateThread =  new Runnable(){
        @Override
        public void run() {
        	mWifiHandler.sendEmptyMessageDelayed(TIME_COUNT_DOWN, 1000);
        }
    };

    //计时标志
    private final int TIME_COUNT_DOWN = 4;
    Handler mWifiHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            
                case TIME_COUNT_DOWN:
                	if (NetUtils.isConnected(BaseActivity.this)||Utils.connected)
            		{
            			System.out.println("==已连接网络==");
            		}
            		else
            		{
            			System.out.println("==正在连接网络==");
            			WiFiInfo wiFiInfo=new WiFiInfo();
            			wiFiInfo=DBUtils.getFirstData(WiFiInfo.class);
            			if(wiFiInfo!=null){
            				System.out.println("wifi对象:"+wiFiInfo.toString());
            				wifiUtils = new WifiUtils(context);
            				int netID;
            				try {
            					netID = wifiUtils.CreateWifi(wiFiInfo);
            					Utils.connected=wifiUtils.ConnectToNetID(netID);
            				} catch (Exception e) {
            					// TODO Auto-generated catch block
            					e.printStackTrace();
            				}
            			}
            			else
            			{
            				System.out.println("wifi对象为空");
						}
            			wifi_state_iv.setImageResource(R.drawable.wifi_disconnect_img);
            			mWifiHandler.sendEmptyMessageDelayed(TIME_COUNT_DOWN, 1000);
            		}
                    break;
            }
        }
    };

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
		
	    wifiHandler.postDelayed(updateThread, 0);
	    registerBoradcastReceiver();
	    
		showPayInfo();
	}

	private void baseView() {
		// TODO Auto-generated method stub
		base_view_ll = (LinearLayout)findViewById(R.id.base_view_ll);
		refresh_ll = (LinearLayout)findViewById(R.id.title_refresh_ll);
		current_time = (TextView)findViewById(R.id.current_time_tv);
		current_city = (TextView)findViewById(R.id.current_city_tv);
		select_city_tv = (TextView)findViewById(R.id.select_city_tv);
		weather_state = (TextView)findViewById(R.id.weather_state_tv);
		weather_temperature = (TextView)findViewById(R.id.weather_temperature_tv);
		back_ll = (LinearLayout)findViewById(R.id.title_back_ll);
		use_mode= (TextView)findViewById(R.id.use_mode_tv);
		value_surplus= (TextView)findViewById(R.id.value_surplus_tv);
		wifi_state_iv = (ImageView) findViewById(R.id.wifi_state_iv);	

		back_ll.setOnClickListener(this);
		refresh_ll.setOnClickListener(this);
		current_city.setOnClickListener(this);
		current_time.setOnClickListener(this);
		select_city_tv.setOnClickListener(this);
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
		
		if (NetUtils.isConnected(this)||Utils.connected) {
			wifi_state_iv.setImageResource(R.drawable.wifi_connect_img);
//			if(SharedPreferencesUtil.getStringData(BaseActivity.this, "province", "").equals("")||SharedPreferencesUtil.getStringData(BaseActivity.this, "city", "").equals("")){
//				MyApplication.getInstance().getLocationAndWeatherUtils().start(new ILocationResult() {
//					@Override
//					public void setResult(String p, String c, String d) {
//						// TODO Auto-generated method stub
//						current_city.setText(c);
//					}
//					@Override
//					public void failResult() {
//						// TODO Auto-generated method stub
//						current_city.setText("点击重新定位");
//					}
//				});
//			}
//			else
//			{
//				current_city.setText(SharedPreferencesUtil.getStringData(BaseActivity.this, "city", ""));
//			}
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
		if(!mTimeThread.isInterrupted()){
			mTimeThread.start();
		}
		showPayInfo();
	}

	protected void showPayInfo() {
		// TODO Auto-generated method stub
				
		verificationData = new VerificationData(BaseActivity.this);
		baseData = mSerialPortUtil.returnBaseData();
		
		if(verificationData != null && verificationData.getBindDate() != -1 && verificationData.getFirstFilter() != -1
				&& verificationData.getFivethFilter() != -1 && verificationData.getFourthFilter() != -1 && verificationData.getPay_proid() != -1
				&& verificationData.getSecondFilter() != -1 && verificationData.getThirdFilter() != -1 && verificationData.getTimeSurplus() != -1
				&& verificationData.getWaterSurplus() != -1){
			if(verificationData.getPay_proid() == 1)
			{
				//保存下支付类型
				SharedPreferencesUtil.saveIntData(
						BaseActivity.this, "payment_type", 1);
				use_mode.setText(getString(R.string.total_type_month));
				value_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.title_total_day_surplus_value),
						verificationData.getTimeSurplus())));
			}
			else
			{
				//保存下支付类型
				SharedPreferencesUtil.saveIntData(
						BaseActivity.this, "payment_type", 0);
				use_mode.setText(getString(R.string.title_total_flow_surplus));
				value_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.title_total_flow_surplus_value),
						verificationData.getWaterSurplus())));
			}
		}
		else
		{
			if(baseData != null)
			{
				if(baseData.getTimeSurplus() != 65535)
				{
					SharedPreferencesUtil.saveIntData(
							BaseActivity.this, "payment_type", 1);
					use_mode.setText(getString(R.string.total_type_month));
					value_surplus.setText(Html.fromHtml(String.format(
							getString(R.string.title_total_day_surplus_value),
							baseData.getTimeSurplus())));
				}
				else
				{
					//保存下支付类型
					SharedPreferencesUtil.saveIntData(
							BaseActivity.this, "payment_type", 0);
					use_mode.setText(getString(R.string.title_total_flow_surplus));
					value_surplus.setText(Html.fromHtml(String.format(
							getString(R.string.title_total_flow_surplus_value),
							baseData.getWaterSurplus())));
				}
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

	public void showWeather(){
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		try {
			weatherInfo=db.findFirst(WeatherInfo.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		if (weatherInfo != null) {		
			if(!ToolsUtils.isEmpty(SharedPreferencesUtil.getStringData(context, "city", "")))
			{
				current_city.setText(SharedPreferencesUtil.getStringData(context, "city", ""));
			}
			weather_state.setText(weatherInfo.getState());
			weather_temperature.setText(weatherInfo.getTemperature());
		}
		else
		{
			MyApplication.getInstance().getLocationAndWeatherUtils().start(new ILocationResult() {
				
				@Override
				public void setResult(String p, String c, String d) {
					// TODO Auto-generated method stub
					current_city.setText(c);
					showWeather();
				}
				
				@Override
				public void failResult() {
					// TODO Auto-generated method stub
					current_city.setText("点击重新定位");
					showWeather();
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isstop=true;
        unregisterReceiver(mBroadcastReceiver);
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
	
	//更改剩余天数或者剩余流量的
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if(action.equals(ConfigUtils.update_value_surplus_action))
            {  
            	showPayInfo();
            }  
            else if(action.equals(ConfigUtils.update_value_city_action))
            {
        		showWeather();
            }
        }  
    };  
    
    //注册广播       
    public void registerBoradcastReceiver(){  
    	Intent intent = new Intent();
    	intent.setAction(ConfigUtils.update_value_surplus_action);
    	intent.setAction(ConfigUtils.update_value_city_action);
    	PackageManager pm = context.getPackageManager();
    	List<ResolveInfo> resolveInfos = pm.queryBroadcastReceivers(intent, 0);
    	if(resolveInfos != null && !resolveInfos.isEmpty()){
    		//查询到相应的BroadcastReceiver
    	}  
    	else
    	{
    		IntentFilter myIntentFilter = new IntentFilter();  
    	    myIntentFilter.addAction(ConfigUtils.update_value_surplus_action);  
    	    myIntentFilter.addAction(ConfigUtils.update_value_city_action);  
    	    registerReceiver(mBroadcastReceiver, myIntentFilter); 
		}
    }  
}