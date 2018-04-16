/*
 * 上传平板操作日志
*/
package com.kxw.smarthome.receiver;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kxw.smarthome.entity.OptionDescriptionInfo;
import com.kxw.smarthome.entity.OptionDescriptions;
import com.kxw.smarthome.utils.AppUtil;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;

public class UpdateOptionDescriptionReceiver extends BroadcastReceiver {  

	private String pro_no = "";
	private String tl_param = "";
	private String tl_option = "";
	private String tl_netdate = "";
	private String tl_localdate = "";
	OptionDescriptions optionDescriptions;
	
	@Override  
	public void onReceive(Context context, Intent intent){  
		System.out.println("==开启上传操作日志广播==");
		if(intent.getAction().equals(ConfigUtils.upload_option_description_action)){
			optionDescriptions = (OptionDescriptions)intent.getSerializableExtra("options");
			if(optionDescriptions != null && optionDescriptions.getDates().size() > 0)
			{
				pro_no = SharedPreferencesUtil.getStringData(context, "pro_no", "");
				for (OptionDescriptionInfo optionDescriptionInfo : optionDescriptions.getDates()) {
					tl_param += optionDescriptionInfo.getId() + "、" + optionDescriptionInfo.getParam() +"; ";
					tl_option += optionDescriptionInfo.getId() + "、" + optionDescriptionInfo.getOption() +"; ";
					tl_netdate += optionDescriptionInfo.getId() + "、" + optionDescriptionInfo.getNetDate() +"; ";
					tl_localdate += optionDescriptionInfo.getId() + "、" + optionDescriptionInfo.getLocalDate() +"; ";
				}
				updateOptionDescription();
			}
		}
	}
	
	public void updateOptionDescription(){
		JSONObject jObj = new JSONObject();		
		try {				
			jObj.accumulate("pro_no", pro_no);
			jObj.accumulate("tl_param", tl_param);
			jObj.accumulate("tl_option", tl_option);
			jObj.accumulate("tl_netdate", tl_netdate);
			jObj.accumulate("tl_localdate", tl_localdate);
			jObj.accumulate("apk_version", AppUtil.getVersionName());
			jObj.accumulate("slab_version", "0");
		} catch (Exception e) {
			
		}	
		
		System.out.println("====日志传输参数===="+jObj.toString());
		
		RequestParams params= new RequestParams(ConfigUtils.upload_option_url);
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
				if(JsonUtils.result(response)==0)		
				{
					System.out.println("======日志上传成功！======");
				}
				else
				{
					System.out.println("======日志上传失败！======");
				}
			}
		});
	}
}