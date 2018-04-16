package com.kxw.smarthome;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;

public class VolumeControlActivity extends BaseActivity implements OnClickListener, OnSeekBarChangeListener, OnCheckedChangeListener{
	
	private SeekBar volume_control_seekbar;
	private AudioManager mAudioManager; 
	private int volume = 0;
	private ToggleButton volume_control_tb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.volume_control_activity);
		
		initView();
		initData();
	
	}


	private void initView() {
		// TODO Auto-generated method stub
		
		volume_control_seekbar = (SeekBar)findViewById(R.id.volume_control_seekbar);
		volume_control_seekbar.setOnSeekBarChangeListener(this);
		volume_control_tb = (ToggleButton)findViewById(R.id.volume_control_tb);
		volume_control_tb.setOnCheckedChangeListener(this);

	}
	
	private void initData() {
		// TODO Auto-generated method stub
		
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		volume = getVolime();
		volume_control_seekbar.setProgress(volume);	
		if(volume==0){
			volume_control_tb.setChecked(true);
		}else{
			volume_control_tb.setChecked(false);		
		}
		volume_control_seekbar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/** 
	 * 获得当前音量值 
	 */  
	private int getVolime(){  
		int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC );
		MyLogger.getInstance().e( " STREAM_MUSIC ="+currentVolume );
		return currentVolume;  
	} 
	

	/*
	 * 音量设置    
	 */ 
	private void setStreamVolume(int paramInt){  
		 mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC , paramInt, 0);
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


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		if(fromUser){
			setStreamVolume(progress);	
			MyLogger.getInstance().e("onProgressChanged"+progress);
		}		
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		MyLogger.getInstance().e("isChecked ="+isChecked);
		if(isChecked){
			setStreamVolume(0);
			volume_control_seekbar.setProgress(0);	
			volume_control_seekbar.setEnabled(false);
		}else{
			volume_control_seekbar.setEnabled(true);
		}
	}

}