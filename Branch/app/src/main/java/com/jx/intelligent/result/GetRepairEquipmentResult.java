package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class GetRepairEquipmentResult extends NormalResult{

    List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String pro_no;
        private String color;
        private String pro_id;
        private String ord_managerno;
        private String ord_no;
        private String name;
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

        public String getPro_id() {
            return pro_id;
        }

        public void setPro_id(String pro_id) {
            this.pro_id = pro_id;
        }

        public String getOrd_managerno() {
            return ord_managerno;
        }

        public void setOrd_managerno(String ord_managerno) {
            this.ord_managerno = ord_managerno;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPro_alias() {
            return pro_alias;
        }

        public void setPro_alias(String pro_alias) {
            this.pro_alias = pro_alias;
        }
    }
}
