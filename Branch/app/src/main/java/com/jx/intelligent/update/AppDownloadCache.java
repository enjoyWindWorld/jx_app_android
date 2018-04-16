package com.jx.intelligent.update;

import android.content.Context;
import android.content.SharedPreferences;

import com.jx.intelligent.constant.Constant;

/**
 * 下载存储
 */
public class AppDownloadCache {
    private static final String DOWNLOAD_CACHE = "ronghui_app_download_cache";

    /**
     * 保存app升级下载的结束标志
     *
     * @param context
     */
    public static void saveFinish(Context context, boolean finish) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(Constant.SPF_NAME,
                0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DOWNLOAD_CACHE, finish);
        editor.commit();


    }

    /**
     * 获取app升级下载的结束标志
     *
     * @param context
     * @return
     */
    public static boolean getFinish(Context context) {
        if (context == null) {
            return false;
        }
        return context.getSharedPreferences(Constant.SPF_NAME, 0).getBoolean(DOWNLOAD_CACHE,false);
    }
}
