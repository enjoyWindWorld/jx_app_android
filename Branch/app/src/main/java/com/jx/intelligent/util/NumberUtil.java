package com.jx.intelligent.util;

import android.support.annotation.NonNull;

/**
 * Number相关工具类
 */

public class NumberUtil {
    /**
     * String类型值转化成int类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回浮点类型值，否则返回0
     */
    public static int toInt(@NonNull String value) {
        return Integer.parseInt(value);
    }

    /**
     * String类型值转化成long类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回浮点类型值，否则返回0
     */
    public static long toLong(@NonNull String value) {
        return Long.parseLong(value);
    }

    /**
     * String类型值转化成float类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回浮点类型值，否则返回0.0f
     */
    public static float toFloat(@NonNull String value) {
        return Float.parseFloat(value);
    }

    /**
     * String类型值转化成double类型值
     *
     * @param value 文本类型值
     * @return 转换成功返回浮点类型值，否则返回0.0
     */
    public static double toDouble(@NonNull String value) {
        return Double.parseDouble(value);
    }
}
