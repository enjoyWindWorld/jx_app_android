/*
 * 加载动画的弹窗
*/
package com.kxw.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LoadingDialogActivity extends Activity implements OnClickListener{
	private static LinearLayout progress_ll;
	private static LinearLayout loading_failed_ll;
	private TextView loading_failed_tv;
	private Button confirm;

	private String url,apkName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_view);

		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		progress_ll=(LinearLayout)findViewById(R.id.progress_ll);
		loading_failed_ll=(LinearLayout)findViewById(R.id.network_disconnect_ll);
		//		root_view_ll.getBackground().setAlpha(200);
		loading_failed_tv=(TextView)findViewById(R.id.loading_failed_tv);
		confirm=(Button)findViewById(R.id.refresh_data_bt);
		confirm.setOnClickListener(this);
	}



	private void initData() {
		// TODO Auto-generated method stub
		loading_failed_tv.setText(getString(R.string.device_bind_failed));
		confirm.setText(getString(R.string.confirm));
	}
	
	public static void loadingFailed(){
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.refresh_data_bt:
			finish();
			break;

		default:
			break;
		}

	}



}