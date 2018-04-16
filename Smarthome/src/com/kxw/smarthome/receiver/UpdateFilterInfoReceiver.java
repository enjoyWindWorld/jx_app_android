/*
 * 上传滤芯状态广播
*/
package com.kxw.smarthome.receiver;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.Utils;

public class UpdateFilterInfoReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;
	private FilterInfoThread mFilterInfoThread;
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		if(intent.getAction().equals(ConfigUtils.update_filter_info_alarm)){
			if(mFilterInfoThread!=null){
				mFilterInfoThread.interrupt();
				mFilterInfoThread=null;
			}	
			mFilterInfoThread = new FilterInfoThread();
			mFilterInfoThread.start();
		}
	}
	
	private class FilterInfoThread extends Thread {
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
								if(mBaseData.waterSurplus == 0||mBaseData.timeSurplus == 0){
									DBUtils.deleteFirst(UserInfo.class);
								}
								updateFilterInfo(Utils.pro_no,mBaseData);
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
	}
	
	public void updateFilterInfo(String UUID,BaseData mBaseData){
		JSONObject jObj = new JSONObject();		
		mBaseData=mSerialPortUtil.returnBaseData();		
		if(mBaseData!=null){
			try {
				jObj.accumulate("pro_id", UUID);
				jObj.accumulate("pp", mBaseData.firstFilter);
				jObj.accumulate("cto", mBaseData.secondFilter);
				jObj.accumulate("ro", mBaseData.thirdFilter);
				jObj.accumulate("t33", mBaseData.fourthFilter);
				jObj.accumulate("wfr", mBaseData.fivethFilter);	
				if(mBaseData.timeSurplus==65535){
					jObj.accumulate("surplus", mBaseData.waterSurplus);	
				}else{
					jObj.accumulate("surplus", mBaseData.timeSurplus);
				}		
				jObj.accumulate("code", Utils.province);
			} catch (Exception e) {
			}
		}	
		RequestParams params= new RequestParams(ConfigUtils.update_filterInfo_url);
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		MyLogger.getInstance().e("updateEquipmentInfo = "+jObj.toString());
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
				MyLogger.getInstance().e(DataProcessingUtils.decode(response));
				if(JsonUtils.result(response)==0){					
				}
			}
		});
	}
}