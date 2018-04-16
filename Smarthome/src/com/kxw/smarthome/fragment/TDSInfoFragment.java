/*
 * TDS值显示
 */
package com.kxw.smarthome.fragment;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.R;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.UseStateToast;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

public class TDSInfoFragment extends Fragment{
	
	private View view = null;
	private TextView water_tds_value,water_type;
	private int tds_value;
	private String type;
	
	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(view == null){
			view = inflater.inflate(R.layout.tds_info_fragment, container,false); 
		}
		
		initView();		
			
		return view;
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		water_tds_value = (TextView)view.findViewById(R.id.water_tds_value);
		water_type = (TextView)view.findViewById(R.id.water_type);	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		getTDSValue();
	}

	private void getTDSValue() {
		
		mSerialPortUtil =MyApplication.getSerialPortUtil();
		mBaseData=mSerialPortUtil.returnBaseData();
		if(mBaseData!=null){
			tds_value=mBaseData.getTds();
			MyLogger.getInstance().e("tds = "+mBaseData.tds);			
			if(tds_value>=0 && tds_value<=9){
				type = getString(R.string.water_type_1);
			}
			if(tds_value>=10 && tds_value<90){
				type = getString(R.string.water_type_2);
			}
			if(tds_value>=90 && tds_value<=250){
				type = getString(R.string.water_type_3);
			}
			if(tds_value>=260 && tds_value<=600){
				type = getString(R.string.water_type_4);
			}
			if(tds_value>600){
				type = getString(R.string.water_type_5);
			}		
			
			water_type.setText(type);
			water_tds_value.setText(String.valueOf(tds_value));
		}else{
			UseStateToast.getManager(getActivity()).showToast(getString(R.string.filter_get_failed));
		}
	}
	
	private int tds(int value){
		if( value<=50){
			return value ;
		}else if(value>50 && value<=100){
			value = value-50;
		}else if(value>100 && value<=150){
			value = value-90;
		}else if(value>150 && value<=200){
			value = value-80;
		}else{
			value = value/3;
		}		
		return value ;
	}
}