/*
 * 上传滤芯状态广播
*/
package com.kxw.smarthome.receiver;

import java.util.ArrayList;
import java.util.List;

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
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.OptionDescriptionInfo;
import com.kxw.smarthome.entity.OptionDescriptions;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.Utils;

public class UpdateFilterInfoReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;
	private Context mContext;
	private UserInfo userInfo;
	private OptionDescriptions optionDescriptions = new OptionDescriptions();
	private List<OptionDescriptionInfo> options = new ArrayList<OptionDescriptionInfo>();
	private OptionDescriptionInfo optionDescriptionInfo = new OptionDescriptionInfo();
	
	private Handler mFilterInfoHandler;  
	private HandlerThread mFilterInfoHandlerThread;  
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		mContext = context;
		if(intent.getAction().equals(ConfigUtils.update_filter_info_alarm)){
			userInfo = DBUtils.getFirstData(UserInfo.class);
			mFilterInfoHandlerThread = new HandlerThread("UpdateFilterInfoReceiver_mFilterInfoHandlerThread", 5);  
			mFilterInfoHandlerThread.start();  
			mFilterInfoHandler = new Handler(mFilterInfoHandlerThread.getLooper()); 
			mFilterInfoHandler.post(mFilterInfoRunnable);
		}
	}
	
	private Runnable mFilterInfoRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if(!Utils.inuse){//串口没有使用
					mSerialPortUtil =MyApplication.getSerialPortUtil();
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						setResult = mSerialPortUtil.setBaseData();
						MyLogger.getInstance().e("  setResult = "+setResult);	
						if(setResult<0){
							//faile to sent data	
							MyLogger.getInstance().e(" faile to sent data ");	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getBaseData();
						MyLogger.getInstance().e("getBaseData  returnsResult = "+returnsResult);	
						if(returnsResult>=0){
							MyLogger.getInstance().e("  get filterInfo success  ");	
							mBaseData=mSerialPortUtil.returnBaseData();
							if(mBaseData!=null){
								updateFilterInfo(SharedPreferencesUtil.getStringData(mContext, "pro_no", ""),mBaseData);
								
								//发送广播，刷新上面的剩新套餐种类和剩余量
				                Intent mIntent = new Intent(ConfigUtils.update_value_surplus_action);  
				                mContext.sendBroadcast(mIntent);
							}
							break;
						}else{
							times++;
							MyLogger.getInstance().e(" try times =   "+ times);
						}
					}while(times < 3);
					Utils.inuse = false;
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}  
	};
	
	public void updateFilterInfo(String UUID,BaseData mBaseData){
		JSONObject jObj = new JSONObject();		
		FilterLifeInfo mFilterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
		VerificationData verificationData = new VerificationData(mContext);
		if(!ToolsUtils.isEmpty(UUID) && mBaseData != null && mFilterLifeInfo != null && userInfo != null 
				&& verificationData != null && verificationData.getBindDate() != -1 && verificationData.getFirstFilter() != -1
				&& verificationData.getFivethFilter() != -1 && verificationData.getFourthFilter() != -1 && verificationData.getPay_proid() != -1
				&& verificationData.getSecondFilter() != -1 && verificationData.getThirdFilter() != -1 && verificationData.getTimeSurplus() != -1
				&& verificationData.getWaterSurplus() != -1){
			try {				
				jObj.accumulate("pro_id", UUID);
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
					jObj.accumulate("surplus_day", "0");	
				}else{
					jObj.accumulate("restflow", "0");	
					jObj.accumulate("surplus_day", verificationData.getTimeSurplus() < 0 ? 0 : verificationData.getTimeSurplus());	
				}		
				jObj.accumulate("code", SharedPreferencesUtil.getStringData(mContext, "province", ""));
				
				optionDescriptionInfo.setLocalDate("原来的BaseData数据："+mBaseData.toString());
				optionDescriptionInfo.setParam("pro_id:"+UUID+";"
						+"pp:"+(verificationData.getFirstFilter() < 0 ? 0 : verificationData.getFirstFilter())+";"
						+"cto:"+(verificationData.getSecondFilter() < 0 ? 0 : verificationData.getSecondFilter())+";"
						+"ro:"+(verificationData.getThirdFilter() < 0 ? 0 : verificationData.getThirdFilter())+";"
						+"t33:"+(verificationData.getFourthFilter() < 0 ? 0 : verificationData.getFourthFilter())+";"
						+"wfr:"+(verificationData.getFivethFilter() < 0 ? 0 : verificationData.getFivethFilter())+";"
						+"temperature:"+mBaseData.getTemperature()+";"
						+"tds:"+mBaseData.getTds()+";"
						+"ordno:"+userInfo.getOrder_no()+";"
						+"restflow"+(userInfo.pay_proid == 0?mBaseData.waterSurplus:"0")+";"
						+"code"+SharedPreferencesUtil.getStringData(mContext, "province", "")
						);
			} catch (Exception e) {
			}
		}	
		System.out.println("params=="+jObj.toString());
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
					optionDescriptionInfo.setId("1");
					optionDescriptionInfo.setOption("UpdateFilterInfoReceiver:广播触发上传滤芯寿命操作");
					optionDescriptionInfo.setNetDate("result:0");
					options.clear();
					options.add(optionDescriptionInfo);
					optionDescriptions.setDates(options);
					
					Intent intent = new Intent(ConfigUtils.upload_option_description_action);
					intent.putExtra("options", optionDescriptions);
					mContext.sendBroadcast(intent);
				}
			}
		});
	}
}