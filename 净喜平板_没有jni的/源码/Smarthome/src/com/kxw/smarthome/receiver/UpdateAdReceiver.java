/*
 * 更新广告广播
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

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.entity.AdvInfo;
import com.kxw.smarthome.entity.VideoInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;

public class UpdateAdReceiver extends BroadcastReceiver {  

	@Override  
	public void onReceive(Context context, Intent intent){  
		if(intent.getAction().equals(ConfigUtils.update_ad_alarm)){
			getAdvUrl();
		}

	}

	public void getAdvUrl(){
		RequestParams params= new RequestParams(ConfigUtils.get_adv_url);
		MyLogger.getInstance().e(DBUtils.getAdvId());
		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("type", ConfigUtils.ad_type);
			//			jObj.accumulate("pro_no", userInfo.getPro_no());
		} catch (Exception e) {
		}
		MyLogger.getInstance().e(jObj.toString());
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
				MyLogger.getInstance().e("onError Throwable="+arg0+"  boolean="+arg1);
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
//				MyLogger.getInstance().e(response);
				if(JsonUtils.result(response)==0){					
					List<AdvInfo> list= new ArrayList<>();
					try {
						MyLogger.getInstance().e(DataProcessingUtils.decode(new JSONObject(response).getString("data")));
						list=JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),AdvInfo.class);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(list!=null&&list.size()>0){
						if(DBUtils.deleteAll(AdvInfo.class))
						{
							if(DBUtils.replaceAdvUrlInfoList(list))
							{
								MyApplication.getInstance().getActivity().changeAdv();
							}
						}
						
						//服务器上面的视频
						List<VideoInfo> videoInfos = new ArrayList<>();
						for(AdvInfo advInfo : list)
						{
							if(advInfo.getVideo_list() != null && advInfo.getVideo_list().size() > 0)
							{
								for(VideoInfo videoInfo : advInfo.getVideo_list())
								{
									videoInfos.add(videoInfo);
								}
							}
						}
//						System.out.println("===服务器上面的视频==="+videoInfos);
						
						//将已经播放的设置为已经播放，顺便把旧的去掉
						List<VideoInfo> dataVideoInfos = DBUtils.getAllToList(VideoInfo.class);
//						System.out.println("===数据库上面的视频==="+dataVideoInfos);
						
						if(dataVideoInfos != null && dataVideoInfos.size() > 0)
						{
							for(VideoInfo videoInfo : videoInfos)
							{
								for(VideoInfo dataVideoInfo : dataVideoInfos)
								{
									if(videoInfo.getId() == dataVideoInfo.getId())
									{
										videoInfo.setIs_accord(dataVideoInfo.getIs_accord());
										break;
									}
								}
							}
						}
//						System.out.println("===替换后的视频==="+videoInfos);
						
						if(DBUtils.deleteAll(VideoInfo.class))
						{
							if(DBUtils.replaceVideoUrlInfoList(videoInfos))
							{
								//在首页停留才会自动播广告
								if(MyApplication.getInstance().getActivity().isIs_ShowADV())
								{
									MyApplication.getInstance().getActivity().getIsAccordVideoUrls();
								}
							}
						}
					}
					else
					{
						//后台已经没有广告
						if(DBUtils.deleteAll(AdvInfo.class) && DBUtils.deleteAll(VideoInfo.class))
						{
							MyApplication.getInstance().getActivity().changeAdv();
						}
					}
				}
			}
		});
	}

}