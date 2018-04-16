package com.kxw.smarthome.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 管理类
 * 
 * 单例模式
 * 
 * 全局调用
 * 
 * 只会显示一个toast 提示
 * 
 */
public class MyToast {

	private Context context;
	/** toast 对象 */
	private Toast toast;
	/** 是否在显示 */
	private boolean isshow = false;

	public MyToast(Context context) {
		this.context = context;
	}

	private static MyToast myTost;

	public static MyToast getManager(Context context) {
		if (myTost == null) {
			myTost = new MyToast(context);
		}
		return myTost;
	}

	public void show(Object str) {
		if (isshow == true) {
			toast.cancel();
			isshow = !isshow;
		}
		toast = Toast.makeText(context, str + "", Toast.LENGTH_LONG);
		toast.show();
		isshow = !isshow;
	}

}
