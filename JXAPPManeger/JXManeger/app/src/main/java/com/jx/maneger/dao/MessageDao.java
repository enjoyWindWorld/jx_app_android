package com.jx.maneger.dao;

import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.MessageNoReadResult;
import com.jx.maneger.results.MessageResult;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MessageDao extends BaseDao{

    DBManager dbManager;
    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取未读消息
     * @param safetyMark
     * @param responseResult
     */
    public void getNoReadMsgTask(String safetyMark, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        sendAsyncRequest(Constant.USER_NO_READ_MSG, com.alibaba.fastjson.JSON.toJSONString(map), MessageNoReadResult.class, new HttpResponseCallback<MessageNoReadResult>() {
            @Override
            public void onFailure(int statusCode, String message, MessageNoReadResult messageNoReadResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, MessageNoReadResult messageNoReadResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (messageNoReadResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(messageNoReadResult);
                } else {
                    responseResult.resFailure(messageNoReadResult.getResult(), messageNoReadResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取消息列表
     * @param safetyMark
     * @param responseResult
     */
    public void getMSGListTask(String safetyMark, final String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        sendAsyncRequest(Constant.USER_GET_MSG_LIST, com.alibaba.fastjson.JSON.toJSONString(map), MessageResult.class, new HttpResponseCallback<MessageResult>() {
            @Override
            public void onFailure(int statusCode, String message, MessageResult messageResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, MessageResult messageResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (messageResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(messageResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(messageResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(messageResult));
                        }
                    }
                } else {
                    responseResult.resFailure(messageResult.getResult(), messageResult.getMsg());
                }
            }
        });
    }

    /**
     * 阅读
     * @param id
     * @param responseResult
     */
    public void readedMsgTask(String safetyMark, String id, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("id", id);
        sendAsyncRequest(Constant.USER_READ_MSG, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getResult(), normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 删除消息
     * @param id
     * @param responseResult
     */
    public void deleteMsgTask(String safetyMark, String id, final ResponseResult responseResult) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("id", id);
        sendAsyncRequest(Constant.USER_DELETE_MSG, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getResult(), normalResult.getMsg());
                }
            }
        });
    }
}
