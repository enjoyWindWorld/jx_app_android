package com.kxw.smarthome.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.kxw.smarthome.R;


public class WiFiConnectProgress implements OnClickListener {
	static Context context;
	static android.app.AlertDialog ad;

	public WiFiConnectProgress(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
//		ad.setCanceledOnTouchOutside(false);
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.connect_progress_view);
	}

	/**
	 * 关闭对话框
	 */
	public static void dismiss() {
		ad.dismiss();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.refresh_data_bt:
			ad.dismiss();
			break;

		default:
			break;
		}
	}

}