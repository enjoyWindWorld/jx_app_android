package com.jx.maneger.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @创建者： zhangbo
 * @项目名: CommonFramework
 * @包名: com.ronghui.common_libray.util
 * @创建时间： 2016/8/15 17:53
 * @描述： ${TODO} 广播接受者
 */

public class BroadcastUtil {
    public static void registerBroadcast(BroadcastReceiver receiver,
                                         Context context, String action) {
        if (receiver == null || context == null || action == null) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
                filter);
    }

    public static void registerBroadcast(Context context, BroadcastReceiver receiver
            , String... actions) {
        if (receiver == null || context == null || actions == null || actions.length == 0) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        try {
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
                    filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterBroadcast(BroadcastReceiver receiver,
                                           Context context) {
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(
                    receiver);
        } catch (Exception e) {
        }
    }

    public static void sendBroadcast(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 监听屏幕打开
     *
     * @param context
     * @param receiver
     */
    public static void registerScreenActionReceiver(Context context, BroadcastReceiver receiver) {
        if (context != null && receiver != null) {
            final IntentFilter filter = new IntentFilter();
            //filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            try {
                context.registerReceiver(receiver, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
