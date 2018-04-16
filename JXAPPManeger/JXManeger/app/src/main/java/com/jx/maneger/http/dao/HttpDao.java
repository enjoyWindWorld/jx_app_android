package com.jx.maneger.http.dao;

import android.support.annotation.NonNull;

import com.jx.maneger.R;
import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.util.NetUtil;
import com.jx.maneger.util.UIUtil;


public abstract class HttpDao<RESPONSE> implements IHttpDao<RESPONSE> {
    @Override
    public boolean checkCurrentNetwork(@NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if(!NetUtil.isNetworkConnected(false)){
            httpResponseCallback.onFailure( -1, UIUtil.getString(R.string.text_network_require), null);
            return false;
        }
        return true;
    }
    /**
     * HTTP请求谓词枚举
     */
    public enum HttpMethod {
        GET, POST, PUT, GET_DATA_URL, DELETE
    }

}
