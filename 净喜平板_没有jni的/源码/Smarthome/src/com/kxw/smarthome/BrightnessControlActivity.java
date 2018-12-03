/*
 * 亮度调节
*/
package com.kxw.smarthome;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.kxw.smarthome.utils.MyLogger;

public class BrightnessControlActivity extends BaseActivity implements
		OnSeekBarChangeListener, OnCheckedChangeListener, OnClickListener {

	private SeekBar brightness_control_seekbar;
	private ToggleButton brightness_control_tb;
	private int setProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.brightness_control_activity);

		initView();
		initData();
		
	}

	private void initView() {
		// TODO Auto-generated method stub

		brightness_control_seekbar = (SeekBar) findViewById(R.id.brightness_control_seekbar);
		brightness_control_seekbar.setOnSeekBarChangeListener(this);
		brightness_control_tb = (ToggleButton) findViewById(R.id.brightness_control_tb);
		brightness_control_tb.setOnCheckedChangeListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (getScreenMode() == 0) {
			brightness_control_tb.setChecked(false);
		} else {
			brightness_control_tb.setChecked(true);
		}
		brightness_control_seekbar.setProgress(getScreenBrightness());
	}

	/**
	 * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	private int getScreenMode() {
		int screenMode = 0;
		try {
			screenMode = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception localException) {
		}
		MyLogger.getInstance().e(screenMode);
		return screenMode;
	}

	/**
	 * 获得当前屏幕亮度值 0--255
	 */
	private int getScreenBrightness() {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception localException) {

		}
		MyLogger.getInstance().e(screenBrightness);
		return screenBrightness;
	}

	/**
	 * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	private void setScreenMode(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 保存当前的屏幕亮度值，并使之生效
	 */
	private void saveScreenBrightness(int paramInt) {
		try {
			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, paramInt);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 设置当前屏幕亮度值 0--255
	 */
	private void setScreenBrightness(int paramInt) {
		if(paramInt <= 30)
		{
			return;
		}
		setProgress = paramInt;
		Window localWindow = getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow
				.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			setScreenMode(1);
			brightness_control_seekbar.setEnabled(false);
		} else {
			setScreenMode(0);
			brightness_control_seekbar.setEnabled(true);
		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		setScreenMode(0);
		setScreenBrightness(progress);
		MyLogger.getInstance().e(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		saveScreenBrightness(setProgress);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back_ll:
			finish();
			break;

		default:
			break;
		}
	}
}