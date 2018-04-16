package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.DeleteToShopPingCartResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/6/16 0016.
 */

public class DeleteToShoppingCartDao extends  BaseDao{

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getDeleteShoppingCartTask(final  String scId,final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("id", scId);



        sendAsyncRequest(Constant.DELETE_SHOPPING_CART, com.alibaba.fastjson.JSON.toJSONString(map), DeleteToShopPingCartResult.class, new HttpResponseCallback<DeleteToShopPingCartResult>() {
            @Override
            public void onFailure(int statusCode, String message, DeleteToShopPingCartResult deleteToShopPingCartResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, DeleteToShopPingCartResult getdeleteToShopPingCartResult) {
                if (getdeleteToShopPingCartResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getdeleteToShopPingCartResult);

                } else {
                    responseResult.resFailure(getdeleteToShopPingCartResult.getMsg());
                }
            }

        });
    }
}
