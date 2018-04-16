package com.jx.intelligent.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jx.intelligent.base.RHBaseApplication;


public final class KeyboardUtil {
    /**
     * 显示软键盘
     *
     * @param view 控件
     */
    public static void showSoftKeyboard(@NonNull View view) {
        showSoftKeyboard(RHBaseApplication.getContext(), view);
    }

    /**
     * 显示软键盘
     *
     * @param context 应用程序上下文
     * @param view    控件
     */
    public static void showSoftKeyboard(@NonNull Context context, @NonNull View view) {
        if (context != null && view != null) {
            try {
                final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view 控件
     */
    public static void hideSoftKeyboard(View view) {
        hideSoftKeyboard(RHBaseApplication.getContext(), view);
    }

    /**
     * 隐藏软键盘
     *
     * @param context 当前Activity
     * @param view    控件
     */
    public static void hideSoftKeyboard(@NonNull Context context, View view) {
        if (context != null && view != null) {
            try {
                final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
