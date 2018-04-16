package com.jx.maneger.http.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jx.maneger.http.callback.HttpResponseCallback;
import com.jx.maneger.util.HTTPSUtils;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public abstract class OkHttpDao<RESPONSE> extends HttpDao<RESPONSE> {
    /**
     * okHttp客户端
     */
    private static OkHttpClient httpClient;

    /**
     * 当前正在执行的请求
     */
    private Call call;

    /**
     * 保存正在执行的请求
     */
    private List<Call> requests = new ArrayList<>();

    /**
     * 用于处理JSON参数
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 用于处理文件表单上传参数
     */
    public static final MediaType FORM = MediaType.parse("image/jpg");

    /**
     * 用于处理String参数,只有键没有值的单个String参数
     */
    public static final MediaType STRING = MediaType.parse("text/x-markdown; charset=utf-8");

    public static final void getInstance(Context context, InputStream inputStream) {
        httpClient = HttpClient.getInstance(); //没有https验证的
//        httpClient = HTTPSUtils.getInstance(context, inputStream).getClient(); //有https验证的
    }

    /**
     * 单例模式获取HTTP客户端
     */
    private static class HttpClient {
        private static OkHttpClient instance;

        /**
         * 获取OkHttpClient实例
         *
         * @return OkHttpClient实例对象
         */
        public static OkHttpClient getInstance() {
            if (instance == null) {
                synchronized (HttpClient.class) {
                    if (instance == null) {
                        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                        instance = builder.build();
                    }
                }
            }
            return instance;
        }
    }

    /**
     * 设置HttpResponseCallback相关参数
     *
     * @param responseEntityClass
     * @param httpResponseCallback
     */
    private void setHttpResponseCallback(Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (responseEntityClass != null) {
            httpResponseCallback.setResponseEntityClass(responseEntityClass);
        }
    }


    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url) {
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> params) {
        return null;
    }

    @Override
    public RESPONSE sendSyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull String params) {
        return null;
    }

    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;

        setHttpResponseCallback(null, httpResponseCallback);

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                Request.Builder builder = new Request.Builder();
                builder = setRequestHeader(builder); //设置请求头,没有则不设置
                final Request request_get = builder.url(url).get().build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().build();
                Request.Builder postBuilder = new Request.Builder();
                postBuilder = setRequestHeader(postBuilder);
                final Request request_post = postBuilder.url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    /**
     * 上传表单文件
     *
     * @param url                  统一资源定位符
     * @param param                请求参数json字符串
     * @param value                请求参数值文件
     * @param responseEntityClass
     * @param httpResponseCallback HTTP响应回调
     */
    @Override
    public void sendAsyncRequest(@NonNull String url, @NonNull String param, @NonNull File value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;

        setHttpResponseCallback(responseEntityClass, httpResponseCallback);
        // 提交post表单请求
        MultipartBody.Builder partBuilder = new MultipartBody.Builder();
        partBuilder.addFormDataPart("formParam", param);
        partBuilder.addFormDataPart("formFile", value.getName(), RequestBody.create(FORM, value));
        final RequestBody formBody = partBuilder.build();
        Request.Builder postBuilder = new Request.Builder();
        postBuilder = setRequestHeader(postBuilder);//添加请求头
        final Request request_post = postBuilder.url(url).post(formBody).build();
        call = httpClient.newCall(request_post);
        addRequest(call);
        call.enqueue(httpResponseCallback);
    }
    /**无参*/
    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;

        setHttpResponseCallback(responseEntityClass, httpResponseCallback);
        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                Request.Builder builder = new Request.Builder();
                builder = setRequestHeader(builder); //设置请求头,没有则不设置
                final Request request_get = builder.url(url).get().build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                // 提交post表单请求
                final RequestBody formBody = new FormBody.Builder().build();
                Request.Builder postBuilder = new Request.Builder();
                postBuilder = setRequestHeader(postBuilder);
                final Request request_post = postBuilder.url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    /**
     * 请求参数为键值对
     */
    @Override
    public void sendAsyncRequest(@NonNull HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;

        setHttpResponseCallback(responseEntityClass, httpResponseCallback);

        // 判断HTTP请求方法
        switch (httpMethod) {
            case GET:
                final Request request_get = new Request.Builder().url(buildGetUrl(url, params)).build();
                call = httpClient.newCall(request_get);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
            case POST:
                final FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
                }
                // 提交post表单请求
                final RequestBody formBody = formBodyBuilder.build();
                Request.Builder postBuilder = new Request.Builder();
                postBuilder = setRequestHeader(postBuilder);
                final Request request_post = postBuilder.url(url).post(formBody).build();
                call = httpClient.newCall(request_post);
                addRequest(call);
                call.enqueue(httpResponseCallback);
                break;
        }
    }

    /**
     * @param url                  统一资源定位符
     * @param json                 请求参数为json字符串
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    @Override
    public void sendAsyncRequest(@NonNull String url, @NonNull String json, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback) {
        LogUtil.e("请求参数："+json);
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;

        setHttpResponseCallback(responseEntityClass, httpResponseCallback);
        RequestBody body = RequestBody.create(JSON, StringUtil.encrypt(json));
        Request.Builder postBuilder = new Request.Builder();
        postBuilder = setRequestHeader(postBuilder);
        Request request_post = postBuilder.url(url).post(body).build();
        call = httpClient.newCall(request_post);
        addRequest(call);
        call.enqueue(httpResponseCallback);
    }

    @Override
    public void sendAsyncRequestString(@NonNull String url, @NonNull String params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback){
        LogUtil.e("请求参数："+params);
        if (!checkCurrentNetwork(url, httpResponseCallback))
            return;

        setHttpResponseCallback(responseEntityClass, httpResponseCallback);
        RequestBody requestBody = RequestBody.create(STRING, params);
        Request request_post = new Request.Builder().url(url).post(requestBody).build();
        call = httpClient.newCall(request_post);
        addRequest(call);
        call.enqueue(httpResponseCallback);
    }
    /**
     * 设置网络请求的请求头
     *
     * @param builder
     * @return
     */
    protected abstract Request.Builder setRequestHeader(Request.Builder builder);

    /**
     * 针对get请求，通过url和参数构建带参数的url
     *
     * @param url    最基本的url
     * @param params 请求的参数
     * @return 添加了参数的url
     */
    private String buildGetUrl(String url, Map<String, Object> params) {
        return url + buildParams(params);
    }

    /**
     * 构建GET请求参数
     *
     * @param params 请求参数Map键值对
     * @return 拼接的GET请求参数
     */
    private String buildParams(@NonNull Map<String, Object> params) {
        final StringBuilder sb = new StringBuilder("?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    @Override
    public void cancelRequests() {
        for (Call call : requests) {
            if (!call.isCanceled())
                call.cancel();
        }
        requests.clear();
        requests = null;
    }


    @Override
    public void cancelRequest() {
        if (!call.isCanceled())
            call.cancel();
        call = null;
    }

    @Override
    public void cancelRequest(@NonNull String tag) {

    }

    private void addRequest(@NonNull Call call) {
        if (call != null && !requests.contains(call)) {
            requests.add(call);
        }
    }
}
