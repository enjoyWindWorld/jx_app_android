package com.jx.maneger.http.callback;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

public abstract class HttpResponseCallback<RESPONSE> extends HttpCallback {
    private Class<RESPONSE> responseEntityClass;

    public final void setResponseEntityClass(@NonNull Class<RESPONSE> responseEntityClass) {
        this.responseEntityClass = responseEntityClass;
    }

    @Override
    public void onSuccess(int statusCode, @NonNull String response) {
        if(statusCode >= 200 && statusCode < 300){ //成功
            handleSuccess(statusCode, response);
        }else{
            handleFailure(statusCode, statusCode +",请求异常", null);
        }

    }

    @Override
    public void onFailure(int statusCode, @NonNull String message) {
        handleFailure(statusCode, message, null);
    }

    private void handleSuccess(int statusCode, String response) {
        if (StringUtil.isEmpty(response)) {
            handleFailure(statusCode, "返回的数据是空或null", null);
        } else {
            RESPONSE resp = null;
            try {
                resp = responseEntityClass.newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (resp != null) {
                new DataParserAsyncTask(statusCode).execute(response);
                return;
            } else {
                onFailure(statusCode, "Json转换失败", null);
            }
        }
    }


    private void handleFailure(int statusCode, String message, String response) {
        onFailure(statusCode,message,response == null ? null : (RESPONSE) response);
    }

    /**
     * 请求失败，指网络问题，如没有网络或网络慢等问题
     *
     * @param statusCode 状态码，如果没有网络返回-1，否则返回其它正常的网络响应码
     * @param message    响应消息文本
     * @param response   响应实体泛型
     */
    public abstract void onFailure(int statusCode, String message, RESPONSE response);
    /**
     * 请求成功，指服务端成功响应并返回正确的数据
     *
     * @param statusCode
     * @param response
     */
    public abstract void onSuccess(int statusCode, RESPONSE response);

    /**
     * 数据解析（将返回的数据转成实体类）处理异步任务
     */
    private class DataParserAsyncTask extends AsyncTask<String, Void, RESPONSE> {
        private int statusCode;
        private RESPONSE responseEntity;

        public DataParserAsyncTask(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        protected RESPONSE doInBackground(final String... params) {
            LogUtil.e("返回参数："+params[0]);
            responseEntity = JSON.parseObject(params[0],responseEntityClass);
            return responseEntity;
        }

        @Override
        protected void onPostExecute(RESPONSE result) {
            onSuccess(statusCode, responseEntity);
        }
    }
}
