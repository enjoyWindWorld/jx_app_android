/*
 * 温度设置
 */
package com.kxw.smarthome.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.R;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.view.CircleSeekBar;

public class SetTemperatureFragment extends Fragment implements OnSeekBarChangeListener, OnClickListener{

	private View view = null;
	private CircleSeekBar mCircleSeekBar;
	private static SeekBar sBar;
	private TextView now_temperature;
	private Button boiling_water;
	private int temperature_value; 
	private static Context ct; 

	byte[] mBuffer;
	private static int temperature,this_value,surplus_value=0,sum_value=0,tds=0;
	private static int sum=0,highbit=0,lowbit=0;
	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MyLogger.getInstance().e(" SetTemperatureFragment start");
		ct=getContext();
		if(view == null){
			view = inflater.inflate(R.layout.set_temperature_fragment, container,false); 
		}
		initView();	
		return view;


	}

	private void initView() {
		// TODO Auto-generated method stub
		sBar = (SeekBar)view.findViewById(R.id.set_temperature_seekBar);
		mCircleSeekBar = (CircleSeekBar)view.findViewById(R.id.set_temperature_progress);
		now_temperature = (TextView)view.findViewById(R.id.now_temperature_tv);
		boiling_water = (Button)view.findViewById(R.id.boiling_water_bt);
		boiling_water.setOnClickListener(this);
		sBar.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getTemperature();
	}

	private void getTemperature() {


		temperature_value =SharedPreferencesUtil.getIntData(getActivity(), "temperature_value",0);
		mSerialPortUtil =MyApplication.getSerialPortUtil();
		mBaseData=mSerialPortUtil.returnBaseData();
		System.out.println("temperature = "+mBaseData.toString());
		if(temperature_value==0){
			if(mBaseData!=null){
				sBar.setProgress(mBaseData.temperature);
				MyLogger.getInstance().e("temperature = "+mBaseData.temperature);
			}
		}else{
			sBar.setProgress(temperature_value);
		}
	}

	private void setTemperature() {

	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		temperature_value = progress;
		mCircleSeekBar.setProgress(progress);
		if(progress==25){
			now_temperature.setText(getString(R.string.normal_temperature_text)+"("+progress+"℃)");
			now_temperature.setTextColor(getResources().getColor(R.color.temperature_text));
		}else if(progress>=40&&progress<=60){
			now_temperature.setText(getString(R.string.blunt_milk_text)+"("+progress+"℃)");
			now_temperature.setTextColor(getResources().getColor(R.color.temperature_text));
		}else if(progress>=80&&progress<=95){
			now_temperature.setText(getString(R.string.make_tea_text)+"("+progress+"℃)");
			now_temperature.setTextColor(getResources().getColor(R.color.temperature_text));
		}else{
			now_temperature.setText(getString(R.string.now_temperature_text)+"("+progress+"℃)");
			now_temperature.setTextColor(getResources().getColor(R.color.now_temperature_text));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		//		UseWaterFragment.reset(temperature_value);
		MyLogger.getInstance().e("onStopTrackingTouch = ");
		SharedPreferencesUtil.saveIntData(
				getActivity(), "temperature_value",temperature_value);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.boiling_water_bt:
			Intent mIntent = new Intent("ON_TOUCH_ACTION");                 
            //发送广播  
            getActivity().sendBroadcast(mIntent);  
			sBar.setProgress(100);
			SharedPreferencesUtil.saveIntData(
					getActivity(), "temperature_value",100);
			break;

		default:
			break;
		}
	}

}