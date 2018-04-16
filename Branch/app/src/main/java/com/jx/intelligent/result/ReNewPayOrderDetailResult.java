package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class ReNewPayOrderDetailResult extends NormalResult{

    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{

        private List<Paytype> paytype = new ArrayList<Paytype>();
        private List<OrderDetail> orderdetail = new ArrayList<>();

        public List<Paytype> getPaytype() {
            return paytype;
        }

        public void setPaytype(List<Paytype> paytype) {
            this.paytype = paytype;
        }

        public List<OrderDetail> getOrderdetail() {
            return orderdetail;
        }

        public void setOrderdetail(List<OrderDetail> orderdetail) {
            this.orderdetail = orderdetail;
        }

        public class Paytype{
            private String paytype;
            private String price;

            public String getPaytype() {
                return paytype;
            }

            public void setPaytype(String paytype) {
                this.paytype = paytype;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }

        public class OrderDetail{
            private String ordNo;
            private String name;
            private String uname;
            private String address;
            private String color;
            private String url;
            private String paytype;
            private String isagain;
            private String phone;
            private String serttime;
            private String way;
            private String price;
            private String status;
            private String proname;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getProname() {
                return proname;
            }

            public void setProname(String proname) {
                this.proname = proname;
            }
        }
    }
}
