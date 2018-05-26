/*
 * 设置模块
*/
package com.kxw.smarthome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.utils.AppUtil;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.ToastUtil;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.Utils;

public class SettingActivity extends BaseActivity implements OnClickListener {
	
	private LinearLayout wifi_manage_ll, brightness_control_ll, volume_control_ll,device_code_ll,equipment_change_ll,version_change_ll, search_ordinfo_ll, machine_synchronization_ll, regulating_temperature_ll, regulating_machine_ll;
	private TextView tv_version_code;
	private Button btn_jia_time, btn_jian_time, btn_jia_filter_1, btn_jian_filter_1, btn_jia_filter_2, btn_jian_filter_2, btn_jia_filter_3, btn_jian_filter_3, btn_jia_filter_4, btn_jian_filter_4;
	
	private boolean mustupgrade = false;
	private SerialPortUtil mSerialPortUtil;
	private UserInfo userInfo;
	private FilterLifeInfo mFilterLifeInfo;
	
	int translate; 
	
	private Handler mUpgradeHandler;  
	private HandlerThread mUpgradeHandlerThread;  
	
	private Handler mFilterLifeHandler;  
	private HandlerThread mFilterLifeHandlerThread;  
	
	private Handler mQuantityHandler;  
	private HandlerThread mQuantityHandlerThread;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.systen_setting_activity);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		mSerialPortUtil =MyApplication.getSerialPortUtil();
		wifi_manage_ll = (LinearLayout)findViewById(R.id.wifi_manage_ll);
		brightness_control_ll = (LinearLayout)findViewById(R.id.brightness_control_ll);
		volume_control_ll = (LinearLayout)findViewById(R.id.volume_control_ll);
		device_code_ll = (LinearLayout)findViewById(R.id.device_code_ll);
		equipment_change_ll= (LinearLayout)findViewById(R.id.equipment_change_ll);
		version_change_ll = (LinearLayout)findViewById(R.id.version_change_ll);
		search_ordinfo_ll = (LinearLayout)findViewById(R.id.search_ordinfo_ll);
		machine_synchronization_ll = (LinearLayout)findViewById(R.id.machine_synchronization_ll);
		regulating_temperature_ll = (LinearLayout)findViewById(R.id.regulating_temperature_ll);
		regulating_machine_ll = (LinearLayout)findViewById(R.id.regulating_machine_ll);
		tv_version_code = (TextView)findViewById(R.id.tv_version_code);
		
		btn_jia_time = (Button) findViewById(R.id.btn_jia_time);
		btn_jian_time = (Button) findViewById(R.id.btn_jian_time);
		btn_jia_filter_1 = (Button) findViewById(R.id.btn_jia_filter_1);
		btn_jian_filter_1 = (Button) findViewById(R.id.btn_jian_filter_1);
		btn_jia_filter_2 = (Button) findViewById(R.id.btn_jia_filter_2);
		btn_jian_filter_2 = (Button) findViewById(R.id.btn_jian_filter_2);
		btn_jia_filter_3 = (Button) findViewById(R.id.btn_jia_filter_3);
		btn_jian_filter_3 = (Button) findViewById(R.id.btn_jian_filter_3);
		btn_jia_filter_4 = (Button) findViewById(R.id.btn_jia_filter_4);
		btn_jian_filter_4 = (Button) findViewById(R.id.btn_jian_filter_4);
		
		btn_jia_time.setOnClickListener(this);
		btn_jian_time.setOnClickListener(this);
		btn_jia_filter_1.setOnClickListener(this);
		btn_jian_filter_1.setOnClickListener(this);
		btn_jia_filter_2.setOnClickListener(this);
		btn_jian_filter_2.setOnClickListener(this);
		btn_jia_filter_3.setOnClickListener(this);
		btn_jian_filter_3.setOnClickListener(this);
		btn_jia_filter_4.setOnClickListener(this);
		btn_jian_filter_4.setOnClickListener(this);
		
		version_change_ll.setOnClickListener(this);
		equipment_change_ll.setOnClickListener(this);
		wifi_manage_ll.setOnClickListener(this);
		brightness_control_ll.setOnClickListener(this);
		volume_control_ll.setOnClickListener(this);
		device_code_ll.setOnClickListener(this);
		search_ordinfo_ll.setOnClickListener(this);
		machine_synchronization_ll.setOnClickListener(this);
		regulating_temperature_ll.setOnClickListener(this);
		regulating_machine_ll.setOnClickListener(this);
		tv_version_code.setText(AppUtil.getVersionName());
		
		mFilterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
	    
	    mUpgradeHandlerThread = new HandlerThread("SettingActivity_mUpgradeHandlerThread", 5);  
	    mUpgradeHandlerThread.start();  
	    mUpgradeHandler = new Handler(mUpgradeHandlerThread.getLooper()); 
	    
		mFilterLifeHandlerThread = new HandlerThread("SettingActivity_mFilterLifeHandlerThread", 5);  
		mFilterLifeHandlerThread.start();  
		mFilterLifeHandler = new Handler(mFilterLifeHandlerThread.getLooper()); 
		
		mQuantityHandlerThread = new HandlerThread("SettingActivity_mQuantityHandlerThread", 5);  
		mQuantityHandlerThread.start();  
		mQuantityHandler = new Handler(mQuantityHandlerThread.getLooper()); 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userInfo = DBUtils.getFirstData(UserInfo.class);
		if(userInfo != null)
		{
			getDeviceCode(userInfo);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();				
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		mUpgradeHandler.removeCallbacks(mUpgradeRunnable);  
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.wifi_manage_ll:
			intent.setClass(this, WifiManageActivity.class);
			startActivity(intent);
			break;
			
		case R.id.brightness_control_ll:
			intent.setClass(this, BrightnessControlActivity.class);
			startActivity(intent);
			break;
		
		case R.id.volume_control_ll:
			intent.setClass(this, VolumeControlActivity.class);
			startActivity(intent);
			break;
			
		case R.id.device_code_ll:

			intent.setClass(this, BindDeviceActivity.class);//绑定返回的时候返回主页面，因为主板数据写入会有延迟，不要让用户马上去绑定界面，不然会出现续费按钮的
			startActivityForResult(intent, 100);
			
			break;
			
		case R.id.equipment_change_ll:
			UserInfo userInfo = DBUtils.getFirstData(UserInfo.class);
			if(userInfo == null)
			{
				ToastUtil.showShortToast(getString(R.string.equipment_change_no_order_info));
			}
			else
			{
				intent.setClass(this, FilterChangeListActivity.class);
				startActivity(intent);
			}
			break;	
			
		case R.id.title_back_ll:
			finish();
			break;
			
		case R.id.version_change_ll:
			checkUpgrade();
			break;
			
		case R.id.search_ordinfo_ll:
			UserInfo userInfo1 = DBUtils.getFirstData(UserInfo.class);
			if(userInfo1 != null)
			{
				ToastUtil.showShortToast(getString(R.string.search_had_order_info));
			}
			else
			{
				intent.setClass(this, SearchOrderInfoActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.machine_synchronization_ll:
			UserInfo userInfo2 = DBUtils.getFirstData(UserInfo.class);
			if(userInfo2 != null)
			{
				ToastUtil.showShortToast(getString(R.string.machine_sycn_had_order_info));
			}
			else
			{
				intent.setClass(this, MachineSynchronizationActivity.class);
				startActivityForResult(intent, 100);
			}
			break;
		case R.id.btn_jia_time:
			
			VerificationData verificationData  = new VerificationData(SettingActivity.this);
			verificationData.clearVerificationData();
			
//			translate = 64901;
//			mQuantityHandler.post(mQuantitykRunnable);
			break;
		case R.id.btn_jian_time:
			translate = -10;
			mQuantityHandler.post(mQuantitykRunnable);
			break;
		case R.id.btn_jia_filter_1:
			mFilterLifeInfo.pp ++;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jian_filter_1:
			mFilterLifeInfo.pp --;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jia_filter_2:
			mFilterLifeInfo.cto ++;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jian_filter_2:
			mFilterLifeInfo.cto --;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jia_filter_3:
			mFilterLifeInfo.ro ++;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jian_filter_3:
			mFilterLifeInfo.ro --;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jia_filter_4:
			mFilterLifeInfo.t33 ++;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.btn_jian_filter_4:
			mFilterLifeInfo.t33 --;
			mFilterLifeHandler.post(mFilterLifeRunnable);
			break;
		case R.id.regulating_temperature_ll:
			intent.setClass(this, RegulatingTemperatureActivity.class);
			startActivity(intent);
			break;
		case R.id.regulating_machine_ll:
			intent.setClass(this, RegulatingMachineActivity.class);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}
	
	//==升级==

	private void checkUpgrade() {
		MyLogger.getInstance().e("  checkUpgrade ");
		if (!NetUtils.isConnected(SettingActivity.this)) {
			Toast.makeText(SettingActivity.this, R.string.network_disConnected,
					Toast.LENGTH_LONG).show();
			return;
		}
		RequestParams params = new RequestParams(ConfigUtils.upgrade_url);
		params.setBodyContent(DataProcessingUtils.encrypt(JsonUtils
				.updateInfoJson()));
		params.setConnectTimeout(10000);
		MyLogger.getInstance().e("params=" + JsonUtils.updateInfoJson());
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e("onError()");
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e("onSuccess response=" + response);
				if (JsonUtils.result(response) == 0) {

					int must = 0;
					JSONArray dataArray = null;
					JSONObject data = null;
					try {
						dataArray = new JSONArray(DataProcessingUtils.decode(new JSONObject(response).getString("data")));
						MyLogger.getInstance().e("onSuccess data =" + dataArray);
						if(dataArray!= null && dataArray.length()>0){
							data = (JSONObject) dataArray.get(0);
							MyLogger.getInstance().e("data = " + data);
							if (data.has("downurl")) {
								must = data.getInt("mustupgrade");  //强制更新 0：不需要强制更新； 1：必须更新
								String updataUrl = data.getString("downurl");
								String apkName = data.getString("name");
								String className = getTopActivity(SettingActivity.this); // 完整类名
								MyLogger.getInstance().e(" className =" + className);
								if(must == 0){
									mustupgrade = false ;
								}else if(must == 1){
									mustupgrade = true ;
								}
								mUpgradeHandler.post(mUpgradeRunnable); 
								if (!ToolsUtils.isShowHint(SettingActivity.this)) {
									Intent intent = new Intent(SettingActivity.this,HintDialogActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.putExtra("type", 0);
									intent.putExtra("must", must);
									intent.putExtra("downloadURL", updataUrl);
									intent.putExtra("apkName", apkName);
									SettingActivity.this.startActivity(intent);
								}
							}
							else
							{
								ToastUtil.showLongToast(SettingActivity.this.getString(R.string.version_name_head));
							}
						} else {
							MyLogger.getInstance().e("no updataUrl!!!!!");
							ToastUtil.showLongToast(SettingActivity.this.getString(R.string.version_name_head));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	//设置净水器可使用	
	private Runnable mUpgradeRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if(!Utils.inuse){//串口没有使用
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{		
						//c++用0表示false，1表示true
						setResult = mSerialPortUtil.setVerSwitch(false);
						MyLogger.getInstance().e("  setResult = "+setResult);	
						if(setResult<0){
							//faile to sent data	
							MyLogger.getInstance().e(" faile to sent data ");	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						MyLogger.getInstance().e("getReturn  returnsResult = "+ returnsResult);	
						if(returnsResult>=0){
							MyLogger.getInstance().e("  Ver Switch success  " + mustupgrade);	
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
	}; 
	
	//用来验证是不是真的绑定成功，防止绑定过程中的非法操作和数据库不同步，草泥马
	public void getDeviceCode(final UserInfo userInfo) {
		JSONObject jObj = new JSONObject();
		try 
		{
			jObj.accumulate("orderno", userInfo.getOrder_no());
			jObj.accumulate("pro_no", userInfo.getPro_no());
		}
		catch (Exception e)
		{
			
		}
		RequestParams params = new RequestParams(
				ConfigUtils.get_old_deviceCode_url);

		// params.setSslSocketFactory(sslSocketFactory)
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		params.setConnectTimeout(10000);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}
			@Override
			public void onSuccess(String response) {
				System.out.println("response==="+response);
				if (JsonUtils.result(response) == 0)//有返回，代表数据库没有绑定，则清除数据库里面的东西
				{
					unbindDevice(userInfo);
				}
			}
		});
	}

	private String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		return info.topActivity.getClassName(); // 完整类名
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 101)
		{
			finish();
		}
	}
	
	// 解绑接口
	private void unbindDevice(UserInfo userInfo) {
		while (true) {
			if (!Utils.inuse) {
				mSerialPortUtil = MyApplication.getSerialPortUtil();
				Utils.inuse = true;
				int times = 0;
				int setResult = -1;
				int returnsResult = -1;
				do {
					setResult = mSerialPortUtil.setUnbind();
					MyLogger.getInstance().e("  setResult = " + setResult);
					if (setResult < 0) {
						// faile to sent data
						MyLogger.getInstance().e(" faile to sent data ");
						break;
					}
					// succes to sent data
					returnsResult = mSerialPortUtil.getReturn();
					MyLogger.getInstance().e(
							"getReturn  returnsResult = " + returnsResult);
					if (returnsResult >= 0) {
						MyLogger.getInstance().e(" set  reset success  ");

						DBUtils.deleteAll(UserInfo.class);

						// 保存下机器码
						SharedPreferencesUtil.saveStringData(
								SettingActivity.this, "pro_no",
								userInfo.getPro_no());

						//清除下订单是否能解绑的标记
						SharedPreferencesUtil.saveIntData(
								SettingActivity.this, "tag", -1);
						
						// 清空下旧的订单号码
						SharedPreferencesUtil.saveStringData(SettingActivity.this,
								"oldOrderno", "");
						
						VerificationData verificationData = new VerificationData(SettingActivity.this);
						
						if(verificationData != null)
						{
							verificationData.clearVerificationData();
						}

						break;
					} else {
						times++;
						MyLogger.getInstance().e(" try times =   " + times);
					}
				} while (times < 3);
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
	
	
	//设置滤芯使用寿命	
	private Runnable mFilterLifeRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			int try_times=0;
			int life[] = {100, 300, 2000, 2000, mFilterLifeInfo.wfr};
			while(try_times<2){
				if(mSerialPortUtil.setFilterLife(life, life.length)>0 && mSerialPortUtil.getReturn()>=0){
					try_times++;
				}
				else{
					try_times++;
				}
			};
		}  
	}; 
	
	//设置套餐类型及总量
	private Runnable mQuantitykRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			mSerialPortUtil = MyApplication.getSerialPortUtil();
			UserInfo userInfo = DBUtils.getFirstData(UserInfo.class);
			while ((mSerialPortUtil.setPayType(userInfo.getPay_proid()) > 0 && mSerialPortUtil
					.getReturn() >= 0)) {
				if (userInfo.getPay_proid() == 0) {
					while ((mSerialPortUtil.setWaterVolume((int) Math
							.rint(userInfo.quantity)) > 0 && mSerialPortUtil
							.getReturn() >= 0)) {
						return;
					}
				} else if (userInfo.getPay_proid() == 1) {
					while ((mSerialPortUtil.setDueTime(translate) > 0 && mSerialPortUtil
							.getReturn() >= 0)) {
						return;
					}
				}
			}
		}  
	}; 
}