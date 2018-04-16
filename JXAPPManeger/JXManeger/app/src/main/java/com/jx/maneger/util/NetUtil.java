package com.jx.maneger.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jx.maneger.R;
import com.jx.maneger.constant.NetType;


public class NetUtil {

    /**
     * 获取详细的网络状态
     *
     * @param context
     * @return {@link NetType}
     */
    public static int getNetState(Context context) {
        if (context == null) {
            return NetType.NET_ERROR;
        }
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con == null) {
            return NetType.NET_ERROR;
        }
        int netType = NetType.UNKNOWN;
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            final int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netType = NetType.WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        netType = NetType.GPRS_2;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        netType = NetType.GPRS_3;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        netType = NetType.GPRS_4;
                        break;
                    default:
                        String _strSubTypeName = networkInfo.getSubtypeName();
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (!TextUtils.isEmpty(_strSubTypeName) && _strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetType.GPRS_3;
                        } else {
                            netType = NetType.GPRS;
                        }
                        break;
                }
            } else if (type == ConnectivityManager.TYPE_ETHERNET) {
                netType = NetType.ETHERNET;
            }
        }
        return netType;
    }

    /**
     * 判断当前是否有网络连接
     *
     * @return -1无网络，1连接wifi，2连接其它网络
     */
    public static int curNetConnectState(Context context) {
        if (context == null) {
            return NetType.NET_ERROR;
        }
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NET_ERROR;
        }
        if (!networkInfo.isAvailable()) {
            return NetType.NET_ERROR;
        }
        if (networkInfo.isConnected()) {
            final int netType = networkInfo.getType();
            //wifi or 网线
            if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_ETHERNET) {
                return NetType.WIFI;
            } else {
                return NetType.GPRS;
            }
        }
        return NetType.NET_ERROR;
    }

    /**
     * 检测网络是否连接
     *
     * @param showNetworkErrorTips 是否提示显示网络错误信息，是表示显示，否表示不显示
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(boolean showNetworkErrorTips) {
        return isNetworkConnected(UIUtil.getContext(), showNetworkErrorTips);
    }

    /**
     * 检测网络是否连接
     *
     * @param context
     * @param showNetworkErrorTips 是否提示显示网络错误信息，是表示显示，否表示不显示
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(Context context, boolean showNetworkErrorTips) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnected();
            LogUtil.e("isConnected = " + isConnected);
            if (!isConnected && showNetworkErrorTips) {
                ToastUtil.showToast(R.string.text_network_error);
            }
            return isConnected;
        } else
            throw new NullPointerException("context is null");
    }

}
