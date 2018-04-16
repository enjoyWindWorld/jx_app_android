package com.jx.maneger.results;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class CustomerServiceTasksReult extends NormalResult {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data
    {
        private String id;
        private String make_time;
        private String contact_person;
        private String contact_way;
        private String user_address;
        private String address_details;
        private String fas_state;
        private String fas_type;
        private String fas_addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}
