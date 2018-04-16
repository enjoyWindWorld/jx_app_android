package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17 0017.
 */

public class ServicePriceResult extends NormalResult{
    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String productId;
        private String ord_no;
        private String name;
        private String phone;
        private String pro_no;
        private String ord_price;
        private String pro_addtime;
        private String pro_invalidtime;
        private String pro_hasflow;
        private String pro_restflow;
        private String type;
        private String pro_name;
        private String ord_color;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getOrd_no() {
            return ord_no;
        }

        public void setOrd_no(String ord_no) {
            this.ord_no = ord_no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }



        public String getPro_no() {
            return pro_no;
        }

        public void setPro_no(String pro_no) {
            this.pro_no = pro_no;
        }

        public String getOrd_price() {
            return ord_price;
        }

        public void setOrd_price(String ord_price) {
            this.ord_price = ord_price;
        }

        public String getPro_addtime() {
            return pro_addtime;
        }

        public void setPro_addtime(String pro_addtime) {
            this.pro_addtime = pro_addtime;
        }

        public String getPro_invalidtime() {
            return pro_invalidtime;
        }

        public void setPro_invalidtime(String pro_invalidtime) {
            this.pro_invalidtime = pro_invalidtime;
        }

        public String getPro_hasflow() {
            return pro_hasflow;
        }

        public void setPro_hasflow(String pro_hasflow) {
            this.pro_hasflow = pro_hasflow;
        }

        public String getPro_restflow() {
            return pro_restflow;
        }

        public void setPro_restflow(String pro_restflow) {
            this.pro_restflow = pro_restflow;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPro_name() {
            return pro_name;
        }

        public void setPro_name(String pro_name) {
            this.pro_name = pro_name;
        }

        public String getOrd_color() {
            return ord_color;
        }

        public void setOrd_color(String ord_color) {
            this.ord_color = ord_color;
        }
    }
}
