/*
 * 提示窗口广播
*/
package com.kxw.smarthome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.HintDialogActivity;
import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.entity.BaseData;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.ToolsUtils;


public class ExpirationHintsReceiver extends BroadcastReceiver {  

	private SerialPortUtil mSerialPortUtil;
	private BaseData mBaseData;
	private Context context;

	@Override  
	public void onReceive(Context context, Intent intent){  
		// TODO Auto-generated method stub  
		this.context = context;
		if(intent.getAction().equals(ConfigUtils.expiration_hints_action)){	
			getInfo();
		}	
	}

	private void getInfo(){
		mSerialPortUtil =MyApplication.getSerialPortUtil();
		mBaseData=mSerialPortUtil.returnBaseData();	
		int type = -1;	
		if(mBaseData!=null){
			if(mBaseData.timeSurplus != 65535){  //按时间计费
				if(ConfigUtils.hint_ratio * 365 >= mBaseData.timeSurplus ){
					if(mBaseData.timeSurplus == 0){
						type = 2; //已到期
					}else if(mBaseData.timeSurplus > 0){
						type = 1; //即将到期
					}
					if(!ToolsUtils.isShowHint(context)){
						Intent intent = new Intent(context,HintDialogActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("type", type);
						context.startActivity(intent);
					}
				}
			}else {
				if(ConfigUtils.hint_ratio * mBaseData.waterSum >= mBaseData.waterSurplus ){
					if(mBaseData.waterSurplus == 0){
						type = 2;
					}else if(mBaseData.waterSurplus > 0){
						type = 1;
					}
					if(!ToolsUtils.isShowHint(context)){
						Intent intent = new Intent(context,HintDialogActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("type",type);
						context.startActivity(intent);
					}
				}
			}

			if(mBaseData.firstFilter == 0 ||  mBaseData.secondFilter  == 0 || mBaseData.thirdFilter == 0 
					|| mBaseData.fourthFilter == 0  || mBaseData.fivethFilter == 0 ){
				type = 4;
				if(!ToolsUtils.isShowHint(context)){
					Intent intent = new Intent(context,HintDialogActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("type",type);
					context.startActivity(intent);
				}
			}else {
				FilterLifeInfo filterLifeInfo= new FilterLifeInfo();
				filterLifeInfo=DBUtils.getFirstData(FilterLifeInfo.class);
				if(filterLifeInfo!=null){
					double first = filterLifeInfo.getPp() * ConfigUtils.hint_ratio ;
					double second = filterLifeInfo.getCto() * ConfigUtils.hint_ratio ;
					double third = filterLifeInfo.getRo() * ConfigUtils.hint_ratio ;
					double fourth = filterLifeInfo.getT33() * ConfigUtils.hint_ratio ;
					double fiveth = filterLifeInfo.getWfr() * ConfigUtils.hint_ratio ;
					if(first < mBaseData.firstFilter || second < mBaseData.secondFilter || third < mBaseData.thirdFilter
							|| fourth < mBaseData.fourthFilter || fiveth < mBaseData.fivethFilter){
						type = 3;
						if(!ToolsUtils.isShowHint(context)){
							Intent intent = new Intent(context,HintDialogActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("type",type);
							context.startActivity(intent);
						}
					}
				}
			}
		}
	}
}