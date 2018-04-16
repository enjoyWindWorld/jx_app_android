package com.jx.intelligent.http.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @创建者： zhangbo
 * @项目名: CommonFramework
 * @包名: com.ronghui.common_libray.http
 * @创建时间： 2016/8/16 10:41
 * @描述： ${TODO}
 */

public abstract class HttpCallback implements Callback {
    /**
     * 请求成功标识
     */
    private static final int     WHAT_SUCCESS = 11;
    /**
     * 请求失败标识
     */
    private static final int     WHAT_FAILURE = 22;
    /**
     * 消息处理器
     */
    private Handler handler      = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                if (msg.what == WHAT_SUCCESS) {
                    if (msg.obj != null) {
                        onSuccess(msg.arg1, msg.obj.toString());
                    }
                } else if (msg.what == WHAT_FAILURE) {
                    if (msg.obj != null) {
                        onFailure(msg.arg1, msg.obj.toString());
                    }
                }
            }
        }
    };
    /**
     * 当请求成功时会调用此方法
     *
     * @param statusCode HTTP响应状态码
     * @param response   返回的字符串数据
     */
    public abstract void onSuccess(int statusCode, @NonNull String response);

    /**
     * 当请求失败时会调用此方法
     *
     * @param statusCode HTTP响应状态码
     * @param message    异常或错误消息
     */
    public abstract void onFailure(int statusCode, @NonNull String message);


    @Override
    public void onFailure(Call call, IOException e) {
        final String message = e.getMessage();
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            onFailure(1, message);
        } else {
            Message msg = Message.obtain();
            msg.obj = message;
            msg.arg1 = 1;
            msg.what = WHAT_FAILURE;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        // 这个方法不是在主线程中调用，不能在回调中更新UI
        String responseString = response.body().string();
        String url = response.request().url().url().toString();
        if(!url.equals(Constant.PLACE_ORDER_ALIPAY_URL)
                && !url.equals(Constant.IMAGE_UPLOAD_URL)
                && !url.equals(Constant.PLACE_ORDER_WXPAY_URL)
                && !url.equals(Constant.PLACE_SERVICE_ALIPAY_URL)
                && !url.equals(Constant.PLACE_SERVICE_WXPAY_URL)
                )
        {
            try {
                if(new JSONObject(responseString).getString("data") != null && !StringUtil.isEmpty(new JSONObject(responseString).getString("data")))
                {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String decodeData = StringUtil.decode(jsonObject.getString("data"));
                    responseString = "{\"result\":"+jsonObject.getInt("result")+",\"msg\":\""+jsonObject.getString("msg")+"\",\"errcode\":"+jsonObject.getInt("errcode")+",\"data\":"+decodeData+"}";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final int code = response.code(); //code >= 200 && code < 300即为成功,否则为异常
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            onSuccess(code, responseString);
        } else {
            Message msg = Message.obtain();
            msg.obj = responseString;
            msg.arg1 = code;
            msg.what = WHAT_SUCCESS;
            handler.sendMessage(msg);
        }
    }
}
