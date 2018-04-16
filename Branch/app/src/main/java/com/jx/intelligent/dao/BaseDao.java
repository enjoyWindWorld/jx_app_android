package com.jx.intelligent.dao;


import com.jx.intelligent.http.dao.OkHttpDao;

import okhttp3.Request;

/**
 *  网络请求的基类,其他业务层只需集成该类
 */

public class BaseDao extends OkHttpDao {
    //王云提交了

    @Override
    protected Request.Builder setRequestHeader(Request.Builder builder) {
//        long time = System.currentTimeMillis();
//        SimpleDateFormat format = new SimpleDateFormat(
//                "MM/dd/yyyy HH:mm:ss");
//        builder = builder.addHeader("authorization", "60606102:"
//                + EncodeUtil.Md5Encode("POST"
//                + ""
//                + format.format(time)
//                + ""
////                + Constant.singkey
//                + ""
//                + EncodeUtil.Md5Encode("31d7e9facddf4313834367da1040f6ba")))
////                .addHeader("signKey", Constant.singkey)
//                .addHeader("date", format.format(time).toString())
//                .addHeader("clientOS", "Android")
//                .addHeader("appName", "driver");
        builder.addHeader("Connection", "close");
        return builder;

    }
}
