package com.jx.maneger.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 布局加载及转换工具类
 */

public class LayoutUtil {
    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@LayoutRes int layoutResId) {
        return LayoutInflater.from(UIUtil.getContext()).inflate(layoutResId, null, false);
    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@NonNull Context context, @LayoutRes int layoutResId) {
        return LayoutInflater.from(context).inflate(layoutResId, null, false);
    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@NonNull ViewGroup root, @LayoutRes int layoutResId) {
        return LayoutInflater.from(UIUtil.getContext()).inflate(layoutResId, root, false);
    }

    /**
     * 布局转换成View对象
     *
     * @param layoutResId 布局文件资源ID
     * @return View对象
     */
    public static View inflate(@NonNull Context context, @NonNull ViewGroup root, @LayoutRes int layoutResId) {
        return LayoutInflater.from(context).inflate(layoutResId, root, false);
    }
}
