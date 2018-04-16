package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.FirstPayRsult;
import com.jx.intelligent.result.GeneraLizeResult;
import com.jx.intelligent.result.GetAdResult;
import com.jx.intelligent.result.GetHomeServiceTypeResult;
import com.jx.intelligent.result.NormalResult;
import com.jx.intelligent.result.ServiceDetailInfoResult;
import com.jx.intelligent.result.ServiceReleaseResult;
import com.jx.intelligent.result.ServicesResult;
import com.jx.intelligent.result.UNPayResult;
import com.jx.intelligent.result.UploadImgResult;
import com.jx.intelligent.result.WXPayResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 社区服务
 */

public class CommunityService extends BaseDao {

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取广告
     *
     * @param responseResult
     */
    public void getAdInfoTask(String type ,final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);

        sendAsyncRequest(Constant.USER_GET_AD_URL, com.alibaba.fastjson.JSON.toJSONString(map), GetAdResult.class, new HttpResponseCallback<GetAdResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetAdResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetAdResult getAdResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getAdResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getAdResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_AD_URL + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_AD_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getAdResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_AD_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getAdResult));
                    }
                } else {
                    responseResult.resFailure(getAdResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取可以查看的分类类型列表
     *
     * @param responseResult
     */
    public void getServiceTypeTask(final ResponseResult responseResult) {

        sendAsyncRequest(HttpMethod.GET, Constant.HOME_SERVICE_TYPE_URL, GetHomeServiceTypeResult.class, new HttpResponseCallback<GetHomeServiceTypeResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetHomeServiceTypeResult getHomeServiceTypeResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetHomeServiceTypeResult getHomeServiceTypeResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getHomeServiceTypeResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getHomeServiceTypeResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_SERVICE_TYPE_URL)))
                    {
                        dbManager.insertUrlJsonData(Constant.HOME_SERVICE_TYPE_URL, StringUtil.obj2JsonStr(getHomeServiceTypeResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.HOME_SERVICE_TYPE_URL, StringUtil.obj2JsonStr(getHomeServiceTypeResult));
                    }
                } else {
                    responseResult.resFailure(getHomeServiceTypeResult.getMsg());
                }
            }
        });
    }

    /**
     * 发布服务
     *
     * @param responseResult
     */
    public void serviceReleaseTask(String userid, String promoterid, String categoryid, String phone, String longitude, String latitude, String  address,
                                   String content, String sellername, String begintime, String endtime, String imgUrl, String userName, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("promoterid", promoterid);
        map.put("categoryid", categoryid);
        map.put("phone", phone);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("address", address);
        map.put("content", content);
        map.put("sellername", sellername);
        map.put("begintime", begintime);
        map.put("endtime", endtime);
        map.put("imgUrl", imgUrl);
        map.put("userName", userName);

        sendAsyncRequest(Constant.HOME_SERVICE_RELEASE_URL, com.alibaba.fastjson.JSON.toJSONString(map), ServiceReleaseResult.class, new HttpResponseCallback<ServiceReleaseResult>() {
            @Override
            public void onFailure(int statusCode, String message, ServiceReleaseResult serviceReleaseResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ServiceReleaseResult serviceReleaseResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (serviceReleaseResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(serviceReleaseResult);
                } else {
                    responseResult.resFailure(serviceReleaseResult.getMsg());
                }
            }
        });
    }


    /**
     * 获取服务列表
     * @param responseResult
     */
    public void servicesTask(String address, String id, final String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("address", address);
        map.put("id", id);
        map.put("page", page);

        sendAsyncRequest(Constant.HOME_SERVICE_LIST_URL, com.alibaba.fastjson.JSON.toJSONString(map), ServicesResult.class, new HttpResponseCallback<ServicesResult>() {
            @Override
            public void onFailure(int statusCode, String message, ServicesResult servicesResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ServicesResult servicesResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (servicesResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(servicesResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.HOME_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(servicesResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.HOME_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(servicesResult));
                        }
                    }
                } else {
                    responseResult.resFailure(servicesResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取商家服务列表
     * @param
     */
    public void customServicesTask(String phoneNum, final String page,String userid ,final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", phoneNum);
        map.put("page", page);
        map.put("userid",userid);

        sendAsyncRequest(Constant.USER_SERVICE_LIST_URL, com.alibaba.fastjson.JSON.toJSONString(map), ServicesResult.class, new HttpResponseCallback<ServicesResult>() {

            @Override
            public void onFailure(int statusCode, String message, ServicesResult servicesResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ServicesResult servicesResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (servicesResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(servicesResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.USER_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(servicesResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.USER_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(servicesResult));
                        }
                    }
                } else {
                    responseResult.resFailure(servicesResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取商家推广列表
     * @param
     */
    public void GeneraLizeTask(String userid , final String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid",userid);
        map.put("page", page);




        sendAsyncRequest(Constant.MY_GENERALIZE, com.alibaba.fastjson.JSON.toJSONString(map), GeneraLizeResult.class, new HttpResponseCallback<GeneraLizeResult>() {

            @Override
            public void onFailure(int statusCode, String message, GeneraLizeResult getGeneraLizeResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GeneraLizeResult getGeneraLizeResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getGeneraLizeResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getGeneraLizeResult);
//                    if("1".equals(page))
//                    {
//                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.MY_GENERALIZE + StringUtil.obj2JsonStr(map))))
//                        {
//                            dbManager.insertUrlJsonData(Constant.MY_GENERALIZE + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getGeneraLizeResult));
//                        }
//                        else
//                        {
//                            dbManager.updateUrlJsonData(Constant.MY_GENERALIZE + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getGeneraLizeResult));
//                        }
//                    }
                } else {
                    responseResult.resFailure(getGeneraLizeResult.getMsg());
                }
            }
        });
    }


    /**
     * 获取服务详情
     * @param responseResult
     */
    public void getServiceDetailTask(String pubId, String userlong, String userlat,  final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("pubId", pubId);
        map.put("userlong", userlong);
        map.put("userlat", userlat);

        sendAsyncRequest(Constant.HOME_SERVICE_DETAIL_URL, com.alibaba.fastjson.JSON.toJSONString(map), ServiceDetailInfoResult.class, new HttpResponseCallback<ServiceDetailInfoResult>() {
            @Override
            public void onFailure(int statusCode, String message, ServiceDetailInfoResult serviceDetailInfoResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ServiceDetailInfoResult serviceDetailInfoResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (serviceDetailInfoResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(serviceDetailInfoResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_SERVICE_DETAIL_URL + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.HOME_SERVICE_DETAIL_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(serviceDetailInfoResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.HOME_SERVICE_DETAIL_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(serviceDetailInfoResult));
                    }
                } else {
                    responseResult.resFailure(serviceDetailInfoResult.getMsg());
                }
            }
        });
    }

    public void upload(String path, final ResponseResult responseResult) {
        sendAsyncRequest(Constant.IMAGE_UPLOAD_URL, "", new File(path), UploadImgResult.class, new HttpResponseCallback<UploadImgResult>() {
            @Override
            public void onFailure(int statusCode, String message, UploadImgResult uploadImgResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, UploadImgResult uploadImgResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (uploadImgResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(uploadImgResult);
                } else {
                    responseResult.resFailure(uploadImgResult.getMsg());
                }
            }
        });
    }

    /**
     * 服务咨询接口
     * @param pubid
     * @param responseResult
     */
    public void serviceInquiries(String pubid, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("pubid", pubid);

        sendAsyncRequest(Constant.HOME_SERVICE_INQUIRIES_URL, com.alibaba.fastjson.JSON.toJSONString(map), ServiceDetailInfoResult.class, new HttpResponseCallback<NormalResult>() {
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

    public void serviceReport(String userid, String pubid, String rptname, String content, String cause, String phone,  final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pubid", pubid);
        map.put("rptname", rptname);
        map.put("content", content);
        map.put("cause", cause);
        map.put("phone", phone);

        sendAsyncRequest(Constant.HOME_SERVICE_INQUIRIES_URL, com.alibaba.fastjson.JSON.toJSONString(map), ServiceDetailInfoResult.class, new HttpResponseCallback<NormalResult>() {
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
     * 预支付接口(支付宝)
     */
    public void serviceReleaseAlipay(String ord_no, String seller, String price, String userid, final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("ord_no", ord_no);
        params.put("seller", seller);
        params.put("price", price);
        params.put("userid", userid);

        sendAsyncRequest(Constant.PLACE_SERVICE_ALIPAY_URL, com.alibaba.fastjson.JSON.toJSONString(params), FirstPayRsult.class, new HttpResponseCallback<FirstPayRsult>() {
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
     */
    public void serviceReleaseWxpay(String ord_no, String seller, String price, String userid, final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("ord_no", ord_no);
        params.put("seller", seller);
        params.put("price", price);
        params.put("userid", userid);

        sendAsyncRequest(Constant.PLACE_SERVICE_WXPAY_URL, com.alibaba.fastjson.JSON.toJSONString(params), WXPayResult.class, new HttpResponseCallback<WXPayResult>() {
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
     * 预支付接口(银联)
     */
    public void serviceReleaseUnpay(String ord_no, String seller, String price, String userid, final ResponseResult responseResult) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("ord_no", ord_no);
        params.put("seller", seller);
        params.put("price", price);
        params.put("userid", userid);

        sendAsyncRequest(Constant.PLACE_SERVICE_UNPAY_URL, com.alibaba.fastjson.JSON.toJSONString(params), UNPayResult.class, new HttpResponseCallback<UNPayResult>() {

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
