package com.jx.maneger.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * SnackBar消息提示工具类
 */

public class SnackbarUtil {
    private static Snackbar snackbar;

    /**
     * 显示Toast提示
     *
     * @param text     提示文本资源ID
     * @param duration 停留时间
     */
    private static void showSnackBar(@NonNull View view, @NonNull CharSequence text, int duration) {
        snackbar = Snackbar.make(view, text, duration);
        snackbar.show();
    }

    /**
     * 短时间SnackBar提示
     *
     * @param resId 资源ID，在res/string.xml中配置的字符ID
     */
    public static void showShortMessage(@NonNull View view, @StringRes int resId) {
        showShortMessage(view, UIUtil.getString(resId));
    }

    /**
     * 短时间SnackBar提示
     *
     * @param message 资源ID，在res/string.xml中配置的字符ID
     */
    public static void showShortMessage(@NonNull View view, @NonNull CharSequence message) {
        showSnackBar(view, message, Snackbar.LENGTH_SHORT);
    }

    /**
     * 长时间Toast提示
     *
     * @param message 要提示的信息
     */
    public static void showLongMessage(@NonNull View view, @NonNull CharSequence message) {
        showSnackBar(view, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间Toast提示
     *
     * @param resId 资源ID
     */
    public static void showLongMessage(@NonNull View view, @StringRes int resId) {
        showLongMessage(view, UIUtil.getString(resId));
    }
}
