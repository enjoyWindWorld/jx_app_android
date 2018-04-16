package com.kxw.smarthome.utils;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.kxw.smarthome.MyApplication;

public class DeviceUtils {

	public static final int NET_NONE = 0;
	public static final int NET_WIFI = 1;
	public static final int NET_WAP = 2;
	public static final int NET_NET = 3;

	private static Context _context;

	private static Context getContext() {
		if (_context == null) {

			_context = MyApplication.getInstances();

		}
		return _context;
	}

	// 获取自身安装路径
	public static String getFilesDir() {

		File file=getContext().getFilesDir();  
		String path=file.getAbsolutePath(); 

		if (path == null && isSdcardExisting()) {
			return Environment.getExternalStorageDirectory()+"/"+_context.getPackageName()+"/.head";
		}
		return path;
	}

	private static boolean isSdcardExisting() {  
		final String state = Environment.getExternalStorageState();  
		if (state.equals(Environment.MEDIA_MOUNTED)) {  
			return true;  
		} else {  
			return false;  
		}  
	}
	// 获取自身安装路径
	public static String getAppPath() {

		String path = getContext().getApplicationInfo().sourceDir;
		if (path == null) {
			return "unkown";
		}
		return path;
	}

	/**
	 * get IMEI
	 * 
	 * @return IMEI
	 * */
	public static String getIMEI() {
		String IMEI = null;
		TelephonyManager telephonemanage = (TelephonyManager) getContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonemanage.getDeviceId();
		if (IMEI == null)
			return "-1";
		return IMEI;
	}

	/**
	 * get IMSI
	 * 
	 * @return IMSI
	 * */
	public static String getIMSI() {
		String IMSI = null;
		TelephonyManager telephonemanage = (TelephonyManager) getContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		IMSI = telephonemanage.getSubscriberId();
		if (IMSI == null)
			return "-1";
		return IMSI;
	}

	/**
	 * get MAC
	 * 
	 * @return MAC
	 * */
	public static String getMac() {
		WifiManager wifiManager = (WifiManager) getContext().getSystemService(
				Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String result = wifiInfo.getMacAddress();
		if (result == null)
			return "-1";
		return result;
	}

	/**
	 * get AndroidID
	 * 
	 * @return AndroidID
	 * */
	public static String getAndroidID() {
		String androidId = Secure.getString(getContext().getContentResolver(),
				Secure.ANDROID_ID);
		if (androidId == null)
			return "-1";
		return androidId;
	}

	/**
	 * get phone Mode
	 * 
	 * @return MODEL
	 * */
	public static String getPhoneMode() {
		return android.os.Build.MODEL;
	}

	/**
	 * get app version
	 * 
	 * @return  version
	 */
	public static String getAppVersion() {
		try {
			PackageManager manager = getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getContext()
					.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "unkown";
		}

	}

	public static int getAppVersionCode() {
		try {
			PackageManager manager = getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getContext()
					.getPackageName(), 0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 系统版本
	public static String getSysVersion() {

		return android.os.Build.VERSION.RELEASE;
	}

	// 品牌
	public static String getPhoneBrand() {

		return android.os.Build.BRAND;
	}

	// ro.product.locale.language=zh #系统语言,zh表示中文
	// ro.product.locale.region=CN #系统所在地区,CN表示中国
	public static String getCountryCode() {
		String country = Locale.getDefault().getCountry();
		if (country == null)
			country = "";
		return country;

	}

	public static String getLang() {
		String lang = Locale.getDefault().getLanguage();
		if (lang == null)
			lang = "";
		return lang;

	}

	public static Integer getNetType() {
		String imsi = getIMSI();
		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")
					|| imsi.startsWith("46007")) {
				return 1;
			} else if (imsi.startsWith("46001")) {
				return 2;
			} else if (imsi.startsWith("46003")) {
				return 3;
			} else if (imsi.startsWith("454")) {
				return 4;
			} else {
				return 5;
			}
		} else {
			return 5;
		}
	}

}
