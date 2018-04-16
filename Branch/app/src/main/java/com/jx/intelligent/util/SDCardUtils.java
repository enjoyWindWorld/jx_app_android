package com.jx.intelligent.util;


import android.content.Context;
import android.os.Environment;

/** 
 * @description：
 * @author  xlq
 * @date 创建时间：2016-1-11 下午4:06:57 
 * @version 1.1 
 */
public class SDCardUtils {
    
    /**
     * 获取根目录
     */
    public static String rootDir = null;
    
    /**
     * 获取sdCard目录
     */
    public static String dicmDir = null;
    
    /**
     * 获取手机根目录路径
     * 
     * @return
     */
    public static String getAbsRootDir(Context context)
    {

        if (rootDir != null)
            return rootDir;
        boolean isAtSD = Utils.haveSdCard();
        if (isAtSD)
        {
            try
            {
                return context.getExternalFilesDir(
                        Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            } catch (Exception e)
            {
                return context.getFilesDir().getAbsolutePath();
            }
        } else
        {
            return context.getFilesDir().getAbsolutePath();
        }
    }
    
	/**得到sdCard路径
	 * @return
	 */
	public  static String getDicmDir(Context context) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			try {
				return Environment.getExternalStorageDirectory().toString();// 获取跟目录
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 return context.getFilesDir().getAbsolutePath();
			}
		}else{
			 return context.getFilesDir().getAbsolutePath();
		}

	}

}
