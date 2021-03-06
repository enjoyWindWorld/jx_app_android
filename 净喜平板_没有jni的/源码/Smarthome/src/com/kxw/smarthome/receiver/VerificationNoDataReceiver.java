package com.kxw.smarthome.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.OptionDescriptionInfo;
import com.kxw.smarthome.entity.OptionDescriptions;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.entity.VerificationDataInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.Utils;

public class VerificationNoDataReceiver extends BroadcastReceiver{

	private Context mContext;
	private SerialPortUtil mSerialPortUtil;
	private VerificationData verificationData;
	private BaseData mBaseData;
	private UserInfo originalUserInfo;
	
	private Handler mQuantityHandler;  
	private HandlerThread mQuantityHandlerThread;  
	
	private OptionDescriptions optionDescriptions = new OptionDescriptions();
	private List<OptionDescriptionInfo> options = new ArrayList<OptionDescriptionInfo>();
	private OptionDescriptionInfo optionDescriptionInfo = new OptionDescriptionInfo();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("====天数用完的验证====");
		
		if(intent.getAction().equals(ConfigUtils.verification_no_data_action)){
			this.mContext = context;
			originalUserInfo = DBUtils.getFirstData(UserInfo.class);
			verificationData = new VerificationData(mContext);
			mSerialPortUtil = MyApplication.getSerialPortUtil();
			mBaseData = mSerialPortUtil.returnBaseData();
			
			mQuantityHandlerThread = new HandlerThread("SyncFilterLifeReceiver_mQuantityHandlerThread", 5);  
			mQuantityHandlerThread.start();  
			mQuantityHandler = new Handler(mQuantityHandlerThread.getLooper()); 
			
			if(mBaseData != null && verificationData != null && originalUserInfo != null)
			{
				getVerificationData(originalUserInfo.getOrder_no());
			}
		}
	}
	
	//设置套餐类型及总量
	private Runnable mQuantitykRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			if(!Utils.inuse){//串口没有使用
				Utils.inuse = true;
				if(verificationData != null)
				{
					while ((mSerialPortUtil.setPayType(verificationData.getPay_proid()) > 0 && mSerialPortUtil
							.getReturn() >= 0)) {
						//原理是65535数据溢出，设置值
						int translate = 65536 - mBaseData.timeSurplus;//天数相差的差值
						while ((mSerialPortUtil.setDueTime(translate) > 0 && mSerialPortUtil.getReturn() >= 0)) {
							try {
								Thread.sleep(10 * 1000);
								verificationData.setTimeSurplus(0);
								verificationData.setWaterSurplus(0);
								
								options.clear();
								options.add(optionDescriptionInfo);
								optionDescriptions.setDates(options);
								
								Intent intent = new Intent(ConfigUtils.upload_option_description_action);
								intent.putExtra("options", optionDescriptions);
								mContext.sendBroadcast(intent);
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Utils.inuse = false;
							return;
						}
					}
				}
			}
		}  
	}; 
	

	public void verificationWater() {
		verificationData.play();
		System.out.println(mBaseData.toString());
		optionDescriptionInfo.setLocalDate("原来的BaseData数据："+mBaseData.toString());
		if(verificationData.getPay_proid() == 1)//包年
		{
			mQuantityHandler.post(mQuantitykRunnable);
		}
	}
	
	public void getVerificationData(String orderno){
		RequestParams params= new RequestParams(ConfigUtils.get_verification_data);
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("pro_no", SharedPreferencesUtil.getStringData(mContext,"pro_no", ""));
			jObj.accumulate("orderno", orderno);
			
//			jObj.accumulate("pro_no", "4498c060-59ac-44c2-9566-4a5374d17bdb");
//			jObj.accumulate("orderno", "600180527223722");
		} catch (Exception e) {
		}
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
				
				System.out.println("===验证天数用完数据==="+response);
				
				if(JsonUtils.result(response)==0){	
					
					List<VerificationDataInfo> list= new ArrayList<>();
					try {
						list=JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")), VerificationDataInfo.class);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(list != null && list.size() > 0)
					{
						VerificationDataInfo verificationDataInfo = list.get(0);
						if(verificationDataInfo.getPay_proid() == 1)//包年
						{
							if(verificationDataInfo.getQuantity() == 0 && verificationDataInfo.getMultiple() != 3)
							{
								optionDescriptionInfo.setId("1");
								optionDescriptionInfo.setParam("pro_no:"+SharedPreferencesUtil.getStringData(mContext,"pro_no", ""));
								optionDescriptionInfo.setOption("VerificationNoDataReceiver:广播触发天数用完的验证操作");
								optionDescriptionInfo.setNetDate("查询到的套餐信息："+verificationDataInfo.toString());
								
								verificationWater();
							}
						}
					}
				}
			}
		});
	}
}
