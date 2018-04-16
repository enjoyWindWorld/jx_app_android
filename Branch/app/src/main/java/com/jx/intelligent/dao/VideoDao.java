package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.VideoResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class VideoDao extends BaseDao {
    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    public void getVideoTask(final String id,String page, final ResponseResult responseResult)
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("page", page);
        sendAsyncRequest(Constant.VIDEO_URL, com.alibaba.fastjson.JSON.toJSONString(map), VideoResult.class, new HttpResponseCallback<VideoResult>() {
            @Override
            public void onFailure(int statusCode, String message, VideoResult getVideoResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, VideoResult getVideoResult) {

                LogUtil.e("statusCode::" + statusCode);
                if (getVideoResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getVideoResult);

                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.VIDEO_URL + StringUtil.obj2JsonStr(map))))
                        {

                            dbManager.insertUrlJsonData(Constant.VIDEO_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getVideoResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.VIDEO_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getVideoResult));
                        }

                } else {
                    responseResult.resFailure(getVideoResult.getMsg());
                }
            }
        });
    }
}
