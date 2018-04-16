package com.jx.intelligent.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @创建者 weifei
 * @创建时间 2016/11/24 15:44
 * @描述 保存用户信息
 * @更新者 $author$
 * @更新时间
 * @更新描述 ${TODO}
 */
public class SesSharedReferences {
    public static String SHAREPREFERENCE_NAME = "jinxizhineng";
    public static String IS_LOGIN = "is_login";//登录状态
    private static String NICK_NAME = "nick_name";
    private static String PHONE_NUM = "phone_num";
    private static String USER_ID = "user_id";
    private static String ABOUT_VIDEO = "about_video";
    private static String RESULT_CITY = "result_city";
    private static String RESULT_DISTRICT = "result_district";

    public static SharedPreferences.Editor getEditor(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences(Context context) {

        SharedPreferences sp = context.getSharedPreferences(
                SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);

        return sp;
    }

    /**
     * 当前登录状态
     *
     * @param context
     * @param isLogin
     */
    public static void setUsrLoginState(Context context, Boolean isLogin) {

        Editor editor = getEditor(context);
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }

    /**
     * 获取当前登录状态值 true登录 false 退出登录
     *
     * @param context
     * @return
     */
    public static Boolean getUsrLoginState(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(IS_LOGIN, false);
    }

    /*
   * 保存用户编号
   *
   * @param context
   * @param nickname
   */
    public static void setUserId(Context context, String userId) {
        Editor editor = getEditor(context);
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    /**
     * 获取用户编号
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(USER_ID, null);
    }

    /*
     * 保存用户昵称
     *
     * @param context
     * @param nickname
     */
    public static void setNickName(Context context, String nickname) {
        Editor editor = getEditor(context);
        editor.putString(NICK_NAME, nickname);
        editor.commit();
    }

    /**
     * 获取用户昵称
     *
     * @param context
     * @return
     */
    public static String getNickName(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(NICK_NAME, null);
    }

    /**
     * 保存用户电话号码
     *
     * @param context
     * @param phoneNum
     */
    public static void setPhoneNum(Context context, String phoneNum) {
        Editor editor = getEditor(context);
        editor.putString(PHONE_NUM, phoneNum);
        editor.commit();
    }

    /**
     * 获取用户电话号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNum(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PHONE_NUM, null);
    }

    /**
     * 保存的城市
     *
     * @param context
     * @param city
     */
    public static void setCity(Context context, String city) {
        Editor editor = getEditor(context);
        editor.putString(RESULT_CITY, city);
        editor.commit();
    }

    /**
     * 获取城市
     *
     * @param context
     * @return
     */
    public static String getCity(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(RESULT_CITY, null);
    }


    /**
     * 保存的区域
     *
     * @param context
     * @param district
     */
    public static void setDistrict(Context context, String district) {
        Editor editor = getEditor(context);
        editor.putString(RESULT_DISTRICT, district);
        editor.commit();
    }

    /**
     * 获取区域
     *
     * @param context
     * @return
     */
    public static String getDistrict(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(RESULT_DISTRICT, null);
    }

    /**
     * 获取关于的aboutVideo
     *
     * @param context
     * @param aboutVideo
     */
    public static void setVideoUrl(Context context, String aboutVideo) {
        Editor editor = getEditor(context);
        editor.putString(ABOUT_VIDEO, aboutVideo);
        editor.commit();
    }

    /**
     * 获取用户电话号码
     *
     * @param context
     * @return
     */
    public static String getVideoUrl(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(ABOUT_VIDEO, "http://data.jx-inteligent.tech:21080/pic/jingxi1228.mp4");
    }


    /**
     * 清空本地用户信息
     *
     * @param context
     */
    public static void cleanShare(Context context) {
        SesSharedReferences.setUsrLoginState(context, false);
        SesSharedReferences.setUserId(context, null);
        SesSharedReferences.setNickName(context, null);
    }



}
