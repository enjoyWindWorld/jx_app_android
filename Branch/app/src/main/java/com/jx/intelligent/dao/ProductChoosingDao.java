package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetMainHomeProductsResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class ProductChoosingDao extends  BaseDao {

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getProductChoosingTask(final int page, final ResponseResult responseResult)
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("page", page+"");
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
                    if(page == 1)
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMainHomeProductsResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getMainHomeProductsResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getMainHomeProductsResult.getMsg());
                }
            }
        });
    }

}
