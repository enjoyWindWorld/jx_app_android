package com.kxw.smarthome.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kxw.smarthome.R;


public class UseStateToast {
	
	private static TextView mTextView;
	
	private View toastRoot; 

	private Context context;
	/** toast 对象 */
	private Toast toast;
	/** 是否在显示 */
	private boolean isshow = false;

	public UseStateToast(Context context) {
		this.context = context;

	}

	private static UseStateToast myTost;

	public static UseStateToast getManager(Context context) {
		if (myTost == null) {
			myTost = new UseStateToast(context);
			myTost.onCreate();
		}
		return myTost;
	}

	public void onCreate() {
		toastRoot = LayoutInflater.from(context).inflate(R.layout.use_state_toast, null);
		mTextView = (TextView) toastRoot.findViewById(R.id.use_state_tv);
	}

	public void showToast(String message) {
		if (isshow == true) {
			toast.cancel();
			isshow = !isshow;
		}
		toast = new Toast(context);
		mTextView.setText(message);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		toast.setGravity(Gravity.TOP, -30, height / 4);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(toastRoot);
		toast.show();
		isshow = !isshow;
	}
}
