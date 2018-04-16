package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5 0005.
 */

public class ServicesResult extends NormalResult{

    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String typename;
        private String pubId;
        private String name;
        private String seller;
        private String address;
        private String vaildtime;
        private String invildtime;
        private String url;
        private String content;
        private String pub_addtime;

        public String getPubId() {
            return pubId;
        }

        public void setPubId(String pubId) {
            this.pubId = pubId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getVaildtime() {
            return vaildtime;
        }

        public void setVaildtime(String vaildtime) {
            this.vaildtime = vaildtime;
        }

        public String getInvildtime() {
            return invildtime;
        }

        public void setInvildtime(String invildtime) {
            this.invildtime = invildtime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getPub_addtime() {
            return pub_addtime;
        }

        public void setPub_addtime(String pub_addtime) {
            this.pub_addtime = pub_addtime;
        }
    }

}
