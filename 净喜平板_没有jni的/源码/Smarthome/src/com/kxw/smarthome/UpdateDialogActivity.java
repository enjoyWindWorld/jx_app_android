/*
 * 升级提示框
 */

package com.kxw.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kxw.smarthome.service.UpdateService;
import com.kxw.smarthome.utils.MyLogger;


public class UpdateDialogActivity extends Activity implements OnClickListener{
	private LinearLayout root_view_ll;
	private Button cancel,confirm;

	private String url,apkName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_dialog_activity);

		initView();
		initData();
	}


	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		MyLogger.getInstance().e("intent = "+intent);
		
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			MyLogger.getInstance().e("bundle = "+intent);
			if (bundle != null) {
				url = intent.getStringExtra("downloadURL");
				apkName = intent.getStringExtra("apkName");
				MyLogger.getInstance().e("url = "+url+"apkname = "+apkName);
			}
		}
	}


	private void initView() {
		// TODO Auto-generated method stub
		//		root_view_ll=(LinearLayout)findViewById(R.id.root_view_ll);
		//		root_view_ll.getBackground().setAlpha(200);
		cancel=(Button)findViewById(R.id.cancel_bt);
		confirm=(Button)findViewById(R.id.confirm_bt);
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.cancel_bt:
			finish();
			break;

		case R.id.confirm_bt:
			if(!TextUtils.isEmpty(url)&&!TextUtils.isEmpty(apkName)){
				try {
					download(url,apkName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			finish();
			break;

		default:
			break;
		}

	}
	private void download(String url,String apkName) throws Exception {
		Intent service = new Intent(this, UpdateService.class);
		MyLogger.getInstance().e("url = "+url+"apkname = "+apkName);
		service.putExtra("DownloadURL", url);
		service.putExtra("apkName", apkName);
		startService(service);
	}


}