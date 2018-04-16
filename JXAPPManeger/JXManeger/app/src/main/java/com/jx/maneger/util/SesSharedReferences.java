package com.jx.maneger.util;

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
    public static String SHAREPREFERENCE_NAME = "jxmanager";
    public static String IS_LOGIN = "is_login";//登录状态
    private static String ACCOUNT = "account";
    private static String PARTNER_NUMBER = "partner_number";
    private static String SAFETY_MARK = "safety_mark";

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
   *p
   * @param context
   * @param partnerNumber
   */
    public static void setPartnerNumber(Context context, String partnerNumber) {
        Editor editor = getEditor(context);
        editor.putString(PARTNER_NUMBER, StringUtil.encrypt(partnerNumber));
        editor.commit();
    }

    /**
     * 获取用户编号
     *
     * @param context
     * @return
     */
    public static String getPartnerNumber(Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        return StringUtil.decode(sp.getString(PARTNER_NUMBER, null));
    }

    /**
     * 保存用户账号
     *
     * @param context
     * @param account
     */
    public static void setAccount(Context context, String account) {
        Editor editor = getEditor(context);
        editor.putString(ACCOUNT, StringUtil.encrypt(account));
        editor.commit();
    }

    /**
     * 获取用户账号
     *
     * @param context
     * @return
     */
    public static String getAccount(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return StringUtil.decode(sp.getString(ACCOUNT, null));
    }

    /**
     * 保存用户加密标识
     *
     * @param context
     * @param safetyMark
     */
    public static void setSafetyMark(Context context, String safetyMark) {
        Editor editor = getEditor(context);
        editor.putString(SAFETY_MARK, StringUtil.encrypt(safetyMark));
        editor.commit();
    }

    /**
     * 获取用户加密标识
     *
     * @param context
     * @return
     */
    public static String getSafetyMark(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return StringUtil.decode(sp.getString(SAFETY_MARK, null));
    }

    /**
     * 清空本地用户信息
     *
     * @param context
     */
    public static void cleanShare(Context context) {
        SesSharedReferences.setUsrLoginState(context, false);
        SesSharedReferences.setPartnerNumber(context, null);
        SesSharedReferences.setSafetyMark(context, null);
    }
}
