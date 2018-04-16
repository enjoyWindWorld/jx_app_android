/*
 * 提示窗口广播
*/
package com.kxw.smarthome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kxw.smarthome.HintDialogActivity;
import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.entity.UserInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.ToolsUtils;

/**
 * 现在改用自己存的验证值来判断服务是否到期
 * @author Administrator
 *
 */
public class ExpirationHintsReceiver extends BroadcastReceiver {  

//	private SerialPortUtil mSerialPortUtil;
//	private BaseData mBaseData;
	private Context context;
	private VerificationData verificationData;

	@Override  
	public void onReceive(Context context, Intent intent){  
		// TODO Auto-generated method stub  
		this.context = context;
		if(intent.getAction().equals(ConfigUtils.expiration_hints_action)){	
			getInfo();
		}	
	}

	private void getInfo(){
//		mSerialPortUtil =MyApplication.getSerialPortUtil();
//		mBaseData=mSerialPortUtil.returnBaseData();	
		UserInfo userInfo = DBUtils.getFirstData(UserInfo.class);
		verificationData = new VerificationData(context);
		int type = -1;	
		if(userInfo != null && verificationData != null && verificationData.getBindDate() != -1 && verificationData.getFirstFilter() != -1
				&& verificationData.getFivethFilter() != -1 && verificationData.getFourthFilter() != -1 && verificationData.getPay_proid() != -1
				&& verificationData.getSecondFilter() != -1 && verificationData.getThirdFilter() != -1 && verificationData.getTimeSurplus() != -1
				&& verificationData.getWaterSurplus() != -1){
			if(userInfo.pay_proid > 0)
			{
				//按时间计费
				if(ConfigUtils.hint_ratio * 365 >= verificationData.getTimeSurplus() ){
					if(verificationData.getTimeSurplus() == 0){
						type = 2; //已到期
					}else if(verificationData.getTimeSurplus() > 0){
						type = 1; //即将到期
					}
					if(!ToolsUtils.isShowHint(context)){
						Intent intent = new Intent(context,HintDialogActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("type", type);
						context.startActivity(intent);
					}
				}
			}
			else
			{
				if(ConfigUtils.hint_ratio * userInfo.getQuantity() >= verificationData.getWaterSurplus() ){
					if(verificationData.getWaterSurplus() == 0){
						type = 2;
					}else if(verificationData.getWaterSurplus() > 0){
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
			
			FilterLifeInfo filterLifeInfo = new FilterLifeInfo();
			filterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
			if (filterLifeInfo != null) {
				if (verificationData.getFirstFilter() == 0 
						|| verificationData.getSecondFilter() == 0
						|| verificationData.getThirdFilter() == 0
						|| verificationData.getFourthFilter() == 0
//						|| mBaseData.fivethFilter == 0
						) {
					type = 4;
					if (!ToolsUtils.isShowHint(context)) {
						Intent intent = new Intent(context,
								HintDialogActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("type", type);
						context.startActivity(intent);
					}
				} else {

					if (filterLifeInfo != null) {
						double first = filterLifeInfo.getPp()
								* ConfigUtils.hint_ratio;
						double second = filterLifeInfo.getCto()
								* ConfigUtils.hint_ratio;
						double third = filterLifeInfo.getRo()
								* ConfigUtils.hint_ratio;
						double fourth = filterLifeInfo.getT33()
								* ConfigUtils.hint_ratio;
//						double fiveth = filterLifeInfo.getWfr()
//								* ConfigUtils.hint_ratio;
						if (first >= verificationData.getFirstFilter()
								|| second >= verificationData.getSecondFilter()
								|| third >= verificationData.getThirdFilter()
								|| fourth >= verificationData.getFourthFilter()
//								|| fiveth >= mBaseData.fivethFilter
								) {
							type = 3;
							if (!ToolsUtils.isShowHint(context)) {
								Intent intent = new Intent(context,
										HintDialogActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("type", type);
								context.startActivity(intent);
							}
						}
					}
				}
			}
		}
	}
}