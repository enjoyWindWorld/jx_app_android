package com.kxw.smarthome.utils;

import android.os.Environment;

public class ConfigUtils {

	// url

//	public static String base_url = "http://data.jx-inteligent.tech:15660/jx_smart/smvc";
	public static String base_url ="http://www.szjxzn.tech:8080/jx_smart/smvc";
//	public static String base_url ="http://39.104.126.204/smvc";

	// 高清视频，空间大
	static String downloadPath = "http://www.szjxzn.tech:8080/pic/jingxi1010.mp4";
	// 非高清，空间小
//	public static String[] videoPath = new String[] {
//			"http://www.szjxzn.tech:8080/pic/jingxi1228.mp4",
//			"http://www.szjxzn.tech:8080/pic/jingxi011801.mp4",
//			"http://www.szjxzn.tech:8080/pic/jingxi011802.mp4" };

	public static String file_path = Environment.getExternalStorageDirectory()
			.getPath() + "/.kxw" + "/video.mp4";

	public static String upgrade_url = "http://www.szjxzn.tech:8080/jx_smart/smvc/launch/test/visit.v";//写死线上升级路径
	public static String get_adv_url = base_url + "/table/tablevideoorpic.v";

	public static String get_servicesList_url = base_url + "/wapPush/wappushtotal.v";
	// http://192.168.1.37:13310/jx_smart/smvc/wapPush/wappushtotal.v
	static String get_paymentType_url = "";
	public static String update_filterInfo_url = base_url + "/user/test/lxstatusupload.v";
	public static String get_deviceCode_url = base_url + "/user/test/tabletbinding.v";
	public static String get_filterInfo_url = base_url + "/user/test/filterchange.v";
	public static String get_elementLife_url = base_url + "/user/test/tabletquerylife.v";
	public static String binding_back_url = base_url + "/user/test/tabletbindingbackas.v";
	public static String get_renowInfo_url = base_url + "/user/test/tabletquerystatus.v";
	public static String get_renowInfo_backcall_url = base_url + "/user/test/tabletquerystatusback.v";
	public static String get_storeList_url = base_url + "/user/publishList.v";
	public static String get_storeInfo_url = base_url + "/userwappush/doultondetails.v";
	public static String get_unbind_device_url = base_url + "/user/test/untabletbinding.v";//解绑
	public static String get_unbind_device_backcall_url = base_url + "/user/test/untabletbindingback.v";//解绑回调
	public static String get_old_filter_life_url = base_url + "/user/test/tabletQueryOldLife.v";//获取旧的滤芯寿命
	public static String get_old_deviceCode_url = base_url + "/user/test/tabletbindings.v";
	public static String get_search_order_info_url = base_url + "/user/test/back.v";//订单信息找回
	public static String upload_option_url = base_url + "/table/tablelog.v";//上传平板的操作日志
	public static String get_verification_data = base_url + "/user/test/getday.v";//获取验证数据
	
	//广播
	public static String on_touch_action = "ON_TOUCH_ACTION";  //触摸监听
	public static String update_ad_alarm = "UPDATE_AD_ALARM"; // 平板主页广告更新广播
	public static String update_filter_info_alarm = "UPDATE_FILTER_INFO_ALARM"; // 滤芯上传广播
	public static String update_time_alarm = "UPDATE_TIME_ALARM";  //校准时间
	public static String get_renew_alarm = "GET_RENEW_ALARM"; //查询续费订单
	public static String upgrade_version_alarm = "UPGRADE_VERSION_ALARM"; // 版本更新广播
	public static String update_weather_alarm = "UPDATE_WEATHER_ALARM"; //更新天气
	public static String reset_device_alarm = "RESET_DEVICE_ALARM";  //重置设备
	public static String expiration_hints_action = "EXPIRATION_HINTS_ACTION"; //套餐或滤芯到期提示
	public static String sync_filter_life_action = "SYNC_FILTER_LIFT_ACTION"; //同步本地主板和线上滤芯寿命
	public static String verification_data_action = "VERIFICATION_DATA_ACTION"; //验证剩余套餐数量和滤芯寿命
	public static String upload_option_description_action = "OPTION_DESCRIPTION_ACTION"; //上传平板操作日志
	public static String update_value_surplus_action = "UPDATE_VALUE_SURPLUS_ACTION"; //修改剩余值
	public static String verification_value_action = "VERIFICATION_VALUE_ACTION"; //验证本地存贮的值对不对
	public static String update_value_city_action = "UPDATE_VALUE_CITY_ACTION"; //修改城市定位的值
	public static String verification_no_data_action = "VERIFICATION_NO_DATA_ACTION"; //验证数据是否用完了
	
	//配置
	public static int ad_type = -1; // -1 平板广告； 0 手机主页广告； 1  手机社区广告；其中0和1为手机APP端的广告
	public static long ad_time = 1000 * 60 * 50;   //自动切换到广告界面的时间
	public static int get_location_only = 0; // 0表示只定位，不获取天气；1表示定位并更新天气
	public static int get_weather_and_location = 1; // 0表示只定位，不获取天气；1表示定位并更新天气
	
	public static double hint_ratio = 0.05;
}
