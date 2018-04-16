package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6 0006.
 */

public class ServiceDetailInfoResult extends NormalResult{

    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String merchantlong;
        private String merchantlat;
        private String url;
        private String name;
        private String address;
        private String content;
        private String vaildtime;
        private String invildtime;
        private String phoneNum;//手机号码
        private String pubName;//发布人
        private String distance;
        private String inquiries;//咨询量
        private String traffic;//访问量

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getPubName() {
            return pubName;
        }

        public void setPubName(String pubName) {
            this.pubName = pubName;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getInquiries() {
            return inquiries;
        }

        public void setInquiries(String inquiries) {
            this.inquiries = inquiries;
        }

        public String getTraffic() {
            return traffic;
        }

        public void setTraffic(String traffic) {
            this.traffic = traffic;
        }

        public String getMerchantlong() {
            return merchantlong;
        }

        public void setMerchantlong(String merchantlong) {
            this.merchantlong = merchantlong;
        }

        public String getMerchantlat() {
            return merchantlat;
        }

        public void setMerchantlat(String merchantlat) {
            this.merchantlat = merchantlat;
        }
    }
}
