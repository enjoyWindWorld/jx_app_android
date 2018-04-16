package com.jx.intelligent.dao;

import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.CustomerServiceTaskDetailResult;
import com.jx.intelligent.result.CustomerServiceTasksResult;
import com.jx.intelligent.result.EquipmentFilterLiftsResult;
import com.jx.intelligent.result.FaultTypeResult;
import com.jx.intelligent.result.GetRepairEquipmentResult;
import com.jx.intelligent.result.NormalResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */

public class CustomerServiceDao extends  BaseDao{
    DBManager dbManager;

    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 获取发布的售后任务列表
     *
     * @param fas_state //售后状态 1进行 200完成
     * @param userid
     * @param page
     */
    public void getMyTaskList(String fas_state, String userid, String page, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("fas_state", fas_state);
        map.put("userid", userid);
        map.put("page", page);

        sendAsyncRequest(Constant.SC_TASK_LIST, com.alibaba.fastjson.JSON.toJSONString(map), CustomerServiceTasksResult.class, new HttpResponseCallback<CustomerServiceTasksResult>() {
            @Override
            public void onFailure(int statusCode, String message, CustomerServiceTasksResult customerServiceTasksResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, CustomerServiceTasksResult customerServiceTasksResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (customerServiceTasksResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(customerServiceTasksResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.SC_TASK_LIST + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.SC_TASK_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTasksResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.SC_TASK_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTasksResult));
                    }
                } else {
                    responseResult.resFailure(customerServiceTasksResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取报修的机器列表
     *
     * @param responseResult
     */
    public void getEquipments(String userid, String page, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("page", page);

        sendAsyncRequest(Constant.SC_REPAIR_EQUIPMENT_LIST, com.alibaba.fastjson.JSON.toJSONString(map), GetRepairEquipmentResult.class, new HttpResponseCallback<GetRepairEquipmentResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetRepairEquipmentResult getRepairEquipmentResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetRepairEquipmentResult getRepairEquipmentResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getRepairEquipmentResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getRepairEquipmentResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_LIST + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getRepairEquipmentResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_LIST + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(getRepairEquipmentResult));
                    }
                } else {
                    responseResult.resFailure(getRepairEquipmentResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取故障现象
     *
     * @param responseResult
     */
    public void getFaultTypes(String is_shelves, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("is_shelves", is_shelves);

        sendAsyncRequest(Constant.SC_FAULT_TYPE, com.alibaba.fastjson.JSON.toJSONString(map), FaultTypeResult.class, new HttpResponseCallback<FaultTypeResult>() {
            @Override
            public void onFailure(int statusCode, String message, FaultTypeResult faultTypeResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, FaultTypeResult faultTypeResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (faultTypeResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(faultTypeResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.SC_FAULT_TYPE + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.SC_FAULT_TYPE + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(faultTypeResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.SC_FAULT_TYPE + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(faultTypeResult));
                    }
                } else {
                    responseResult.resFailure(faultTypeResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取报修的机器的滤芯寿命
     *
     * @param responseResult
     */
    public void getEquipmentFilterLifts(String pro_no, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("pro_no", pro_no);

        sendAsyncRequest(Constant.SC_REPAIR_EQUIPMENT_FILTER_LIFT, com.alibaba.fastjson.JSON.toJSONString(map), EquipmentFilterLiftsResult.class, new HttpResponseCallback<EquipmentFilterLiftsResult>() {
            @Override
            public void onFailure(int statusCode, String message, EquipmentFilterLiftsResult equipmentFilterLiftsResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, EquipmentFilterLiftsResult equipmentFilterLiftsResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (equipmentFilterLiftsResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(equipmentFilterLiftsResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_FILTER_LIFT + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_FILTER_LIFT + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(equipmentFilterLiftsResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_FILTER_LIFT + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(equipmentFilterLiftsResult));
                    }
                } else {
                    responseResult.resFailure(equipmentFilterLiftsResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取售后任务详情
     *
     * @param responseResult
     */
    public void getCustomerServiceTaskDetail(String id, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);

        sendAsyncRequest(Constant.SC_TASK_DETAIL, com.alibaba.fastjson.JSON.toJSONString(map), CustomerServiceTaskDetailResult.class, new HttpResponseCallback<CustomerServiceTaskDetailResult>() {
            @Override
            public void onFailure(int statusCode, String message, CustomerServiceTaskDetailResult customerServiceTaskDetailResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, CustomerServiceTaskDetailResult customerServiceTaskDetailResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (customerServiceTaskDetailResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(customerServiceTaskDetailResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.SC_TASK_DETAIL + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.SC_TASK_DETAIL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTaskDetailResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.SC_TASK_DETAIL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(customerServiceTaskDetailResult));
                    }
                } else {
                    responseResult.resFailure(customerServiceTaskDetailResult.getMsg());
                }
            }
        });
    }

    /**
     * 发布其他类型任务
     * @param userid
     * @param pro_id
     * @param pro_name
     * @param ord_color
     * @param make_time
     * @param contact_person
     * @param contact_way
     * @param user_address
     * @param address_details
     * @param filter_name
     * @param specific_reason
     * @param fautl_url
     * @param pro_no
     * @param ord_managerno
     * @param responseResult
     */
    public void otherTask(String userid, String pro_id, String pro_name, String ord_color, String make_time, String contact_person,
                                 String contact_way, String user_address, String address_details, String filter_name, String specific_reason,
                                 String fautl_url, String pro_no, String ord_managerno, String ord_no, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pro_id", pro_id);
        map.put("pro_name", pro_name);
        map.put("ord_color", ord_color);
        map.put("make_time", make_time);
        map.put("contact_person", contact_person);
        map.put("contact_way", contact_way);
        map.put("user_address", user_address);
        map.put("address_details", address_details);
        map.put("filter_name", filter_name);
        map.put("specific_reason", specific_reason);
        map.put("fautl_url", fautl_url);
        map.put("pro_no", pro_no);
        map.put("ord_managerno", ord_managerno);
        map.put("ord_no", ord_no);
        map.put("fas_type", "3");

        sendAsyncRequest(Constant.SC_ADD_TASK, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 发布更换滤芯类型任务
     * @param userid
     * @param pro_id
     * @param pro_name
     * @param ord_color
     * @param make_time
     * @param contact_person
     * @param contact_way
     * @param user_address
     * @param address_details
     * @param proflt_life
     * @param filter_name
     * @param pro_no
     * @param ord_managerno
     * @param responseResult
     */
    public void changeFilterTask(String userid, String pro_id, String pro_name, String ord_color, String make_time, String contact_person,
                                 String contact_way, String user_address, String address_details, String proflt_life, String filter_name,
                                 String pro_no, String ord_managerno, String ord_no, String specific_reason, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pro_id", pro_id);
        map.put("pro_name", pro_name);
        map.put("ord_color", ord_color);
        map.put("make_time", make_time);
        map.put("contact_person", contact_person);
        map.put("contact_way", contact_way);
        map.put("user_address", user_address);
        map.put("address_details", address_details);
        map.put("proflt_life", proflt_life);
        map.put("filter_name", filter_name);
        map.put("pro_no", pro_no);
        map.put("ord_managerno", ord_managerno);
        map.put("ord_no", ord_no);
        map.put("specific_reason", specific_reason);
        map.put("fas_type", "1");

        sendAsyncRequest(Constant.SC_ADD_TASK, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 发布机器报修类型任务
     * @param userid
     * @param pro_id
     * @param pro_name
     * @param ord_color
     * @param make_time
     * @param contact_person
     * @param contact_way
     * @param user_address
     * @param address_details
     * @param filter_name
     * @param fault_cause
     * @param specific_reason
     * @param fautl_url
     * @param pro_no
     * @param ord_managerno
     * @param responseResult
     */
    public void machineRepairTask(String userid, String pro_id, String pro_name, String ord_color, String make_time, String contact_person,
                                 String contact_way, String user_address, String address_details, String filter_name,
                                 String fault_cause, String specific_reason, String fautl_url, String pro_no, String ord_managerno, String ord_no, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pro_id", pro_id);
        map.put("pro_name", pro_name);
        map.put("ord_color", ord_color);
        map.put("make_time", make_time);
        map.put("contact_person", contact_person);
        map.put("contact_way", contact_way);
        map.put("user_address", user_address);
        map.put("address_details", address_details);
        map.put("filter_name", filter_name);
        map.put("fault_cause", fault_cause);
        map.put("specific_reason", specific_reason);
        map.put("fautl_url", fautl_url);
        map.put("pro_no", pro_no);
        map.put("ord_managerno", ord_managerno);
        map.put("ord_no", ord_no);
        map.put("fas_type", "2");

        sendAsyncRequest(Constant.SC_ADD_TASK, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }

    /**
     * 售后评价
     * @param id
     * @param pro_no
     * @param ord_no
     * @param service_type
     * @param service_master
     * @param service_master_phone
     * @param evaluation_people
     * @param evaluation_people_phnoe
     * @param userid
     * @param content
     * @param appraise_url
     * @param is_badge
     * @param is_overalls
     * @param is_anonymous
     * @param satisfaction
     * @param service_attitude
     * @param ord_managerno
     * @param responseResult
     */
    public void evaluateCustomerService(String id, String pro_no, String ord_no, String service_type, String service_master, String service_master_phone,
                                  String evaluation_people, String evaluation_people_phnoe, String userid, String content,
                                  String appraise_url, String is_badge, String is_overalls, String is_anonymous, String satisfaction, String service_attitude, String ord_managerno, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("pro_no", pro_no);
        map.put("ord_no", ord_no);
        map.put("service_type", service_type);
        map.put("service_master", service_master);
        map.put("service_master_phone", service_master_phone);
        map.put("evaluation_people", evaluation_people);
        map.put("evaluation_people_phnoe", evaluation_people_phnoe);
        map.put("userid", userid);
        map.put("content", content);
        map.put("appraise_url", appraise_url);
        map.put("is_badge", is_badge);
        map.put("is_overalls", is_overalls);
        map.put("is_anonymous", is_anonymous);
        map.put("satisfaction", satisfaction);
        map.put("service_attitude", service_attitude);
        map.put("ord_managerno", ord_managerno);

        sendAsyncRequest(Constant.SC_EVALUATE, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
            @Override
            public void onFailure(int statusCode, String message, NormalResult normalResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, NormalResult normalResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (normalResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(normalResult);
                } else {
                    responseResult.resFailure(normalResult.getMsg());
                }
            }
        });
    }
}
