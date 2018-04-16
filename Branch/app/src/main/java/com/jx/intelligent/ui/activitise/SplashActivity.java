package com.jx.intelligent.ui.activitise;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.igexin.sdk.PushManager;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.ui.activitise.getui.DemoIntentService;
import com.jx.intelligent.ui.activitise.getui.DemoPushService;
import com.jx.intelligent.ui.activitise.loginAndRegister.LoginActivity;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.SharedPreferencesUtil;
import com.jx.intelligent.util.UIUtil;

/**
 * @创建者： weifei
 * @项目名: jx
 * @包名: com.jx.intelligent.ui.fragments
 * @创建时间： 2016/11/10 5:50
 * @描述： ${TODO} 闪屏页
 */

public class SplashActivity extends Activity {
    private NotificationManager notificationManager;
    private DBManager dbManager;
    private String result;

    //延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private GoogleApiClient client;
    private static mSplashHandler handler;
    private String userId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void init() {
        clearNotification();
        if (handler == null) {
            handler =new mSplashHandler();
        }


        //如果是登录状态开启推送服务初始化
        PushManager.getInstance().initialize(UIUtil.getContext(),DemoPushService.class);
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(SplashActivity.this, DemoIntentService.class);



        dbManager = new DBManager(SplashActivity.this);
        dbManager.copyDBFile();
        RHBaseApplication.getInstance().setmAllCitise(dbManager.getAllCitiesFromMT());

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(!SharedPreferencesUtil.getBoolean(SplashActivity.this, "notOpenFistTime", false))
                {
                    Intent intent = new Intent(SplashActivity.this, GuideUi.class);
                    startActivityForResult(intent, 100);
                }
                else
                {
                    goHome();
                }
            }
        }, SPLASH_DELAY_MILLIS);


    }

    public static void sendMessage2(Message msg) {
        handler.sendMessage(msg);
    }

    public  class mSplashHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    result = (String) msg.obj;
                    if(result.length()>0){
                        showNotification(result);

                    }
                    break;
                case 1:

                    break;
            }


        }
    }
    /**
     * 在状态栏显示通知
     */
    private  void showNotification(String result) {

        // 创建一个NotificationManager的引用
        notificationManager = (NotificationManager)
                this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(SplashActivity.this);
        builder.setContentInfo("");
        builder.setContentText(result);
        builder.setContentTitle("通知标题");
        builder.setSmallIcon(R.mipmap.icon_jx);
        builder.setTicker("新消息");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(SplashActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
            notification.defaults = Notification.DEFAULT_ALL;
        }
        notificationManager.notify(0, notification);
    }

    //删除通知
    private void clearNotification() {
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

    }

    private void goHome() {
        //首先要判断是否登录
        if (SesSharedReferences.getUsrLoginState(UIUtil.getContext())) {
            //如果是登录状态 就拿Userid 绑定别名
            SesSharedReferences sp =new SesSharedReferences();
            userId = sp.getUserId(UIUtil.getContext());
            PushManager.getInstance().bindAlias(SplashActivity.this, userId);

             //跳转主页
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();

        } else {
            //如果没有登录 则跳转登录界面
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra("flag", Constant.LOGIN_FLAG);
            startActivityForResult(intent, Constant.REQUEST_CODE);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Splash Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();


        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            SesSharedReferences sp =new SesSharedReferences();
            userId = sp.getUserId(UIUtil.getContext());
            PushManager.getInstance().bindAlias(SplashActivity.this, userId);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
        else if(resultCode == Constant.EXIT_APP)
        {
            exitApp();
        }
        else if(resultCode == Constant.SHOW_GUIDE)
        {
            goHome();
        }
    }

    //退出app
    public void exitApp() {
        SplashActivity.this.finish();
        System.exit(0);
        Process.killProcess(Process.myPid());
    }
}

