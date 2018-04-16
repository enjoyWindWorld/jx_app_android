package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.HomeTextResult;
import com.jx.intelligent.result.HomeWaterResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王云 on 2017/5/24 0024.
 */

public class HomeWaterReportDao extends BaseDao {
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
    public void getWaterReportTask(final String userid,String cityCode,final ResponseResult responseResult)
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("cityCode", cityCode);
        sendAsyncRequest(Constant.HOME_WATER_REPORT, com.alibaba.fastjson.JSON.toJSONString(map), HomeWaterResult.class, new HttpResponseCallback<HomeWaterResult>() {
            @Override
            public void onFailure(int statusCode, String message, HomeWaterResult getHomeWaterResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, HomeWaterResult getHomeWaterResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getHomeWaterResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getHomeWaterResult);

                    if(userid!=null)
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_WATER_REPORT + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.HOME_WATER_REPORT + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getHomeWaterResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.HOME_WATER_REPORT + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getHomeWaterResult));
                        }
                    }
                } else {
                    responseResult.resFailure(getHomeWaterResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取首页新闻滚动textView的网络请求
     * @param
     * @param
     * @param responseResult
     */
    public void getScrollTextViewTask(final String type,final ResponseResult responseResult)
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);

        sendAsyncRequest(Constant.HOME_TEXT_URL, com.alibaba.fastjson.JSON.toJSONString(map), HomeTextResult.class, new HttpResponseCallback<HomeTextResult>() {
            @Override
            public void onFailure(int statusCode, String message, HomeTextResult gethomeTextResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, HomeTextResult gethomeTextResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (gethomeTextResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(gethomeTextResult);
                    if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_TEXT_URL + StringUtil.obj2JsonStr(map)))) {
                        dbManager.insertUrlJsonData(Constant.HOME_TEXT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(gethomeTextResult));
                    } else {
                        dbManager.updateUrlJsonData(Constant.HOME_TEXT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(gethomeTextResult));
                    }

                } else {
                    responseResult.resFailure(gethomeTextResult.getMsg());
                }
            }
        });

        }
    }



