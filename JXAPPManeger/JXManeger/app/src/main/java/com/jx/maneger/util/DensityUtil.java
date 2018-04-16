package com.jx.maneger.util;


public final class DensityUtil {
    /**
     * 屏幕密度
     */
    private static float density = UIUtil.getResources().getDisplayMetrics().density;
    /**
     * 屏幕按比例缩小的密度
     */
    private static float scaledDensity = UIUtil.getResources().getDisplayMetrics().scaledDensity;

    /**
     * dp转换成px
     *
     * @param dp 独立像素单位
     * @return dp对应的像素
     */
    public static int dp2px(int dp) {
        return (int) (dp * density + .5f);
    }

    /**
     * px转换成dp
     *
     * @param px 像素单位
     * @return 像素px对应的独立像素dp
     */
    public static int px2dp(int px) {
        return (int) (px / density + .5f);
    }

    /**
     * px值转换为sp值，保证文字大小不变
     *
     * @param px 像素单位
     * @return px对应的字体大小单位
     */
    public static int px2sp(float px) {
        return (int) (px / scaledDensity + 0.5f);
    }

    /**
     * sp值转换为px值，保证文字大小不变
     *
     * @param sp 字体大小单位
     * @return sp对应的像素
     */
    public static int sp2px(float sp) {
        return (int) (sp * scaledDensity + 0.5f);
    }
}
