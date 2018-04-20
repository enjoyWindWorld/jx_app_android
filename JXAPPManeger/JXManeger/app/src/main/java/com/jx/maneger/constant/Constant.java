package com.jx.maneger.constant;


/**
 * 常量类
 */

public class Constant {
    /**
     * 根目录
     */
    public static String ROOT_DIR = null;

    public static final int DEBUGLEVEL = 7;

    /**
     * 升级url
     */
    public static final String SPF_NAME = "";

//    private static final String IP = "http://192.168.1.45:8080/";
    private static final String IP = "http://www.szjxzn.tech:7686/";

    private static final String BASE_HEAD = IP + "jx_partner/smvc/partner/";
    private static final String LAUNDCH_URL = BASE_HEAD + "launch/test/";
    public static final String USER_REGISTER_URL = BASE_HEAD + "register.v";
    public static final String USER_GET_CODE = BASE_HEAD + "registerCode.v";
    public static final String USER_CHECK_CODE = BASE_HEAD + "checkCode.v";
    public static final String USER_LOGIN_URL = BASE_HEAD + "toLogin.v";
    public static final String USER_INFO_URL = BASE_HEAD + "logininformation.v";
    public static final String USER_RESET_PWD_URL = BASE_HEAD + "modifyPwdBack.v";
    public static final String USER_UPDATE_APP_URL = LAUNDCH_URL + "visit.v";
    public static final String USER_MY_PARTNER = BASE_HEAD + "mystaff.v";
    public static final String USER_ALIPAY_INFO = BASE_HEAD + "checkalipayinformation.v";
    public static final String USER_BIND_ALIPAY = BASE_HEAD + "bindingalipay.v";
    public static final String USER_UNBIND_ALIPAY = BASE_HEAD + "unbundlingaccount.v";
    public static final String USER_GET_MY_ORD_WP = BASE_HEAD + "myOrders.v";
    public static final String USER_GET_SUB_ORD_WP = BASE_HEAD + "mystaffdetails.v";
    public static final String USER_GET_ORD_DET_WP = BASE_HEAD + "orderuDetail.v";
    public static final String USER_GET_MSG_LIST = BASE_HEAD + "mymessage.v";
    public static final String USER_NO_READ_MSG = BASE_HEAD + "numberofmessage.v";
    public static final String USER_READ_MSG = BASE_HEAD + "updatemessage.v";
    public static final String USER_DELETE_MSG = BASE_HEAD + "delmessage.v";
    public static final String USER_WITHDRAWAL_AMOUNT = BASE_HEAD + "withdrawalamount.v";
    public static final String USER_SALE_AMOUNT = BASE_HEAD + "salesamount.v";
    public static final String USER_SUB_SALE_AMOUNT = BASE_HEAD + "lowerdetails.v";
    public static final String USER_WITHRAWAL_AUDIT = BASE_HEAD + "withdrawalaudit.v";
    public static final String USER_WITHDRAWAL_RECORD = BASE_HEAD + "withdrawalrecord.v";
    public static final String USER_DO_WITHDRAWAL = BASE_HEAD + "withdrawalorder.v";
    public static final String USER_GET_CASH_WITHRAWAL_RRATIO = BASE_HEAD + "permissions.v";
    public static final String USER_UPDATE_CASH_WITHRAWAL_RRATIO = BASE_HEAD + "updatepermission.v";
    public static final String DOMAIN_URL ="http://www.szjxzn.tech:8080/old_jx/";

    public static final String CS_FILTER_LIST = BASE_HEAD + "filterofsetmeal.v";
    public static final String CS_FILTER_SEARCH = BASE_HEAD + "search.v";
    public static final String CS_REPAIR_RECORD = BASE_HEAD + "maintenancerecord.v";
    public static final String CS_TASK_LIST = BASE_HEAD + "afterthetask.v";
    public static final String CS_FILTER_NOTICE_LIST = BASE_HEAD + "filterwarning.v";
    public static final String CS_TASK_DETAIL = BASE_HEAD + "afterthetaskparticulars.v";
    /**
     * 首页新闻 滚动textview的数据
     */
    public static final String  HOME_TEXT_URL="http://www.szjxzn.tech:8080/jx_smart/smvc/news/information.v";
    /**
     * 验证码获取间隔
     */
    public static int TOTALTIME = 1;

    /**
     * 手机号码前缀
     */
    public static String TELEPHONE_NUMBER_PREFIX = "+86";

    /**
     * 成功的结果码
     */
    public static int retCode_ok = 0;

    /**
     * 请求
     */
    public static int REQUEST_CODE = 2;

    /**
     * 注册成功
     */
    public static int REGISTER_OK = 3;

    /**
     * 登录成功
     */
    public static int LOGIN_OK = 4;

    /**
     * 先登录标识
     */
    public static int LOGIN_FLAG = 5;

    /**
     * 退出app
     */
    public static int EXIT_APP = 6;

    /**
     * 看完了引导页
     */
    public static int SHOW_GUIDE = 7;

    /**
     * 删除订单
     */
    public static int DELETE_ORDER = 8;

    /**
     * 绑定支付宝
     */
    public static int BIND_ALIPAY = 9;

    /**
     * 解绑支付宝
     */
    public static int UNBIND_ALIPAY = 10;

    /**
     * 请求消息界面
     */
    public static int REQUEST_MESSAGE = 11;

    /**
     * 退出登录
     */
    public static int LOGIN_OUT = 12;

    /**
     * 提现成功
     */
    public static int WITHDRAWAL_OK = 13;


    /**
     * 请求我的收入界面
     */
    public static int REQUEST_MY_INCOME = 14;

    /**
     * 修改密码
     */
    public static int UPDATE_PWD_OK = 15;

    /**
     * 请求我的订单
     */
    public static int REQUEST_MY_ORDER = 16;

    /**
     * 请求我的下属
     */
    public static int REQUEST_MY_SUBORDINATE = 17;

    /**
     * 请求设置界面
     */
    public static int REQUEST_MY_SETTING = 18;
}
