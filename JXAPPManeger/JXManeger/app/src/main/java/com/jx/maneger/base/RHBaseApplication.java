package com.jx.maneger.base;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.jx.maneger.constant.Constant;
import com.jx.maneger.http.dao.OkHttpDao;
import com.jx.maneger.util.SDCardUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareConfig;
import com.yolanda.nohttp.NoHttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class RHBaseApplication extends MultiDexApplication {

    private static Context mContext;
    private static Handler mHandler;
    private static RHBaseApplication instance;
    private static boolean isFirstCome = true;

    public static RHBaseApplication getInstance() {
        return instance;
    }

    public static boolean isFirstCome() {
        return isFirstCome;
    }

    public static void setIsFirstCome(boolean isFirstCome) {
        RHBaseApplication.isFirstCome = isFirstCome;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 1.上下文
        mContext = getApplicationContext();
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

        //bugly================
        // 获取当前包名
        String packageName = mContext.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mContext);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(mContext, "ea238f4c34", true, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
         CrashReport.initCrashReport(mContext, strategy);

        //初始化友盟
        UMConfigure.init(this,"5adb50e08f4a9d72980000d3","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //开启Log
        UMConfigure.setLogEnabled(true);
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
