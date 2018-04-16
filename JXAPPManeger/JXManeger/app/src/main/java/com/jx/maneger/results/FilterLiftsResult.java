package com.jx.maneger.results;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class FilterLiftsResult extends NormalResult {

    private List<FilterLiftInfo> data;

    public List<FilterLiftInfo> getData() {
        return data;
    }

    public void setData(List<FilterLiftInfo> data) {
        this.data = data;
    }

    public class FilterLiftInfo
    {
        private String ord_receivename;
        private String ord_phone;
        private String adr_id;
        private String ord_protypeid;
        private String pro_restflow;
        private String pro_day;
        private String pro_no;
        private String color;
        private String pro_id;
        private String ord_managerno;
        private String ord_no;
        private String name;
        private String url;
        private String pro_alias;
        private List<FilterInfo> Filter_state;

        public String getOrd_receivename() {
            return ord_receivename;
        }

        public void setOrd_receivename(String ord_receivename) {
            this.ord_receivename = ord_receivename;
        }

        public String getOrd_phone() {
            return ord_phone;
        }

        public void setOrd_phone(String ord_phone) {
            this.ord_phone = ord_phone;
        }

        public String getAdr_id() {
            return adr_id;
        }

        public void setAdr_id(String adr_id) {
            this.adr_id = adr_id;
        }

        public String getOrd_protypeid() {
            return ord_protypeid;
        }

        public void setOrd_protypeid(String ord_protypeid) {
            this.ord_protypeid = ord_protypeid;
        }

        public String getPro_restflow() {
            return pro_restflow;
        }

        public void setPro_restflow(String pro_restflow) {
            this.pro_restflow = pro_restflow;
        }

        public String getPro_day() {
            return pro_day;
        }

        public void setPro_day(String pro_day) {
            this.pro_day = pro_day;
        }

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

        public List<FilterInfo> getFilter_state() {
            return Filter_state;
        }

        public void setFilter_state(List<FilterInfo> filter_state) {
            Filter_state = filter_state;
        }

        public class FilterInfo
        {
            private String proportion;
            private String proflt_life;
            private String name;

            public String getProportion() {
                return proportion;
            }

            public void setProportion(String proportion) {
                this.proportion = proportion;
            }

            public String getProflt_life() {
                return proflt_life;
            }

            public void setProflt_life(String proflt_life) {
                this.proflt_life = proflt_life;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
