package com.jx.maneger.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * 单例模式 获取屏幕宽高的帮助类
 */
public class ScreenSizeUtil {

    private WindowManager manager;
    private DisplayMetrics dm;
    private static ScreenSizeUtil instance = null;
    private int screenWidth, screenHeigth;

    public static ScreenSizeUtil getInstance(Context mContext) {

        if (instance == null) {
            synchronized (ScreenSizeUtil.class) {

                if (instance == null)
                    instance = new ScreenSizeUtil(mContext);

            }
        }
        return instance;
    }

    private ScreenSizeUtil(Context mContext) {

        manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        screenHeigth = dm.heightPixels;

    }

    //获取屏幕宽度
    public int getScreenWidth() {
        return screenWidth;
    }

    //获取屏幕高度
    public int getScreenHeight() {

        return screenHeigth;
    }


}
