package com.kxw.smarthome.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.kxw.smarthome.MyApplication;

import java.util.List;


/**
 * App相关工具类
 */

public class AppUtil {
    /**
     * 获取APP包名称
     *
     * @return APP包名称
     */
    public static String getPackageName() {
        return MyApplication.getInstances().getPackageName();
    }

    /**
     * 获取当前APP版本名称
     *
     * @return APP版本名称
     */
    public static String getVersionName() {
        try {
            return MyApplication.getInstances().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前APP版本号
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            return MyApplication.getInstances().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取卸载App的意图
     *
     * @param packageName 包名
     * @return 意图
     */
    public Intent getUninstallAppIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取打开App的意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    public static Intent getLaunchAppItent(Context context, String packageName) {
        return getIntentByPackageName(context, packageName);
    }

    /**
     * 获取App信息的意图
     *
     * @param packageName 包名
     * @return 意图
     */
    public static Intent getAppInfoIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 获取App信息分享的意图
     *
     * @param info 分享信息
     * @return 意图
     */
    public static Intent getShareInfoIntent(String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        return intent.putExtra(Intent.EXTRA_TEXT, info);
    }

    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return getIntentByPackageName(context, packageName) != null;
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return Intent
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 判断当前App处于前台还是后台
     * 需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
     * 并且必须是系统应用该方法才有效</p>
     *
     * @param context 上下文
     * @return {@code true}: 后台<br>{@code false}: 前台
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}
