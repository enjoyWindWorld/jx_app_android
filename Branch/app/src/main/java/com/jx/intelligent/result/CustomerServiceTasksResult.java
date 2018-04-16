package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class CustomerServiceTasksResult extends NormalResult {

    List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data
    {
        private String id;
        private String fas_state;
        private String fas_type;
        private String fas_addtime;
        private String fas_modtime;
        private String specific_reason;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSpecific_reason() {
            return specific_reason;
        }

        public void setSpecific_reason(String specific_reason) {
            this.specific_reason = specific_reason;
        }
    }
}
