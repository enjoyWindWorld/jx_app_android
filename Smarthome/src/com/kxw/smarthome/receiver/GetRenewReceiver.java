/*
 * 续费广播
*/
package com.kxw.smarthome.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.Utils;

public class GetRenewReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private UserInfo userInfo ;
	private RenewThread mRenewThread;
	private String uuid= null; 

	@Override  
	public void onReceive(Context context, Intent intent){  
		MyLogger.getInstance().e("GetRenewReceiver Action = "+intent.getAction());
		if(intent.getAction().equals(ConfigUtils.get_renew_alarm)){
			getRenewInfo();
		}
	}


	public void getRenewInfo(){
		//"054cd7cf-b754-4de3-99e7-8eb97b1f5a83";
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		UserInfo mUserInfo = new UserInfo();
		try {
			mUserInfo = db.findFirst(UserInfo.class);
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(mUserInfo!=null){
			uuid=mUserInfo.getOrder_no();
		}else{
			return ;
		}
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("prono", uuid);
			//			jObj.accumulate("pro_no", userInfo.getPro_no());
		} catch (Exception e) {
		}
		RequestParams params= new RequestParams(ConfigUtils.get_renowInfo_url);
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e("onError Throwable="+arg0+"  boolean="+arg1);
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
					MyLogger.getInstance().e("  list ="+list.toString());
					if(list!=null&&list.size()>0){
						userInfo= list.get(0);	
						userInfo.setOrder_no(uuid);
						Utils.pro_no = uuid;
						MyLogger.getInstance().e("  userInfo ="+userInfo.toString());
//						DBUtils.deleteAll(UserInfo.class);
						if(DBUtils.saveDB(userInfo)){
							Utils.pro_no=userInfo.getPro_no();
							if(mRenewThread!=null){
								mRenewThread.interrupt();
								mRenewThread=null;
							}	
							mRenewThread = new RenewThread();
							mRenewThread.start();
						}
					}
				}						
			}
		});		
	}

	private class RenewThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (true) {
				if(!Utils.inuse){//串口没有使用
					mSerialPortUtil =MyApplication.getSerialPortUtil();
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						setResult = mSerialPortUtil.setPayType(userInfo.pay_proid);
						MyLogger.getInstance().e("  setResult = "+setResult);	
						if(setResult<0){
							//faile to sent data	
							MyLogger.getInstance().e(" faile to sent data ");	
							break;
						}	
						//succes to sent data					
						returnsResult = mSerialPortUtil.getReturn();
						MyLogger.getInstance().e("getBaseData  returnsResult = "+returnsResult);	
						if(returnsResult>=0){
							MyLogger.getInstance().e("  get filterInfo success  ");	
							int setVolume = -1;
							int returnsVolume = -1;
							int ste_time = 0;
							if(userInfo.pay_proid==0){
								do{
									setVolume = mSerialPortUtil.setWaterVolume((int)Math.rint(userInfo.quantity+0.5));
									MyLogger.getInstance().e("  setResult = "+setResult);	
									if(setVolume<0){
										//faile to sent data	
										MyLogger.getInstance().e(" faile to sent data ");	
										break;
									}	
									//succes to sent data					
									returnsVolume = mSerialPortUtil.getReturn();
									MyLogger.getInstance().e("getBaseData  returnsVolume = "+returnsResult);	
									if(returnsVolume>=0){
										MyLogger.getInstance().e("  set volume success ");	
										break;
									}else{
										ste_time++;
										MyLogger.getInstance().e(" try ste_time =   "+ ste_time);
									}
								}while(ste_time<3);						
							}else if(userInfo.pay_proid==1){
								do{
									setVolume = mSerialPortUtil.setDueTime(userInfo.day);
									MyLogger.getInstance().e("  setResult = "+setResult);	
									if(setVolume<0){
										//faile to sent data	
										MyLogger.getInstance().e(" faile to sent data ");	
										break;
									}	
									//succes to sent data					
									returnsVolume = mSerialPortUtil.getReturn();
									MyLogger.getInstance().e("getBaseData  returnsVolume = "+returnsResult);	
									if(returnsVolume>=0){
										MyLogger.getInstance().e("  set volume success ");	
										break;
									}else{
										ste_time++;
										MyLogger.getInstance().e(" try ste_time =   "+ ste_time);
									}
								}while(ste_time<3);	
							}
							if(ste_time <= 3){
								break;
							}						
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
	}


}