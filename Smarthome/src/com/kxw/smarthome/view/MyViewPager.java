package com.kxw.smarthome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止viewpager滑动，点击也不响应
 * 
 * @author Administrator
 * 
 */
public class MyViewPager extends LazyViewPager {

	private boolean HAS_TOUCH_MODE = false;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (HAS_TOUCH_MODE)
			return super.onInterceptHoverEvent(ev);
		else
			// 返回false让当前布局的子布局去响应事件
			return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (HAS_TOUCH_MODE)
			return super.onTouchEvent(ev);
		else
			// 返回false让当前布局的子布局去响应事件
			return false;
	}
	

}
