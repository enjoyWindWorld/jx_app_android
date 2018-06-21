package com.kxw.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.Utils;

public class RegulatingTemperatureActivity extends BaseActivity implements OnClickListener {
	
	private TextView txt_regulating_Temperature_add, txt_regulating_Temperature_reduce, txt_flow_adjustment_add, txt_flow_adjustment_reduce;
	private EditText edit_regulating_Temperature, edit_flow_adjustment, pwd_et;
	private Button regulating_Temperature_bt, flow_adjustment_bt, login_bt, reset_bt;
	private LinearLayout layout_regulating, layout_login;
	
	//修正温度句柄
	private Handler mRegulatingTemperatureHandler;
	private HandlerThread mRegulatingTemperatureHandlerThread;
	
	//修正流量句柄
	private Handler mFlowAdjustmentHandler;
	private HandlerThread mFlowAdjustmentHandlerThread;
	
	private SerialPortUtil mSerialPortUtil;
	private int temperature, flow;
	
	private Handler handler;
	private Message msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_regulating_temperature);
		
		txt_regulating_Temperature_add = (TextView) findViewById(R.id.txt_regulating_Temperature_add);
		txt_regulating_Temperature_reduce = (TextView) findViewById(R.id.txt_regulating_Temperature_reduce);
		txt_flow_adjustment_add = (TextView) findViewById(R.id.txt_flow_adjustment_add);
		txt_flow_adjustment_reduce = (TextView) findViewById(R.id.txt_flow_adjustment_reduce);
		
		edit_regulating_Temperature = (EditText) findViewById(R.id.edit_regulating_Temperature);
		edit_flow_adjustment = (EditText) findViewById(R.id.edit_flow_adjustment);
		pwd_et = (EditText) findViewById(R.id.pwd_et);
		
		regulating_Temperature_bt = (Button) findViewById(R.id.regulating_Temperature_bt);
		flow_adjustment_bt = (Button) findViewById(R.id.flow_adjustment_bt);
		login_bt = (Button) findViewById(R.id.login_bt);
		
		layout_regulating = (LinearLayout) findViewById(R.id.layout_regulating);
		layout_login = (LinearLayout) findViewById(R.id.layout_login);
		reset_bt = (Button) findViewById(R.id.reset_bt);
		
		txt_regulating_Temperature_add.setOnClickListener(this);
		txt_regulating_Temperature_reduce.setOnClickListener(this);
		txt_flow_adjustment_add.setOnClickListener(this);
		txt_flow_adjustment_reduce.setOnClickListener(this);
		regulating_Temperature_bt.setOnClickListener(this);
		flow_adjustment_bt.setOnClickListener(this);
		login_bt.setOnClickListener(this);
		reset_bt.setOnClickListener(this);
		
	    mRegulatingTemperatureHandlerThread = new HandlerThread("RegulatingTemperatureActivity_RegulatingTemperature", 5);  
	    mRegulatingTemperatureHandlerThread.start();  
	    mRegulatingTemperatureHandler = new Handler(mRegulatingTemperatureHandlerThread.getLooper()); 
	    
	    mFlowAdjustmentHandlerThread = new HandlerThread("RegulatingTemperatureActivity_FlowAdjustment", 5);  
	    mFlowAdjustmentHandlerThread.start();  
	    mFlowAdjustmentHandler = new Handler(mFlowAdjustmentHandlerThread.getLooper()); 
	    
		mSerialPortUtil = MyApplication.getSerialPortUtil();
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				try {
					switch (msg.arg1) 
					{
						case 0:
							MyToast.getManager(getApplicationContext()).show("温度修正成功！");
							temperature  = 0;
							edit_regulating_Temperature.setText(temperature+"");
							break;
						case 1:
							MyToast.getManager(getApplicationContext()).show("流量修正成功！");
							flow = 0;
							edit_flow_adjustment.setText(flow+"");
							break;
						case 2:
							MyToast.getManager(getApplicationContext()).show("机器修正失败！");
							break;
						default:
							break;
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.handleMessage(msg);
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId())
		{
			case R.id.txt_regulating_Temperature_add:
				if(temperature < 5)
				{
					temperature ++;
					edit_regulating_Temperature.setText(temperature+"");
				}
				break;
			case R.id.txt_regulating_Temperature_reduce:
				if(temperature > -5)
				{
					temperature --;
					edit_regulating_Temperature.setText(temperature+"");
				}
				break;
			case R.id.txt_flow_adjustment_add:
				if(flow < 50)
				{
					flow ++;
					edit_flow_adjustment.setText(flow+"");
				}
				break;
			case R.id.txt_flow_adjustment_reduce:
				if(flow > -50)
				{
					flow --;
					edit_flow_adjustment.setText(flow+"");
				}
				break;
			case R.id.regulating_Temperature_bt:
				if(temperature != 0)
				{
					 mRegulatingTemperatureHandler.post(mRegulatingTemperatureRunnable); 
				}
				break;
			case R.id.flow_adjustment_bt:
				if(flow != 0)
				{
					mFlowAdjustmentHandler.post(mFlowAdjustmentRunnable); 
				}
				break;
			case R.id.login_bt:
				if(pwd_et.getText().toString() == null)
				{
					MyToast.getManager(getApplicationContext()).show("请输入密码");
				}
				else if(!pwd_et.getText().toString().equals("12345"))
				{
					MyToast.getManager(getApplicationContext()).show("密码错误");
				}
				else
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
					// 强制隐藏软键盘
					imm.hideSoftInputFromWindow(pwd_et.getWindowToken(), 0); 
					layout_login.setVisibility(View.GONE);
					layout_regulating.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.title_back_ll:
				finish();
				break;
			case R.id.reset_bt:
				showHintDialog(5);
				break;
		}
	}
	
	/**
	 * 显示操作提示框
	 * @param type
	 */
	private void showHintDialog(int type)
	{
		Intent intent = new Intent(RegulatingTemperatureActivity.this, HintDialogActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("type", type);
		startActivityForResult(intent, 100);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 105)
		{
			setResult(101);
			finish();
		}
	}
	
	private Runnable mRegulatingTemperatureRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if(!Utils.inuse){//串口没有使用
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(temperature > 0)
						{
							setResult = mSerialPortUtil.setTemperatureCorrectionPlus(Math.abs(temperature));
						}
						else if(temperature < 0)
						{
							setResult = mSerialPortUtil.setTemperatureCorrectionReduce(Math.abs(temperature));
						}
						if(setResult<0){
							//faile to sent data	
							msg = handler.obtainMessage();
							msg.arg1 = 2;
							handler.sendMessage(msg);
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						if(returnsResult>=0){
							msg = handler.obtainMessage();
							msg.arg1 = 0;
							handler.sendMessage(msg);
							break;
						}else{
							times++;
						}
					}while(times < 3);
					Utils.inuse = false;
					if(times >= 3)
					{
						msg = handler.obtainMessage();
						msg.arg1 = 2;
						handler.sendMessage(msg);
					}
					return;
				}
			}
	    }  
	};
	
	private Runnable mFlowAdjustmentRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if(!Utils.inuse){//串口没有使用
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(flow > 0)
						{
							setResult = mSerialPortUtil.setFlowCorrectionPlus(Math.abs(flow));
						}
						else if(flow < 0)
						{
							setResult = mSerialPortUtil.setFlowCorrectionReduce(Math.abs(flow));
						}
						if(setResult<0){
							//faile to sent data	
							msg = handler.obtainMessage();
							msg.arg1 = 2;
							handler.sendMessage(msg);
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						if(returnsResult>=0){
							msg = handler.obtainMessage();
							msg.arg1 = 1;
							handler.sendMessage(msg);
							break;
						}else{
							times++;
						}
					}while(times < 3);
					Utils.inuse = false;
					if(times >= 3)
					{
						msg = handler.obtainMessage();
						msg.arg1 = 2;
						handler.sendMessage(msg);
					}
					return;
				}
			}
	    }  
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mRegulatingTemperatureHandler.removeCallbacks(mRegulatingTemperatureRunnable);  
		mFlowAdjustmentHandler.removeCallbacks(mFlowAdjustmentRunnable);  
	}
}
