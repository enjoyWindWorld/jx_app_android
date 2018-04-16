package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.HomeBannerResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/5/24 0024.
 */

public class HomeBannerDao extends BaseDao {
    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getHomeBannerTask(final String type, final ResponseResult responseResult)
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("type", type+"");
        sendAsyncRequest(Constant.HOME_BANNER_URL, com.alibaba.fastjson.JSON.toJSONString(map), HomeBannerResult.class, new HttpResponseCallback<HomeBannerResult>() {
            @Override
            public void onFailure(int statusCode, String message, HomeBannerResult getHomeBannerResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, HomeBannerResult getHomeBannerResult) {

                LogUtil.e("statusCode::" + statusCode);
                if (getHomeBannerResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getHomeBannerResult);

                    if(type == "-2")
                    {

                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_BANNER_URL + StringUtil.obj2JsonStr(map))))
                        {

                            dbManager.insertUrlJsonData(Constant.HOME_BANNER_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getHomeBannerResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.HOME_BANNER_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getHomeBannerResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getHomeBannerResult.getMsg());
                }
            }
        });
    }


}
