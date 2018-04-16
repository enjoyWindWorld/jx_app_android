package com.kxw.smarthome.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kxw.smarthome.R;


public class LoadingDialog implements OnClickListener {
	static Context context;
	android.app.AlertDialog ad;
	private static LinearLayout progress_ll;
	private static LinearLayout loading_failed_ll;
	private static TextView loading_failed_tv;
	private Button confirm;

	public LoadingDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		ad.setCanceledOnTouchOutside(false);
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.loading_view);
		progress_ll=(LinearLayout)window.findViewById(R.id.progress_ll);
		loading_failed_ll=(LinearLayout)window.findViewById(R.id.network_disconnect_ll);
		loading_failed_tv=(TextView)window.findViewById(R.id.loading_failed_tv);
		confirm=(Button)window.findViewById(R.id.refresh_data_bt);
		confirm.setOnClickListener(this);
		
		confirm.setText(context.getString(R.string.confirm));
	}
	public static void loadingFailed(){
		loading_failed_tv.setText(context.getString(R.string.device_bind_failed));
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}

	public static void loadingSuccess(){
		loading_failed_tv.setText(context.getString(R.string.device_bind_success));
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	public static void setFailed(){
		loading_failed_tv.setText(context.getString(R.string.device_set_failed));
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	public static void setSuccess(){
		loading_failed_tv.setText(context.getString(R.string.device_set_success));
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	public void setTitle(int resId){
		loading_failed_tv.setText(resId);
	}
	
	public void setTitle(String title) {
		loading_failed_tv.setText(title);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
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