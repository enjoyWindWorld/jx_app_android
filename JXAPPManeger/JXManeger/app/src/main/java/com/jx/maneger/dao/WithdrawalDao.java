package com.jx.maneger.dao;

import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetAliPayResult;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.results.SalesAmountResult;
import com.jx.maneger.results.SubWithdrawalDetailResult;
import com.jx.maneger.results.SubordinateResult;
import com.jx.maneger.results.WidthdrawalRecordResult;
import com.jx.maneger.results.WithdrawalAmountResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/17.
 */

public class WithdrawalDao extends BaseDao{
    DBManager dbManager;
    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取绑定的支付宝信息
     * @param safetyMark
     * @param responseResult
     */
    public void getAliPayInfo(String safetyMark, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);

        sendAsyncRequest(Constant.USER_ALIPAY_INFO, com.alibaba.fastjson.JSON.toJSONString(map), GetAliPayResult.class, new HttpResponseCallback<GetAliPayResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetAliPayResult getAliPayResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, GetAliPayResult getAliPayResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getAliPayResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(getAliPayResult);
                } else {
                    responseResult.resFailure(getAliPayResult.getResult(), getAliPayResult.getMsg());
                }
            }
        });
    }

    /**
     * 绑定支付宝账号
     * @param safetyMark
     * @param pay_name
     * @param pay_account
     * @param responseResult
     */
    public void bindAliPayInfo(String safetyMark, String pay_name, String pay_account, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("pay_name", pay_name);
        map.put("pay_account", pay_account);

        sendAsyncRequest(Constant.USER_BIND_ALIPAY, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getResult(), normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 解绑支付宝账号
     * @param safetyMark
     * @param responseResult
     */
    public void unBindAliPayInfo(String safetyMark, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);

        sendAsyncRequest(Constant.USER_UNBIND_ALIPAY, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getResult(), normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取提现金额
     * @param safetyMark
     * @param responseResult
     */
    public void getWithdrawalAmount(String safetyMark, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);

        sendAsyncRequest(Constant.USER_WITHDRAWAL_AMOUNT, com.alibaba.fastjson.JSON.toJSONString(map), WithdrawalAmountResult.class, new HttpResponseCallback<WithdrawalAmountResult>() {
            @Override
            public void onFailure(int statusCode, String message, WithdrawalAmountResult withdrawalAmountResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, WithdrawalAmountResult withdrawalAmountResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (withdrawalAmountResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(withdrawalAmountResult);
                } else {
                    responseResult.resFailure(withdrawalAmountResult.getResult(), withdrawalAmountResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取提现的销售量
     * @param safetyMark
     * @param withdrawal_order_no
     * @param responseResult
     */
    public void getSalesAmount(String safetyMark, String withdrawal_order_no, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("withdrawal_order_no", withdrawal_order_no);

        sendAsyncRequest(Constant.USER_SALE_AMOUNT, com.alibaba.fastjson.JSON.toJSONString(map), SalesAmountResult.class, new HttpResponseCallback<SalesAmountResult>() {
            @Override
            public void onFailure(int statusCode, String message, SalesAmountResult salesAmountResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, SalesAmountResult salesAmountResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (salesAmountResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(salesAmountResult);
                } else {
                    responseResult.resFailure(salesAmountResult.getResult(), salesAmountResult.getMsg());
                }
            }
        });
    }

    /**
     * 用户提现
     * @param safetyMark
     * @param withdrawal_order_no
     * @param responseResult
     */
    public void doWithdrawal(String safetyMark, String withdrawal_order_no,  final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("withdrawal_order_no", withdrawal_order_no);

        sendAsyncRequest(Constant.USER_DO_WITHDRAWAL, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getResult(), normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取提现记录
     * @param safetyMark
     * @param page
     * @param responseResult
     */
    public void getWidthdrawalRecords(String safetyMark, String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);

        sendAsyncRequest(Constant.USER_WITHDRAWAL_RECORD, com.alibaba.fastjson.JSON.toJSONString(map), WidthdrawalRecordResult.class, new HttpResponseCallback<WidthdrawalRecordResult>() {
            @Override
            public void onFailure(int statusCode, String message, WidthdrawalRecordResult widthdrawalRecordResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, WidthdrawalRecordResult widthdrawalRecordResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (widthdrawalRecordResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(widthdrawalRecordResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_WITHDRAWAL_RECORD + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_WITHDRAWAL_RECORD + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(widthdrawalRecordResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_WITHDRAWAL_RECORD + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(widthdrawalRecordResult));
                    }
                } else {
                    responseResult.resFailure(widthdrawalRecordResult.getResult(), widthdrawalRecordResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取下级的提现详情
     * @param safetyMark
     * @param withdrawal_order_no
     * @param id
     * @param responseResult
     */
    public void getSubWithdrawalDetail(String safetyMark, String withdrawal_order_no, String id, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("withdrawal_order_no", withdrawal_order_no);
        map.put("id", id);

        sendAsyncRequest(Constant.USER_SUB_SALE_AMOUNT, com.alibaba.fastjson.JSON.toJSONString(map), SubWithdrawalDetailResult.class, new HttpResponseCallback<SubWithdrawalDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, SubWithdrawalDetailResult subWithdrawalDetailResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, SubWithdrawalDetailResult subWithdrawalDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (subWithdrawalDetailResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(subWithdrawalDetailResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_SUB_SALE_AMOUNT + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_SUB_SALE_AMOUNT + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(subWithdrawalDetailResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_SUB_SALE_AMOUNT + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(subWithdrawalDetailResult));
                    }
                } else {
                    responseResult.resFailure(subWithdrawalDetailResult.getResult(), subWithdrawalDetailResult.getMsg());
                }
            }
        });
    }

    /**
     * 提现审核
     * @param safetyMark
     * @param withdrawal_order_no
     * @param state
     * @param reason
     * @param responseResult
     */
    public void withdrawalaudit(String safetyMark, String withdrawal_order_no, String state, String reason, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("withdrawal_order", withdrawal_order_no);
        map.put("state", state);
        map.put("reason", reason);

        sendAsyncRequest(Constant.USER_WITHRAWAL_AUDIT, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getResult(), normalResult.getMsg());
                }
            }
        });
    }
}
