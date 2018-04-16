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
	static android.app.AlertDialog ad;
	private static LinearLayout progress_ll;
	private static LinearLayout loading_failed_ll;
	private static TextView loading_failed_tv, hit_tv;
	private Button confirm;
	private boolean mISCanDismiss;

	public LoadingDialog(Context context, String title, boolean iSCanDismiss) {
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
		hit_tv=(TextView)window.findViewById(R.id.hit_tv);
		confirm=(Button)window.findViewById(R.id.refresh_data_bt);
		progress_ll.setOnClickListener(this);
		confirm.setOnClickListener(this);
		
		confirm.setText(context.getString(R.string.confirm));
		hit_tv.setText(title);
		mISCanDismiss = iSCanDismiss;
	}
	public static void loadingFailed(String msg){
		loading_failed_tv.setText(msg);
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}

	public static void loadingSuccess(){
		loading_failed_tv.setText(context.getString(R.string.device_bind_success));
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	public static void loadingUnbindFailed(String msg){
		loading_failed_tv.setText(msg);
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	public static void loadingSearchOrderInfoResult(String result){
		loading_failed_tv.setText(result);
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}

	public static void loadingUnbindSuccess(){
		loading_failed_tv.setText(context.getString(R.string.device_unbind_success));
		progress_ll.setVisibility(View.GONE);
		loading_failed_ll.setVisibility(View.VISIBLE);
	}
	
	public static void setFailed(String title){
		loading_failed_tv.setText(title == null ? context.getString(R.string.device_set_failed) : title);
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
	public static void dismiss() {
		if(ad != null)
		{
			ad.dismiss();
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.refresh_data_bt:
			ad.dismiss();
			break;
		case R.id.progress_ll:
			System.out.println("====mISCanDismiss====="+mISCanDismiss);
			if(mISCanDismiss)
			{
				ad.dismiss();
			}
			break;
		default:
			break;
		}
	} 
}