package com.jx.maneger.http.dao;

import android.support.annotation.NonNull;

import com.jx.maneger.http.callback.HttpResponseCallback;

import java.io.File;
import java.util.Map;


/**
 * @创建者： zhangbo
 * @项目名: CommonFramework
 * @包名: com.ronghui.common_libray.http.dao
 * @创建时间： 2016/8/16 16:20
 * @描述： ${TODO} HTTP业务模块接口
 */

public interface IHttpDao<RESPONSE> {
    /**
     * 检查当前网络是否可用
     *
     * @param url                  统一资源定位符
     * @param httpResponseCallback 请求响应回调类
     * @return 可用返回true，否则返回false
     */
    boolean checkCurrentNetwork(@NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送同步请求 无参
     *
     * @param url 统一资源定位符
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpDao.HttpMethod httpMethod, @NonNull String url);


    /**
     * 发送同步请求 键值对
     *
     * @param url    统一资源定位符
     * @param params 请求参数名称和值Map键值对
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpDao.HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> params);

    /**
     * 发送同步请求 json
     *
     * @param url    统一资源定位符
     * @param params 请求参数json字符串
     * @return 成功返回HTTP响应数据解析对应的实体类，也可以直接返回String，失败返回null
     */
    RESPONSE sendSyncRequest(@NonNull HttpDao.HttpMethod httpMethod, @NonNull String url, @NonNull String params);


    void sendAsyncRequest(@NonNull HttpDao.HttpMethod httpMethod, @NonNull String url, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求 键值对上传文件
     *
     * @param url                  统一资源定位符
     * @param param                请求参数json字符串
     * @param value                请求参数值文件
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull String url, @NonNull String param, @NonNull File value, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求 无参
     *
     * @param url                  统一资源定位符
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpDao.HttpMethod httpMethod, @NonNull String url, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);


    /**
     * 发送异步请求 键值对
     *
     * @param url                  统一资源定位符
     * @param param                请求参数名称和值
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull HttpDao.HttpMethod httpMethod, @NonNull String url, @NonNull Map<String, Object> param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求 json
     *
     * @param url                  统一资源定位符
     * @param param                请求参数为json字符串
     * @param responseEntityClass  HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequest(@NonNull String url, @NonNull String param, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);

    /**
     * 发送异步请求 单个string
     *
     * @param url 统一资源定位符
     * @param params 请求参数为单个字符串
     * @param responseEntityClass HTTP响应数据解析对应的实体类的class字节码
     * @param httpResponseCallback HTTP响应回调
     */
    void sendAsyncRequestString(@NonNull String url, @NonNull String params, @NonNull Class<RESPONSE> responseEntityClass, @NonNull HttpResponseCallback<RESPONSE> httpResponseCallback);
    /**
     * 取消所有的HTTP请求
     */
    void cancelRequests();

    /**
     * 取消当前正在执行的HTTP请求
     */
    void cancelRequest();

    /**
     * 取消指定tag的HTTP请求
     *
     * @param tag 请求标识
     */
    void cancelRequest(@NonNull String tag);

}
