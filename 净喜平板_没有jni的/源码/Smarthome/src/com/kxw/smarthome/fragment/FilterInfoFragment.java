/*
 * 滤芯使用情况显示
 */
package com.kxw.smarthome.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.kxw.smarthome.R;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.utils.DBUtils;

public class FilterInfoFragment extends Fragment implements OnSeekBarChangeListener{

	private View view = null;
	private TextView equipment_cto,equipment_ppf,equipment_ro,equipment_t33;
	private SeekBar equipment_cto_seekbar,equipment_ppf_seekbar,equipment_ro_seekbar,equipment_t33_seekbar;
	private int cto=100,pp=100,ro=100,t33=100,wfr=100;
	private FilterLifeInfo mFilterLifeInfo;
//	private SerialPortUtil mSerialPortUtil;
//	private BaseData mBaseData;
	VerificationData verificationData;
	private Handler handler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view == null)
		{
			view = inflater.inflate(R.layout.filter_info_fragment, container,false); 
		}
		initView();
		handler = new Handler();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		
//		mSerialPortUtil =MyApplication.getSerialPortUtil();
		
		//现在只有四级
		equipment_ppf = (TextView)view.findViewById(R.id.equipment_ppf_tv);
		equipment_cto = (TextView)view.findViewById(R.id.equipment_cto_tv);
		equipment_ro = (TextView)view.findViewById(R.id.equipment_ro_tv);
		equipment_t33 = (TextView)view.findViewById(R.id.equipment_t33_tv);
		
		equipment_ppf_seekbar = (SeekBar)view.findViewById(R.id.equipment_ppf_seekbar);
		equipment_cto_seekbar = (SeekBar)view.findViewById(R.id.equipment_cto_seekbar);
		equipment_ro_seekbar = (SeekBar)view.findViewById(R.id.equipment_ro_seekbar);
		equipment_t33_seekbar = (SeekBar)view.findViewById(R.id.equipment_t33_seekbar);

		equipment_ppf_seekbar.setOnSeekBarChangeListener(this);
		equipment_cto_seekbar.setOnSeekBarChangeListener(this);
		equipment_ro_seekbar.setOnSeekBarChangeListener(this);
		equipment_t33_seekbar.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	    
		mFilterLifeInfo = new FilterLifeInfo();
		mFilterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
		verificationData = new VerificationData(getActivity());
				
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getEquipmentInfo();
			}
		}, 500);
	}

	private void initData() {
		// TODO Auto-generated method stub
		equipment_ppf.setText(pp+"%");
		equipment_cto.setText(cto+"%");
		equipment_t33.setText(t33+"%");
		equipment_ro.setText(ro+"%");

		equipment_ppf_seekbar.setProgress(pp);
		equipment_ppf_seekbar.setOnSeekBarChangeListener(this);
		equipment_cto_seekbar.setProgress(cto);
		equipment_cto_seekbar.setOnSeekBarChangeListener(this);
		equipment_ro_seekbar.setProgress(ro);
		equipment_ro_seekbar.setOnSeekBarChangeListener(this);
		equipment_t33_seekbar.setProgress(t33);
		equipment_t33_seekbar.setOnSeekBarChangeListener(this);
	}


	private void getEquipmentInfo() {
		// TODO Auto-generated method stub
		//调用接口获取各级滤芯使用状态
//		mBaseData = mSerialPortUtil.returnBaseData();
		if(mFilterLifeInfo!=null && verificationData != null && verificationData.getBindDate() != -1 && verificationData.getFirstFilter() != -1
				&& verificationData.getFivethFilter() != -1 && verificationData.getFourthFilter() != -1 && verificationData.getPay_proid() != -1
				&& verificationData.getSecondFilter() != -1 && verificationData.getThirdFilter() != -1 && verificationData.getTimeSurplus() != -1
				&& verificationData.getWaterSurplus() != -1){
			System.out.println("mFilterLifeInfo=="+mFilterLifeInfo.toString());
			pp = (int) Math.floor((float)verificationData.getFirstFilter()/mFilterLifeInfo.getPp()*100);
			cto = (int) Math.floor((float)verificationData.getSecondFilter()/mFilterLifeInfo.getCto()*100);
			ro =(int) Math.floor((float)verificationData.getThirdFilter()/mFilterLifeInfo.getRo()*100);
			t33 = (int) Math.floor((float)verificationData.getFourthFilter()/mFilterLifeInfo.getT33()*100);
			wfr = (int) Math.floor((float)verificationData.getFivethFilter()/mFilterLifeInfo.getWfr()*100);
			if(pp>100 || cto>100 || ro>100 || t33>100 || wfr>100)
			{
//				UseStateToast.getManager(getActivity()).showToast(getString(R.string.filter_get_failed));
				equipment_ppf.setText("100%");
				equipment_cto.setText("100%");
				equipment_t33.setText("100%");
				equipment_ro.setText("100%");
			}
			else
			{
				initData();
			}
		}
		else 
		{
			equipment_ppf.setText("100%");
			equipment_cto.setText("100%");
			equipment_t33.setText("100%");
			equipment_ro.setText("100%");
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(fromUser){
			equipment_ppf_seekbar.setProgress(pp);			
			equipment_cto_seekbar.setProgress(cto);
			equipment_ro_seekbar.setProgress(ro);
			equipment_t33_seekbar.setProgress(t33);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();		
	}
}