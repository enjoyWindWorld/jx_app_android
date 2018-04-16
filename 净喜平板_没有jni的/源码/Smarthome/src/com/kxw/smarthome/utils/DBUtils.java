package com.kxw.smarthome.utils;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.x;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import com.kxw.smarthome.entity.AdvInfo;
import com.kxw.smarthome.entity.VideoInfo;

public class DBUtils{

	private static DaoConfig daoConfig;
	public static DaoConfig getDaoConfig(){
		if(daoConfig==null){
			daoConfig = new DbManager.DaoConfig()
			.setAllowTransaction(true)//设置允许开启事务
			.setDbName("Smarthome.db")//创建数据库的名称
			// 不设置dbDir时, 默认存储在app的私有目录.
			//             .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
			.setDbVersion(1)//数据库版本号
			.setDbUpgradeListener(new DbUpgradeListener() {

				@Override
				public void onUpgrade(DbManager arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub

				}
			})
			.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
				@Override
				public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
					// TODO: ...
					//db.addColumn(...);
					// db.dropTable(...);
					// ...
					// or
					// db.dropDb();
				}
			});
		}
		return daoConfig;
	}

	public static  boolean  replaceAdvUrlInfoList(List<AdvInfo> list) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		MyLogger.getInstance().e("list saveAdvUrlInfoList="+list.toString());
		for(int size=0;size<list.size();size++){
			AdvInfo advUrlInfo = new AdvInfo();
			advUrlInfo = list.get(size);
			MyLogger.getInstance().e("list item="+list.get(size));
			try {
				db.replace(advUrlInfo);				
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.getInstance().e("e="+e);
				return false;
			}
		}
		return true;
	}
	
	public static  boolean  replaceVideoUrlInfoList(List<VideoInfo> list) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		MyLogger.getInstance().e("list saveAdvUrlInfoList="+list.toString());
		for(int size=0;size<list.size();size++){
			VideoInfo videoInfo = new VideoInfo();
			videoInfo = list.get(size);
			MyLogger.getInstance().e("list item="+list.get(size));
			try {
				db.replace(videoInfo);				
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.getInstance().e("e="+e);
				return false;
			}
		}
		return true;
	}

	public static  <T>  boolean replaceList(Class<T> clazz,List<T> list) throws DbException {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		db.delete(clazz);
		MyLogger.getInstance().e("list saveAdvUrlInfoList="+list.toString());
		for(int size=0;size<list.size();size++){
			T t =list.get(size);
			MyLogger.getInstance().e("list item="+list.get(size));
			try {
				db.save(t);				
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.getInstance().e("e="+e);
				return false;
			}
		}
		return true;
	}

	public static <T> T getFirstData(Class<T> clazz) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);   	
		try {
			return db.findFirst(clazz);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  	 	
	}

	public static <T> boolean updateDB (T t) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);   	
		try {
			db.saveOrUpdate(t);
			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;  	 	
	}
	
	public static <T> boolean saveDB (T t) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);   	
		try {
			db.save(t);
			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;  	 	
	}
	
	public static <T> boolean deleteFirst (Class<T> clazz) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);  		
		if(db != null)
		{
			try {
				db.delete(db.findFirst(clazz));
				return true;
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;  	 	
	}
	
	public static <T> boolean deleteAll (Class<T> clazz) {
		DaoConfig daoConfig=DBUtils.getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);   	
		try {
			db.delete(clazz);
			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;  	 	
	}

	public static String getAdvId(){
		List<DbModel> columnList = new ArrayList<DbModel>();
		String columnValus = new String();
		DaoConfig daoConfig=getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		int len=1;
		try {
			columnList = db.selector(AdvInfo.class).select("id").findAll();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(columnList!=null&&columnList.size()>0){
			for(int i=0;i<columnList.size();i++){
				//				MyLogger.getInstance().e("idList ="+columnList.get(i).getInt("id"));

				if(columnList.size()>len){
					columnValus=columnValus+columnList.get(i).getInt("id")+"#";
				}else{
					columnValus=columnValus+columnList.get(i).getInt("id");
				}
				len++;
			}
		}
		return columnValus;
	}

	public static <T> List<DbModel> getStringColumn(String columnName,Class<T> clazz){
		List<DbModel> columnList = new ArrayList<DbModel>();
		DaoConfig daoConfig=getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		try {
			columnList = db.selector(clazz).select(columnName).findAll();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(columnList!=null&&columnList.size()>0){
			for(int i=0;i<columnList.size();i++){
				MyLogger.getInstance().e("List ="+columnList.get(i).getString(columnName));
			}
		}
		return columnList;
	}

	
	public static <T> List<T> getAllToList(Class<T> clazz){
		List<T> list = new ArrayList<T>();
		DaoConfig daoConfig=getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		try {
			list = db.findAll(clazz);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				MyLogger.getInstance().e("idList ="+list.get(i));
			}
		}
		return list;
	}

	public static <T> List<T> getSpecificColumnToList(Class<T> clazz, String columnName, Object value){
		List<T> list = new ArrayList<T>();
		DaoConfig daoConfig=getDaoConfig(); 
		DbManager db = x.getDb(daoConfig);
		try {
			list = db.selector(clazz).where(columnName, "=", value).findAll();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}