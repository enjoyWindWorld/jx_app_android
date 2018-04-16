package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class HomeWaterResult extends NormalResult {


    /**
     * data : [{"current_exponent":[{"current_exponent":"TDS:-"},{"current_exponent":"水质指数:-"}],"water_quality":[{"water_quality":"饮水量:0ML"},{"water_quality":"TDS:10"},{"water_quality":"水质指数:优秀"}]}]
     * errcode : 0
     * result : 0
     */


    private List<DataBean> data;




    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private List<CurrentExponentBean> current_exponent;
        private List<WaterQualityBean> water_quality;

        public List<CurrentExponentBean> getCurrent_exponent() {
            return current_exponent;
        }

        public void setCurrent_exponent(List<CurrentExponentBean> current_exponent) {
            this.current_exponent = current_exponent;
        }

        public List<WaterQualityBean> getWater_quality() {
            return water_quality;
        }

        public void setWater_quality(List<WaterQualityBean> water_quality) {
            this.water_quality = water_quality;
        }

        public static class CurrentExponentBean {
            /**
             * current_exponent : TDS:-
             */

            private String current_exponent;

            public String getCurrent_exponent() {
                return current_exponent;
            }

            public void setCurrent_exponent(String current_exponent) {
                this.current_exponent = current_exponent;
            }
        }

        public static class WaterQualityBean {
            /**
             * water_quality : 饮水量:0ML
             */

            private String water_quality;

            public String getWater_quality() {
                return water_quality;
            }

            public void setWater_quality(String water_quality) {
                this.water_quality = water_quality;
            }
        }
    }
}