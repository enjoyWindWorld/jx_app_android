package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.AddToShopPingCartResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/6/16 0016.
 */

public class AddToShoppingCartDao extends  BaseDao{

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getAddShoppingCartTask(final  String name,String price,String url,String number ,String ppdnum,String color,String userid, String proid,String type,final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("number", number);
        map.put("ppdnum", ppdnum);
        map.put("color", color);
        map.put("userid", userid);
        map.put("proid", proid);
        map.put("type", type);
        map.put("name",name);
        map.put("price",price);
        map.put("url",url);

        System.out.println("====url===="+Constant.ADDTO_SHOPPING_CART);

        sendAsyncRequest(Constant.ADDTO_SHOPPING_CART, com.alibaba.fastjson.JSON.toJSONString(map), AddToShopPingCartResult.class, new HttpResponseCallback<AddToShopPingCartResult>() {
            @Override
            public void onFailure(int statusCode, String message, AddToShopPingCartResult getaddToShopPingCartResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, AddToShopPingCartResult getaddToShopPingCartResult) {
                if (getaddToShopPingCartResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getaddToShopPingCartResult);

//                    if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map)))) {
//                        dbManager.insertUrlJsonData(Constant.ADDTO_SHOPPING_CART + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getaddToShopPingCartResult));
//                    } else {
//                        dbManager.updateUrlJsonData(Constant.ADDTO_SHOPPING_CART + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getaddToShopPingCartResult));
//                    }

                } else {
                    responseResult.resFailure(getaddToShopPingCartResult.getMsg());
                }
            }


        });
    }
}
