package com.jx.maneger.dao;

import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetCodeResult;
import com.jx.maneger.results.HomeTextResult;
import com.jx.maneger.results.LoginResult;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.results.RegisterResult;
import com.jx.maneger.results.ShareContentResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 登录和注册的数据访问类
 */

public class LoginAndRegister extends BaseDao {

    DBManager dbManager;
    {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
    }

    /**
     * 登录方法
     *
     * @param responseResult
     */
    public void loginTask(String username, String password, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);

        sendAsyncRequest(Constant.USER_LOGIN_URL, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(loginResult);
                } else {
                    responseResult.resFailure(loginResult.getResult(), loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 获取合伙人信息
     * @param safetyMark
     * @param responseResult
     */
    public void getUserInfoTask(String safetyMark, final ResponseResult responseResult) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", safetyMark);
        sendAsyncRequest(Constant.USER_INFO_URL, com.alibaba.fastjson.JSON.toJSONString(map), LoginResult.class, new HttpResponseCallback<LoginResult>() {
            @Override
            public void onFailure(int statusCode, String message, LoginResult checkOpeTaskResultBean) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, LoginResult loginResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (loginResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(loginResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_INFO_URL + StringUtil.obj2JsonStr(map))))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_INFO_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(loginResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_INFO_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(loginResult));
                    }
                } else {
                    responseResult.resFailure(loginResult.getResult(), loginResult.getMsg());
                }
            }
        });
    }

    /**
     * 注册创客
     * @param RecommenderCode
     * @param NameReferee
     * @param nameApplicant
     * @param cardNumber
     * @param mobilePhone
     * @param homeAddress
     * @param s_province
     * @param s_city
     * @param s_county
     * @param responseResult
     */
    public void registerTask(String RecommenderCode, String NameReferee, String nameApplicant, String ord_no, String cardNumber, String mobilePhone, String homeAddress, String s_province, String s_city, String s_county, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("RecommenderCode", RecommenderCode);
        map.put("NameReferee", NameReferee);
        map.put("ord_no", ord_no);
        map.put("nameApplicant", nameApplicant);
        map.put("cardNumber", cardNumber);
        map.put("mobilePhone", mobilePhone);
        map.put("homeAddress", homeAddress);
        map.put("s_province", s_province);
        map.put("s_city", s_city);
        map.put("s_county", s_county);


        sendAsyncRequest(Constant.USER_REGISTER_URL, com.alibaba.fastjson.JSON.toJSONString(map), RegisterResult.class, new HttpResponseCallback<RegisterResult>() {

            @Override
            public void onFailure(int statusCode, String message, RegisterResult registerResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, RegisterResult registerResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (registerResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(registerResult);
                } else {
                    responseResult.resFailure(registerResult.getResult(), registerResult.getMsg());
                }
            }
        });
    }

    /**
     * 重新设置密码
     *
     * @param username
     * @param oldPwd
     * @param newPwd
     * @param responseResult
     */
    public void resetPassWordTask(String username, String oldPwd, String newPwd, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("oldPwd", oldPwd);
        map.put("newPwd", newPwd);

        sendAsyncRequest(Constant.USER_RESET_PWD_URL, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
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
     * 获取验证码
     *
     * @param username
     * @param type           type=0表示注册，type=1表示找回密码, type=2表示修改手机号
     * @param responseResult
     */
    public void getCodeTask(String username, String type, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("type",type);

        sendAsyncRequest(Constant.USER_GET_CODE, com.alibaba.fastjson.JSON.toJSONString(map), GetCodeResult.class, new HttpResponseCallback<GetCodeResult>() {
            @Override
            public void onFailure(int statusCode, String message, GetCodeResult getCodeResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, GetCodeResult getCodeResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (getCodeResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(getCodeResult);
                } else {
                    responseResult.resFailure(getCodeResult.getResult(), getCodeResult.getMsg());
                }
            }
        });
    }

    /**
     * 检测验证码的
     * @param username
     * @param code
     * @param responseResult
     */
    public void checkCodeTask(String username, String code, final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("code", code);

        sendAsyncRequest(Constant.USER_CHECK_CODE, com.alibaba.fastjson.JSON.toJSONString(map), NormalResult.class, new HttpResponseCallback<NormalResult>() {
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
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, HomeTextResult gethomeTextResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (gethomeTextResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(gethomeTextResult);
                    if (StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.HOME_TEXT_URL + StringUtil.obj2JsonStr(map)))) {
                        dbManager.insertUrlJsonData(Constant.HOME_TEXT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(gethomeTextResult));
                    } else {
                        dbManager.updateUrlJsonData(Constant.HOME_TEXT_URL + StringUtil.obj2JsonStr(map), StringUtil.obj2JsonStr(gethomeTextResult));
                    }

                } else {
                    responseResult.resFailure(gethomeTextResult.getResult(), gethomeTextResult.getMsg());
                }
            }
        });

    }

    /**
     * 获取分享的内容
     *
     * @param responseResult
     */
    public void geShareContentTask(final ResponseResult responseResult) {
        Map<String, String> map = new HashMap<String, String>();

        sendAsyncRequest(Constant.USER_SHARE_CONTENT_URL, com.alibaba.fastjson.JSON.toJSONString(map), ShareContentResult.class, new HttpResponseCallback<ShareContentResult>() {
            @Override
            public void onFailure(int statusCode, String message, ShareContentResult shareContentResult) {
                responseResult.resFailure(statusCode, message);
            }

            @Override
            public void onSuccess(int statusCode, ShareContentResult shareContentResult) {
                LogUtil.e("statusCode::" + statusCode);
                if (shareContentResult.getResult() == Constant.retCode_ok) {
                    responseResult.resSuccess(shareContentResult);
                    if(StringUtil.isEmpty(dbManager.getUrlJsonData(Constant.USER_SHARE_CONTENT_URL)))
                    {
                        dbManager.insertUrlJsonData(Constant.USER_SHARE_CONTENT_URL, StringUtil.obj2JsonStr(shareContentResult));
                    }
                    else
                    {
                        dbManager.updateUrlJsonData(Constant.USER_SHARE_CONTENT_URL, StringUtil.obj2JsonStr(shareContentResult));
                    }
                } else {
                    responseResult.resFailure(shareContentResult.getResult(), shareContentResult.getMsg());
                }
            }
        });
    }

}
