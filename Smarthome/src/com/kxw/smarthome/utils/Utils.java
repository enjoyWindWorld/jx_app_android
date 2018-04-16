package com.kxw.smarthome.utils;

public class Utils{
	
	public static boolean connected=false;
	public static String city = null;
	public static String province = null;
	public static String district = null;
	public static int temperature=25;
	public static int payment_type = -1;
	public static int value = 0;
	public static String phoneNum = null;
	public static String pro_no = null;
	public static int tds=0;
	public static int surplus_value=0;
	public static int sum_value=0;
	public static int this_value=0;
	public static boolean inuse=false;
	public static boolean isUsing=false;  //串口为半双通道的，所以同时只能允许一种操作，所以必须使用全局变量控制

}