/*
 * 用水开关
 */
package com.kxw.smarthome.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.kxw.smarthome.entity.WaterStateInfo;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
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
	private boolean isStartState = true, isOpen = false, isOff = false;
	private Handler handler;
	private Message msg;
	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;
	private WaterStateInfo mWaterStateInfo;
	private StateThread mStateThread;
	private StartThread mStartThread;
	private OffThread mOffThread;
	private Button switchBt;
	private Button btn_reset;

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

		mBaseData = mSerialPortUtil.returnBaseData();
		if (mBaseData != null) {
			MyLogger.getInstance().e("mBaseData = " + mBaseData.toString());
			if (mBaseData.getTimeSurplus() == 65535) {
				total_title.setText(getString(R.string.total_flow));
				surplus_title.setText(getString(R.string.total_flow_surplus));
				total_flow_value.setText(Html.fromHtml(String.format(
						getString(R.string.total_flow_value),
						mBaseData.getWaterSum())));
				total_flow_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_flow_surplus_value),
						mBaseData.getWaterSurplus())));
				water_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_flow_surplus_value),
						mBaseData.getWaterSurplus())));
			} else {
				total_title.setText(getString(R.string.total_type_month));
				surplus_title
						.setText(getString(R.string.title_total_day_surplus));
				total_flow_value.setText("");
				total_flow_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_day_surplus_value),
						mBaseData.getTimeSurplus())));
				water_surplus.setText(Html.fromHtml(String.format(
						getString(R.string.total_make_flow_surplus_value),
						mBaseData.getWaterUsed())));
			}
			water_tds.setText(Html.fromHtml(String.format(
					getString(R.string.water_tds), mBaseData.getTds())));
			// if(mBaseData.state!=2||mBaseData.state!=4){
			if (mBaseData.state == 1) {
				UseStateToast.getManager(getActivity()).showToast(
						getString(R.string.use_state_1));
				Utils.isUsing = false;
			}
			if (mBaseData.state == 8) {
				UseStateToast.getManager(getActivity()).showToast(
						getString(R.string.use_state_8));
				Utils.isUsing = false;
			}
			if(mBaseData.state == 4 || mBaseData.state == 6)//检修或满水检修
			{
				UseStateToast.getManager(getActivity()).showToast(
						getString(R.string.use_state_4));
				Utils.isUsing = false;
			}
			/*
			 * else{
			 * UseStateToast.getManager(getActivity()).showToast(getString(
			 * R.string.use_state_error)); }
			 */
			water_temperature.setText(setTemperature + "℃");

			MyLogger.getInstance().e(" Utils.isUsing = " + Utils.isUsing);

		}
	}

	private void showBt() {
		try {
			if (Utils.isUsing) {
				switchBt.setBackgroundResource(R.drawable.start_water_bt);
			} else {
				switchBt.setBackgroundResource(R.drawable.stop_water_bt);
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
		MyLogger.getInstance().e(" isStartState  = " + isStartState);
		if (mStateThread != null) {
			mStateThread.interrupt();
			mStateThread = null;
		}
		mStateThread = new StateThread();
		mStateThread.start();
		
		/*
		 * temperature=SharedPreferencesUtil.getIntData(getActivity(),
		 * "temperature_value", 25);
		 * water_temperature.setText(Html.fromHtml(String
		 * .format(getString(R.string.water_temperature), Utils.temperature)));
		 */

	}
	
	
	
	int waterStatetimes = 0;
	int errTimes = 0;
	private class StateThread extends Thread {

		@Override
		public void run() {
			super.run();
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
						MyLogger.getInstance().e(" e " + e);
					}
					MyLogger.getInstance().e("  setValue = " + setValue);
					if (setValue < 0) {
						// faile to sent data
						MyLogger.getInstance().e(" faile to sent data ");
					}
					else
					{
						// succes to sent data
						returnValue = mSerialPortUtil.getWaterState();
						MyLogger.getInstance().e(
								"getReturn  returnValue = " + returnValue);
						System.out
								.println("---------------------------------出水状态：=="
										+ returnValue);
						if (returnValue >= 0) {
							MyLogger.getInstance().e(" off success   ");
							waterStatetimes = 0;
							errTimes = 0;
							if(!isOff && !isOpen)
							{
								msg = handler.obtainMessage();
								msg.arg1 = 2;
								handler.sendMessage(msg);
							}
						} else {
							waterStatetimes++;
							MyLogger.getInstance().e(
									"````````` try off times =   "
											+ waterStatetimes);
						}
					}


					if (waterStatetimes >= 3 || setValue < 0) {
						Utils.isUsing = false;
						waterStatetimes = 0;
						
						msg = handler.obtainMessage();
						msg.arg1 = 3;
						handler.sendMessage(msg);
						
//						errTimes ++;
//						if(errTimes > 3)//麻痹，一直在错就重启端口
//						{
//							errTimes = 0;
//							mSerialPortUtil.close();
//							mSerialPortUtil.getNewInstance();
//						}
					}

					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Utils.inuse = false;
						e.printStackTrace();
					}

					// 获取水量水质
					if (mSerialPortUtil.setBaseData() > 0) {
						int getBaseDataRet = mSerialPortUtil.getBaseData();
						System.out
								.println("---------------------------------水量水质：=="
										+ getBaseDataRet);
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
	}

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

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.start_water_bt:
			mSerialPortUtil = SerialPortUtil.getInstance();
			if (isFastDoubleClick()) {
				MyLogger.getInstance().e("is isFastDoubleClick");
				return;
			}
			Utils.isUsing = !Utils.isUsing;
			MyLogger.getInstance().e("is using" + Utils.isUsing);
			if (Utils.isUsing) {
				if (mStartThread != null) {
					mStartThread.interrupt();
					mStartThread = null;
				}
				isOpen = true;
				mStartThread = new StartThread();
				mStartThread.start(); // 开水
			} else {
				if (mOffThread != null) {
					mOffThread.interrupt();
					mOffThread = null;
				}
				isOff = true;
				mOffThread = new OffThread();
				mOffThread.start(); // 关水
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

	private class StartThread extends Thread {
		@Override
		public void run() {
			super.run();
			System.out.println("====开水=====isOpen="+isOpen+"; inuse=="+!Utils.inuse);
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
						MyLogger.getInstance().e("  setValue = " + setValue);
						if (setValue < 0) {
							// faile to sent data
							MyLogger.getInstance().e(" faile to sent data ");

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
						MyLogger.getInstance().e(
								"getBaseData  returnBaseValue = "
										+ returnBaseValue);
						if (returnBaseValue >= 0) {
							MyLogger.getInstance().e("  start success  ");
							
							Utils.inuse = false;
							isOpen = false;
							msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
							break;
						} else {
							times++;
							MyLogger.getInstance().e(
									"````````` start times =   " + times);
						}
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
							Utils.isUsing = false;
							Utils.inuse = false;
							isOpen = false;
						}
					} while (times < 5);
					
					System.out.println("开水尝试次数===="+times);
					
					if (times >= 5) { // 尝试3次开水后如果失败，则发送关水指令
						if (mOffThread != null) {
							mOffThread.interrupt();
							mOffThread = null;
						}
						isOff = true;
						isOpen = false;
						Utils.inuse = false;
						mOffThread = new OffThread();
						mOffThread.start();
					}
					break;
				}	
			}
			
			try {
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				Utils.inuse = false;
				isOpen = false;
			}
		}
	}

	private class OffThread extends Thread {
		@Override
		public void run() {
			super.run();
			System.out.println("====关水=====isOff=="+isOff+"; inuse=="+!Utils.inuse);
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
						MyLogger.getInstance().e("  setValue = " + setValue);
						if (setValue < 0) {
							// faile to sent data
							MyLogger.getInstance().e(" faile to sent data ");
							break;
						}
						// succes to sent data
						returnValue = mSerialPortUtil.getReturn();
						MyLogger.getInstance().e(
								"getReturn  returnValue = " + returnValue);
						if (returnValue >= 0) {
							MyLogger.getInstance().e(" off success   ");
							msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
							break;
						} else {
							times++;
							MyLogger.getInstance().e(
									"````````` try off times =   " + times);
						}
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							isOff = false;
							Utils.inuse = false;
							e.printStackTrace();
						}
					} while (times < 5);
					
					System.out.println("关水尝试次数===="+times);
					
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
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				isOff = false;
				Utils.inuse = false;
				e.printStackTrace();
			}
		}
	}

	private void mgetWaterState() {

		int waterStatetimes = 0;
		int setValue = -1;
		int returnValue = -1;

		do {
			try {
				setValue = mSerialPortUtil.setWaterState();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.getInstance().e(" e " + e);
			}
			MyLogger.getInstance().e("  setValue = " + setValue);
			if (setValue < 0) {
				// faile to sent data
				MyLogger.getInstance().e(" faile to sent data ");
				// break;
			}
			// succes to sent data
			returnValue = mSerialPortUtil.getWaterState();
			MyLogger.getInstance().e("getReturn  returnValue = " + returnValue);
			System.out.println("---------------------------------出水状态：==" + returnValue);
			if (returnValue >= 0) {
				MyLogger.getInstance().e(" off success   ");
				waterStatetimes = 0;
				msg = handler.obtainMessage();
				msg.arg1 = 2;
				handler.sendMessage(msg);

				// break;
			} else {
				waterStatetimes++;
				MyLogger.getInstance().e(
						"````````` try off times =   " + waterStatetimes);
			}
			System.out.println("-------------------------times==" + waterStatetimes);
		} while (waterStatetimes < 1);

		if (waterStatetimes >= 5 || setValue < 0) {
			waterStatetimes = 0;
			Utils.isUsing = false;
			msg = handler.obtainMessage();
			msg.arg1 = 3;
			handler.sendMessage(msg);
		}
		Utils.inuse = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		MyLogger.getInstance().e(" onPause ");
		mWaveHelper.cancel();
		if (mStateThread != null) {
			if (!mStateThread.isInterrupted() && isStartState)
				isStartState = false;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
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
}