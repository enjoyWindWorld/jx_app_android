package com.jx.maneger.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.jx.maneger.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{

    public static Boolean isObjNotEmpty(Object obj)
    {

        if (obj.equals(null) || obj.equals(""))
        {
            return false;
        }
        return true;
    }

    public boolean isSDCard = false;
    private Context context;

    /**
     * 判断是否有Sd卡
     * 
     * @return
     */
    public static boolean haveSdCard()
    {

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 判断手机号码是否合法
     * 
     * @param mobile
     * @return
     */
    public static boolean isMobileNO(String mobile)
    {

        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 判断是否是11位数字
     * 
     * @param value
     * @return
     */
    public static boolean is11Number(String value)
    {

        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 获取手机号码
     * 
     * @return
     */
    public static String getMobilePhone(Context context)
    {

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phone = tm.getLine1Number();
        if (null == phone)
            phone = "";

        if (phone.startsWith(Constant.TELEPHONE_NUMBER_PREFIX))
            phone = phone
                    .substring(Constant.TELEPHONE_NUMBER_PREFIX.length(),
                            phone.length());

        return phone;
    }

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable(Context context)
    {

        NetworkInfo info = getNetworkInfo(context);
        if (info != null)
        {
            return info.isAvailable();
        }
        ToastUtil.showToast("无网络，请查看网络是否连接");
        return false;
    }

    /**
     * 检查是否是WIFI
     */
    public static boolean isWifi(Context context)
    {

        NetworkInfo info = getNetworkInfo(context);
        if (info != null)
        {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /**
     * 检查是否是移动网络
     */
    public static boolean isMobile(Context context)
    {

        NetworkInfo info = getNetworkInfo(context);
        if (info != null)
        {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context)
    {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static String Md5Encode(String md5)
    {

        String Md5Value = null;
        try
        {
            MessageDigest msg = MessageDigest.getInstance("MD5");
            msg.update(md5.getBytes());
            byte mByte[] = msg.digest();
            int value;
            StringBuffer buffer = new StringBuffer("");
            for (int i = 0; i < mByte.length; i++)
            {
                value = mByte[i];
                if (value < 0)
                {
                    value += 256;
                }
                if (value < 16)
                {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(value));
            }
            Md5Value = buffer.toString();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return Md5Value;
    }

    /**
     * 获取该软件版本
     * 
     * @param context
     * @return
     */
    public static int getVersionNumber(Context context)
    {

        int version = 0;
        try
        {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = packInfo.versionCode;
            return version;
        } catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取版本名字
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context)
    {

        String version;
        try
        {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否是email
     * 
     * @param email
     * @return
     */
    public static boolean isEmail(String email)
    {

        if (TextUtils.isEmpty(email))
        {
            return false;
        }
        String s = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
        Pattern p = Pattern.compile(s);

        Matcher m = p.matcher(email);

        boolean matches = m.matches();

        return matches;
    }

    @SuppressLint("DefaultLocale")
    public static String printHexString(byte[] buf)
    {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++)
        {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr)
    {

        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++)
        {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * function:计算文件大小
     * 
     * @param filePath
     * @return
     */
    public static String getTotaolFileSize(String filePath)
    {

        File file = new File(filePath);
        Long size = 0L;
        if (file.isDirectory())
        {
            size = getFileSizes(file);
        } else
        {
            size = getFileSize(file);
        }
        return ChangeToUnit(size);

    }

    private static Long getFileSizes(File file)
    {

        long size = 0;
        File flist[] = file.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size += getFileSizes(flist[i]);
            } else
            {
                size += getFileSize(flist[i]);
            }

        }
        return size;
    }

    public static String ChangeToUnit(Long size)
    {
        DecimalFormat fomat = new DecimalFormat("0.00");
        
        return fomat.format(size / (float)(1024 * 1024)) + " MB";

    }

    /**
     * function:清除缓存
     * 
     * @param file
     * @throws Exception
     */
    public static void clearCache(String file) throws Exception
    {

        File fileCahce = new File(file);
        if (fileCahce.exists())
        {
            if (fileCahce.isDirectory())
            {
                File[] childFiles = fileCahce.listFiles();
                for (int i = 0; i < childFiles.length; i++)
                {
                    clearCache(childFiles[i].getAbsolutePath());
                }
            } else
            {
                fileCahce.delete();
            }

        }
    }

    public static Long getFileSize(File file)
    {

        // long size = 0L;
        if (file.exists())
        {
            try
            {
                InputStream in = new FileInputStream(file);
                try
                {
                    return (long) in.available();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e)
            {

                e.printStackTrace();
            }

        }

        return 0L;

    }

    /**
     * 判断是否是密码
     * 
     * @param pass
     * @return
     */
    public static boolean isPass(String pass)
    {

        if (TextUtils.isEmpty(pass))
        {
            return false;
        }
        String s = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,16}";
//        String s = "[A-Za-z0-9]{6,16}";
        Pattern p = Pattern.compile(s);

        Matcher m = p.matcher(pass);

        boolean matches = m.matches();

        return matches;
    }

    public static Boolean isValidName(String name)
    {

        String s = "[.\\sA-Za-z\u4e00-\u9fa5_-]+";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(name);
        return m.matches();
    }
    public static Boolean isValidHanziPinyinName(String name)
    {

        String s = "[A-Za-z\u4e00-\u9fa5_-]{2,14}";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static Boolean isValid(String express, String email){
    	 Pattern p = Pattern.compile(express);
         Matcher m = p.matcher(email);
         return m.matches();
    }
    
    public static int dip2px(Context context, float dpValue)
    {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float px)
    {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 是否是汉字
     * @param name
     * @return
     */
    public static Boolean isValidHanZi(String name)
    {

        String s = "[\u4e00-\u9fa5]+";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 是否是拼音
     * @param name
     * @return
     */
    public static Boolean isValidPinYin(String name)
    {

        String s = "[A-Za-z]+";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * @param context
     * @param name
     * @param number
     * @param housePhone
     * @param address
     * @param qq
     * @return
     * 
     * add by xlq ：取消number为空时，数据库不插入的操作
     */
    public static Boolean insertDataToSystemSqlite(Context context,
                                                   String name, String number, String housePhone, String address,
                                                   String qq)
    {

        try
        {   String condition = null;
            if(!TextUtils.isEmpty(number)&&!TextUtils.isEmpty(housePhone)) {
        	condition = number;
            }
            if(!TextUtils.isEmpty(number) && TextUtils.isEmpty(housePhone)) {
        	condition = number;
            }
            if(!TextUtils.isEmpty(housePhone)&& TextUtils.isEmpty(number)) {
        	condition = housePhone;
            }
             int count = find(context,condition);
             if(count != 0) {
        	 ToastUtil.showToast("已存在本机通讯录");
	            return false;
             }            
            
            ContentValues mValues = new ContentValues();
            Uri mRawContactUri = context.getContentResolver().insert(
                    RawContacts.CONTENT_URI, mValues);

            long mId = ContentUris.parseId(mRawContactUri);
            mValues.clear();
            if (!TextUtils.isEmpty(name))
            {

                mValues.put(Data.RAW_CONTACT_ID, mId);
                mValues.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                mValues.put(StructuredName.GIVEN_NAME, name);
                context.getContentResolver().insert(
                        Data.CONTENT_URI, mValues);
                mValues.clear();
            }
            if (!TextUtils.isEmpty(number))
            {
                mValues.put(Data.RAW_CONTACT_ID, mId);
                mValues.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                mValues.put(Phone.NUMBER, number);
                mValues.put(Phone.TYPE, Phone.TYPE_MOBILE);
                context.getContentResolver().insert(
                        Data.CONTENT_URI, mValues);
                mValues.clear();
            } /*else
            {
                return false;
            }*/
            if (!TextUtils.isEmpty(housePhone))
            {
                mValues.put(Data.RAW_CONTACT_ID, mId);
                mValues.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                mValues.put(Phone.NUMBER, housePhone);
                mValues.put(Phone.TYPE, Phone.TYPE_HOME);
                context.getContentResolver().insert(
                        Data.CONTENT_URI, mValues);
                mValues.clear();
            }
            if (!TextUtils.isEmpty(address))
            {
                mValues.put(Data.RAW_CONTACT_ID, mId);
                mValues.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
                mValues.put(StructuredPostal.FORMATTED_ADDRESS, address);
                mValues.put(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
                context.getContentResolver().insert(
                        Data.CONTENT_URI, mValues);
                mValues.clear();
            }
            if (!TextUtils.isEmpty(qq))
            {
                mValues.put(Data.RAW_CONTACT_ID, mId);
                mValues.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
                mValues.put(Im.DATA, qq);
                mValues.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
                context.getContentResolver().insert(
                        Data.CONTENT_URI, mValues);

            }
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 通过条件查询数据库中是否已经保存了相同的记录
     * @param context
     * @param condition
     * @return
     */
    private static int find(Context context, String condition) {
	    // TODO Auto-generated method stub
        int count;
        Cursor c = context.getContentResolver().query(Uri.withAppendedPath(
                    PhoneLookup.CONTENT_FILTER_URI,condition), new String[] {
                    PhoneLookup._ID,
                    PhoneLookup.NUMBER,
                    PhoneLookup.DISPLAY_NAME,
                    PhoneLookup.TYPE, PhoneLookup.LABEL }, null, null,   null );
        count = c.getCount();
        c.close();
        return count;
    }

    public static void deleteDataFromSystemSqlite(Context context, String id)
    {

        Uri uri = Uri.withAppendedPath(RawContacts.CONTENT_URI, id);
        context.getContentResolver().delete(uri, null, null);
    }


    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                return true;
            }
        }
        return false;

    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 编码手机号码
     * @param s
     * @return
     */
    public static String encodePhoneNum(String s)
    {
        s = s.substring(0,3) + "****" + s.substring(7);
        return s;
    }

    public static boolean filterEmoji(String source) {
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern. CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是汉字
     * @param keyword
     * @return
     */
    public static Boolean isHanZi(String keyword)
    {
        String s = "[\u4e00-\u9fa5]+";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(keyword);
        return m.matches();
    }

    /**
     * 是否是字母
     * @param keyword
     * @return
     */
    public static Boolean isZhiMu(String keyword)
    {

        String s = "[.\\sA-Za-z]+";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(keyword);
        return m.matches();
    }
}
