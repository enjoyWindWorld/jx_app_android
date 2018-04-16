/*
 * 广告轮播界面，包含视频广告；初始化所有广播；应用主activity。
 */
package com.kxw.smarthome;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.image.ImageOptions;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kxw.smarthome.entity.AdvInfo;
import com.kxw.smarthome.entity.WiFiInfo;
import com.kxw.smarthome.imagecycleview.ImageCycleView;
import com.kxw.smarthome.imagecycleview.ImageCycleView.ImageCycleViewListener;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.Utils;
import com.kxw.smarthome.utils.WifiUtils;

@SuppressLint("NewApi")
public class AdvMainActivity extends BaseActivity implements 
OnClickListener, Callback, OnCompletionListener, OnPreparedListener {

	private static Context context;
	private static ImageCycleView adv_view;
	private LinearLayout back_ll, home_page_ll, app_main_ll,community_services_ll, personal_center_ll;
	private static ImageView media_play_bt;
	private static List<AdvInfo> advUrlInfolist ;
	private SurfaceView adv_sfv;
	private SurfaceHolder holder;
	private MediaPlayer player;
	private boolean isPlaying = false;
	private boolean replaying = false;
	private WifiUtils wifiUtils;
	private static int mCount = 0;  //jack add


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.adv_activity);
		context=this;
		initView();

		setAlarm();
	}


	private void initView() {
		// TODO Auto-generated method stub

		back_ll = (LinearLayout) findViewById(R.id.title_back_ll);
		back_ll.setVisibility(View.INVISIBLE);

		adv_view = (ImageCycleView) findViewById(R.id.ad_view);
		media_play_bt = (ImageView) findViewById(R.id.media_play_bt);
		media_play_bt.setOnClickListener(this);
		adv_sfv = (SurfaceView) findViewById(R.id.adv_sfv);

		home_page_ll = (LinearLayout) findViewById(R.id.home_page_ll);
		app_main_ll = (LinearLayout) findViewById(R.id.app_main_ll);
		community_services_ll = (LinearLayout) findViewById(R.id.community_services_ll);
		personal_center_ll = (LinearLayout) findViewById(R.id.personal_center_ll);
		home_page_ll.setOnClickListener(this);
		community_services_ll.setOnClickListener(this);
		app_main_ll.setOnClickListener(this);
		personal_center_ll.setOnClickListener(this);

	}

	private void initData() {
		// TODO Auto-generated method stub

		changeAdv(); 

		WiFiInfo wiFiInfo=new WiFiInfo();
		wiFiInfo=DBUtils.getFirstData(WiFiInfo.class);
		if(wiFiInfo!=null){
			MyLogger.getInstance().e(wiFiInfo.toString());
			wifiUtils = new WifiUtils(context);
			int netID;
			try {
				netID = wifiUtils.CreateWifi(wiFiInfo);
				MyLogger.getInstance().e("Dialog---  netid = " + netID);
				Utils.connected=wifiUtils.ConnectToNetID(netID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		adv_view.startImageCycle();
		isPlaying = false; //jack add
		initData();
		initMedia();
	};

	public static void changeAdv(){
		advUrlInfolist = new ArrayList<AdvInfo>();
		advUrlInfolist = DBUtils.getSpecificColumnToList(AdvInfo.class,"adv_type",-1);
		if(advUrlInfolist!=null&&advUrlInfolist.size()>0){
			MyLogger.getInstance().e( " advUrlInfolist  ="+advUrlInfolist.toString());
			adv_view.setImageResources(advUrlInfolist, mAdCycleViewListener);
		}
	}

	public static void changeMediaPlayBt(){
		
		File file=new File(ConfigUtils.videoPath[0]);
		
		if(file == null || !file.exists()){ 
			media_play_bt.setVisibility(View.GONE);
		}else{
			media_play_bt.setVisibility(View.VISIBLE);
		}
	}

	private void initMedia() {

		holder = adv_sfv.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 实例化MediaPlayer对象
		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		player.setOnPreparedListener(this);
		// 指定需要播放文件的路径，初始化MediaPlayer

		try {
			player.setDataSource(ConfigUtils.videoPath[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void replayMedia() {

		player.reset();// 重置为初始状态

		try {
			player.setDataSource(ConfigUtils.videoPath[0]);
			player.prepare();// 预加载音频
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(AdvInfo info, int position, View imageView) {
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			display(imageView,imageURL);
		}
	};

	public static void display(ImageView imageView, String iconUrl) {
		ImageOptions imageOptions = new ImageOptions.Builder()
		.setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
		.setImageScaleType(ImageView.ScaleType.FIT_XY)
		.build();
		x.image().bind(imageView, iconUrl,imageOptions);
	}

	@Override
	protected void onPause() {
		super.onPause();
		player.stop();
		player.reset();// 重置为初始状态
		isPlaying = false;
		adv_sfv.setVisibility(View.GONE);
		adv_view.pushImageCycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.release();
		isPlaying = false;  //jack add
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (v.getId()) {

		case R.id.app_main_ll:
			intent.setClass(this, FragmentMainActivity.class);
			startActivity(intent);
			break;

		case R.id.media_play_bt:
			if(!NetUtils.isConnected(context)){
				MyToast.getManager(context).show(getString(R.string.network_disconnected));
			}else{
				if (isPlaying) {
					player.stop();
					adv_sfv.setAlpha(0);
					isPlaying = false;
					replaying = true;
				} else {
					adv_sfv.setVisibility(View.VISIBLE);
					if (replaying) {
						adv_sfv.setAlpha(100);
						replayMedia();
						player.start();
					}
					isPlaying = true;
				}
			}
			break;

		case R.id.community_services_ll:
			intent.setClass(this, CommunityServicesActivity.class);
			startActivity(intent);
			break;

		case R.id.personal_center_ll:
			intent.setClass(this, SettingActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	//begin jack add
	private void playNextMedia(int count) {
		adv_sfv.setVisibility(View.VISIBLE);
		adv_sfv.setAlpha(100);
		player.reset();// 重置为初始状态

		try {
			player.setDataSource(ConfigUtils.videoPath[count]);
			player.prepare();// 预加载音频
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.start();
		isPlaying = true;
	}
	//end jack add

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// 当SurfaceView中的Surface被创建的时候被调用

		// 指定MediaPlayer在当前的Surface中进行播放
		player.setDisplay(holder);
		// 在指定了MediaPlayer播放的容器后，使用prepare或者prepareAsync来准备播放了
		try
		{
			player.prepareAsync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 当MediaPlayer播放完成后触发

		adv_sfv.setAlpha(0);
		isPlaying = false;
		
		//begin jack add
		mCount++;

		if(mCount >= 3) {
			mCount = 0;
		}

		playNextMedia(mCount);
		//end jack add
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 当prepare完成后，该方法触发，在这里我们播放视频
		player.start();

	}

	//设置各个定时任务的Alarm，为了避免所有工作同时进行，闹钟起始时间不同
	private void setAlarm(){
		//set weather alarm 
		Intent weather_intent = new Intent(ConfigUtils.update_weather_alarm);
		AlarmManager weather_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent weather_pi = PendingIntent.getBroadcast(this,0,weather_intent,0);
		weather_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),24*60*60*1000,weather_pi); 
		//		weather_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() ,10*1000,weather_pi); 

		Intent time_intent = new Intent(ConfigUtils.update_time_alarm);
		AlarmManager time_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent time_pi = PendingIntent.getBroadcast(this,0,time_intent,0);
		time_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5*60*1000,24*60*60*1000,time_pi); 
		//		time_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() ,10*1000,time_pi); 


		Intent ad_intent = new Intent(ConfigUtils.update_ad_alarm);
		AlarmManager ad_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent ad_pi = PendingIntent.getBroadcast(this,0,ad_intent,0);
		ad_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),24*60*60*1000,ad_pi); 
		//		ad_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() ,10*1000,ad_pi); 


		Intent renew_intent = new Intent(ConfigUtils.get_renew_alarm);
		AlarmManager renew_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent renew_pi = PendingIntent.getBroadcast(this,0,renew_intent,0);
		renew_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1*60*1000,24*60*60*1000,renew_pi); 
		//		renew_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() ,10*1000,renew_pi); 

		Intent filter_intent = new Intent(ConfigUtils.update_filter_info_alarm);
		AlarmManager filter_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent filter_pi = PendingIntent.getBroadcast(this,0,filter_intent,0);
		filter_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 15*60*1000,24*60*60*1000,filter_pi); 
		//		filter_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10*1000,20*1000,filter_pi); 

		Intent upgrade_intent = new Intent(ConfigUtils.upgrade_version_alarm);
		AlarmManager upgrade_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent upgrade_pi = PendingIntent.getBroadcast(this,0,upgrade_intent,0);
		upgrade_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5*1000,24*60*60*1000,upgrade_pi); 
		//		upgrade_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10*1000,20*1000,upgrade_pi); 

		//服务器暂未提供接口
		Intent reset_intent = new Intent(ConfigUtils.reset_device_alarm);
		AlarmManager reset_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent reset_pi = PendingIntent.getBroadcast(this,0,reset_intent,0);
//		reset_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 30*1000,24*60*60*1000,reset_pi); 
		//		reset_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10*1000,20*1000,reset_pi); 

		Intent hints_intent = new Intent(ConfigUtils.expiration_hints_action);
		AlarmManager hints_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent hints_pi = PendingIntent.getBroadcast(this,0,hints_intent,0);
		hints_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() ,24*60*60*1000,hints_pi); 
		//		hints_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10*1000,20*1000,hints_pi); 
	}
}