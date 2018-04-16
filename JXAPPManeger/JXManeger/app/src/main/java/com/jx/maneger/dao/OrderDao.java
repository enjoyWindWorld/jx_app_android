package com.jx.maneger.dao;

import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetOrderListResult;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.results.OrderDetailResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/11.
 */

public class OrderDao extends BaseDao{

    DBManager dbManager;
    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取我的订单
     */
    public void getMyOrderListTask(String ord_managerno, final String page, String state, String safetyMark, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("ord_managerno", ord_managerno);
        map.put("page", page);
        map.put("state",state);
        map.put("safetyMark", safetyMark);

        sendAsyncRequest(Constant.USER_GET_MY_ORD_WP, com.alibaba.fastjson.JSON.toJSONString(map), GetOrderListResult.class, new HttpResponseCallback<GetOrderListResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetOrderListResult getOrderListResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, GetOrderListResult getOrderListResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getOrderListResult.getResult() == Constant.retCode_ok) {
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
                    responseResult.resFailure(getOrderListResult.getResult(), getOrderListResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取下级的订单
     * @param ord_managerno
     * @param page
     * @param state
     * @param safetyMark
     * @param responseResult
     */
    public void getSubordinateOrderListTask(String ord_managerno, final String page, String state, String safetyMark, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("username", ord_managerno);
        map.put("page", page);
        map.put("state",state);
        map.put("safetyMark", safetyMark);

        sendAsyncRequest(Constant.USER_GET_SUB_ORD_WP, com.alibaba.fastjson.JSON.toJSONString(map), GetOrderListResult.class, new HttpResponseCallback<GetOrderListResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetOrderListResult getOrderListResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, GetOrderListResult getOrderListResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getOrderListResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(getOrderListResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_SUB_ORD_WP + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.USER_GET_SUB_ORD_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getOrderListResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.USER_GET_SUB_ORD_WP + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getOrderListResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getOrderListResult.getResult(), getOrderListResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取我的订单详情
     *
     * @param responseResult
     */
    public void getMyOrderDetailTask(String ord_no, String safetyMark, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("ord_no", ord_no);
        map.put("safetyMark", safetyMark);

        sendAsyncRequest(Constant.USER_GET_ORD_DET_WP, com.alibaba.fastjson.JSON.toJSONString(map), OrderDetailResult.class, new HttpResponseCallback<OrderDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, OrderDetailResult orderDetailResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, OrderDetailResult orderDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (orderDetailResult.getResult() == Constant.retCode_ok) {
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
                    responseResult.resFailure(orderDetailResult.getResult(), orderDetailResult.getMsg());
                }
            }
        });
    }
}
