package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.result.GetOrderListResult;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetAddrResult;
import com.jx.intelligent.result.GetMyWPInfoResult;
import com.jx.intelligent.result.GetMyWPListResult;
import com.jx.intelligent.result.LoginResult;
import com.jx.intelligent.result.MessageNoReadResult;
import com.jx.intelligent.result.MessageResult;
import com.jx.intelligent.result.NormalResult;
import com.jx.intelligent.result.OrderDetailResult;
import com.jx.intelligent.result.ServicePriceResult;
import com.jx.intelligent.result.ShareContentResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 用户信息获取和修改
 */

public class UserCenter extends BaseDao {

    DBManager dbManager;
    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取用户信息
     *
     * @param responseResult
     */
    public void getUserInfoTask(String userid, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);

        sendAsyncRequest(Constant.USER_GET_MY_INFO_WP, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_MY_INFO_WP + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_MY_INFO_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(loginResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_MY_INFO_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(loginResult));
                    }
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取我的净水器
     * @param userid
     * @param page
     * @param responseResult
     */
    public void getMyWaterPurifierListTask(String userid, String type, final String page, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("type", type);
        map.put("page", page);


        sendAsyncRequest(Constant.USER_GET_WP, com.alibaba.fastjson.JSON.toJSONString(map), GetMyWPListResult.class, new HttpResponseCallback<GetMyWPListResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetMyWPListResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetMyWPListResult getMyWPResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getMyWPResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getMyWPResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_WP + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.USER_GET_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMyWPResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.USER_GET_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMyWPResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getMyWPResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取净水器的支付方式
     * @param pro_no
     * @param responseResult
     */
    public void getMyWaterPurifierPayTypeTask(String pro_no, final ResponseResult responseResult) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pro_no", pro_no);

        sendAsyncRequest(Constant.USER_GET_WP_PAYTYPE, com.alibaba.fastjson.JSON.toJSONString(params), GetMyWPListResult.class, new HttpResponseCallback<GetMyWPListResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetMyWPListResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetMyWPListResult getMyWPResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getMyWPResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getMyWPResult);
                } else {
                    responseResult.resFailure(getMyWPResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取净水器的服务费用详情
     * @param pro_no
     * @param responseResult
     */
    public void getMyWaterPurifierServiceDetailTask(String pro_no, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("pro_no", pro_no);

        sendAsyncRequest(Constant.USER_GET_WP_SERVICE_DETAIL_INFO, com.alibaba.fastjson.JSON.toJSONString(map), ServicePriceResult.class, new HttpResponseCallback<ServicePriceResult>() {
            @Override
            public void onFailure(int statusCode, String message, ServicePriceResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ServicePriceResult servicePriceResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (servicePriceResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(servicePriceResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_WP_SERVICE_DETAIL_INFO + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_WP_SERVICE_DETAIL_INFO + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(servicePriceResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_WP_SERVICE_DETAIL_INFO + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(servicePriceResult));
                    }
                } else {
                    responseResult.resFailure(servicePriceResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取滤芯状态
     *
     * @param responseResult
     */
    public void getFilterElementStatTask(String mobile, String pro_id, final ResponseResult responseResult) {


        final Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", mobile);
        map.put("pro_id", pro_id);


        sendAsyncRequest(Constant.USER_GET_FE_STAT_WP, com.alibaba.fastjson.JSON.toJSONString(map), GetMyWPInfoResult.class, new HttpResponseCallback<GetMyWPInfoResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetMyWPInfoResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetMyWPInfoResult getMyWPResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getMyWPResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getMyWPResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_FE_STAT_WP + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_FE_STAT_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMyWPResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_FE_STAT_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMyWPResult));
                    }
                } else {
                    responseResult.resFailure(getMyWPResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取我的订单
     *
     * @param responseResult
     */
    public void getMyOrderListTask(String uid, final String page,String state, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("uid", uid);
        map.put("page", page);
        map.put("state",state);

        sendAsyncRequest(Constant.USER_GET_MY_ORD_WP, com.alibaba.fastjson.JSON.toJSONString(map), GetOrderListResult.class, new HttpResponseCallback<GetOrderListResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetOrderListResult getOrderListResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetOrderListResult getOrderListResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getOrderListResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getOrderListResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_MY_ORD_WP + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.USER_GET_MY_ORD_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getOrderListResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.USER_GET_MY_ORD_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getOrderListResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getOrderListResult.getMsg());
                }
            }
        });
    }

    /**
     * 删除我的订单
     *
     * @param responseResult
     */
    public void myOrderDeleteTask(String ord_no, final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("ord_no", ord_no);

        sendAsyncRequest(Constant.USER_DEL_ORD_WP, com.alibaba.fastjson.JSON.toJSONString(params), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取我的订单详情
     *
     * @param responseResult
     */
    public void getMyOrderDetailTask(String ord_no, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("ord_no", ord_no);

        sendAsyncRequest(Constant.USER_GET_ORD_DET_WP, com.alibaba.fastjson.JSON.toJSONString(map), OrderDetailResult.class, new HttpResponseCallback<OrderDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, OrderDetailResult orderDetailResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, OrderDetailResult orderDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (orderDetailResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(orderDetailResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_ORD_DET_WP + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_ORD_DET_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(orderDetailResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_ORD_DET_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(orderDetailResult));
                    }
                } else {
                    responseResult.resFailure(orderDetailResult.getMsg());
                }
            }
        });
    }

    /**
     * 提交反馈
     *
     * @param responseResult
     */
    public void commitFeedbackTask(String phoneNum, String context, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", phoneNum);
        map.put("context", context);

        sendAsyncRequest(Constant.USER_COMMIT_IDER, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }


    /**
     * 修改昵称
     *
     * @param responseResult
     */
    public void updateUserNickNameTask(String userid, String value, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("userid", userid);
        map.put("value", value);

        sendAsyncRequest(Constant.USER_UPDAT_NICKNAME_WP, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 修改性别
     *
     * @param responseResult
     */
    public void updateUserSexTask(String userid, String value, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("userid", userid);
        map.put("value", value);

        sendAsyncRequest(Constant.USER_UPDAT_SEX_WP, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 修改签名
     *
     * @param responseResult
     */
    public void updateUserSignTask(String userid, String value, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("userid", userid);
        map.put("value", value);

        sendAsyncRequest(Constant.USER_UPDAT_SIGN_WP, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取我的家庭地址列表
     *
     * @param responseResult
     */
    public void getHomeAddrListTask(String userid, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);

        sendAsyncRequest(Constant.USER_GET_ADDR_LIST_WP, com.alibaba.fastjson.JSON.toJSONString(map), GetAddrResult.class, new HttpResponseCallback<GetAddrResult>() {
            @Override
            public void onSuccess(int statusCode, GetAddrResult getAddrResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getAddrResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getAddrResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_ADDR_LIST_WP + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_ADDR_LIST_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getAddrResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_ADDR_LIST_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getAddrResult));
                    }
                } else {
                    responseResult.resFailure(getAddrResult.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String message, GetAddrResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }
        });
    }

    /**
     * 添加家庭地址
     *
     * @param responseResult
     */
    public void addHomeAddrTask(Map<String, String> map, final ResponseResult responseResult) {


        sendAsyncRequest(Constant.USER_ADD_ADDR_WP, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取分享的内容
     *
     * @param responseResult
     */
    public void geShareContentTask(final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();

        sendAsyncRequest(Constant.USER_SHARE_CONTENT_URL, com.alibaba.fastjson.JSON.toJSONString(map), ShareContentResult.class, new HttpResponseCallback<ShareContentResult>() {
            @Override
            public void onFailure(int statusCode, String message, ShareContentResult shareContentResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ShareContentResult shareContentResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (shareContentResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(shareContentResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_SHARE_CONTENT_URL)))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_SHARE_CONTENT_URL, StringUtil.obj2JsonStr(shareContentResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_SHARE_CONTENT_URL, StringUtil.obj2JsonStr(shareContentResult));
                    }
                } else {
                    responseResult.resFailure(shareContentResult.getMsg());
                }
            }
        });
    }

    /**
     * 提交新的绑定号码
     *
     * @param responseResult
     */
    public void commitBDNewPhoneTask(String userid, String newNum, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("newNum", newNum);

        sendAsyncRequest(Constant.USER_RESET_PHONE_NUM_URL, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 删除地址
     *
     * @param responseResult
     */
    public void commitDelAddrTask(String id, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        sendAsyncRequest(Constant.USER_DEL_ADDR_IDER, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 修改用户头像
     * @param userid
     * @param url
     * @param responseResult
     */
    public void setUserHeadImg(String userid, String url, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("value", url);
        sendAsyncRequest(Constant.USER_RESET_HEAD_URL, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取未读消息
     * @param userid
     * @param responseResult
     */
    public void getNoReadMsgTask(String userid, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        sendAsyncRequest(Constant.USER_NO_READ_MSG, com.alibaba.fastjson.JSON.toJSONString(map), MessageNoReadResult.class, new HttpResponseCallback<MessageNoReadResult>() {
            @Override
            public void onFailure(int statusCode, String message, MessageNoReadResult messageNoReadResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, MessageNoReadResult messageNoReadResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (messageNoReadResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(messageNoReadResult);
                } else {
                    responseResult.resFailure(messageNoReadResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取消息列表
     * @param userid
     * @param responseResult
     */
    public void getMSGListTask(String userid, final String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("page", page);
        sendAsyncRequest(Constant.USER_GET_MSG_LIST, com.alibaba.fastjson.JSON.toJSONString(map), MessageResult.class, new HttpResponseCallback<MessageResult>() {
            @Override
            public void onFailure(int statusCode, String message, MessageResult messageResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, MessageResult messageResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (messageResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(messageResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(messageResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(messageResult));
                        }
                    }
                } else {
                    responseResult.resFailure(messageResult.getMsg());
                }
            }
        });
    }

    /**
     * 阅读
     * @param id
     * @param responseResult
     */
    public void readedMsgTask(String id, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        sendAsyncRequest(Constant.USER_READ_MSG, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 删除消息
     * @param id
     * @param responseResult
     */
    public void deleteMsgTask(String id, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        sendAsyncRequest(Constant.USER_DELETE_MSG, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }



    /**
     * 分享绑定
     * @param targetNum
     * @param responseResult
     */
    public void shareBindTask(String userid, String targetNum, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("targetNum", targetNum);

        sendAsyncRequest(Constant.USER_SHARE_BIND, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

}
