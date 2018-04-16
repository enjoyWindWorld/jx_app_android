package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ShopPingCartResult;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/6/16 0016.
 */

public class ShoppingCartDao extends  BaseDao{

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getShoppingCartTask(final String userid,final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
                    map.put("userid",userid);

        sendAsyncRequest(Constant.SHOPPING_CART, com.alibaba.fastjson.JSON.toJSONString(map), ShopPingCartResult.class, new HttpResponseCallback<ShopPingCartResult>() {
            @Override
            public void onFailure(int statusCode, String message, ShopPingCartResult getShopPingCartResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ShopPingCartResult getShopPingCartResult) {
                if (getShopPingCartResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getShopPingCartResult);

                    if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.SHOPPING_CART + StringUtil.obj2JsonStr(map)))) {
                        dbManager.insertUrlJsonData(Constant.SHOPPING_CART + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getShopPingCartResult));
                    } else {
                        dbManager.updateUrlJsonData(Constant.SHOPPING_CART + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getShopPingCartResult));
                    }

                } else {
                    responseResult.resFailure(getShopPingCartResult.getMsg());
                }
            }


        });
    }
}
