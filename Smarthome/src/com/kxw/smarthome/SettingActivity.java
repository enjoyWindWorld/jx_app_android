/*
 * 设置模块
*/
package com.kxw.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;

public class SettingActivity extends BaseActivity implements OnClickListener {
	
	private LinearLayout wifi_manage_ll, brightness_control_ll, volume_control_ll,device_code_ll,equipment_change_ll,version_change_ll, regulating_temperature_ll;
	private BaseData mBaseData;
	private UserInfo mUserInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.systen_setting_activity);

		initView();
//		initData();

	}

	private void initView() {
		// TODO Auto-generated method stub
		
		wifi_manage_ll = (LinearLayout)findViewById(R.id.wifi_manage_ll);
		brightness_control_ll = (LinearLayout)findViewById(R.id.brightness_control_ll);
		volume_control_ll = (LinearLayout)findViewById(R.id.volume_control_ll);
		device_code_ll = (LinearLayout)findViewById(R.id.device_code_ll);
		equipment_change_ll= (LinearLayout)findViewById(R.id.equipment_change_ll);
		version_change_ll = (LinearLayout)findViewById(R.id.version_change_ll);
		regulating_temperature_ll = (LinearLayout)findViewById(R.id.regulating_temperature_ll);
		
		version_change_ll.setOnClickListener(this);
		equipment_change_ll.setOnClickListener(this);
		wifi_manage_ll.setOnClickListener(this);
		brightness_control_ll.setOnClickListener(this);
		volume_control_ll.setOnClickListener(this);
		device_code_ll.setOnClickListener(this);
		regulating_temperature_ll.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
			intent.setClass(this, BindDeviceActivity.class);
			startActivity(intent);
			break;
			
		case R.id.equipment_change_ll:
			intent.setClass(this, FilterChangeListActivity.class);
			startActivity(intent);
			break;	
			
		case R.id.title_back_ll:
			finish();
			break;
			
		case R.id.version_change_ll:
			Intent upgrade_intent = new Intent(ConfigUtils.upgrade_version_alarm);
			sendBroadcast(upgrade_intent);
			
//			Intent reset_intent = new Intent(ConfigUtils.reset_device_alarm);
//			sendBroadcast(reset_intent);
//			DBUtils.deleteAll(UserInfo.class);
//			DBUtils.deleteAll(FilterLifeInfo.class);
			break;
		case R.id.regulating_temperature_ll:
			intent.setClass(this, RegulatingTemperatureActivity.class);
			startActivity(intent);
			break;
		
		default:
			break;
		}
	}
}