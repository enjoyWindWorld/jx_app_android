/*
 * 用水开关
 */
package com.kxw.smarthome.fragment;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.FragmentMainActivity;
import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.R;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.entity.WaterStateInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.ToastUtil;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.UseStateToast;
import com.kxw.smarthome.utils.Utils;
import com.kxw.smarthome.view.WaveHelper;
import com.kxw.smarthome.view.WaveView;

public class UseWaterFragment extends Fragment implements OnClickListener {

	private View view = null;
	private WaveHelper mWaveHelper;
	private WaveView waveView;
	private TextView total_flow_value, total_flow_surplus, total_title,
			surplus_title;
	private static TextView water_temperature;
	private TextView water_tds;
	private TextView water_surplus;
	private static int setTemperature;
	private int mBorderColor = Color.parseColor("#551bb6ef");
	private int mBorderWidth = 2;
	private boolean isStartState = true, isOpen = false, isOff = false, isUpdateFilterLife = false;
	private Handler handler;
	private Message msg;
	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;
	private WaterStateInfo mWaterStateInfo;
	private Button switchBt;
	private Button btn_reset;
	private FragmentMainActivity mFragmentMainActivity;
	private UserInfo userInfo;
	
	//检测用水状态句柄
	private Handler mStatusHandler;
	private HandlerThread mStatusHandlerThread;
	
	//开水句柄
	private Handler mStartHandler;
	private HandlerThread mStartHandlerThread;
	
	//关水句柄
	private Handler mOffHandler;
	private HandlerThread mOffHandlerThread;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (view == null) {
			view = inflater.inflate(R.layout.use_water_fragment, container,
					false);
		}
		mSerialPortUtil = MyApplication.getSerialPortUtil();
		initView();
		setTemperature = SharedPreferencesUtil.getIntData(getActivity(),
				"temperature_value", 25);
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		total_flow_value = (TextView) view.findViewById(R.id.total_flow_value);
		total_flow_surplus = (TextView) view
				.findViewById(R.id.total_flow_surplus);
		water_temperature = (TextView) view
				.findViewById(R.id.water_temperature);
		total_title = (TextView) view.findViewById(R.id.total_title_tv);
		surplus_title = (TextView) view.findViewById(R.id.surplus_title_tv);

		water_surplus = (TextView) view.findViewById(R.id.water_surplus);
		water_tds = (TextView) view.findViewById(R.id.water_tds);
		waveView = (WaveView) view.findViewById(R.id.wave);
		mWaveHelper = new WaveHelper(waveView);
		waveView.setBorder(mBorderWidth, mBorderColor);
		waveView.setShapeType(WaveView.ShapeType.CIRCLE);

		switchBt = (Button) view.findViewById(R.id.start_water_bt);
		btn_reset = (Button) view.findViewById(R.id.btn_reset);
		
		switchBt.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	    mStatusHandlerThread = new HandlerThread("UseWaterFragment_Status", 5);  
	    mStatusHandlerThread.start();  
	    mStatusHandler = new Handler(mStatusHandlerThread.getLooper()); 
	    
	    mStartHandlerThread = new HandlerThread("UseWaterFragment_Start", 5);  
	    mStartHandlerThread.start();  
	    mStartHandler = new Handler(mStartHandlerThread.getLooper()); 
	    
	    mOffHandlerThread = new HandlerThread("UseWaterFragment_Off", 5);  
	    mOffHandlerThread.start();  
	    mOffHandler = new Handler(mOffHandlerThread.getLooper()); 

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				try {
					switch (msg.arg1) {

					case 0:
						initData();
						showBt();
						break;

					case 1:
						UseStateToast.getManager(getActivity()).showToast(
								getString(R.string.use_state_error));
						initData();
						showBt();
						break;

					case 2:
						initData();
						changeUseState();
						showBt();
						break;

					case 3:
						UseStateToast.getManager(getActivity()).showToast(
								getString(R.string.use_state_error));
						initData();
						showBt();
						/*
						 * initData(); changeUseState();
						 */
						break;

					default:
						break;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MyLogger.getInstance().e("e  " + e);
				}
				super.handleMessage(msg);
			}
		};

		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		userInfo = DBUtils.getFirstData(UserInfo.class);
		mBaseData = mSerialPortUtil.returnBaseData();
		
		if (mBaseData != null)
		{
			MyLogger.getInstance().e("mBaseData = " + mBaseData.toString());
			if (mBaseData.getTimeSurplus() != 65535)
			{
				//包年
				total_title.setText(getString(R.string.total_type_month));
				surplus_title
						.setText(getString(R.string.title_total_day_surplus));
				total_flow_value.setText("");
				total_flow_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_day_surplus_value),
						mBaseData.getTimeSurplus() == 65535 ? 0 : mBaseData.getTimeSurplus())));
				water_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_make_flow_surplus_value),
						Double.parseDouble(mBaseData.getWaterUsed()+"")/1000)));
			} 
			else
			{
				//包流量
				total_title.setText(getString(R.string.total_flow));
				surplus_title.setText(getString(R.string.total_flow_surplus));
				total_flow_value.setText(Html.fromHtml(String.format(
						getString(R.string.total_flow_value),
						mBaseData.getWaterSum())));
				total_flow_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_flow_surplus_value),
						mBaseData.getWaterSurplus() == 65535 ? 0 : mBaseData.getWaterSurplus())));
				water_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_flow_surplus_value),
						mBaseData.getWaterSurplus() == 65535 ? 0 : mBaseData.getWaterSurplus())));
			}
			water_tds.setText(Html.fromHtml(String.format(
					getString(R.string.water_tds), mBaseData.getTds())));
			// if(mBaseData.state!=2||mBaseData.state!=4){
			if (mBaseData.state == 1) {//缺少状态
				UseStateToast.getManager(getActivity()).showToast(
						getString(R.string.use_state_1));
				Utils.isUsing = false;
			}
//			if (mBaseData.state == 8) {
//				UseStateToast.getManager(getActivity()).showToast(
//						getString(R.string.use_state_8));
//				Utils.isUsing = false;
//			}
			if(mBaseData.state == 4 || mBaseData.state == 6)//检修或满水检修
			{
				UseStateToast.getManager(getActivity()).showToast(
						getString(R.string.use_state_4));
				Utils.isUsing = false;
			}
			water_temperature.setText(setTemperature + "℃");
		}
	}

	private void showBt() {
		try {
			if (Utils.isUsing) {
				isUpdateFilterLife = true;
				switchBt.setBackgroundResource(R.drawable.start_water_bt);
			} else {
				switchBt.setBackgroundResource(R.drawable.stop_water_bt);
				if(isUpdateFilterLife)
				{
					updateFilterInfo(SharedPreferencesUtil.getStringData(getActivity(), "pro_no", ""), mBaseData);
					isUpdateFilterLife = false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mWaveHelper.start();
		// 开启查询当前用水开关状态，当前水质水量等值的线程
		isStartState = true;
	    mStatusHandler.post(mStatusRunnable); 
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mWaveHelper.cancel();
		isStartState = false;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isStartState = false;
		mStatusHandler.removeCallbacks(mStatusRunnable);  
	}
	
	
	//检测到物理键用水
	int waterStatetimes = 0;
	private Runnable mStatusRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (isStartState) {
				if (!Utils.inuse && !isOpen && !isOff) {// 串口没有使用, 没有开关水的操作
					Utils.inuse = true;// 串口正在被使用。。。

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Utils.inuse = false;
						e.printStackTrace();
					}

					// 获取用水开关状态
					int setValue = -1;
					int returnValue = -1;

					try {
						setValue = mSerialPortUtil.setWaterState();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Utils.inuse = false;
					}
					if (setValue < 0) {
						// faile to sent data
					}
					else
					{
						// succes to sent data
						returnValue = mSerialPortUtil.getWaterState();
						if (returnValue >= 0) {
							waterStatetimes = 0;
							if(!isOff && !isOpen)
							{
								msg = handler.obtainMessage();
								msg.arg1 = 2;
								handler.sendMessage(msg);
							}
						} else {
							waterStatetimes++;
						}
					}


					if (waterStatetimes >= 3 || setValue < 0) {
						Utils.isUsing = false;
						waterStatetimes = 0;
						
						msg = handler.obtainMessage();
						msg.arg1 = 3;
						handler.sendMessage(msg);
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Utils.inuse = false;
						e.printStackTrace();
					}

					// 获取水量水质
					if (mSerialPortUtil.setBaseData() > 0) {
						int getBaseDataRet = mSerialPortUtil.getBaseData();
						if (getBaseDataRet >= 0) {
							msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
						}
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Utils.inuse = false;
						e.printStackTrace();
					}

					Utils.inuse = false;// 串口使用完毕。。。
				}
			}
		}  
	};
	
	private Runnable mStartRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while(isOpen)
			{
				while (!Utils.inuse) {
					Utils.inuse = true;
					int times = 0;
					int setValue = -1;
					int returnBaseValue = -1;
					do {
						setValue = mSerialPortUtil.setWaterSwitch(
								Utils.isUsing, setTemperature);
						if (setValue < 0) {
							// faile to sent data
							Utils.isUsing = false;
							Utils.inuse = false;
							isOpen = false;
							
							msg = handler.obtainMessage();
							msg.arg1 = 1;
							handler.sendMessage(msg);
							break;
						}
						// succes to sent data
						returnBaseValue = mSerialPortUtil.getBaseData();
						if (returnBaseValue >= 0) {
							Utils.inuse = false;
							isOpen = false;
							msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
							break;
						} else {
							times++;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
							Utils.isUsing = false;
							Utils.inuse = false;
							isOpen = false;
						}
					} while (times < 5);
					
					if (times >= 5) { // 尝试3次开水后如果失败，则发送关水指令
						isOff = true;
						isOpen = false;
						Utils.inuse = false;
						mOffHandler.post(mOffRunnable); // 关水
					}
					break;
				}	
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				Utils.inuse = false;
				isOpen = false;
			}
		
	    }  
	};
	
	private Runnable mOffRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while(isOff)
			{
				while (!Utils.inuse) {
					Utils.inuse = true;
					int times = 0;
					int setValue = -1;
					int returnValue = -1;
					do {
						setValue = mSerialPortUtil.setWaterSwitch(false,
								setTemperature);
						if (setValue < 0) {
							// faile to sent data
							break;
						}
						// succes to sent data
						returnValue = mSerialPortUtil.getReturn();
						if (returnValue >= 0) {
							msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
							break;
						} else {
							times++;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							isOff = false;
							Utils.inuse = false;
							e.printStackTrace();
						}
					} while (times < 5);
										
					if (times >= 5 || setValue < 0) {
						msg = handler.obtainMessage();
						msg.arg1 = 1;
						handler.sendMessage(msg);
					}
					
					isOff = false;
					Utils.inuse = false;
					break;
				}
			}


			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				isOff = false;
				Utils.inuse = false;
				e.printStackTrace();
			}
		
	    }  
	};

	private void changeUseState() {
		// 调用底层接口，获取用水状态、温度、tds值
		mWaterStateInfo = mSerialPortUtil.returnWaterStateInfo();
		MyLogger.getInstance().e(
				"mWaterStateInfo = " + mWaterStateInfo.toString());
		if (mWaterStateInfo != null) {
			if (mWaterStateInfo.useState == 0) {
				Utils.isUsing = false;
			} else if (mWaterStateInfo.useState == 1) {
				Utils.isUsing = true;
			}

			if (mWaterStateInfo.temperature == 1) {
				setTemperature = 25;
			} else if (mWaterStateInfo.temperature == 2) {
				setTemperature = 50;
			} else if (mWaterStateInfo.temperature == 3) {
				setTemperature = 85;
			} else if (mWaterStateInfo.temperature == 4) {
				setTemperature = 100;
			}
			water_temperature.setText(setTemperature + "℃");
			SharedPreferencesUtil.saveIntData(getActivity(),
					"temperature_value", setTemperature);
			
			SharedPreferencesUtil.saveIntData(getActivity(),
					"model", mWaterStateInfo.model);//机型
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.start_water_bt:

			if (((mBaseData.waterSurplus <= 0 || mBaseData.waterSurplus == 65535) && (mBaseData.timeSurplus <= 0 || mBaseData.timeSurplus == 65535)) && userInfo != null)
			{
				ToastUtil.showShortToast("您的服务套餐已用光，请使用手机APP续费！");
				return;
			}
			
			mSerialPortUtil = SerialPortUtil.getInstance();
			if (isFastDoubleClick()) {
				MyLogger.getInstance().e("is isFastDoubleClick");
				return;
			}
			Utils.isUsing = !Utils.isUsing;
			MyLogger.getInstance().e("is using" + Utils.isUsing);
			if (Utils.isUsing) {
				isOpen = true;
			    mStartHandler.post(mStartRunnable); // 开水
			} else {
				isOff = true;
				mOffHandler.post(mOffRunnable); // 关水
			}

			if (Utils.isUsing) {
				switchBt.setBackgroundResource(R.drawable.start_water_bt);
			} else {
				switchBt.setBackgroundResource(R.drawable.stop_water_bt);
			}
			break;
		case R.id.btn_reset:
			mSerialPortUtil.close();
			mSerialPortUtil.getNewInstance();
			break;
		}

	}

	// 避免重复点击
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long lead_time = time - lastClickTime;
		if (0 < lead_time && lead_time < 1500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	
	/**
	 * 上传滤芯寿命和用水
	 * @param pro_no
	 * @param mBaseData
	 */
	public void updateFilterInfo(String pro_no,BaseData mBaseData){
		JSONObject jObj = new JSONObject();		
		FilterLifeInfo mFilterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
		VerificationData verificationData = new VerificationData(getActivity());
		if(!ToolsUtils.isEmpty(pro_no) && mBaseData != null && mFilterLifeInfo != null && userInfo != null
				&& verificationData != null && verificationData.getBindDate() != -1 && verificationData.getFirstFilter() != -1
				&& verificationData.getFivethFilter() != -1 && verificationData.getFourthFilter() != -1 && verificationData.getPay_proid() != -1
				&& verificationData.getSecondFilter() != -1 && verificationData.getThirdFilter() != -1 && verificationData.getTimeSurplus() != -1
				&& verificationData.getWaterSurplus() != -1){
			try {		
				
				System.out.println("mBaseData===="+mBaseData.toString());
				
				jObj.accumulate("pro_id", pro_no);
				jObj.accumulate("pp", verificationData.getFirstFilter() < 0 ? 0 : verificationData.getFirstFilter());
				jObj.accumulate("cto", verificationData.getSecondFilter() < 0 ? 0 : verificationData.getSecondFilter());
				jObj.accumulate("ro", verificationData.getThirdFilter() < 0 ? 0 : verificationData.getThirdFilter());
				jObj.accumulate("t33", verificationData.getFourthFilter() < 0 ? 0 : verificationData.getFourthFilter());
				jObj.accumulate("wfr", verificationData.getFivethFilter() < 0 ? 0 : verificationData.getFivethFilter());	
				jObj.accumulate("temperature", mBaseData.getTemperature());
				jObj.accumulate("tds", mBaseData.getTds());	
				jObj.accumulate("output_water", mBaseData.getWaterUsed());	
				jObj.accumulate("ordno", userInfo.getOrder_no());	
					
				if(userInfo.pay_proid == 0){
					jObj.accumulate("restflow", verificationData.getWaterSurplus() < 0 ? 0 : verificationData.getWaterSurplus());	
					jObj.accumulate("surplus_day", 0);	
				}else{
					jObj.accumulate("restflow", "0");	
					jObj.accumulate("surplus_day", verificationData.getTimeSurplus() < 0 ? 0 : verificationData.getTimeSurplus());	
				}		
				jObj.accumulate("code", SharedPreferencesUtil.getStringData(getActivity(), "province", ""));
				
			} catch (Exception e) {
			}
		}
		System.out.println("==fuck you== "+jObj.toString());
		RequestParams params= new RequestParams(ConfigUtils.update_filterInfo_url);
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
				// TODO Auto-generated method stub
				if(JsonUtils.result(response)==0){
					
				}
			}
		});
	}
}