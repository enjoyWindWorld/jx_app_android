package com.jx.maneger.results;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12 0012.
 */

public class OrderDetailResult extends NormalResult{
    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String ordNo;
        private String name;
        private String uname;
        private String address;
        private String color;
        private String phone;
        private String serttime;
        private String ord_modtime;
        private String way;
        private float price;
        private String status;
        private String url;
        private String paytype;
        private String isagain;
        private String tag;

        public String getOrdNo() {
            return ordNo;
        }

        public void setOrdNo(String ordNo) {
            this.ordNo = ordNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSerttime() {
            return serttime;
        }

        public void setSerttime(String serttime) {
            this.serttime = serttime;
        }

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getIsagain() {
            return isagain;
        }

        public void setIsagain(String isagain) {
            this.isagain = isagain;
        }

        public String getOrd_modtime() {
            return ord_modtime;
        }

        public void setOrd_modtime(String ord_modtime) {
            this.ord_modtime = ord_modtime;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
