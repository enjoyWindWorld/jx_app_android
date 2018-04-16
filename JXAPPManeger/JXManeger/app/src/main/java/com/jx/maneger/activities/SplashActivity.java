package com.jx.maneger.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.igexin.sdk.PushManager;
import com.jx.maneger.R;
import com.jx.maneger.activities.loginAndRegister.LoginActivity;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.getui.JXMIntentService;
import com.jx.maneger.getui.JXMPushService;
import com.jx.maneger.util.DateUtil;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.UIUtil;

/**
 * @创建者： weifei
 * @项目名: jx
 * @创建时间： 2016/11/10 5:50
 * @描述： ${TODO} 闪屏页
 */

public class SplashActivity extends Activity {
    private NotificationManager notificationManager;
    private String result;

    //延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private static final int REQUEST_PERMISSION = 0;
    private GoogleApiClient client;
    private static mSplashHandler handler;
    private String partnerNumber;

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

        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            PushManager.getInstance().initialize(UIUtil.getContext(), JXMPushService.class);
        }
        PushManager.getInstance().registerPushIntentService(SplashActivity.this, JXMIntentService.class);

        if(RHBaseApplication.getInstance().isFirstCome())
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    goHome();
                }
            }, SPLASH_DELAY_MILLIS);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    goHome();
                }
            }, 600);
        }
    }

    public static void sendMessage2(Message msg) {
        handler.sendMessage(msg);
    }

    public class mSplashHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    result = (String) msg.obj;
                    if(result.length()>0){
                        showNotification(result);
                    }
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
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(SplashActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);//这个是点击的效果
        builder.setContentIntent(pendingIntent);
        Notification notification = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
            if(isFastRemind())
            {
                notification.defaults = Notification.FLAG_ONLY_ALERT_ONCE;
            }
            else
            {
                notification.defaults = Notification.DEFAULT_ALL;
            }
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
            partnerNumber = SesSharedReferences.getPartnerNumber(UIUtil.getContext());
            PushManager.getInstance().bindAlias(this.getApplicationContext(), partnerNumber);
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

        RHBaseApplication.getInstance().setIsFirstCome(false);
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
        if(resultCode == Constant.LOGIN_OK && data != null)
        {
            partnerNumber = SesSharedReferences.getPartnerNumber(UIUtil.getContext());
            PushManager.getInstance().bindAlias(this.getApplicationContext(), partnerNumber);

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

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    // 避免震动的太频繁
    private static long lastRemindTime;

    public static boolean isFastRemind() {
        long time = DateUtil._getGMTime();
        if (DateUtil.nowCurrentTime(lastRemindTime) > 0 && DateUtil.nowCurrentTime(lastRemindTime) < 15) {
            return true;
        }
        lastRemindTime = time;
        return false;
    }
}

