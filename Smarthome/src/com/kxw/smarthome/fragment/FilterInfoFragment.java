/*
 * 滤芯使用情况显示
 */
package com.kxw.smarthome.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.R;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.UseStateToast;

public class FilterInfoFragment extends Fragment implements OnSeekBarChangeListener{

	private View view = null;
	private TextView equipment_cto,equipment_ppf,equipment_ro,equipment_t33,equipment_postposition,equipment_wfr;
	private SeekBar equipment_cto_seekbar,equipment_ppf_seekbar,equipment_ro_seekbar,equipment_t33_seekbar,equipment_postposition_seekbar,equipment_wfr_seekbar;
	private int cto=100,pp=100,ro=100,t33=100,postposition=100,wfr=100;
	private FilterLifeInfo mFilterLifeInfo;
	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if(view == null){
			view = inflater.inflate(R.layout.filter_info_fragment, container,false); 
		}
		initView();

		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		equipment_ppf = (TextView)view.findViewById(R.id.equipment_ppf_tv);
		equipment_cto = (TextView)view.findViewById(R.id.equipment_cto_tv);
		equipment_ro = (TextView)view.findViewById(R.id.equipment_ro_tv);
		equipment_t33 = (TextView)view.findViewById(R.id.equipment_t33_tv);
		//		equipment_postposition = (TextView)view.findViewById(R.id.equipment_postposition_tv);
		equipment_wfr = (TextView)view.findViewById(R.id.equipment_wfr_tv);

		equipment_ppf_seekbar = (SeekBar)view.findViewById(R.id.equipment_ppf_seekbar);
		equipment_cto_seekbar = (SeekBar)view.findViewById(R.id.equipment_cto_seekbar);
		equipment_ro_seekbar = (SeekBar)view.findViewById(R.id.equipment_ro_seekbar);
		equipment_t33_seekbar = (SeekBar)view.findViewById(R.id.equipment_t33_seekbar);
		//		equipment_postposition_seekbar = (SeekBar)view.findViewById(R.id.equipment_postposition_seekbar);
		equipment_wfr_seekbar = (SeekBar)view.findViewById(R.id.equipment_wfr_seekbar);
		equipment_ppf_seekbar.setOnSeekBarChangeListener(this);
		equipment_cto_seekbar.setOnSeekBarChangeListener(this);
		equipment_ro_seekbar.setOnSeekBarChangeListener(this);
		equipment_t33_seekbar.setOnSeekBarChangeListener(this);
		//		equipment_postposition_seekbar.setProgress(postposition);
		//		equipment_postposition_seekbar.setOnSeekBarChangeListener(this);
		equipment_wfr_seekbar.setOnSeekBarChangeListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mSerialPortUtil = MyApplication.getSerialPortUtil();

		mFilterLifeInfo = new FilterLifeInfo();
		mFilterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
		MyLogger.getInstance().e("mFilterLifeInfo  =  "+ mFilterLifeInfo);
		getEquipmentInfo();

	}

	private void initData() {
		// TODO Auto-generated method stub
		equipment_ppf.setText(pp+"%");
		equipment_cto.setText(cto+"%");
		equipment_t33.setText(t33+"%");
		equipment_ro.setText(ro+"%");
		//		equipment_postposition.setText(postposition+"%");
		equipment_wfr.setText(wfr+"%");

		equipment_ppf_seekbar.setProgress(pp);
		equipment_ppf_seekbar.setOnSeekBarChangeListener(this);
		equipment_cto_seekbar.setProgress(cto);
		equipment_cto_seekbar.setOnSeekBarChangeListener(this);
		equipment_ro_seekbar.setProgress(ro);
		equipment_ro_seekbar.setOnSeekBarChangeListener(this);
		equipment_t33_seekbar.setProgress(t33);
		equipment_t33_seekbar.setOnSeekBarChangeListener(this);
		//		equipment_postposition_seekbar.setProgress(postposition);
		//		equipment_postposition_seekbar.setOnSeekBarChangeListener(this);
		equipment_wfr_seekbar.setProgress(wfr);
		equipment_wfr_seekbar.setOnSeekBarChangeListener(this);
	}


	private void getEquipmentInfo() {
		// TODO Auto-generated method stub
		//调用接口获取各级滤芯使用状态

		mBaseData=mSerialPortUtil.returnBaseData();
		MyLogger.getInstance().e(mBaseData.toString());
//		System.out.println("mBaseData=="+mBaseData.toString());
//		System.out.println("mFilterLifeInfo=="+mFilterLifeInfo.toString());
		if(mBaseData!=null && mFilterLifeInfo!=null){
			pp = (int) Math.floor((float)mBaseData.getFirstFilter()/mFilterLifeInfo.getPp()*100);
			cto = (int) Math.floor((float)mBaseData.getSecondFilter()/mFilterLifeInfo.getCto()*100);
			ro =(int) Math.floor((float)mBaseData.getThirdFilter()/mFilterLifeInfo.getRo()*100);
			t33 = (int) Math.floor((float)mBaseData.getFourthFilter()/mFilterLifeInfo.getT33()*100);
			wfr = (int) Math.floor((float)mBaseData.getFivethFilter()/mFilterLifeInfo.getWfr()*100);
			if(pp>100 || cto>100 || ro>100 || t33>100 || wfr>100)
				UseStateToast.getManager(getActivity()).showToast(getString(R.string.filter_get_failed));
			initData();
		}
		if(mFilterLifeInfo == null){
			equipment_ppf.setText("100%");
			equipment_cto.setText("100%");
			equipment_t33.setText("100%");
			equipment_ro.setText("100%");
			//		equipment_postposition.setText(postposition+"%");
			equipment_wfr.setText("100%");
		}
		
		//现在不知道有没有用
//		if(mBaseData.getFirstFilter() == 0 && mBaseData.getSecondFilter() == 0 && mBaseData.getThirdFilter() == 0 && 
//				mBaseData.getFourthFilter() == 0 && mBaseData.getFivethFilter() == 0){
//			UseStateToast.getManager(getActivity()).showToast(getString(R.string.filter_get_failed));
//		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(fromUser){
			MyLogger.getInstance().e("fromUser = "+fromUser);
			equipment_ppf_seekbar.setProgress(pp);			
			equipment_cto_seekbar.setProgress(cto);
			equipment_ro_seekbar.setProgress(ro);
			equipment_t33_seekbar.setProgress(t33);
			//			equipment_postposition_seekbar.setProgress(postposition);
			equipment_wfr_seekbar.setProgress(wfr);
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
}