package com.jx.maneger.results;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class HomeTextResult extends NormalResult {

    /**
     * data : [{"news_content":"三生三世打的电毒贩夫妇","news_type":1,"news_type_name":"水之文章","news_url":"ww.baidu.com"},{"news_content":"jsaasnkasdjka","news_type":1,"news_type_name":"水之文章","news_url":"www.hao123.com"},{"news_content":"近年来污染越来越严重","news_type":2,"news_type_name":"水之报道","news_url":"www.163.com"},{"news_content":"xxxxxxssssssss","news_type":2,"news_type_name":"水之报道","news_url":"www.taobao.com"},{"news_content":"现场晚上的方法","news_type":3,"news_type_name":"水之地图","news_url":"www.youku.com"}]
     * errcode : 0
     * result : 0
     */


    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * news_content : 三生三世打的电毒贩夫妇
         * news_type : 1
         * news_type_name : 水之文章
         * news_url : ww.baidu.com
         */

        private String news_content;
        private int news_type;
        private String news_type_name;
        private String news_url;

        public String getNews_content() {
            return news_content;
        }

        public void setNews_content(String news_content) {
            this.news_content = news_content;
        }

        public int getNews_type() {
            return news_type;
        }

        public void setNews_type(int news_type) {
            this.news_type = news_type;
        }

        public String getNews_type_name() {
            return news_type_name;
        }

        public void setNews_type_name(String news_type_name) {
            this.news_type_name = news_type_name;
        }

        public String getNews_url() {
            return news_url;
        }

        public void setNews_url(String news_url) {
            this.news_url = news_url;
        }
    }
}
