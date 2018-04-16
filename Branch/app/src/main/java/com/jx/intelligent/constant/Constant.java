package com.jx.intelligent.constant;


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

//    public static final String IP = "http://192.168.1.46:8080/";
    public static final String IP = "http://www.szjxzn.tech:8080/";


    private static final String BASE_HEAD = IP + "jx_smart/smvc/";
    private static final String USER_URL = BASE_HEAD + "user/test/";
    private static final String CS_URL = IP + "jx_smart/after/users/";
    private static final String LAUNDCH_URL = BASE_HEAD + "launch/test/";
    public static final String USER_REGISTER_URL = USER_URL + "register.v";
    public static final String USER_GET_CODE = USER_URL + "registerCode.v";
    public static final String USER_CHECK_CODE = USER_URL + "checkCode.v";
    public static final String USER_LOGIN_URL = USER_URL + "login.v";
    public static final String USER_RESET_PWD_URL = USER_URL + "modifyPwd.v";
    public static final String USER_RESET_HEAD_URL = USER_URL + "modifyHead.v";
    public static final String USER_RESET_PHONE_NUM_URL = USER_URL + "modifyPhoneNum.v";
    public static final String USER_SHARE_CONTENT_URL = USER_URL + "shareContent.v";
    public static final String USER_UPDATE_APP_URL = LAUNDCH_URL + "visit.v";
    public static final String USER_GET_AD_URL = LAUNDCH_URL + "getAdver.v";
    public static final String IMAGE_UPLOAD_URL = BASE_HEAD + "file/fileupload";


//    public  static  final String DOMAIN_URL ="http://www.szjxzn.tech:8080/old_jx/";
    /**
     * 修改购物车数量
     */

    public static final String  UPDAPTER_SHOP_CART_URL=BASE_HEAD+"shoppingcart/updatecat.v";

    /**
     * 购物车商品数量 购物车图标上面那个圆圈上的数字
     */
    public static final String  CART_AMOUNT_URL=BASE_HEAD+"shoppingcart/selectnum.v";

    /**
     * 首页新闻 滚动textview的数据
     */
    public static final String  HOME_TEXT_URL=BASE_HEAD+"news/information.v";
    /**
     * 我的推广接口
     */
    public static final String  MY_GENERALIZE=BASE_HEAD+"release/AllPromoter.v";
    /**
     * 视频娱乐列表
     */
    public static final String  VIDEO_URL=BASE_HEAD+"webspider/recreation.v";


    /**
     * 获取购物车列表
     */
    public static final String  SHOPPING_CART=BASE_HEAD+"shoppingcart/showcat.v";


    /**
     * 新版支付信息页面  listView数据接口
     */
    public static final String  PAMENT_DETAILS_LIST=BASE_HEAD+"setup/attribute.v";


    /**
     * 删除购物车
     */
    public static final String DELETE_SHOPPING_CART=BASE_HEAD+"shoppingcart/delcat.v";

    /**
     * 新版加入购物车
     */
    public static final String ADDTO_SHOPPING_CART=BASE_HEAD+"shoppingcart/addshoppingcart.v";

    /**
     * 今日饮水数据和水质报告的URL地址
     */
    public static final String  HOME_WATER_REPORT=BASE_HEAD+"setup/waterQuantity.v";

    /**
     * 首页banner图片的URL地址
     */
    public static final String HOME_BANNER_URL=BASE_HEAD+"setup/homepage.v";
    /**
     * 获取 城市 用户数量地址
     * 社区服务 定位那个地方 选择合肥市 出现 用户数量的URL
     */
    public static final String USER_NUMBER= USER_URL+"citybutton.v";

    /**
     * 获取首页商品列表
     * 新版本的产品选购URL 也是用的这个
     */
    public static final String HOME_PRODUCT_URL = BASE_HEAD + "setup/mainImg.v";

    /**
     * 获取首页商品详情
     */
    public static final String HOME_PRODUCT_DETAIL_URL = BASE_HEAD + "setup/productdetail.v";

    /**
     * 获取生成订单
     */
    public static final String PLACE_ORDER_URL = BASE_HEAD + "order/addorder.v";


    /**
     * 获取续费生成订单详情
     */
    public static final String RENEW_PLACE_ORDER_DETAIL_URL = BASE_HEAD + "order/ordeAgainDetail.v";

    /**
     * 获取续费生成订单
     */
    public static final String RENEW_PLACE_ORDER_URL = BASE_HEAD + "order/renewalsOrder.v";

    /**
     * 支付宝预支付
     */
    public static final String PLACE_ORDER_ALIPAY_URL = BASE_HEAD + "pay/alipay.v";

    /**
     * 支付宝预支付（服务发布的）
     */
    public static final String PLACE_SERVICE_ALIPAY_URL = BASE_HEAD + "releasepay/alipay.v";

    /**
     * 微信预支付
     */
    public static final String PLACE_ORDER_WXPAY_URL = BASE_HEAD + "pay/wxpaysign.v";

    /**
     * 微信预支付（服务发布的）
     */
    public static final String PLACE_SERVICE_WXPAY_URL = BASE_HEAD + "releasepay/wxpay.v";

    /**
     * 银联预支付
     */
    public static final String PLACE_ORDER_UNPAY_URL = BASE_HEAD+"/pay/unionpay.v";

    /**
     * 银联预支付（服务发布的）
     */
    public static final String PLACE_SERVICE_UNPAY_URL = BASE_HEAD+"/releasepay/unionpay.v";

    /**
     * 获取服务类型
     */
    public static final String HOME_SERVICE_TYPE_URL = BASE_HEAD + "wapPush/wappushtotal.v";

    /**
     * 发布服务
     */
    public static final String HOME_SERVICE_RELEASE_URL = BASE_HEAD + "release/addreleaseorder.v";

    /**
     * 获取服务列表
     */
    public static final String HOME_SERVICE_LIST_URL = BASE_HEAD + "user/publishList.v";

    /**
     *  获取商家服务列表
     */
    public static final String USER_SERVICE_LIST_URL = BASE_HEAD + "userwappush/mydoulton.v";

    /**
     * 获取服务详情
     */
    public static final String HOME_SERVICE_DETAIL_URL = BASE_HEAD + "userwappush/doultondetails.v";

    /**
     * 服务咨询
     */
    public static final String HOME_SERVICE_INQUIRIES_URL = BASE_HEAD + "userwappush/inquiries.v";

    /**
     * 获取我的净水器列表
     */
    public static final String USER_GET_WP = BASE_HEAD + "product/waterCleaner.v";

    /**
     * 获取我的净水器支付方式
     */
    public static final String USER_GET_WP_PAYTYPE = BASE_HEAD + "product/payType.v";

    /**
     * 获取我的净水器的服务详情
     */
    public static final String USER_GET_WP_SERVICE_DETAIL_INFO = BASE_HEAD + "product/myproductServiceDetail.v";

    /**
     * 获取消息列表
     */
    public static final String USER_GET_MSG_LIST = BASE_HEAD + "product/queryAllMess.v";

    /**
     * 获取消息列表
     */
    public static final String USER_READ_MSG = BASE_HEAD + "product/alterMessStatus.v";

    /**
     * 删除消息
     */
    public static final String USER_DELETE_MSG = BASE_HEAD + "/message/deleteMessage.v";

    /**
     * 获取未读消息条数
     */
    public static final String USER_NO_READ_MSG = BASE_HEAD + "/message/queryMessages.v";

    /**
     * 获取分享设备
     */
    public static final String USER_SHARE_BIND = USER_URL + "shareDevice.v";

    /**
     * 获取滤芯状态
     */
    public static final String USER_GET_FE_STAT_WP = BASE_HEAD + "userwappush/doulton.v";

    /**
     * 获取我的订单列表
     */
    public static final String USER_GET_MY_ORD_WP = BASE_HEAD +"order/myOrders.v";

    /**
     * 获取我的订单详情
     */
    public static final String USER_GET_ORD_DET_WP = BASE_HEAD + "order/orderuDetail.v";

    /**
     * 获取我的订单详情
     */
    public static final String USER_DEL_ORD_WP = BASE_HEAD + "order/deleteorder.v";

    /**
     * 获取我的信息
     */
    public static final String USER_GET_MY_INFO_WP = BASE_HEAD + "user/test/refreshInfo.v";
    /**
     * 获取地址列表
     */
    public static final String USER_GET_ADDR_LIST_WP = BASE_HEAD + "user/test/getAddress.v";
    /**
     * 增加家庭地址
     */
    public static final String USER_ADD_ADDR_WP = BASE_HEAD + "user/test/modifyAddress.v";
    /**
     * 修改昵称
     */
    public static final String USER_UPDAT_NICKNAME_WP = BASE_HEAD + "user/test/modifyNickName.v";
    /**
     * 修改昵称
     */
    public static final String USER_UPDAT_SEX_WP = BASE_HEAD + "user/test/modifySex.v";
    /**
     * 修改签名
     */
    public static final String USER_UPDAT_SIGN_WP = BASE_HEAD + "user/test/modifySign.v";
    /**
     * 用户提交反馈
     */
    public static final String USER_COMMIT_IDER = BASE_HEAD + "setup/addOption.v";
    /**
     * 用户提交反馈
     */
    public static final String USER_DEL_ADDR_IDER = BASE_HEAD + "user/test/deleteAddress.v";

    /**
     * 净水器介绍及服务说明
     */
    public static final String PRODUCT_INTRODUCE_AND_SERVICE_UTL = IP + "jx_smart/promise.jsp";

    /**
     * 获取报修的机器列表
     */
    public static final String SC_REPAIR_EQUIPMENT_LIST = CS_URL + "filterreplacement.v";

    /**
     * 获取报修的机器列表
     */
    public static final String SC_REPAIR_EQUIPMENT_FILTER_LIFT = CS_URL + "cartridgereplacement.v";

    /**
     * 发布售后任务
     */
    public static final String SC_ADD_TASK = CS_URL + "addfilterafter.v";

    /**
     * 获取售后任务列表
     */
    public static final String SC_TASK_LIST = CS_URL + "afterofappraise.v";

    /**
     * 获取故障现象
     */
    public static final String SC_FAULT_TYPE = CS_URL + "fault.v";

    /**
     * 获取售后任务详情
     */
    public static final String SC_TASK_DETAIL = CS_URL + "afterthedetails.v";

    /**
     * 售后评价
     */
    public static final String SC_EVALUATE = CS_URL + "appraise.v";

    /**
     * 对话框按钮操作类型
     */
    public static final class DIALOG_CLICK_TYPE {
        public static final int CANCLE = 1;//取消
        public static final int ENSURE = 2;//确定
    }

    /**
     * 验证码获取间隔
     */
    public static int TOTALTIME = 120;

    public static final String KEY_PICKED_CITY = "picked_city";

    public static final String KEY_PICKED_AREA = "picked_area";

    /**
     * 获取本地照片
     */
    public static int TAKE_LOCAL_PICTURE = 1;

    /**
     * 获取相机
     */
    public static int TAKE_PHOTO = 2;

    /**
     * 获取4.4以上图片
     */
    public static int TAKE_LOCAL_PICTURE_CROP = 3;

    /**
     * 剪切
     */
    public static int CLIP_CODE = 4;

    /**
     * 浏览照片
     */
    public static int SHOW_PHOTO = 5;

    /**
     * 打电话
     */
    public static int CALL = 6;

    /**
     * 发短信
     */
    public static int MSM = 7;

    /**
     * 手机号码前缀
     */
    public static String TELEPHONE_NUMBER_PREFIX = "+86";

    /**
     * 成功的结果码
     */
    public static String retCode_ok = "0";

    /**
     * 请求
     */
    public static int REQUEST_CODE = 0;

    /**
     * 注册成功
     */
    public static int REGISTER_OK = 100;

    /**
     * 登录成功
     */
    public static int LOGIN_OK = 101;

    /**
     * 先登录标识
     */
    public static int LOGIN_FLAG = 102;

    /**
     * 获取家庭住址标识
     */
    public static int GET_HOME_ADDR_FLAG = 103;

    /**
     * 成功返回地址
     */
    public static int GET_HOME_ADDR_OK = 104;

    /**
     * 成功返回服务内容
     */
    public static int GET_SERVICE_CONTENT_OK = 105;

    //=========支付结果的==========
    /**
     * 取消订单
     */
    public final static int QXDD = 106;

    /**
     * 交易详情
     */
    public final static int JYXQ = 107;

    /**
     * 等待支付
     */
    public final static int DDZF = 108;

    /**
     * 支付成功
     */
    public static int PAY_OK = 110;
    //=========支付结果的==========

    /**
     *  购买须知TextView的跳转Itent返回常量
     */
    public final static int BUY_DETAILS = 109;

    /**
     *  购物车图标跳转购物车的Itent返回常量
     */
    public final static int CART_NUMBER = 110;

    /**
     *   购物车结算 跳转 返回常量
     */
    public final static int CART_JIESUAN = 113;

    /**
    *   填写支付信息 确定 返回常量
    */
    public final static int CART_ZHIFU = 114;

    /**
     *  购物车返回按钮 返回的常量
     */
    public final static int SHOPPINGCART_NUMBER = 115;
    /**
     *  全屏播放 返回的常量
     */
    public final static int VIDEO_POSITION = 116;


    /**
     * 退出app
     */
    public static int EXIT_APP = 111;

    /**
     * 看完了引导页
     */
    public static int SHOW_GUIDE = 112;

    /**
     * 删除订单
     */
    public static int DELETE_ORDER = 113;

    /**
     * 地址删除完了
     */
    public static int ALL_HOME_ADDR_DELETE = 115;

    /**
     * 修改个性签名
     */
    public static int UPDATE_SIGN = 117;

    /**
     * 修改昵称
     */
    public static int UPDATE_NICK_NAME = 118;

    /**
     * 添加或修改收货地址
     */
    public static int ADD_UPDATE_HOME_ADDR = 119;

    /**
     * 选择售后的设备
     */
    public static int SELECT_EQUIPMENT = 120;

    /**
     * 选择对应的滤芯
     */
    public static int SELECT_FILTER = 121;

    /**
     * 添加故障描述
     */
    public static int ADD_FAULT_DESCRIPTION = 122;

    /**
     * 选择故障说明
     */
    public static int SELECT_FAULT_TYPE = 123;

    /**
     * 评价成功
     */
    public static int EVALUATE_OK = 124;

    /**
     *
     */
    public static final String FIRST_SHOW = "first_show";

}
