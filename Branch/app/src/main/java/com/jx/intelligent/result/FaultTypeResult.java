package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class FaultTypeResult extends NormalResult {

    private List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data
    {
        private String id;
        private String fault_cause;
        private String is_shelves;
        private String sb_addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFault_cause() {
            return fault_cause;
        }

        public void setFault_cause(String fault_cause) {
            this.fault_cause = fault_cause;
        }

        public String getIs_shelves() {
            return is_shelves;
        }

        public void setIs_shelves(String is_shelves) {
            this.is_shelves = is_shelves;
        }

        public String getSb_addtime() {
            return sb_addtime;
        }

        public void setSb_addtime(String sb_addtime) {
            this.sb_addtime = sb_addtime;
        }
    }
}
