package com.jx.maneger.dao;

import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.CashWithdrawalRatioResult;
import com.jx.maneger.results.GetOrderListResult;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.results.SubordinateResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/11.
 */

public class SubordinateDao extends BaseDao{
    DBManager dbManager;
    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getSubordinates(String safetyMark, String page, String tag, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        map.put("tag", tag);

        sendAsyncRequest(Constant.USER_MY_PARTNER, com.alibaba.fastjson.JSON.toJSONString(map), SubordinateResult.class, new HttpResponseCallback<SubordinateResult>() {
            @Override
            public void onFailure(int statusCode, String message, SubordinateResult subordinateResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, SubordinateResult subordinateResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (subordinateResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(subordinateResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_MY_PARTNER + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_MY_PARTNER + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(subordinateResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_MY_PARTNER + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(subordinateResult));
                    }
                } else {
                    responseResult.resFailure(subordinateResult.getResult(), subordinateResult.getMsg());
                }
            }
        });
    }

    /**
     * 比例分配
     * @param safetyMark
     * @param par_level
     * @param username
     * @param responseResult
     */
    public void getCashWithdrawalRatio(String safetyMark, String par_level, String username, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("par_level", par_level);
        map.put("username", username);

        sendAsyncRequest(Constant.USER_GET_CASH_WITHRAWAL_RRATIO, com.alibaba.fastjson.JSON.toJSONString(map), CashWithdrawalRatioResult.class, new HttpResponseCallback<CashWithdrawalRatioResult>() {
            @Override
            public void onFailure(int statusCode, String message, CashWithdrawalRatioResult cashWithdrawalRatioResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, CashWithdrawalRatioResult cashWithdrawalRatioResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (cashWithdrawalRatioResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(cashWithdrawalRatioResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_CASH_WITHRAWAL_RRATIO + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_GET_CASH_WITHRAWAL_RRATIO + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(cashWithdrawalRatioResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_GET_CASH_WITHRAWAL_RRATIO + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(cashWithdrawalRatioResult));
                    }
                } else {
                    responseResult.resFailure(cashWithdrawalRatioResult.getResult(), cashWithdrawalRatioResult.getMsg());
                }
            }
        });
    }

    /**
     * 修改下级比例
     * @param safetyMark
     * @param username
     * @param rebates
     * @param installed
     * @param total
     * @param responseResult
     */
    public void updateCashWithdrawalRatio(String safetyMark, String username, String rebates, String installed, String total, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("username", username);
        map.put("rebates", rebates);
        map.put("installed", installed);
        map.put("total", total);

        sendAsyncRequest(Constant.USER_UPDATE_CASH_WITHRAWAL_RRATIO, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
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
