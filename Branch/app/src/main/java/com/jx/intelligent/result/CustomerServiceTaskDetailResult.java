package com.jx.intelligent.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class CustomerServiceTaskDetailResult extends NormalResult {

    private List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable{
        private String id;
        private String pro_id;
        private String u_id;
        private String ord_color;
        private String pro_no;
        private String ord_no;
        private String pro_name;
        private String proflt_life;
        private String filter_name;
        private String make_time;
        private String contact_person;
        private String contact_way;
        private String user_address;
        private String address_details;
        private String fault_cause;
        private String specific_reason;
        private String fautl_url;
        private String ord_managerno;
        private String fas_state;
        private String fas_type;
        private String fas_addtime;
        private String fas_modtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPro_id() {
            return pro_id;
        }

        public void setPro_id(String pro_id) {
            this.pro_id = pro_id;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getOrd_color() {
            return ord_color;
        }

        public void setOrd_color(String ord_color) {
            this.ord_color = ord_color;
        }

        public String getPro_no() {
            return pro_no;
        }

        public void setPro_no(String pro_no) {
            this.pro_no = pro_no;
        }

        public String getOrd_no() {
            return ord_no;
        }

        public void setOrd_no(String ord_no) {
            this.ord_no = ord_no;
        }

        public String getPro_name() {
            return pro_name;
        }

        public void setPro_name(String pro_name) {
            this.pro_name = pro_name;
        }

        public String getProflt_life() {
            return proflt_life;
        }

        public void setProflt_life(String proflt_life) {
            this.proflt_life = proflt_life;
        }

        public String getFilter_name() {
            return filter_name;
        }

        public void setFilter_name(String filter_name) {
            this.filter_name = filter_name;
        }

        public String getMake_time() {
            return make_time;
        }

        public void setMake_time(String make_time) {
            this.make_time = make_time;
        }

        public String getContact_person() {
            return contact_person;
        }

        public void setContact_person(String contact_person) {
            this.contact_person = contact_person;
        }

        public String getContact_way() {
            return contact_way;
        }

        public void setContact_way(String contact_way) {
            this.contact_way = contact_way;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public String getAddress_details() {
            return address_details;
        }

        public void setAddress_details(String address_details) {
            this.address_details = address_details;
        }

        public String getFault_cause() {
            return fault_cause;
        }

        public void setFault_cause(String fault_cause) {
            this.fault_cause = fault_cause;
        }

        public String getSpecific_reason() {
            return specific_reason;
        }

        public void setSpecific_reason(String specific_reason) {
            this.specific_reason = specific_reason;
        }

        public String getFautl_url() {
            return fautl_url;
        }

        public void setFautl_url(String fautl_url) {
            this.fautl_url = fautl_url;
        }

        public String getOrd_managerno() {
            return ord_managerno;
        }

        public void setOrd_managerno(String ord_managerno) {
            this.ord_managerno = ord_managerno;
        }

        public String getFas_state() {
            return fas_state;
        }

        public void setFas_state(String fas_state) {
            this.fas_state = fas_state;
        }

        public String getFas_type() {
            return fas_type;
        }

        public void setFas_type(String fas_type) {
            this.fas_type = fas_type;
        }

        public String getFas_addtime() {
            return fas_addtime;
        }

        public void setFas_addtime(String fas_addtime) {
            this.fas_addtime = fas_addtime;
        }

        public String getFas_modtime() {
            return fas_modtime;
        }

        public void setFas_modtime(String fas_modtime) {
            this.fas_modtime = fas_modtime;
        }
    }
}
