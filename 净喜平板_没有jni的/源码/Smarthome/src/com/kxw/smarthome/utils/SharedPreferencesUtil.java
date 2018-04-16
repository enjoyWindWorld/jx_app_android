package com.kxw.smarthome.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author sun
 * 
 */
public class SharedPreferencesUtil {
	private static String CONFIG = "config";
	public static SharedPreferences sharedPreferences;

	public static void saveStringData(Context context, String key, String data) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(CONFIG,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, data).commit();
	}

	public static String getStringData(Context context, String key,
			String defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(CONFIG,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}
	
	public static void saveIntData(Context context, String key, int data) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(CONFIG,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putInt(key, data).commit();
	}

	public static int getIntData(Context context, String key,
			int defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(CONFIG,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}
	
	public static void saveLongData(Context context, String key, long data) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(CONFIG,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putLong(key, data).commit();
	}

	public static long getLongtData(Context context, String key,
			long defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(CONFIG,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getLong(key, defValue);
	}

	public static void setStringClear() {
		sharedPreferences.edit().clear().commit();
	}

	public static void setStringClear(String key) {
		sharedPreferences.edit().remove(key).commit();
	}
	
	public static Boolean getBoolean(Context context, String strKey) {
        SharedPreferences setPreferences = context.getSharedPreferences(
        		CONFIG, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, false);
        return result;
    }

    public static Boolean getBoolean(Context context, String strKey,
                                     Boolean strDefault) {
        SharedPreferences setPreferences = context.getSharedPreferences(
        		CONFIG, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, strDefault);
        return result;
    }


    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
        		CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }

}
