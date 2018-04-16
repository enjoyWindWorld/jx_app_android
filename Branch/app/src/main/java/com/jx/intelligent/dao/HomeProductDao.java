package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.FirstPayRsult;
import com.jx.intelligent.result.GetMainHomeProductsResult;
import com.jx.intelligent.result.NewPlaceOrderResult;
import com.jx.intelligent.result.PlaceOrderResult;
import com.jx.intelligent.result.ProductDetailResult;
import com.jx.intelligent.result.ReNewPayOrderDetailResult;
import com.jx.intelligent.result.UNPayResult;
import com.jx.intelligent.result.WXPayResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2016/11/24 0024.
 * 首页商品
 */

public class HomeProductDao extends BaseDao {

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取首页商品列表
     *
     * @param responseResult
     */
    public void getHomeProdustListTask(final int page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");

        sendAsyncRequest(Constant.HOME_PRODUCT_URL, com.alibaba.fastjson.JSON.toJSONString(map), GetMainHomeProductsResult.class, new HttpResponseCallback<GetMainHomeProductsResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetMainHomeProductsResult getMainHomeProductsResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetMainHomeProductsResult getMainHomeProductsResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getMainHomeProductsResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getMainHomeProductsResult);
                    if (page == 1) {
                        if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map)))) {
                            dbManager.insertUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMainHomeProductsResult));
                        } else {
                            dbManager.updateUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMainHomeProductsResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getMainHomeProductsResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取首页商品详情
     *
     * @param responseResult
     */
    public void getHomeProdustDetailTask(int id, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("id", id + "");

        sendAsyncRequest(Constant.HOME_PRODUCT_DETAIL_URL, com.alibaba.fastjson.JSON.toJSONString(map), ProductDetailResult.class, new HttpResponseCallback<ProductDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, ProductDetailResult getMainHomeProductsResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ProductDetailResult productDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (productDetailResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(productDetailResult);
                    if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_PRODUCT_DETAIL_URL + StringUtil.obj2JsonStr(map)))) {
                        dbManager.insertUrlJsonData(Constant.HOME_PRODUCT_DETAIL_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(productDetailResult));
                    } else {
                        dbManager.updateUrlJsonData(Constant.HOME_PRODUCT_DETAIL_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(productDetailResult));
                    }
                } else {
                    responseResult.resFailure(productDetailResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取续费商品详情
     *
     * @param responseResult
     */
    public void getOrdeAgainDetailTask(String ord_no, String productId, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("ord_no", ord_no);
        map.put("productId", productId);

        sendAsyncRequest(Constant.RENEW_PLACE_ORDER_DETAIL_URL, com.alibaba.fastjson.JSON.toJSONString(map), ReNewPayOrderDetailResult.class, new HttpResponseCallback<ReNewPayOrderDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, ReNewPayOrderDetailResult reNewPayOrderDetailResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ReNewPayOrderDetailResult reNewPayOrderDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (reNewPayOrderDetailResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(reNewPayOrderDetailResult);
                } else {
                    responseResult.resFailure(reNewPayOrderDetailResult.getMsg());
                }
            }
        });
    }

    /**
     * 下单
     *
     * @param userid
     * @param adrid
     * @param proid
     * @param managerNo
     * @param settime
     * @param price
     * @param responseResult
     */
    public void getPlaceOrderTask(String paytype, String userid, String adrid, String proid, String proname, String managerNo, String settime, String price, String url, String color, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("paytype", paytype);
        map.put("userid", userid);
        map.put("adrid", adrid);
        map.put("proid", proid);
        map.put("proname", proname);
        map.put("managerNo", managerNo);
        map.put("settime", settime);
        map.put("price", price);
        map.put("url", url);
        map.put("color", color);

        sendAsyncRequest(Constant.PLACE_ORDER_URL, com.alibaba.fastjson.JSON.toJSONString(map), PlaceOrderResult.class, new HttpResponseCallback<PlaceOrderResult>() {
            @Override
            public void onFailure(int statusCode, String message, PlaceOrderResult placeOrderResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, PlaceOrderResult placeOrderResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (placeOrderResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(placeOrderResult);
                } else {
                    responseResult.resFailure(placeOrderResult.getMsg());
                }
            }
        });
    }

    /**
     * 新版下单网络请求 王云编写   比旧版多了几个参数
     *
     * @param adrid
     * @param managerNo
     * @param settime
     */
    public void getNewPlaceOrderTask(String id, String managerNo, String settime, String adrid, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("adrid", adrid);
        map.put("managerNo", managerNo);
        map.put("settime", settime);


        sendAsyncRequest(Constant.PLACE_ORDER_URL, com.alibaba.fastjson.JSON.toJSONString(map), NewPlaceOrderResult.class, new HttpResponseCallback<NewPlaceOrderResult>() {
                    @Override
                    public void onFailure(int statusCode, String message, NewPlaceOrderResult getnewPlaceOrderResult) {
                        responseResult.resFailure(message);
                    }

                    @Override
                    public void onSuccess(int statusCode, NewPlaceOrderResult getnewPlaceOrderResult) {
                        LogUtil.e("statusCode::" + statusCode);
                        if (getnewPlaceOrderResult.getResult().equals(Constant.retCode_ok)) {
                            responseResult.resSuccess(getnewPlaceOrderResult);
                        } else {
                            responseResult.resFailure(getnewPlaceOrderResult.getMsg());
                        }
                    }

        });
    }


    /**
     * 重新续费下单接口
     * @param ord_no
     * @param paytype
     * @param price
     * @param proname
     * @param responseResult
     */
    public void getReNewPlaceOrderTask(String ord_no, String paytype, String price, String proname, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ord_no", ord_no);
        map.put("paytype", paytype);
        map.put("price", price);
        map.put("proname", proname);

        sendAsyncRequest(Constant.RENEW_PLACE_ORDER_URL, com.alibaba.fastjson.JSON.toJSONString(map), PlaceOrderResult.class, new HttpResponseCallback<PlaceOrderResult>() {
            @Override
            public void onFailure(int statusCode, String message, PlaceOrderResult placeOrderResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, PlaceOrderResult placeOrderResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (placeOrderResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(placeOrderResult);
                } else {
                    responseResult.resFailure(placeOrderResult.getMsg());
                }
            }
        });
    }


    /**
     * 预支付接口(支付宝)
     *
     * @param isAgain        0是第一次支付 非零是续费
     * @param ord_no
     * @param context
     * @param price
     * @param responseResult
     */
    public void alipay(String isAgain, String ord_no, String context, String price, String tag,final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("isAgain", isAgain);
        params.put("ord_no", ord_no);
        params.put("context", context);
        params.put("price", price);
        params.put("tag",tag);

        sendAsyncRequest(Constant.PLACE_ORDER_ALIPAY_URL, com.alibaba.fastjson.JSON.toJSONString(params), FirstPayRsult.class, new HttpResponseCallback<FirstPayRsult>() {
            @Override
            public void onFailure(int statusCode, String message, FirstPayRsult firstPayRsult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, FirstPayRsult firstPayRsult) {
                LogUtil.e("statusCode::" + statusCode);
                if (firstPayRsult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(firstPayRsult);
                } else {
                    responseResult.resFailure(firstPayRsult.getMsg());
                }
            }
        });
    }

    /**
     * 预支付接口(微信)
     *
     * @param isAgain        0是第一次支付 非零是续费
     * @param ord_no
     * @param context
     * @param price
     * @param
     */
    public void wxpay(String isAgain, String ord_no, String context, String price,String tag, final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("isAgain", isAgain);
        params.put("ord_no", ord_no);
        params.put("context", context);
        params.put("price", price);
        params.put("tag", tag);

        sendAsyncRequest(Constant.PLACE_ORDER_WXPAY_URL, com.alibaba.fastjson.JSON.toJSONString(params), WXPayResult.class, new HttpResponseCallback<WXPayResult>() {
            @Override
            public void onFailure(int statusCode, String message, WXPayResult wxPayResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, WXPayResult wxPayResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (wxPayResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(wxPayResult);
                } else {
                    responseResult.resFailure(wxPayResult.getMsg());
                }
            }
        });
    }

    /**
     * 王云修改过的
     * 预支付接口(银联)
     *
     * @param isAgain        0是第一次支付 非零是续费
     * @param ord_no
     * @param context
     * @param price
     * @param responseResult
     */
    public void unpay(String isAgain, String ord_no, String context, String price,String tag, final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("isAgain", isAgain);
        params.put("ord_no", ord_no);
        params.put("context", context);
        params.put("price", price);
        params.put("tag", tag);
        /**
         *这里URL需要改成银联接口：
         */
        sendAsyncRequest(Constant.PLACE_ORDER_UNPAY_URL, com.alibaba.fastjson.JSON.toJSONString(params), UNPayResult.class, new HttpResponseCallback<UNPayResult>() {

            @Override
            public void onFailure(int statusCode, String message, UNPayResult unPayResult) {

            }

            @Override
            public void onSuccess(int statusCode, UNPayResult unPayResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (unPayResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(unPayResult);


                } else {
                    responseResult.resFailure(unPayResult.getMsg());
                }
            }


        });
    }

}
