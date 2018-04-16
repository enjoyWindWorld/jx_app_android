package com.kxw.smarthome.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.kxw.smarthome.R;



public class WiFiConnectDialog extends Dialog implements  OnCheckedChangeListener{

	private TextView dialog_title,wifi_name,cancel,confirm;
	private CheckBox show_pwd;
	private EditText wifi_pwd;
	private String titleStr;//从外界设置的title文本  
	private String messageStr;//从外界设置的消息文本 
	private onCancelOnClickListener cancelOnClickListener;
	private onConfirmOnClickListener confirmOnClickListener;
	
	private Context context;

	public WiFiConnectDialog(Context context) {
		super(context,R.style.AlertDialog);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.wifi_connect_dialog);  
		//按空白处不能取消动画  
		setCanceledOnTouchOutside(false);  

		initView();  
		//初始化界面数据  
		initData();  	 
		initEvent();//初始化界面控件的事件 
	}

	private void initView() {
		// TODO Auto-generated method stub
		dialog_title  = (TextView)findViewById(R.id.dialog_title);
		wifi_name = (TextView)findViewById(R.id.wifi_name);
		wifi_pwd = (EditText)findViewById(R.id.wifi_pwd);
		confirm = (TextView)findViewById(R.id.confirm);
		cancel = (TextView)findViewById(R.id.cancel);
		show_pwd = (CheckBox)findViewById(R.id.show_pwd);
		show_pwd.setOnCheckedChangeListener(this);
	}

	private void initData() {  
		//如果用户自定了title和message  
		if (titleStr != null) {  
			dialog_title.setText(titleStr);  
		}  
		if (messageStr != null) {  
			wifi_name.setText(messageStr);  
		}
	}
	private void initEvent() {  
		//设置确定按钮被点击后，向外界提供监听  
		confirm.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {  
				if (confirmOnClickListener != null) {  
					confirmOnClickListener.onConfirmClick();  
				}  
			}  
		});  
		//设置取消按钮被点击后，向外界提供监听  
		cancel.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {  
				if (cancelOnClickListener != null) {  
					cancelOnClickListener.onCancelClick();  
				}  
			}  
		});  
	}  

	/** 
	 * 从外界Activity为Dialog设置标题 
	 * 
	 * @param title 
	 */  
	public void setTitle(String title) {  
		titleStr = title;  
	}  

	/** 
	 * 从外界Activity为Dialog设置dialog的message 
	 * 
	 * @param message 
	 */  
	public void setMessage(String message) {  
		messageStr = message;  
	}  
	
	public String getText(){		
		return wifi_pwd.getText().toString();
	}
	
	public void SetCancelOnClickListener(onCancelOnClickListener cancelOnClickListener){
		this.cancelOnClickListener = cancelOnClickListener;
	}

	public void SetConfirmOnClickListener(onConfirmOnClickListener confirmOnClickListener){
		this.confirmOnClickListener = confirmOnClickListener;
	}


	public interface onCancelOnClickListener {  
		public void onCancelClick();  
	}  

	public interface onConfirmOnClickListener {  
		public void onConfirmClick();  
	} 

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			//如果选中，显示密码      
			wifi_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		}else{
			//否则隐藏密码
			wifi_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
		wifi_pwd.setSelection(wifi_pwd.length());
	}  
}