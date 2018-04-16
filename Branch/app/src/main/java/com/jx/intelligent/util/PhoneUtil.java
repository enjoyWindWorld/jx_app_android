package com.jx.intelligent.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class PhoneUtil {

    /**
     * 获取当前手机语言
     *
     * @param context
     * @return
     */
    public static String getPhoneLanguage(Context context) {
        if (context != null) {
            Locale locale = context.getResources().getConfiguration().locale;
            return locale.getLanguage();
        }
        return "zh";

    }

    /**
     * @param context
     * @return true当前手机语言为中文
     */
    public static boolean isCN(Context context) {
        if (context != null) {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.toLowerCase().contains("zh")||language.toLowerCase().contains("cn"))
                return true;
            else
                return false;
        }
        return false;
    }

    public static float pixelToDp(Context context, float val) {
        float density = context.getResources().getDisplayMetrics().density;
        return val * density;
    }

    /**
     * 屏幕分辨率
     *
     * @param activity
     * @return pixels[0]X轴(宽度)像素，pixels[1]Y轴(高度)像素
     */
    public static int[] getScreenPixels(Activity activity) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaysMetrics);
        int screenWidth = displaysMetrics.widthPixels;
        int screenHeight = displaysMetrics.heightPixels;
        int[] pixels = new int[2];
        pixels[0] = screenWidth;
        pixels[1] = screenHeight;
        return pixels;
    }

    /**
     * 5.0.2_21, 5.0.2为系统版本名，21为系统版本号
     *
     * @return Android_5.0.2_21
     */
    public static String getPhoneSystemVersion() {
        return "Android" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT;
    }

    public static String getLocalLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

    /**
     * 用户绑定主机和登录接口的language字段值。用于告诉主机设置的默认语言
     *
     * @param context
     * @return
     */
    public static String getLanguage(Context context) {
        String lan = "chinese";
        if (!PhoneUtil.isCN(context)) {
            lan = "english";
        }
        return lan;
    }

    public static int getAndroidSdk(Context context) {
        return Build.VERSION.SDK_INT;
    }
}
