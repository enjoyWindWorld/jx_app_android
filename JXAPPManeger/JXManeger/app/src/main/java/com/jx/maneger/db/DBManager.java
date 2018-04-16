package com.jx.maneger.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


import com.jx.maneger.bean.CcityBean;
import com.jx.maneger.bean.City;
import com.jx.maneger.util.PinyinUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 处理地址的
 */
public class DBManager {
    private static final String ASSETS_NAME = "location.db";
    private static final String DB_NAME = "location.db";
    private static final String TABLE_NAME = "citysTabble";
    private static final String LOCATION_TABLE_NAME = "locationTabble";
    private static final String CACHE_TABLE_NAME = "cacheTabble";
    private static final String DEF_TABLE_NAME = "def";
    private static final String LINK_TABLE_NAME = "link";
    private static final String CITY_KEY = "city_key";
    private static final String CITY_NAME = "city_name";
    private static final String INITIALS = "initials";
    private static final String PINYIN = "pinyin";
    private static final String URL = "_url";
    private static final String JSON = "_json";
    private static final String SHORT_NAME = "short_name";
    private static final int BUFFER_SIZE = 1024;
    private static final String TAG = "DBManager";
    private String DB_PATH;
    private Context mContext;

//    public static DBManager init(){
//        if (mInstance == null){
//            synchronized (DBManager.class){
//                if (mInstance != null){
//                    mInstance = new DBManager();
//                }
//            }
//        }
//        return mInstance;
//    }

    public DBManager(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(ASSETS_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取所有城市
     *
     * @return
     */
    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<City> result = new ArrayList<City>();
        City city;
        while (cursor.moveToNext()) {
            String city_key = cursor.getString(cursor.getColumnIndex(CITY_KEY));
            String city_name = cursor.getString(cursor.getColumnIndex(CITY_NAME));
            String initials = cursor.getString(cursor.getColumnIndex(INITIALS));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            String short_name = cursor.getString(cursor.getColumnIndex(SHORT_NAME));

            city = new City(city_key, city_name, initials, pinyin, short_name);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }


    /**
     * 读取所有城市
     *
     * @return
     */
    public List<CcityBean> getAllCitiesSF() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + LOCATION_TABLE_NAME, null);
        List<CcityBean> result = new ArrayList<CcityBean>();
        CcityBean ccityBean;
        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String sheng = cursor.getString(cursor.getColumnIndex("sheng"));
            String di = cursor.getString(cursor.getColumnIndex("di"));
            String xian = cursor.getString(cursor.getColumnIndex("xian"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int level = cursor.getInt(cursor.getColumnIndex("level"));


            ccityBean = new CcityBean(code, sheng, di, xian, name, level);

            result.add(ccityBean);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CcityBeanComparator());
        return result;
    }


    /**
     * 通过名字或者拼音搜索
     *
     * @param keyword
     * @return
     */
    public List<City> searchCity(final String keyword) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "SELECT NAME FROM  DEF C WHERE C.NAME LIKE '%"+keyword+"%' and level = 2";
        Cursor cursor = db.rawQuery(sql, null);
        List<City> result = new ArrayList<City>();
        City city;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String fullname = cursor.getString(cursor.getColumnIndex("fullname"));
            int level = cursor.getInt(cursor.getColumnIndex("level"));
            city = new City(id+"", fullname, name, PinyinUtils.getPinyinString(name), PinyinUtils.getFirstLetterByChinese(name));
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }


    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }

    /**
     * a-z排序
     */
    private class CcityBeanComparator implements Comparator<CcityBean> {
        @Override
        public int compare(CcityBean lhs, CcityBean rhs) {
            String a = lhs.sheng.substring(0, 1);
            String b = rhs.sheng.substring(0, 1);


            return a.compareTo(b);
        }
    }

    /**
     * 读取对应的url的json数据
     *
     * @return
     */
    public String getUrlJsonData (String url) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select " + JSON + " from " + CACHE_TABLE_NAME + " where " + URL + " = '" + url +"'", null);
        String _json = " ";
        while (cursor.moveToNext()) {
            _json = cursor.getString(cursor.getColumnIndex(JSON));
        }
        cursor.close();
        db.close();
        return _json;
    }

    /**
     * 添加对应的url的json数据
     *
     * @return
     */
    public void insertUrlJsonData (String url, String js) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "INSERT INTO " + CACHE_TABLE_NAME + " VALUES('" + url + "', '" + js + "')";
        db.execSQL(sql);
        db.close();
    }

    /**
     * 修改对应的url的json数据
     *
     * @return
     */
    public void updateUrlJsonData (String url, String js) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "UPDATE " + CACHE_TABLE_NAME + " SET " + JSON + " = '" + js + "' where " + URL + " = '" + url + "'";
        db.execSQL(sql);
        db.close();
    }

    /**
     * 删除所有url的缓存数据
     *
     * @return
     */
    public void deleteUrlJsonData () {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "delete from " + CACHE_TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }

    /**
     * 获取url的缓存数据的数目
     * @return
     */
    public int getCountUrlJsonData()
    {
        int count = 0;
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "select count(*) from " + CACHE_TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);
        if (c != null && c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }


    //美团的===========
    /**
     * 读取所有城市
     * @return
     */
    public List<City> getAllCitiesFromMT() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "select * from def where id in (select toid from link where level = 2)";
        Cursor cursor = db.rawQuery(sql, null);
        List<City> result = new ArrayList<City>();
        City city;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String fullname = cursor.getString(cursor.getColumnIndex("fullname"));
            int level = cursor.getInt(cursor.getColumnIndex("level"));
            city = new City(id+"", fullname, name, PinyinUtils.getPinyinString(name), PinyinUtils.getFirstLetterByChinese(name));
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    /**
     * 获取对应城市的区域
     * @param cityname
     * @return
     */
    public List<City> getAllAreasFromMT(String cityname) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        String sql = "select * from def where id in (select toid from link where fromid in (select id from def where fullname = '"+cityname+"' and fullname != '台湾省'))and fullname != '"+cityname+"'";
        Cursor cursor = db.rawQuery(sql, null);
        List<City> result = new ArrayList<City>();
        City city;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String fullname = cursor.getString(cursor.getColumnIndex("fullname"));
            int level = cursor.getInt(cursor.getColumnIndex("level"));
            city = new City(id+"", fullname, name, PinyinUtils.getPinyinString(name), PinyinUtils.getFirstLetterByChinese(name));
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }
}
