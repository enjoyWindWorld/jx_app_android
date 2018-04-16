package com.jx.intelligent.dao;

import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.CartUpdaterResult;
import com.jx.intelligent.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/5/24 0024.
 */

public class UpdaterShopDao extends BaseDao {


    public void getUpdaterCartTask(final String scid, String number,final ResponseResult responseResult)
    {
        final Map<String, String> map = new HashMap<String, String>();

        map.put("id", scid);
        map.put("number", number);


        sendAsyncRequest(Constant.UPDAPTER_SHOP_CART_URL, com.alibaba.fastjson.JSON.toJSONString(map), CartUpdaterResult.class, new HttpResponseCallback<CartUpdaterResult>() {
            @Override
            public void onFailure(int statusCode, String message, CartUpdaterResult getCartUpdaterResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, CartUpdaterResult getCartUpdaterResult) {

                LogUtil.e("statusCode::" + statusCode);
                if (getCartUpdaterResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getCartUpdaterResult);

                } else {
                    responseResult.resFailure(getCartUpdaterResult.getMsg());
                }
            }
        });
    }


}
