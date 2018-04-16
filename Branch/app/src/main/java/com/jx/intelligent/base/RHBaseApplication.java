package com.jx.intelligent.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.jx.intelligent.bean.City;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.http.dao.OkHttpDao;
import com.jx.intelligent.result.LoginResult;
import com.jx.intelligent.ui.activitise.MainActivity;
import com.jx.intelligent.util.SDCardUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yolanda.nohttp.NoHttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RHBaseApplication extends MultiDexApplication {


    private static Context mContext;
    private static Handler mHandler;
    private static MainActivity mainActivity;
    private int notReadMsg=0;//未读消息
    private String ord_no;//订单编号
    private String tag;//这是标记微信支付回调界面里的服务发布的一个区分标记
    private int wXPay = -1;//是否微信的支付,服务发布的时候用到
    List<City> mAllCitise = new ArrayList<City>();
    private AMapLocation loc;

    private static RHBaseApplication instance;

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信 wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx2e4c1225544f04b1", "0be12d6a1ca7216eb97e5ed0c825f312");
        //豆瓣RENREN平台目前只能在服务器端配置
        //新浪微博
        //"3921700954", "04b48b094faeb16683c32669824ebdad"
        PlatformConfig.setSinaWeibo("4037901690", "8269d7d1ea11f8d7443191b049802fab");

        PlatformConfig.setQQZone("1105888900", "gPbHq1ipC4hpSYBE");

        Config.REDIRECT_URL="http://www.szjxzn.cn";
    }

    public int getNotReadMsg() {
        return notReadMsg;
    }

    public void setNotReadMsg(int notReadMsg) {
        this.notReadMsg = notReadMsg;
    }

    private LoginResult loginResult;


    public LoginResult getLoginResult() {
        if(loginResult == null)
        {
            loginResult = new LoginResult();
        }
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }


    public static RHBaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 1.上下文
        mContext = getApplicationContext();
        Config.DEBUG = false;
        // 2.主线程handler
        mHandler = new Handler();
        super.onCreate();
        try {
            OkHttpDao.getInstance(mContext, getAssets().open("jxhttps_2.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        NoHttp.initialize(this);
        Constant.ROOT_DIR = SDCardUtils.getAbsRootDir(this) + File.separator;
        UMShareAPI.get(this);

        // 获取当前包名
        String packageName = mContext.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mContext);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(mContext, "1967b62684", true, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy);
    }


    /**
     * 得到上下文
     */
    public static Context getContext() {
        return mContext;
    }


    /**
     * 得到主线程的handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        RHBaseApplication.mainActivity = mainActivity;
    }

    public String getOrd_no() {
        return ord_no;
    }

    public void setOrd_no(String ord_no) {
        this.ord_no = ord_no;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getwXPay() {
        return wXPay;
    }

    public AMapLocation getLoc() {
        return loc;
    }

    public void setLoc(AMapLocation loc) {
        this.loc = loc;
    }

    public void setwXPay(int wXPay) {
        this.wXPay = wXPay;
    }

    public List<City> getmAllCitise() {
        return mAllCitise;
    }

    public void setmAllCitise(List<City> mAllCitise) {
        this.mAllCitise = mAllCitise;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
