/*
 * 广告轮播界面，包含视频广告；初始化所有广播；应用主activity。
 */
package com.kxw.smarthome;

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
import android.os.Handler;
import android.os.HandlerThread;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android_serialport_api.SerialPortUtil;

import com.kxw.smarthome.entity.AdvInfo;
import com.kxw.smarthome.entity.VerificationData;
import com.kxw.smarthome.entity.VideoInfo;
import com.kxw.smarthome.entity.WiFiInfo;
import com.kxw.smarthome.imagecycleview.ImageCycleView;
import com.kxw.smarthome.imagecycleview.ImageCycleView.ImageCycleViewListener;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.ToolsUtils;
import com.kxw.smarthome.utils.Utils;
import com.kxw.smarthome.utils.WifiUtils;

@SuppressLint("NewApi")
public class AdvMainActivity extends BaseActivity implements 
OnClickListener, Callback, OnCompletionListener, OnPreparedListener {

	private Context context;
	private ImageCycleView adv_view;
	private ImageView media_play_bt;
	private LinearLayout back_ll, home_page_ll, app_main_ll,community_services_ll, personal_center_ll;
	private RelativeLayout adv_layout;
	private List<AdvInfo> advUrlInfolist ;
	private SurfaceView adv_sfv;
	private SurfaceHolder holder;
	private MediaPlayer player;
	private boolean isPlaying = false;
	private boolean replaying = false;
	private WifiUtils wifiUtils;
	private int mCount = 0;  //jack add
	
	private SerialPortUtil mSerialPortUtil;
	private Handler mUpgradeHandler;  
	private HandlerThread mUpgradeHandlerThread;  
	
	private Handler mVideoHandler;  
	private HandlerThread mVideoHandlerThread;  
	
	private static List<VideoInfo> videoInfos = new ArrayList<>();
	
	private boolean is_ShowADV;//是否在广告首页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.loadLibrary("jxsmart");	
		setBaseContentView(R.layout.adv_activity);
		context=this;
		initView();
		setAlarm();
	}


	private void initView() {
		// TODO Auto-generated method stub

		back_ll = (LinearLayout) findViewById(R.id.title_back_ll);
		back_ll.setVisibility(View.GONE);
		refresh_ll.setVisibility(View.VISIBLE);

		adv_view = (ImageCycleView) findViewById(R.id.ad_view);
		adv_sfv = (SurfaceView) findViewById(R.id.adv_sfv);
		media_play_bt = (ImageView) findViewById(R.id.media_play_bt);
		home_page_ll = (LinearLayout) findViewById(R.id.home_page_ll);
		app_main_ll = (LinearLayout) findViewById(R.id.app_main_ll);
		community_services_ll = (LinearLayout) findViewById(R.id.community_services_ll);
		personal_center_ll = (LinearLayout) findViewById(R.id.personal_center_ll);
		adv_layout = (RelativeLayout) findViewById(R.id.adv_layout);
		home_page_ll.setOnClickListener(this);
		community_services_ll.setOnClickListener(this);
		app_main_ll.setOnClickListener(this);
		personal_center_ll.setOnClickListener(this);
		adv_sfv.setOnClickListener(this);
		media_play_bt.setOnClickListener(this);
		mSerialPortUtil = MyApplication.getSerialPortUtil();
	    mUpgradeHandlerThread = new HandlerThread("AdMainActiivity_mUpgradeHandlerThread", 5);  
	    mUpgradeHandlerThread.start();  
	    mUpgradeHandler = new Handler(mUpgradeHandlerThread.getLooper()); 
	    mUpgradeHandler.post(mUpgradeRunnable); 
	    
	    mVideoHandlerThread = new HandlerThread("AdMainActiivity_mVideoHandlerThread", 5);  
	    mVideoHandlerThread.start();  
	    mVideoHandler = new Handler(mVideoHandlerThread.getLooper()); 
	    
	    MyApplication.getInstance().setActivity(AdvMainActivity.this);
	    
	    is_ShowADV = true;
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

	public void changeAdv(){
		advUrlInfolist = new ArrayList<AdvInfo>();
		advUrlInfolist = DBUtils.getSpecificColumnToList(AdvInfo.class,"adv_type",-1);
		if(advUrlInfolist == null)
		{
			advUrlInfolist = new ArrayList<>();
		}
		adv_view.setImageResources(advUrlInfolist, mAdCycleViewListener);
	}

	private void initMedia() {
		
		mCount = 0;
		holder = adv_sfv.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 实例化MediaPlayer对象
		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		player.setOnPreparedListener(this);
		// 指定需要播放文件的路径，初始化MediaPlayer
	}

	protected void replayMedia() {

		if(videoInfos != null && videoInfos.size() > 0)
		{
			try {
				adv_sfv.setAlpha(100);
				player.reset();// 重置为初始状态
				player.setDataSource(videoInfos.get(0).getVideo_url());
				player.prepare();// 预加载音频
				player.start();
				isPlaying = true;
			} catch (Exception e) {
				MyToast.getManager(AdvMainActivity.this).show(getString(R.string.play_video_faild));
				e.printStackTrace();
				stopVideo();
			}
		}
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(AdvInfo info, int position, View imageView) {
			if(!NetUtils.isConnected(context)){
				MyToast.getManager(context).show(getString(R.string.network_disconnected));
			}else{
				getPicVideoUrls(info.getId()+"");
			}
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
		.setFailureDrawableId(R.drawable.icon_error)
		.build();
		
		if(!ToolsUtils.isEmpty(iconUrl))
		{
			if(iconUrl.contains("data.jx-inteligent.tech:15010/jx"))
			{
				iconUrl = iconUrl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
			}
			else if(iconUrl.contains("http://113.106.93.195:15010/jx")) 
			{
				iconUrl = iconUrl.replace("http://113.106.93.195:15010/jx", "www.szjxzn.tech:8080/old_jx");
			}
		}
		
		x.image().bind(imageView, iconUrl, imageOptions);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adv_view.startImageCycle();
		isPlaying = false; //jack add
		initData();
		initMedia();
		is_ShowADV = true;
		
		//检查有没有主动播放的视频资源
        new Handler().postDelayed(new Runnable() {
            public void run() {
            	if(is_ShowADV)
            	{
            		getIsAccordVideoUrls();
            	}
            }
        }, 10*1000);
	};

	@Override
	protected void onPause() {
		super.onPause();
		stopVideo();
		is_ShowADV = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.release();
		isPlaying = false;  //jack add
		is_ShowADV = false;
		mUpgradeHandler.removeCallbacks(mUpgradeRunnable);
		mVideoHandler.removeCallbacks(mVideoRunnable);
	}
	
	public void stopVideo()
	{
		if(player != null)
		{
			player.stop();
			player.reset();// 重置为初始状态
			isPlaying = false;
			adv_layout.setVisibility(View.GONE);
			adv_view.pushImageCycle();
		}
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

		case R.id.adv_sfv:
			if (videoInfos != null && videoInfos.size() > mCount
					&& !ToolsUtils.isEmpty(videoInfos.get(mCount).adv_url)) {
				Intent intent_web = new Intent(AdvMainActivity.this,
						WebViewActivity.class);
				intent_web.putExtra("url", videoInfos.get(mCount).adv_url);
				startActivity(intent_web);
			}
			break;

		case R.id.media_play_bt:
			if (isFastDoubleClick()) {
				return;
			}
			playVideo(videoInfos);
			break;

		case R.id.community_services_ll:
			intent.setClass(this, CommunityServicesActivity.class);
			startActivity(intent);
			break;

		case R.id.personal_center_ll:
			intent.setClass(this, SettingActivity.class);
			startActivity(intent);
			break;

		case R.id.title_refresh_ll:
			System.out.println("===refresh===");

			Intent ad_intent = new Intent(ConfigUtils.update_ad_alarm);
			sendBroadcast(ad_intent);

			VerificationData verificationData = new VerificationData(
					AdvMainActivity.this);
			verificationData.play();

			 Intent v_intent = new Intent(ConfigUtils.verification_data_action);
			 sendBroadcast(v_intent);
			break;
			
		case R.id.current_city_tv:
			Intent weather_intent = new Intent(ConfigUtils.update_weather_alarm);
			sendBroadcast(weather_intent);
			break;
			
		case R.id.current_time_tv:
			MyToast.getManager(getApplicationContext()).show("开始同步网络时间");
			Intent time_intent = new Intent(ConfigUtils.update_time_alarm);
			sendBroadcast(time_intent);
			break;

		default:
			break;
		}
	}
	
	// 避免重复点击
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long lead_time = time - lastClickTime;
		if (0 < lead_time && lead_time < 2000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	//begin jack add
	private void playNextMedia(int count) {
		if(videoInfos != null && videoInfos.size() > 0)
		{
			mCount = count;
			adv_layout.setVisibility(View.VISIBLE);
			adv_sfv.setAlpha(100);
			mVideoHandler.post(mVideoRunnable);
		}
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

	/* (non-Javadoc)
	 * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer)
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 当MediaPlayer播放完成后触发

//		adv_sfv.setAlpha(0);
//		isPlaying = false;
				
		if(videoInfos.size() > 0 && videoInfos.size() > mCount)
		{
			videoInfos.get(mCount).setIs_accord(0);
			DBUtils.updateDB(videoInfos.get(mCount));
			
			//begin jack add
			mCount++;

			if(videoInfos != null && mCount >= videoInfos.size()) {
				mCount = 0;
			}

			playNextMedia(mCount);
			//end jack add
		}
		else
		{
			stopVideo();
		}
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
		weather_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 24*60*60*1000, weather_pi); 

		Intent time_intent = new Intent(ConfigUtils.update_time_alarm);
		AlarmManager time_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent time_pi = PendingIntent.getBroadcast(this,0,time_intent,0);
		time_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1*60*1000, 24*60*60*1000, time_pi); 

		Intent ad_intent = new Intent(ConfigUtils.update_ad_alarm);
		AlarmManager ad_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent ad_pi = PendingIntent.getBroadcast(this,0,ad_intent,0);
		ad_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 2*60*1000, 1*60*60*1000, ad_pi); 

		Intent renew_intent = new Intent(ConfigUtils.get_renew_alarm);
		AlarmManager renew_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent renew_pi = PendingIntent.getBroadcast(this,0,renew_intent,0);
		renew_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 3*60*1000, 24*60*60*1000, renew_pi); 

		//上传滤芯寿命（半个小时传一次）
		Intent filter_intent = new Intent(ConfigUtils.update_filter_info_alarm);
		AlarmManager filter_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent filter_pi = PendingIntent.getBroadcast(this,0,filter_intent,0);
		filter_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 4*60*1000, 30*60*1000, filter_pi); 

		Intent upgrade_intent = new Intent(ConfigUtils.upgrade_version_alarm);
		AlarmManager upgrade_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent upgrade_pi = PendingIntent.getBroadcast(this,0,upgrade_intent,0);
		upgrade_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5*60*1000, 24*60*60*1000, upgrade_pi); 

		Intent hints_intent = new Intent(ConfigUtils.expiration_hints_action);
		AlarmManager hints_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent hints_pi = PendingIntent.getBroadcast(this,0,hints_intent,0);
		hints_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 6*60*1000, 24*60*60*1000, hints_pi); 
		
		//同步本地数据库、主板和线上滤芯寿命
		Intent sync_intent = new Intent(ConfigUtils.sync_filter_life_action);
		AlarmManager sync_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent sync_pi = PendingIntent.getBroadcast(this,0,sync_intent,0);
		sync_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 7*60*1000, 24*60*60*1000, sync_pi); 
		
		//验证存在主板里的套餐数据还正不正常，不正常就纠正过来（验证每一个小时验证一次）
		Intent verification_data_intent = new Intent(ConfigUtils.verification_data_action);
		AlarmManager verification_data_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent verification_data_pi = PendingIntent.getBroadcast(this, 0, verification_data_intent, 0);
		verification_data_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 8*60*1000, 1*60*60*1000, verification_data_pi); 
		
		//验证存在服务器里的套餐数据是否用完了， 用完了就清了主板的，24小时验证一次
		Intent verification_no_data_intent = new Intent(ConfigUtils.verification_no_data_action);
		AlarmManager verification_no_data_am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		PendingIntent verification_no_data_pi = PendingIntent.getBroadcast(this, 0, verification_no_data_intent, 0);
		verification_no_data_am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 10*60*1000, 24*60*60*1000, verification_no_data_pi); 
	}
	
	//开启净水器	
	private Runnable mUpgradeRunnable = new Runnable() {  
	    @Override  
	    public void run() {
			while (true) {
				if(!Utils.inuse){//串口没有使用
					Utils.inuse = true;
					int times=0;
					int setResult = -1;
					int returnsResult = -1;
					do{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//c++用0表示false，1表示true
						setResult = mSerialPortUtil.setVerSwitch(false);
						if(setResult<0){
							//faile to sent data	
							break;
						}	
						//succes to sent data					
						returnsResult=mSerialPortUtil.getReturn();
						if(returnsResult>=0){
							mUpgradeHandler.removeCallbacks(mUpgradeRunnable);  
							break;
						}else{
							times++;
						}
					}while(times < 3);
					Utils.inuse = false;
					return;
				}
			}
		}  
	}; 	
	
	/**
	 * 获取对应的图片广告视频
	 * @param picid
	 */
	public void getPicVideoUrls(String picid)
	{
		videoInfos = DBUtils.getSpecificColumnToList(VideoInfo.class, "sup_id", picid);
//		System.out.println("==对应图片的视频路径=="+videoInfos);
		playVideo(videoInfos);
	}
	
	public void getIsAccordVideoUrls()
	{
		if(!isPlaying)
		{
			videoInfos = DBUtils.getSpecificColumnToList(VideoInfo.class, "is_accord", 1);
//			System.out.println("==主动推送的视频路径=="+videoInfos);
			playVideo(videoInfos);
		}
	}
	
	public void playVideo(List<VideoInfo> videoInfos)
	{
    	if(videoInfos != null && videoInfos.size() > 0)
		{
			if (isPlaying) 
			{
				player.stop();
//				adv_sfv.setAlpha(0);
				isPlaying = false;
				replaying = true;
			} 
			else
			{
				if (replaying)
				{
//					replayMedia();
					playNextMedia(0);
				}
				else
				{
					playNextMedia(mCount);
				}
			}
		}
	}

	public boolean isIs_ShowADV() {
		return is_ShowADV;
	}


	public void setIs_ShowADV(boolean is_ShowADV) {
		this.is_ShowADV = is_ShowADV;
	}
	
	//续费	
	private Runnable mVideoRunnable = new Runnable() {  
	    @Override  
	    public void run() {
	    	try 
	    	{
	    		System.out.println("==========="+videoInfos.get(mCount).getId());
	    		player.reset();// 重置为初始状态
				player.setDataSource(videoInfos.get(mCount).getVideo_url());
				player.prepare();// 预加载音频
				player.start();
				isPlaying = true;
			} catch (Exception e) {				
				MyToast.getManager(AdvMainActivity.this).show(getString(R.string.play_video_faild));
				e.printStackTrace();
				if(player != null)
				{
					player.stop();
					player.reset();// 重置为初始状态
					isPlaying = false;
					adv_view.pushImageCycle();
				}
			}
	    }  
	}; 
}