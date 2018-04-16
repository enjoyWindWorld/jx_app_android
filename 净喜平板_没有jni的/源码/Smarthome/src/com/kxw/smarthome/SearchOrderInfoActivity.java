/*
 * 拉回之前平板里面的信息，但是不会对下位机进行数据的写入
 */
package com.kxw.smarthome;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.OldFilterLifeInfo;
import com.kxw.smarthome.entity.OptionDescriptionInfo;
import com.kxw.smarthome.entity.OptionDescriptions;
import com.kxw.smarthome.entity.TagInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.entity.WaterStateInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.LoadingDialog;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.ToastUtil;
import com.kxw.smarthome.utils.Utils;

public class SearchOrderInfoActivity extends BaseActivity implements OnClickListener{

	private EditText order_num_et, pro_no_et;
	private Button get_device_code_bt;
	private static Context context;
	private UserInfo userInfo;
	private FilterLifeInfo mFilterLifeInfo, pFilterLifeInfo;
	private SerialPortUtil mSerialPortUtil;
	private WaterStateInfo mWaterStateInfo;
	private BaseData mBaseData;
	private OptionDescriptions optionDescriptions = new OptionDescriptions();
	private List<OptionDescriptionInfo> options = new ArrayList<OptionDescriptionInfo>();
	private OptionDescriptionInfo get_order_option = new OptionDescriptionInfo();
	private OptionDescriptionInfo get_filter_option = new OptionDescriptionInfo();
	private int tag = -1;
	private String orderNo, proNoString;
	private VerificationData verificationData;
	private Handler mStateHandler;  
	private HandlerThread mStateHandlerThread;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.search_order_info_activity);
		context=this;
		initView();		
		mSerialPortUtil = MyApplication.getSerialPortUtil();
		mBaseData = mSerialPortUtil.returnBaseData();
		
		mStateHandlerThread = new HandlerThread("SearchOrderInfoActivity_mStateHandlerThread", 5);  
		mStateHandlerThread.start();  
		mStateHandler = new Handler(mStateHandlerThread.getLooper()); 
		
		if(SharedPreferencesUtil.getIntData(SearchOrderInfoActivity.this, "model", -1) < 0)
		{
			isStartState = true;
			 mStateHandler.post(mStateRunnable); 
		}
		else
		{
			System.out.println("model==="+SharedPreferencesUtil.getIntData(SearchOrderInfoActivity.this, "model", -1));
		}
	}

	private void initView() {
		// TODO Auto-generated method stub

		order_num_et = (EditText)findViewById(R.id.order_num_et);
		pro_no_et = (EditText)findViewById(R.id.pro_no_et);
		get_device_code_bt = (Button)findViewById(R.id.get_device_code_bt);
		get_device_code_bt.setOnClickListener(this);
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
				orderNo= order_num_et.getText().toString();
				proNoString = pro_no_et.getText().toString();
				if(orderNo!=null&&orderNo.length()>0){
					new LoadingDialog(context, getString(R.string.device_binding), false); 
					getDeviceCode(orderNo, proNoString);
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
		mStateHandler.removeCallbacks(mStateRunnable);  
	}

	private void bindFailed(String msg){
		LoadingDialog.loadingFailed(msg);
		DBUtils.deleteAll(UserInfo.class);
	}

	//获取设备码
	public void getDeviceCode(final String orderNo, String pro_no){

		if(SharedPreferencesUtil.getIntData(SearchOrderInfoActivity.this, "model", -1) < 0)
		{
			LoadingDialog.loadingFailed(getString(R.string.bind_no_model_data));
			return;
		}
		
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("orderno", orderNo);
			jObj.accumulate("pro_no", pro_no);
			get_order_option.setId("1");
			get_order_option.setOption("SearchOrderInfoActivity：平板同步获取套餐信息");
			get_order_option.setParam("orderno："+orderNo+"; "
					+"pro_no："+pro_no);
			get_order_option.setLocalDate("原始的BaseData："+mBaseData.toString());
		} catch (Exception e) {
		}
		RequestParams params= new RequestParams(ConfigUtils.get_search_order_info_url);
		
		//		params.setSslSocketFactory(sslSocketFactory)
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
				LoadingDialog.loadingFailed(getString(R.string.data_get_error));
				
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
					List<TagInfo> taglist = new ArrayList<TagInfo>();
					
					try {
						list.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),UserInfo.class));
						
						taglist.addAll(JsonUtils.jsonToArrayList(
								DataProcessingUtils.decode(new JSONObject(
										response).getString("data")),
										TagInfo.class));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MyLogger.getInstance().e("  list ="+list.toString());
					if(list!=null&&list.size()>0){
						userInfo= list.get(0);	
						tag = taglist.get(0).getTag();
						
						if(SharedPreferencesUtil.getIntData(SearchOrderInfoActivity.this, "model", -1) != Integer.parseInt(userInfo.proname) -1)
						{
							LoadingDialog.loadingFailed(getString(R.string.bind_model_data_err));
							return;
						}
						
						userInfo.setOrder_no(orderNo);
						userInfo._id=1;
						get_order_option.setNetDate("查询到的套餐信息："+userInfo.toString());
						options.clear();
						options.add(get_order_option);
//						getFilterLife();
						getOldFilterLife();
						
					}else{
						LoadingDialog.loadingFailed(getString(R.string.bind_no_order_data));
					}				
				}else{
					LoadingDialog.loadingFailed(JsonUtils.msg(response));
				}
			}
		});
	}

	public void getFilterLife(){

		if(!NetUtils.isConnected(context)){
			LoadingDialog.loadingFailed(getString(R.string.network_disconnected));
			return;
		}
		
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("code", SharedPreferencesUtil.getStringData(SearchOrderInfoActivity.this, "province", ""));
			get_filter_option.setId("2");
			get_filter_option.setOption("SearchOrderInfoActivity：平板同步获取滤芯寿命");
			get_filter_option.setParam("code："+SharedPreferencesUtil.getStringData(SearchOrderInfoActivity.this, "province", ""));
		} catch (Exception e) {
		}
		MyLogger.getInstance().e(jObj.toString());
		RequestParams params= new RequestParams(ConfigUtils.get_elementLife_url);
		//		params.setSslSocketFactory(sslSocketFactory)
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
				bindFailed(getString(R.string.data_get_error));
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

				MyLogger.getInstance().e(" onFinished  ");
			}
			@Override
			public void onSuccess(String response) {

				// TODO Auto-generated method stub
				
				mFilterLifeInfo = new FilterLifeInfo();
				if(JsonUtils.result(response)==0){				
					List<FilterLifeInfo> list= new ArrayList<FilterLifeInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),FilterLifeInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(list!=null&&list.size()>0){
						mFilterLifeInfo= list.get(0);
						mFilterLifeInfo._id=1;
						
						get_filter_option.setNetDate("查询到的对应城市的滤芯寿命："+mFilterLifeInfo.toString());
						options.add(get_filter_option);
						optionDescriptions.setDates(options);
						
						DBUtils.deleteAll(UserInfo.class);
						DBUtils.deleteAll(FilterLifeInfo.class);
						
						if(DBUtils.saveDB(userInfo) && DBUtils.saveDB(mFilterLifeInfo))
						{
							Intent intent = new Intent(ConfigUtils.upload_option_description_action);
							intent.putExtra("options", optionDescriptions);
							sendBroadcast(intent);
							
							ToastUtil.showLongToast(getString(R.string.bind_find_order_success));
							//保存下机器码
							SharedPreferencesUtil.saveStringData(
									SearchOrderInfoActivity.this, "pro_no", userInfo.getPro_no());
							
							// 保存下是否能解绑的标记
							SharedPreferencesUtil.saveIntData(SearchOrderInfoActivity.this,
									"tag", tag);
							
							verificationData.setFirstFilter(mFilterLifeInfo.pp);
							verificationData.setSecondFilter(mFilterLifeInfo.cto);
							verificationData.setThirdFilter(mFilterLifeInfo.ro);
							verificationData.setFourthFilter(mFilterLifeInfo.t33);
							verificationData.setFivethFilter(mFilterLifeInfo.wfr);
							
							finish();
						}
						else
						{
							DBUtils.deleteAll(UserInfo.class);
							DBUtils.deleteAll(FilterLifeInfo.class);
							LoadingDialog.loadingSearchOrderInfoResult(getString(R.string.bind_find_order_failed));
						}
						
					}else{
						bindFailed(getString(R.string.bind_no_life_data));
					}				
				}else{
					bindFailed(JsonUtils.msg(response));
				}
			}
		});
	}
	
	// 获取滤芯寿命（用过的滤芯）
	public void getOldFilterLife() {

		if (!NetUtils.isConnected(context)) {
			LoadingDialog
					.loadingFailed(getString(R.string.network_disconnected));
			return;
		}
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("pro_no", proNoString);
			jObj.accumulate("code", SharedPreferencesUtil.getStringData(
					SearchOrderInfoActivity.this, "province", ""));
			
			get_filter_option.setId("2");
			get_filter_option.setOption("BindDeviceActivity：绑定设备获取剩余的滤芯寿命");
			get_filter_option.setParam(
					"pro_no："+proNoString+"；"
					+"code："+SharedPreferencesUtil.getStringData(SearchOrderInfoActivity.this, "province", ""));
		} catch (Exception e) {
		}
		RequestParams params = new RequestParams(
				ConfigUtils.get_old_filter_life_url);
		// params.setSslSocketFactory(sslSocketFactory)
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		params.setConnectTimeout(10000);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				bindFailed(getString(R.string.data_get_error));
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				bindFailed(getString(R.string.data_get_error));
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String response) {

				// TODO Auto-generated method stub
				// [{"pp":100,"cto":200,"ro":150,"t33":450,"wfr":666}]
				mFilterLifeInfo = new FilterLifeInfo();// 原来机器的滤芯寿命
				pFilterLifeInfo = new FilterLifeInfo();// 对应省份的滤芯寿命
				if (JsonUtils.result(response) == 0) {
					List<OldFilterLifeInfo> list = new ArrayList<OldFilterLifeInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(
								DataProcessingUtils.decode(new JSONObject(
										response).getString("data")),
								OldFilterLifeInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (list != null && list.size() > 0
							&& list.get(0).getCode() != null
							&& list.get(0).getCode().size() > 0
							&& list.get(0).getPro_no() != null
							&& list.get(0).getPro_no().size() > 0) {
						mFilterLifeInfo = list.get(0).getPro_no().get(0);
						mFilterLifeInfo._id = 1;
						pFilterLifeInfo = list.get(0).getCode().get(0);
						pFilterLifeInfo._id = 1;

						get_filter_option.setNetDate("剩余滤芯寿命："+mFilterLifeInfo.toString()+";"
								+"原始滤芯寿命："+pFilterLifeInfo.toString());
						options.add(get_filter_option);
						optionDescriptions.setDates(options);
						
						DBUtils.deleteAll(UserInfo.class);
						DBUtils.deleteAll(FilterLifeInfo.class);
						
						if(DBUtils.saveDB(userInfo) && DBUtils.saveDB(pFilterLifeInfo))
						{
							Intent intent = new Intent(ConfigUtils.upload_option_description_action);
							intent.putExtra("options", optionDescriptions);
							sendBroadcast(intent);
							
							ToastUtil.showLongToast(getString(R.string.bind_find_order_success));
							//保存下机器码
							SharedPreferencesUtil.saveStringData(
									SearchOrderInfoActivity.this, "pro_no", userInfo.getPro_no());
							
							// 保存下是否能解绑的标记
							SharedPreferencesUtil.saveIntData(SearchOrderInfoActivity.this,
									"tag", tag);
							
							//保存验证的数据
							verificationData = new VerificationData(SearchOrderInfoActivity.this);
							verificationData.setPay_proid(userInfo.getPay_proid());
							verificationData.setBindDate(System.currentTimeMillis() / (long) 1000);
							if(userInfo.getPay_proid() == 1)//包年
							{
								verificationData.setTimeSurplus((int)userInfo.getQuantity());
								verificationData.setWaterSurplus(0);
							}
							else
							{
								verificationData.setTimeSurplus(0);
								verificationData.setWaterSurplus((int)userInfo.getQuantity());
							}
							
							/**
							 * 这是过滤数据库里已经存在的错误的滤芯寿命
							 */
							verificationData.setFirstFilter(mFilterLifeInfo.getPp() > pFilterLifeInfo.getPp() ? pFilterLifeInfo.getPp() : mFilterLifeInfo.getPp());
							verificationData.setSecondFilter(mFilterLifeInfo.getCto() > pFilterLifeInfo.getCto() ? pFilterLifeInfo.getCto() : mFilterLifeInfo.getCto());
							verificationData.setThirdFilter(mFilterLifeInfo.getRo() > pFilterLifeInfo.getRo() ? pFilterLifeInfo.getRo() : mFilterLifeInfo.getRo());
							verificationData.setFourthFilter(mFilterLifeInfo.getT33() > pFilterLifeInfo.getT33() ? pFilterLifeInfo.getT33() : mFilterLifeInfo.getT33());
							verificationData.setFivethFilter(mFilterLifeInfo.getWfr() > pFilterLifeInfo.getWfr() ? pFilterLifeInfo.getWfr() : mFilterLifeInfo.getWfr());
							
							finish();
						}
						else
						{
							DBUtils.deleteAll(UserInfo.class);
							DBUtils.deleteAll(FilterLifeInfo.class);
							LoadingDialog.loadingSearchOrderInfoResult(getString(R.string.bind_find_order_failed));
						}
						
					} else {
						bindFailed(getString(R.string.bind_no_life_data));
					}
				} else {
					bindFailed(JsonUtils.msg(response));
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
	
	boolean isStartState;
	private Runnable mStateRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (isStartState) {
				if (!Utils.inuse) {// 串口没有使用, 没有开关水的操作
					Utils.inuse = true;// 串口正在被使用。。。

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
					if (setValue < 0) {
						// faile to sent data
					}
					else
					{
						// succes to sent data
						returnValue = mSerialPortUtil.getWaterState();
						MyLogger.getInstance().e(
								"getReturn  returnValue = " + returnValue);

						if (returnValue >= 0) {
							MyLogger.getInstance().e(" off success   ");
							mWaterStateInfo = mSerialPortUtil.returnWaterStateInfo();
							if(mWaterStateInfo != null && mWaterStateInfo.model >= 0)
							{
								SharedPreferencesUtil.saveIntData(SearchOrderInfoActivity.this, "model", mWaterStateInfo.model);	//保存下机型信息
							}
							Utils.inuse = false;// 串口使用完毕。。。
							isStartState = false;
							break;
						}
					}
					Utils.inuse = false;// 串口使用完毕。。。
				}
			}
		}  
	}; 
}