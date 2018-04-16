package com.jx.maneger.dao;

import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.CustomerServiceTaskDetailResult;
import com.jx.maneger.results.CustomerServiceTasksReult;
import com.jx.maneger.results.FilterLiftNoticeResult;
import com.jx.maneger.results.FilterLiftsResult;
import com.jx.maneger.results.RepairRecordResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/17.
 */

public class CustomerServiceDao extends BaseDao {

    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取套餐滤芯状况统计数据
     * @param safetyMark
     * @param responseResult
     */
    public void getfilterLifts(String safetyMark, final String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        sendAsyncRequest(Constant.CS_FILTER_LIST, com.alibaba.fastjson.JSON.toJSONString(map), FilterLiftsResult.class, new HttpResponseCallback<FilterLiftsResult>() {
            @Override
            public void onFailure(int statusCode, String message, FilterLiftsResult filterLiftsResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, FilterLiftsResult filterLiftsResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (filterLiftsResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(filterLiftsResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CS_FILTER_LIST + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.CS_FILTER_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(filterLiftsResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.CS_FILTER_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(filterLiftsResult));
                        }
                    }
                } else {
                    responseResult.resFailure(filterLiftsResult.getResult(), filterLiftsResult.getMsg());
                }
            }
        });
    }

    /**
     * 搜索接口
     * @param safetyMark
     * @param page
     * @param search
     * @param responseResult
     */
    public void searchFilterLifts(String safetyMark, final String page, String search, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        map.put("search", search);
        sendAsyncRequest(Constant.CS_FILTER_SEARCH, com.alibaba.fastjson.JSON.toJSONString(map), FilterLiftsResult.class, new HttpResponseCallback<FilterLiftsResult>() {
            @Override
            public void onFailure(int statusCode, String message, FilterLiftsResult filterLiftsResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, FilterLiftsResult filterLiftsResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (filterLiftsResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(filterLiftsResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CS_FILTER_SEARCH + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.CS_FILTER_SEARCH + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(filterLiftsResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.CS_FILTER_SEARCH + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(filterLiftsResult));
                        }
                    }
                } else {
                    responseResult.resFailure(filterLiftsResult.getResult(), filterLiftsResult.getMsg());
                }
            }
        });
    }

    /**
     * 维修记录接口
     * @param safetyMark
     * @param page
     * @param pro_no
     * @param ord_no
     * @param responseResult
     */
    public void repairRecords(String safetyMark, final String page, String pro_no, String ord_no, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        map.put("pro_no", pro_no);
        map.put("ord_no", ord_no);
        sendAsyncRequest(Constant.CS_REPAIR_RECORD, com.alibaba.fastjson.JSON.toJSONString(map), RepairRecordResult.class, new HttpResponseCallback<RepairRecordResult>() {
            @Override
            public void onFailure(int statusCode, String message, RepairRecordResult repairRecordResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, RepairRecordResult repairRecordResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (repairRecordResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(repairRecordResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CS_REPAIR_RECORD + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.CS_REPAIR_RECORD + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(repairRecordResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.CS_REPAIR_RECORD + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(repairRecordResult));
                        }
                    }
                } else {
                    responseResult.resFailure(repairRecordResult.getResult(), repairRecordResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取售后任务列表
     * @param safetyMark
     * @param page
     * @param fsa_state
     * @param responseResult
     */
    public void getTasks(String safetyMark, final String page, String fsa_state, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        map.put("fas_state", fsa_state);
        sendAsyncRequest(Constant.CS_TASK_LIST, com.alibaba.fastjson.JSON.toJSONString(map), CustomerServiceTasksReult.class, new HttpResponseCallback<CustomerServiceTasksReult>() {
            @Override
            public void onFailure(int statusCode, String message, CustomerServiceTasksReult customerServiceTasksReult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, CustomerServiceTasksReult customerServiceTasksReult) {
                LogUtil.e("statusCode::" + statusCode);
                if (customerServiceTasksReult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(customerServiceTasksReult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CS_TASK_LIST + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.CS_TASK_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTasksReult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.CS_TASK_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTasksReult));
                        }
                    }
                } else {
                    responseResult.resFailure(customerServiceTasksReult.getResult(), customerServiceTasksReult.getMsg());
                }
            }
        });
    }

    /**
     * 滤芯警告接口
     * @param safetyMark
     * @param page
     * @param responseResult
     */
    public void getFilterLiftNotices(String safetyMark, final String page, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("page", page);
        sendAsyncRequest(Constant.CS_FILTER_NOTICE_LIST, com.alibaba.fastjson.JSON.toJSONString(map), FilterLiftNoticeResult.class, new HttpResponseCallback<FilterLiftNoticeResult>() {
            @Override
            public void onFailure(int statusCode, String message, FilterLiftNoticeResult filterLiftNoticeResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, FilterLiftNoticeResult filterLiftNoticeResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (filterLiftNoticeResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(filterLiftNoticeResult);
                    if("1".equals(page))
                    {
                        if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CS_FILTER_NOTICE_LIST + StringUtil.obj2JsonStr(map))))
                        {
                            dbManager.insertUrlJsonData(Constant.CS_FILTER_NOTICE_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(filterLiftNoticeResult));
                        }
                        else
                        {
                            dbManager.updateUrlJsonData(Constant.CS_FILTER_NOTICE_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(filterLiftNoticeResult));
                        }
                    }
                } else {
                    responseResult.resFailure(filterLiftNoticeResult.getResult(), filterLiftNoticeResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取售后详情
     * @param safetyMark
     * @param id
     * @param responseResult
     */
    public void getCustomerServiceDetail(String safetyMark, String id, final ResponseResult responseResult) {

        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        map.put("id", id);
        sendAsyncRequest(Constant.CS_TASK_DETAIL, com.alibaba.fastjson.JSON.toJSONString(map), CustomerServiceTaskDetailResult.class, new HttpResponseCallback<CustomerServiceTaskDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, CustomerServiceTaskDetailResult customerServiceTaskDetailResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, CustomerServiceTaskDetailResult customerServiceTaskDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (customerServiceTaskDetailResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(customerServiceTaskDetailResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.CS_TASK_DETAIL + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.CS_TASK_DETAIL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTaskDetailResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.CS_TASK_DETAIL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTaskDetailResult));
                    }
                } else {
                    responseResult.resFailure(customerServiceTaskDetailResult.getResult(), customerServiceTaskDetailResult.getMsg());
                }
            }
        });
    }
}