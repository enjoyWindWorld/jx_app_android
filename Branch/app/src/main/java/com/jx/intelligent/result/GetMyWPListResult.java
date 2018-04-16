package com.jx.intelligent.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetMyWPListResult extends NormalResult {

    private List<WaterPurifierBean> data = new ArrayList<WaterPurifierBean>();

    public List<WaterPurifierBean> getData() {
        return data;
    }

    public void setData(List<WaterPurifierBean> data) {
        this.data = data;
    }

    public class WaterPurifierBean implements Serializable{
        private String pro_no;
        private String color;
        private String name;
        private String ord_protypeid;
        private String url;
        private String pro_alias;

        public String getPro_no() {
            return pro_no;
        }

        public void setPro_no(String pro_no) {
            this.pro_no = pro_no;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrd_protypeid() {
            return ord_protypeid;
        }

        public void setOrd_protypeid(String ord_protypeid) {
            this.ord_protypeid = ord_protypeid;
        }
        public String getPro_alias() {
            return pro_alias;
        }

        public void setPro_alias(String pro_alias) {
            this.pro_alias = pro_alias;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
