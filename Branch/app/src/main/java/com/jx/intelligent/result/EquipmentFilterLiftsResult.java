package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class EquipmentFilterLiftsResult extends NormalResult {

    private List<EquipmentFilterLiftInfo> data = new ArrayList<>();

    public List<EquipmentFilterLiftInfo> getData() {
        return data;
    }

    public void setData(List<EquipmentFilterLiftInfo> data) {
        this.data = data;
    }

    public class EquipmentFilterLiftInfo{
        private String proportion;
        private String proflt_life;
        private String name;
        private boolean isCheck;

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

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
