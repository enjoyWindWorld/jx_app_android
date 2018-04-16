/*
 * 版本更新广播
*/
package com.kxw.smarthome.receiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.HintDialogActivity;
import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.R;
import com.kxw.smarthome.utils.AppUtil;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.ToastUtil;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.Utils;

public class UpgradeReceiver extends BroadcastReceiver {
	private Context mContext;
	private boolean mustupgrade = false;
	private SerialPortUtil mSerialPortUtil;
	private UpgradeThread mUpgradeThread;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mContext = context;
		if (intent.getAction().equals(ConfigUtils.upgrade_version_alarm)) {
			checkUpgrade();
		}
	}

	private void checkUpgrade() {
		MyLogger.getInstance().e("  checkUpgrade ");
		if (!NetUtils.isConnected(mContext)) {
			Toast.makeText(mContext, R.string.network_disConnected,
					Toast.LENGTH_LONG).show();
			return;
		}
		RequestParams params = new RequestParams(ConfigUtils.upgrade_url);
		params.setBodyContent(DataProcessingUtils.encrypt(JsonUtils
				.updateInfoJson()));
		MyLogger.getInstance().e("params=" + JsonUtils.updateInfoJson());
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e("onError()");
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e("onSuccess response=" + response);
				if (JsonUtils.result(response) == 0) {

					int must = 0;
					JSONArray dataArray = null;
					JSONObject data = null;
					try {
						dataArray = new JSONArray(DataProcessingUtils.decode(new JSONObject(response).getString("data")));
						MyLogger.getInstance().e("onSuccess data =" + dataArray);
						if(dataArray!= null && dataArray.length()>0){
							data = (JSONObject) dataArray.get(0);
							MyLogger.getInstance().e("data = " + data);
							if (data.has("downurl")) {
								must = data.getInt("mustupgrade");  //强制更新 0：不需要强制更新； 1：必须更新
								String updataUrl = data.getString("downurl");
								String apkName = data.getString("name");
								String className = getTopActivity(mContext); // 完整类名
								MyLogger.getInstance().e(" className =" + className);
								if(must == 0){
									mustupgrade = false ;
								}else if(must == 1){
									mustupgrade = true ;
								}
								if(mUpgradeThread!=null){
									mUpgradeThread.interrupt();
									mUpgradeThread=null;
								}	
								mUpgradeThread = new UpgradeThread();
								mUpgradeThread.start();
								if (!ToolsUtils.isShowHint(mContext)) {
									Intent intent = new Intent(mContext,HintDialogActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.putExtra("type", 0);
									intent.putExtra("must", must);
									intent.putExtra("downloadURL", updataUrl);
									intent.putExtra("apkName", apkName);
									mContext.startActivity(intent);
								}
							}
						} else {
							MyLogger.getInstance().e("no updataUrl!!!!!");
							ToastUtil.showLongToast(mContext.getString(R.string.version_name_head)+AppUtil.getVersionName());
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	//设置净水器不可使用
	private class UpgradeThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (true) {
				if(!Utils.inuse){//串口没有使用
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					mSerialPortUtil =MyApplication.getSerialPortUtil();
					do{		
						//c++用0表示false，1表示true
						setResult = mSerialPortUtil.setVerSwitch(mustupgrade);
						MyLogger.getInstance().e("  setResult = "+setResult);	
						if(setResult<0){
							//faile to sent data	
							MyLogger.getInstance().e(" faile to sent data ");	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						MyLogger.getInstance().e("getReturn  returnsResult = "+ returnsResult);	
						if(returnsResult>=0){
							MyLogger.getInstance().e("  Ver Switch success  " + mustupgrade);	
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

	private String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		return info.topActivity.getClassName(); // 完整类名
	}
}