/*
 * 更换滤芯
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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.OptionDescriptionInfo;
import com.kxw.smarthome.entity.OptionDescriptions;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.LoadingDialog;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;

public class FilterChangeActivity extends BaseActivity implements OnClickListener{

	private EditText equipment_no_et;
	private Button equipment_change_bt;
	private static Context context;
	private FilterLifeInfo filterLifeInfo;
	private boolean save=false;
	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;
	private Handler handler;
	private Message msg;
	private int filter_no;
	private OptionDescriptions optionDescriptions = new OptionDescriptions();
	private List<OptionDescriptionInfo> options = new ArrayList<OptionDescriptionInfo>();
	private OptionDescriptionInfo optionDescriptionInfo = new OptionDescriptionInfo();
	private VerificationData verificationData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.filter_change_activity);
		context=this;
		mSerialPortUtil = MyApplication.getSerialPortUtil();
		mBaseData = mSerialPortUtil.returnBaseData();
		verificationData = new VerificationData(FilterChangeActivity.this);
		
		initView();
		initData();

	}

	private void initView() {
		// TODO Auto-generated method stub

		equipment_no_et = (EditText)findViewById(R.id.equipment_no_et);
		equipment_change_bt = (Button)findViewById(R.id.equipment_change_bt);
		equipment_change_bt.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		filter_no=intent.getIntExtra("filter_no", 0);
		MyLogger.getInstance().e("filter_no= "+filter_no);
		
		handler = new Handler() {  
			@Override  
			public void handleMessage(Message msg) {  
				MyLogger.getInstance().e(msg.toString());
				switch (msg.arg1) {
					case 0:
						DBUtils.deleteAll(FilterLifeInfo.class);
						save=DBUtils.saveDB(filterLifeInfo);
						if(save){
							LoadingDialog.setSuccess();
							
							if(filter_no==1){
								verificationData.setFirstFilter(filterLifeInfo.getPp());
							}
							if(filter_no==2){
								verificationData.setSecondFilter(filterLifeInfo.getCto());
							}
							if(filter_no==3){
								verificationData.setThirdFilter(filterLifeInfo.getRo());
							}
							if(filter_no==4){
								verificationData.setFourthFilter(filterLifeInfo.getT33());
							}
							if(filter_no==5){
								verificationData.setFivethFilter(filterLifeInfo.getWfr());
							}
							
							optionDescriptionInfo.setId("1");
							optionDescriptionInfo.setOption("FilterChangeActivity:更换滤芯的操作");
							optionDescriptionInfo.setNetDate("result：0");
							options.clear();
							options.add(optionDescriptionInfo);
							optionDescriptions.setDates(options);
							
							Intent intent = new Intent(ConfigUtils.upload_option_description_action);
							intent.putExtra("options", optionDescriptions);
							sendBroadcast(intent);
						}else{
							LoadingDialog.setFailed(null);
						}
						break;
	
					case 1:
						LoadingDialog.setFailed(null);
						break;
	
					default:
						break;
				}
				
				super.handleMessage(msg);  
			}
		}; 

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.title_back_ll:
			finish();
			break;

		case R.id.equipment_change_bt:			
			
			if(!NetUtils.isConnected(context)){
				MyToast.getManager(context).show(getString(R.string.network_disconnected));
			}else{
				String orderNo= equipment_no_et.getText().toString();
				if(orderNo!=null&&orderNo.length()>0){
					new LoadingDialog(context, getString(R.string.device_binding), false);
					setUserInfo(orderNo);
				}else{
					MyToast.getManager(context).show(getString(R.string.edit_text_error));
				}
			}
			break;

		default:
			break;
		}
	}

	public void getFilterInfo(final String orderNo){

		JSONObject jObj = new JSONObject();		
		try {
			if(filter_no==1){
				jObj.accumulate("filterno", "pp滤芯");
			}
			if(filter_no==2){
				jObj.accumulate("filterno", "cto滤芯");
			}
			if(filter_no==3){
				jObj.accumulate("filterno", "ro滤芯");
			}
			if(filter_no==4){
				jObj.accumulate("filterno", "t33滤芯");
			}
			if(filter_no==5){
				jObj.accumulate("filterno", "wfr滤芯");
			}
			jObj.accumulate("orderno", orderNo);
			jObj.accumulate("code", SharedPreferencesUtil.getStringData(context, "province", ""));
			
			optionDescriptionInfo.setLocalDate("原来的BaseData数据："+mBaseData.toString());
			optionDescriptionInfo.setParam("filter_no："+filter_no+"；"
					+"orderno："+orderNo+"；"
					+"code："+SharedPreferencesUtil.getStringData(context, "province", ""));
			
		} catch (Exception e) {
		}
		RequestParams params= new RequestParams(ConfigUtils.get_filterInfo_url);
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
				LoadingDialog.setFailed(null);
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				System.out.println("response===="+response);

				if(JsonUtils.result(response)==0){
					filterLifeInfo= new FilterLifeInfo();
					List<FilterLifeInfo> list= new ArrayList<FilterLifeInfo>();
					String json = null;
					try {
						json = DataProcessingUtils.decode(new JSONObject(response).getString("data"));
						list.addAll(JsonUtils.jsonToArrayList(json,FilterLifeInfo.class));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}						

					filterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
					int times=0;
					if(filterLifeInfo != null && mBaseData != null){	
						filterLifeInfo._id=1;
						int life[] = {};
						if(filter_no==1){
							filterLifeInfo.setPp(list.get(0).getPp());
							life = new int[]{filterLifeInfo.getPp(),mBaseData.getSecondFilter(),mBaseData.getThirdFilter(),mBaseData.getFourthFilter(),mBaseData.getFivethFilter()};						
						}
						if(filter_no==2){
							filterLifeInfo.setCto(list.get(0).getCto());
							life = new int[]{mBaseData.getFirstFilter(),filterLifeInfo.getCto(),mBaseData.getThirdFilter(),mBaseData.getFourthFilter(),mBaseData.getFivethFilter()};
						}
						if(filter_no==3){
							filterLifeInfo.setRo(list.get(0).getRo());
							life = new int[]{mBaseData.getFirstFilter(),mBaseData.getSecondFilter(),filterLifeInfo.getRo(),mBaseData.getFourthFilter(),mBaseData.getFivethFilter()};
						}
						if(filter_no==4){
							filterLifeInfo.setT33(list.get(0).getT33());
							life = new int[]{mBaseData.getFirstFilter(),mBaseData.getSecondFilter(),mBaseData.getThirdFilter(),filterLifeInfo.getT33(),mBaseData.getFivethFilter()};
						}
						if(filter_no==5){
							filterLifeInfo.setWfr(list.get(0).getWfr());
							life = new int[]{mBaseData.getFirstFilter(),mBaseData.getSecondFilter(),mBaseData.getThirdFilter(),mBaseData.getFourthFilter(),filterLifeInfo.getWfr()};
						}
						do{
							if(mSerialPortUtil.setFilterLife(life, life.length)>0 && mSerialPortUtil.getReturn()>=0){ 
								msg= handler.obtainMessage();
								msg.arg1=0;
								handler.sendMessage(msg);
								break;
							}else{
								times++;
							}								
						}
						while(times<5);
						if(times>=5){
							msg= handler.obtainMessage();
							msg.arg1=1;
							handler.sendMessage(msg);
						}
					}else{
						LoadingDialog.setFailed(null);
					}

				}else{
					LoadingDialog.setFailed(JsonUtils.msg(response));
				}
			}
		});
	}

	
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
	
	//设置套餐类型及总量
	void setUserInfo(String orderNo) {
		UserInfo userInfo = DBUtils.getFirstData(UserInfo.class);
		userInfo.quantity = 0;

		mSerialPortUtil = MyApplication.getSerialPortUtil();
		while ((mSerialPortUtil.setPayType(userInfo.getPay_proid()) > 0 && mSerialPortUtil
				.getReturn() >= 0)) {
			if (userInfo.getPay_proid() == 0) {
				while ((mSerialPortUtil.setWaterVolume((int) Math.rint(userInfo
						.getQuantity())) > 0 && mSerialPortUtil.getReturn() >= 0)) {
					getFilterInfo(orderNo);
					return;
				}
			} else if (userInfo.getPay_proid() == 1) {
				while ((mSerialPortUtil.setDueTime((int) Math.rint(userInfo
						.getQuantity())) > 0 && mSerialPortUtil.getReturn() >= 0)) {
					getFilterInfo(orderNo);
					return;
				}
			}
		}
	}
}