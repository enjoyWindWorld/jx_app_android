package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.CartAmountResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/14.
 */

public class CartAmountDao extends BaseDao {

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     *  获取饮水数据和水质报告的网络请求
     * @param userid 用户ID
     * @param responseResult
     */
    public void getCartAmountTask(final String userid,final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        sendAsyncRequest(Constant.CART_AMOUNT_URL, com.alibaba.fastjson.JSON.toJSONString(map), CartAmountResult.class, new HttpResponseCallback<CartAmountResult>() {
            @Override
            public void onFailure(int statusCode, String message, CartAmountResult getCartAmountResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, CartAmountResult getCartAmountResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getCartAmountResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getCartAmountResult);

                    if (userid != null) {
                        if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CART_AMOUNT_URL + StringUtil.obj2JsonStr(map)))) {
                            dbManager.insertUrlJsonData(Constant.CART_AMOUNT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getCartAmountResult));
                        } else {
                            dbManager.updateUrlJsonData(Constant.CART_AMOUNT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getCartAmountResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getCartAmountResult.getMsg());
                }
            }
        });
    }
}
