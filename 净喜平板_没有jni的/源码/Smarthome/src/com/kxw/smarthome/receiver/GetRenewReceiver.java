/*
 * 续费广播
*/
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
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.utils.Utils;

public class GetRenewReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private UserInfo userInfo ;
	private BaseData mBaseData;
	private String pro_no = null; 
	private String nOrder_no = null; 
	private String oOrder_no = null; 
	private Context mContext;
	private OptionDescriptions optionDescriptions = new OptionDescriptions();
	private List<OptionDescriptionInfo> options = new ArrayList<OptionDescriptionInfo>();
	private OptionDescriptionInfo optionDescriptionInfo = new OptionDescriptionInfo();
	
	private Handler mRenewHandler;  
	private HandlerThread mRenewHandlerThread;  
	private VerificationData verificationData;
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		mContext = context;
		if(intent.getAction().equals(ConfigUtils.get_renew_alarm)){
			userInfo = DBUtils.getFirstData(UserInfo.class);
			mSerialPortUtil = MyApplication.getSerialPortUtil();
			mBaseData = mSerialPortUtil.returnBaseData();
		    verificationData = new VerificationData(mContext);
			//先判断是否用完了
			
			if(userInfo != null && userInfo.pay_proid == 1)
			{
				if(verificationData != null && verificationData.getTimeSurplus() == 0)
				{
					mRenewHandlerThread = new HandlerThread("GetRenewReceiver_mRenewHandlerThread", 5);  
				    mRenewHandlerThread.start();  
				    mRenewHandler = new Handler(mRenewHandlerThread.getLooper()); 
						    
					getRenewInfo();
				}
			}
			else if(userInfo != null && userInfo.pay_proid == 0)
			{
				if(verificationData != null && verificationData.getWaterSurplus() == 0)
				{
					mRenewHandlerThread = new HandlerThread("GetRenewReceiver_mRenewHandlerThread", 5);  
				    mRenewHandlerThread.start();  
				    mRenewHandler = new Handler(mRenewHandlerThread.getLooper()); 
						    
					getRenewInfo();
				}
			}
		}
	}

	//获取续费订单信息
	public void getRenewInfo(){
		if (userInfo.pro_no != null && userInfo.order_no != null && mBaseData != null) {
			pro_no = userInfo.getPro_no();
			oOrder_no = userInfo.getOrder_no();
		} else {
			return;
		}
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("prono", pro_no);
			optionDescriptionInfo.setLocalDate("原来的BaseData数据："+mBaseData.toString());
		} catch (Exception e) {
		}
		RequestParams params = new RequestParams(ConfigUtils.get_renowInfo_url);
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
				if (JsonUtils.result(response) == 0) {
					List<UserInfo> list = new ArrayList<UserInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(
								DataProcessingUtils.decode(new JSONObject(
										response).getString("data")),
								UserInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (list != null && list.size() > 0) {
						userInfo = list.get(0);
						userInfo._id = 1;
						DBUtils.deleteAll(UserInfo.class);
						if (DBUtils.saveDB(userInfo)) {
							nOrder_no = userInfo.getOrder_no();
							mRenewHandler.post(mRenewRunnable);
						}
					}
				}
			}
		});
	}
	
	//续费数据写入成功回调接口
	public void getRenewInfoBaskcall(){
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("prono", pro_no);
			jObj.accumulate("orderno", oOrder_no);
			jObj.accumulate("newOrderno", nOrder_no);
		} catch (Exception e) {
		}
		RequestParams params = new RequestParams(
				ConfigUtils.get_renowInfo_backcall_url);
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		params.setConnectTimeout(10000);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				SharedPreferencesUtil.saveStringData(mContext, "oldOrderno", oOrder_no);//保存起来，要是续费服务不成功，第二次进来对比，不一样就在回调一次，麻痹	
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferencesUtil.saveStringData(mContext, "oldOrderno", oOrder_no);//保存起来，要是续费服务不成功，第二次进来对比，不一样就在回调一次，麻痹	
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				
				System.out.println("===自动触发的续费时间==="+userInfo.toString());
				
				//保存验证的数据
				verificationData.setPay_proid(userInfo.getPay_proid());
				verificationData.setBindDate(System.currentTimeMillis() / (long) 1000);
				if(userInfo.getPay_proid() == 1)//包年
				{
					verificationData.setTimeSurplus((int)userInfo.getDay());
					verificationData.setWaterSurplus(0);
				}
				else
				{
					verificationData.setTimeSurplus(0);
					verificationData.setWaterSurplus((int)userInfo.getQuantity());
				}
				
				if (JsonUtils.result(response) == 0) 
				{
					SharedPreferencesUtil.saveStringData(mContext, "oldOrderno", nOrder_no);//保存起来，要是续费服务不成功，第二次进来对比，不一样就在回调一次，麻痹	
				}
				else 
				{				
					SharedPreferencesUtil.saveStringData(mContext, "oldOrderno", oOrder_no);//保存起来，要是续费服务不成功，第二次进来对比，不一样就在回调一次，麻痹	
				}
				
				optionDescriptionInfo.setId("1");
				optionDescriptionInfo.setParam("pro_no:"+pro_no);
				optionDescriptionInfo.setOption("GetRenewReceiver:广播触发续费操作");
				optionDescriptionInfo.setNetDate("查询到的套餐信息："+userInfo.toString());
				options.clear();
				options.add(optionDescriptionInfo);
				optionDescriptions.setDates(options);
				
				Intent intent = new Intent(ConfigUtils.upload_option_description_action);
				intent.putExtra("options", optionDescriptions);
				mContext.sendBroadcast(intent);
			}
		});
	}
	
	private Runnable mRenewRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if (!Utils.inuse) {// 串口没有使用
					Utils.inuse = true;
					int times = 0;
					int setResult = -1;
					int returnsResult = -1;
					do {
						setResult = mSerialPortUtil
								.setPayType(userInfo.pay_proid);
						MyLogger.getInstance().e("  setResult = " + setResult);
						if (setResult < 0) {
							// faile to sent data
							MyLogger.getInstance().e(" faile to sent data ");
							break;
						}
						// succes to sent data
						returnsResult = mSerialPortUtil.getReturn();
						MyLogger.getInstance()
								.e("getBaseData  returnsResult = "
										+ returnsResult);
						if (returnsResult >= 0) {
							MyLogger.getInstance().e(
									"  get filterInfo success  ");
							int setVolume = -1;
							int returnsVolume = -1;
							int ste_time = 0;
							if (userInfo.pay_proid == 0) {
								do {
									setVolume = mSerialPortUtil
											.setWaterVolume((int) Math
													.rint(userInfo.quantity + 0.5));
									MyLogger.getInstance().e(
											"  setResult = " + setResult);
									if (setVolume < 0) {
										// faile to sent data
										MyLogger.getInstance().e(
												" faile to sent data ");
										break;
									}
									// succes to sent data
									returnsVolume = mSerialPortUtil.getReturn();
									MyLogger.getInstance().e(
											"getBaseData  returnsVolume = "
													+ returnsResult);
									if (returnsVolume >= 0) {
										MyLogger.getInstance().e(
												"  set volume success ");
										getRenewInfoBaskcall();
										break;
									} else {
										ste_time++;
										MyLogger.getInstance()
												.e(" try ste_time =   "
														+ ste_time);
									}
								} while (ste_time < 3);
							} else if (userInfo.pay_proid == 1) {
								do {
									setVolume = mSerialPortUtil
											.setDueTime(userInfo.day);
									MyLogger.getInstance().e(
											"  setResult = " + setResult);
									if (setVolume < 0) {
										// faile to sent data
										MyLogger.getInstance().e(
												" faile to sent data ");
										break;
									}
									// succes to sent data
									returnsVolume = mSerialPortUtil.getReturn();
									MyLogger.getInstance().e(
											"getBaseData  returnsVolume = "
													+ returnsResult);
									if (returnsVolume >= 0) {
										MyLogger.getInstance().e(
												"  set volume success ");
										getRenewInfoBaskcall();
										break;
									} else {
										ste_time++;
										MyLogger.getInstance()
												.e(" try ste_time =   "
														+ ste_time);
									}
								} while (ste_time < 3);
							}
							if (ste_time <= 3) {
								break;
							}
						} else {
							times++;
							MyLogger.getInstance().e(" try times =   " + times);
						}
					} while (times < 3);
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
}