package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public class GeneraLizeResult extends NormalResult {

    /**
     * result : 0
     * errcode : 0
     * data : [{"pub_addtime":"2017-06-30 19:57:04.0","content":"啊啊\n","address":"北京市-东城区 天安门","pubuserid":"3","seller":"啊啊","url":"http://data.jx-inteligent.tech:15010/jx/1/0/063019561047761_1498823700603.jpg,","pubid":219}]
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
         * pub_addtime : 2017-06-30 19:57:04.0
         * content : 啊啊
         * address : 北京市-东城区 天安门
         * pubuserid : 3
         * seller : 啊啊
         * url : http://data.jx-inteligent.tech:15010/jx/1/0/063019561047761_1498823700603.jpg,
         * pubid : 219
         */

        private String pub_addtime;
        private String content;
        private String address;
        private String pubuserid;
        private String seller;
        private String url;
        private int pubid;

        public String getPub_addtime() {
            return pub_addtime;
        }

        public void setPub_addtime(String pub_addtime) {
            this.pub_addtime = pub_addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPubuserid() {
            return pubuserid;
        }

        public void setPubuserid(String pubuserid) {
            this.pubuserid = pubuserid;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPubid() {
            return pubid;
        }

        public void setPubid(int pubid) {
            this.pubid = pubid;
        }
    }
}
