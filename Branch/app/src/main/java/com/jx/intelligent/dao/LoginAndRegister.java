package com.jx.intelligent.dao;

import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.http.callback.HttpResponseCallback;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetCodeResult;
import com.jx.intelligent.result.LoginResult;
import com.jx.intelligent.result.NormalResult;
import com.jx.intelligent.result.RegisterResult;
import com.jx.intelligent.result.ResetPasswordResult;
import com.jx.intelligent.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 登录和注册的数据访问类
 */

public class LoginAndRegister extends BaseDao {

    /**
     * 登录方法
     *
     * @param responseResult
     */
    public void loginTask(String mobile, String password, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", mobile);
        map.put("password", password);

        sendAsyncRequest(Constant.USER_LOGIN_URL, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 注册方法
     *
     * @param mobile
     * @param password
     * @param responseResult
     */
    public void RegisterTask(String mobile, String password, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", mobile);
        map.put("password", password);

        sendAsyncRequest(Constant.USER_REGISTER_URL, com.alibaba.fastjson.JSON.toJSONString(map), RegisterResult.class, new HttpResponseCallback<RegisterResult>() {

            @Override
            public void onFailure(int statusCode, String message, RegisterResult registerResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, RegisterResult registerResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (registerResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(registerResult);
                } else {
                    responseResult.resFailure(registerResult.getMsg());
                }
            }
        });
    }

    /**
     * 重新设置密码
     *
     * @param phone
     * @param oldPwd
     * @param newPwd
     * @param responseResult
     */
    public void resetPassWordTask(String phone, String oldPwd, String newPwd, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", phone);
        map.put("oldPwd", oldPwd);
        map.put("newPwd", newPwd);

        sendAsyncRequest(Constant.USER_RESET_PWD_URL, com.alibaba.fastjson.JSON.toJSONString(map), ResetPasswordResult.class, new HttpResponseCallback<ResetPasswordResult>() {
            @Override
            public void onFailure(int statusCode, String message, ResetPasswordResult resetPasswordResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, ResetPasswordResult resetPasswordResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (resetPasswordResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(resetPasswordResult);
                } else {
                    responseResult.resFailure(resetPasswordResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param phoneNum
     * @param type           type=0表示注册，type=1表示找回密码, type=2表示修改手机号
     * @param responseResult
     */
    public void getCodeTask(String phoneNum, String type, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", phoneNum);
        map.put("type",type);

        sendAsyncRequest(Constant.USER_GET_CODE, com.alibaba.fastjson.JSON.toJSONString(map), GetCodeResult.class, new HttpResponseCallback<GetCodeResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetCodeResult getCodeResult) {
                responseResult.resFailure(message);
            }

            @Override
            public void onSuccess(int statusCode, GetCodeResult getCodeResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getCodeResult.getResult().equals(Constant.retCode_ok)) {
                    responseResult.resSuccess(getCodeResult);
                } else {
                    responseResult.resFailure(getCodeResult.getMsg());
                }
            }
        });
    }

    /**
     * 检测验证码的
     * @param phoneNum
     * @param code
     * @param responseResult
     */
    public void checkCodeTask(String phoneNum, String code, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", phoneNum);
        map.put("code", code);

        sendAsyncRequest(Constant.USER_CHECK_CODE, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
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
