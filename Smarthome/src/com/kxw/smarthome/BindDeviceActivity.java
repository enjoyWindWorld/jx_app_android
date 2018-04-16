/*
 * 绑定设备，绑定设备的只要流程是：
 * 1.填写订单号，然后服务器生成一个UUID作为此台设备的设备码，并将订单的内容传递到平板
 * 2.平板将订单中的付费方式（包年或包流量）、套餐量（包年为365天，包流量为具体的流量值）写入单片机
 * 3.写完付费方式与套餐量后，查询当前地址的滤芯使用时长，然后写入单片机
 * 4.当套餐与滤芯都写入成功，则回调绑定成功接口
 */
package com.kxw.smarthome;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.LoadingDialog;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.Utils;

public class BindDeviceActivity extends BaseActivity implements OnClickListener{

	private EditText phone_num_et;
	private Button get_device_code_bt;
	private LinearLayout bind_view;
	private LinearLayout hit_view;
	private TextView phone_num;
	private static Context context;
	private UserInfo userInfo;
	private FilterLifeInfo mFilterLifeInfo;
	private int pay_proid=-1,times=0;
	private boolean set_payType=false,set_value=false,set_life=false;
	private int setting = -1;
	private SerialPortUtil mSerialPortUtil;

	private WorkThread mWorkThread;
	private FilterLifeThread mFilterLifeThread;

	private boolean setType=true;
	private boolean setLife=true;

	private Handler handler;
	private Message msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.device_code_activity);
		context=this;
		initView();
		handler = new Handler() {  
			@Override  
			public void handleMessage(Message msg) {  
				MyLogger.getInstance().e(msg.toString());
				switch (msg.arg1) {

				case 0:
					setType=false;
					setLife=false;
//					DBUtils.updateDB(userInfo);
					initData();			
					LoadingDialog.loadingSuccess();
					break;

				case 1:
					bindFailed();
					break;

				default:
					break;
				}
				super.handleMessage(msg);  
			}
		}; 		
		initData();	
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initView() {
		// TODO Auto-generated method stub

		phone_num_et = (EditText)findViewById(R.id.phone_num_et);
		get_device_code_bt = (Button)findViewById(R.id.get_device_code_bt);
		get_device_code_bt.setOnClickListener(this);

		bind_view = (LinearLayout)findViewById(R.id.bind_view);
		hit_view = (LinearLayout)findViewById(R.id.hit_view);
		phone_num = (TextView)findViewById(R.id.phone_num);
	}

	private void initData() {
		// TODO Auto-generated method stub
		userInfo = new UserInfo();
		userInfo=DBUtils.getFirstData(UserInfo.class);

		if(userInfo!=null){
			MyLogger.getInstance().e(userInfo.toString());
			bind_view.setVisibility(View.GONE);
			hit_view.setVisibility(View.VISIBLE);
			//			phone_num.setText(userInfo.phone_num());
			phone_num.setText(Html.fromHtml(String.format(getString(R.string.order_no),userInfo.getOrder_no())));
		}else{
			MyLogger.getInstance().e("userInfo = null");
			hit_view.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.title_back_ll:
			finish();
			break;

		case R.id.get_device_code_bt:		
			/*	getFilterLife();
			new LoadingDialog(context); */
			if(!NetUtils.isConnected(context)){
				MyToast.getManager(context).show(getString(R.string.network_disconnected));
				return;
			}else{
				String orderNo= phone_num_et.getText().toString();
				if(orderNo!=null&&orderNo.length()>0){
					new LoadingDialog(context); 
					getDeviceCode(orderNo);
				}else{
					MyToast.getManager(context).show(getString(R.string.edit_text_error));
				}
			}
			break;

		default:
			break;
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		setType=false;
		setLife=false;
	}

	private void bindFailed(){
		setType=false;
		setLife=false;
		LoadingDialog.loadingFailed();
		DBUtils.deleteAll(UserInfo.class);
	}

	//设置套餐类型及总量
	private class WorkThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(setType){
				times++;
				mSerialPortUtil =MyApplication.getSerialPortUtil();
				MyLogger.getInstance().e("WorkThread success  userInfo"+userInfo.toString());
				while((mSerialPortUtil.setPayType(userInfo.getPay_proid()) > 0 && mSerialPortUtil.getReturn()>=0)){
					if(userInfo.getPay_proid()==0){
						MyLogger.getInstance().e("set vol "+(int)Math.rint(userInfo.getQuantity()+0.5));
						while((mSerialPortUtil.setWaterVolume((int)Math.rint(userInfo.getQuantity()+0.5))>0 && mSerialPortUtil.getReturn()>=0)){
							times=0;
							getFilterLife();
							return;						
						}
					}else if(userInfo.getPay_proid()==1){
						MyLogger.getInstance().e("set time "+(int)Math.rint(userInfo.getQuantity()));
						while((mSerialPortUtil.setDueTime((int)Math.rint(userInfo.getQuantity()))>0 && mSerialPortUtil.getReturn()>=0)){
							times=0;
							getFilterLife();
							return;		
						}
					}
				}	
				if(times>=10){
					msg= handler.obtainMessage();
					msg.arg1=1;
					handler.sendMessage(msg);
					return;
				}
			}
		}
	}

	//设置滤芯使用寿命
	private class FilterLifeThread extends Thread {

		@Override
		public void run() {
			super.run();
			setType=false;
			int try_times=0;
			MyLogger.getInstance().e(" FilterLifeThread start ");

			int life[] = {mFilterLifeInfo.pp,mFilterLifeInfo.cto,mFilterLifeInfo.ro,mFilterLifeInfo.t33,mFilterLifeInfo.wfr};

			MyLogger.getInstance().e(" set  FilterLife = "+life[0]+" , "+life[1]+" , "+life[2]+" , "+life[3]+" , "+life[4]);
			while(setLife){
				while(try_times<5){
					if(mSerialPortUtil.setFilterLife(life, life.length)>0 && mSerialPortUtil.getReturn()>=0){
						try_times=0;
						MyLogger.getInstance().e(" set  success");
						break;
					}else{
						try_times++;
						MyLogger.getInstance().e(" set  try_times = " +try_times);
					}
				};
				if(try_times>=5){
					break;
				}
				while(try_times<5){
					MyLogger.getInstance().e(" save data ");
//					MyToast.getManager(context).show(userInfo.toString());
					MyLogger.getInstance().e(userInfo.toString());
					MyLogger.getInstance().e(mFilterLifeInfo.toString());
					if(DBUtils.saveDB(userInfo) &&
							DBUtils.saveDB(mFilterLifeInfo)){
						MyLogger.getInstance().e(" save data success");
						setLife=false;
						break;	
					}else{
						try_times++;
					}			
				}
				if(try_times>=5){
					break;
				}
			}
			if(try_times>=5){
				msg= handler.obtainMessage();
				msg.arg1=1;
				handler.sendMessage(msg);				
				DBUtils.deleteAll(FilterLifeInfo.class);
			}else{
				bindingFeedback();
			}
		}
	}

	//获取设备码
	public  void getDeviceCode(final String orderNo){

		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("orderno", orderNo);
			//			jObj.accumulate("pro_no", userInfo.getPro_no());
		} catch (Exception e) {
		}
		RequestParams params= new RequestParams(ConfigUtils.get_deviceCode_url);
		//		params.setSslSocketFactory(sslSocketFactory)
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyToast.getManager(context).show(getString(R.string.device_bind_failed));
				LoadingDialog.loadingFailed();
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e(response);
				userInfo = new UserInfo();

				if(JsonUtils.result(response)==0){				
					List<UserInfo> list= new ArrayList<UserInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),UserInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//					UseStateToast.getManager(context).showToast("  list ="+list.toString());
					MyLogger.getInstance().e("  list ="+list.toString());
					if(list!=null&&list.size()>0){
						userInfo= list.get(0);				
						userInfo.setOrder_no(orderNo);
						userInfo._id=1;
						MyLogger.getInstance().e("  userInfo ="+userInfo.toString());
						DBUtils.deleteAll(UserInfo.class);
						//						if(DBUtils.updateDB(userInfo)){
						Utils.pro_no=userInfo.getPro_no();
						Utils.payment_type=userInfo.getPay_proid();
						if(mWorkThread!=null){
							mWorkThread.interrupt();
							mWorkThread=null;
						}
						mWorkThread=new WorkThread();
						mWorkThread.start();
						//						}else{
						//							LoadingDialog.loadingFailed();
						//						}
					}else{
						LoadingDialog.loadingFailed();
					}				
				}else{
					LoadingDialog.loadingFailed();
				}
			}
		});
	}

	//获取滤芯寿命
	public  void getFilterLife(){

		if(!NetUtils.isConnected(context)){
			LoadingDialog.loadingFailed();
			return;
		}
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("code", Utils.province);
		} catch (Exception e) {
		}
		MyLogger.getInstance().e(jObj.toString());
		RequestParams params= new RequestParams(ConfigUtils.get_elementLife_url);
		//		params.setSslSocketFactory(sslSocketFactory)
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		x.http().post(params, new CommonCallback<String>() {


			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyToast.getManager(context).show(getString(R.string.device_bind_failed));
				bindFailed();
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

				MyLogger.getInstance().e(" onFinished  ");
			}
			@Override
			public void onSuccess(String response) {

				// TODO Auto-generated method stub
				//[{"pp":100,"cto":200,"ro":150,"t33":450,"wfr":666}]
				MyLogger.getInstance().e(response);
				mFilterLifeInfo = new FilterLifeInfo();
				if(JsonUtils.result(response)==0){				
					List<FilterLifeInfo> list= new ArrayList<FilterLifeInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),FilterLifeInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//					UseStateToast.getManager(context).showToast("  list ="+list.toString());
					MyLogger.getInstance().e("  list ="+list.toString());
					if(list!=null&&list.size()>0){
						mFilterLifeInfo= list.get(0);
						mFilterLifeInfo._id=1;
						setLife=true;
						//						if(DBUtils.updateDB(mFilterLifeInfo)){
						try {
							if(mFilterLifeThread!=null){
								mFilterLifeThread.interrupt();
								mFilterLifeThread=null;
							}
							mFilterLifeThread= new FilterLifeThread();
							mFilterLifeThread.start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/*}else{
							MyLogger.getInstance().e(" mFilterLifeInfo save failed ");
							LoadingDialog.loadingFailed();
						}	*/			
					}else{
						bindFailed();
					}				
				}else{
					bindFailed();
				}
			}
		});
	}

	//绑定成功回调
	public void bindingFeedback(){
		RequestParams params= new RequestParams(ConfigUtils.binding_back_url);
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("prono", userInfo.getPro_no());
			jObj.accumulate("orderno", userInfo.getOrder_no());
			jObj.accumulate("status", 0);//0表示绑定成功，1表示失败
			//			jObj.accumulate("pro_no", userInfo.getPro_no());
		} catch (Exception e) {
		}
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		MyLogger.getInstance().e("bindingFeedback = "+jObj.toString());
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
				MyLogger.getInstance().e(response);
				if(JsonUtils.result(response)==0){	
					msg= handler.obtainMessage();
					msg.arg1=0;
					handler.sendMessage(msg);
				}
			}
		});
	}

	@Override  
	public boolean onTouchEvent(MotionEvent event) {  
		// TODO Auto-generated method stub  
		if(event.getAction() == MotionEvent.ACTION_DOWN){  
			if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
				InputMethodManager manager  = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
			}  
		}  
		return super.onTouchEvent(event);  
	} 
}