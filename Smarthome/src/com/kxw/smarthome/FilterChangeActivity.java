/*
 * 更换滤芯
*/
package com.kxw.smarthome;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.FilterLifeInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.LoadingDialog;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.SerialPortHandleUtils;
import com.kxw.smarthome.utils.Utils;

public class FilterChangeActivity extends BaseActivity implements OnClickListener{

	private EditText equipment_no_et;
	private Button equipment_change_bt;
	private static Context context;
	private int pay_proid=-1,times=0;
	private FilterLifeInfo filterLifeInfo;
	private boolean set_life =true,save=false;
	private SerialPortUtil mSerialPortUtil;
	private Handler handler;
	private Message msg;
	private int filter_no;

	private int setting=0,pp=0,cto=0,ro=0,t33=0,wfr=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.filter_change_activity);
		context=this;
		mSerialPortUtil = MyApplication.getSerialPortUtil();
		initView();
		initData();

	}

	private void initView() {
		// TODO Auto-generated method stub

		equipment_no_et = (EditText)findViewById(R.id.equipment_no_et);
		equipment_change_bt = (Button)findViewById(R.id.equipment_change_bt);
		equipment_change_bt.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		filter_no=intent.getIntExtra("filter_no", 0);
		MyLogger.getInstance().e("filter_no= "+filter_no);
		
		handler = new Handler() {  
			@Override  
			public void handleMessage(Message msg) {  
				MyLogger.getInstance().e(msg.toString());
				switch (msg.arg1) {
			
				case 0:
					save=DBUtils.updateDB(filterLifeInfo);
					if(save){
						LoadingDialog.setSuccess();
					}else{
						LoadingDialog.setFailed();
					}
					break;

				case 1:
					LoadingDialog.setFailed();
					break;

				default:
					break;
				}
				super.handleMessage(msg);  
			}
		}; 

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.title_back_ll:
			finish();
			break;

		case R.id.equipment_change_bt:
			if(!NetUtils.isConnected(context)){
				MyToast.getManager(context).show(getString(R.string.network_disconnected));
			}else{
				String orderNo= equipment_no_et.getText().toString();
				if(orderNo!=null&&orderNo.length()>0){
					new LoadingDialog(context);
					getFilterInfo(orderNo);
				}else{
					MyToast.getManager(context).show(getString(R.string.edit_text_error));
				}
			}
			break;

		default:
			break;
		}
	}



	public boolean setFilterLife(int no,int life) {
		MyLogger.getInstance().e("no= "+no+"  life= "+life);
		return mSerialPortUtil.sendString(SerialPortHandleUtils.life(no, life));
	}


	public  void getFilterInfo(final String orderNo){

		JSONObject jObj = new JSONObject();		
		try {
			if(filter_no==1){
				jObj.accumulate("filterno", "pp");
			}
			if(filter_no==2){
				jObj.accumulate("filterno", "cto");
			}
			if(filter_no==3){
				jObj.accumulate("filterno", "ro");
			}
			if(filter_no==4){
				jObj.accumulate("filterno", "t33");
			}
			if(filter_no==5){
				jObj.accumulate("filterno", "wfr");
			}
			jObj.accumulate("orderno", orderNo);
			jObj.accumulate("code", Utils.province);
		} catch (Exception e) {
		}
		RequestParams params= new RequestParams(ConfigUtils.get_filterInfo_url);
		//		params.setSslSocketFactory(sslSocketFactory)
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				LoadingDialog.setFailed();
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e(DataProcessingUtils.decode(response));
				MyLogger.getInstance().e(response);

				if(JsonUtils.result(response)==0){
					filterLifeInfo= new FilterLifeInfo();
					List<FilterLifeInfo> list= new ArrayList<FilterLifeInfo>();
					String json = null;
					try {
						json = DataProcessingUtils.decode(new JSONObject(response).getString("data"));
						list.addAll(JsonUtils.jsonToArrayList(json,FilterLifeInfo.class));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}						
					MyLogger.getInstance().e("  list ="+list.toString());
					filterLifeInfo = DBUtils.getFirstData(FilterLifeInfo.class);
					int times=0;
					if(filterLifeInfo!=null){	
						filterLifeInfo._id=1;
						int life[] = {};
						if(filter_no==1){
							filterLifeInfo.setPp(list.get(0).getPp());
							life = new int[]{filterLifeInfo.getPp(),0,0,0,0};						
						}
						if(filter_no==2){
							filterLifeInfo.setCto(list.get(0).getCto());
							life = new int[]{0,filterLifeInfo.getCto(),0,0,0};
							
						}
						if(filter_no==3){
							filterLifeInfo.setRo(list.get(0).getRo());
							life = new int[]{0,0,filterLifeInfo.getRo(),0,0};
						}
						if(filter_no==4){
							filterLifeInfo.setT33(list.get(0).getT33());
							life = new int[]{0,0,0,filterLifeInfo.getT33(),0};
						}
						if(filter_no==5){
							filterLifeInfo.setWfr(list.get(0).getWfr());
							life = new int[]{0,0,0,0,filterLifeInfo.getWfr()};
						}
						do{
							if(mSerialPortUtil.setFilterLife(life, life.length)>0 && mSerialPortUtil.getReturn()>=0){ 
								msg= handler.obtainMessage();
								msg.arg1=0;
								handler.sendMessage(msg);
							}else{
								times++;
							}								
						}while(times<5);
						if(times>=5){
							msg= handler.obtainMessage();
							msg.arg1=1;
							handler.sendMessage(msg);
						}
						
					}else{
						LoadingDialog.setFailed();
					}

				}else{
					LoadingDialog.setFailed();

				}
			}
		});
	}

	
	public boolean onTouchEvent(MotionEvent event) {  
		// TODO Auto-generated method stub  
		if(event.getAction() == MotionEvent.ACTION_DOWN){  
			if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
				InputMethodManager manager  = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
			}  
		}  
		return super.onTouchEvent(event);  
	} 
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}


}