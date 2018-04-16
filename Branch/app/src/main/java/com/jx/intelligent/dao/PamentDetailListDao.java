package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ParmentDetailsListResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/5/24 0024.
 */

public class PamentDetailListDao extends BaseDao {
    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getPamentDetailsLisTask(final String number ,String type,String color,String url,String ppdnum,String typename,String proid,String  userid, final ResponseResult responseResult)
    {
        final Map<String,String> map = new HashMap<String,String>();
        map.put("number",number+"");
        map.put("type", type+"");
        map.put("color", color);
        map.put("url", url);
        map.put("ppdnum", ppdnum+"");
        map.put("typename", typename);
        map.put("proid", proid);
        map.put("userid", userid);

        sendAsyncRequest(Constant.PAMENT_DETAILS_LIST, com.alibaba.fastjson.JSON.toJSONString(map), ParmentDetailsListResult.class, new HttpResponseCallback<ParmentDetailsListResult>() {
            @Override
            public void onFailure(int statusCode, String message, ParmentDetailsListResult getParmentDetailsListResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ParmentDetailsListResult getParmentDetailsListResult) {
                if (getParmentDetailsListResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getParmentDetailsListResult);
//                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.PAMENT_DETAILS_LIST + StringUtil.obj2JsonStr(map))))
//                        {
//
//                            dbManager.insertUrlJsonData(Constant.PAMENT_DETAILS_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getParmentDetailsListResult));
//                        }
//                        else
//                        {
//                            dbManager.updateUrlJsonData(Constant.PAMENT_DETAILS_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getParmentDetailsListResult));
//                        }

                } else {
                    responseResult.resFailure(getParmentDetailsListResult.getMsg());
                }

            }
        });
    }
}


