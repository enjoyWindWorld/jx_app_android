package com.jx.maneger.results;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class SubWithdrawalDetailResult extends BaseResult{

    private List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String sales_time;
        private int vertical;
        private String withdrawal_order_no;
        private int desktop_renew;
        private float service_fee;
        private int vertical_renew;
        private int ispact;
        private int desktop;
        private int wall_renew;
        private float renewal;
        private int wall;
        private float withdrawal_total_amount;
        private float by_tkr_rebates;
        private float total_money;

        public String getSales_time() {
            return sales_time;
        }

        public void setSales_time(String sales_time) {
            this.sales_time = sales_time;
        }

        public int getVertical() {
            return vertical;
        }

        public void setVertical(int vertical) {
            this.vertical = vertical;
        }

        public String getWithdrawal_order_no() {
            return withdrawal_order_no;
        }

        public void setWithdrawal_order_no(String withdrawal_order_no) {
            this.withdrawal_order_no = withdrawal_order_no;
        }

        public int getDesktop_renew() {
            return desktop_renew;
        }

        public void setDesktop_renew(int desktop_renew) {
            this.desktop_renew = desktop_renew;
        }

        public float getService_fee() {
            return service_fee;
        }

        public void setService_fee(float service_fee) {
            this.service_fee = service_fee;
        }

        public int getVertical_renew() {
            return vertical_renew;
        }

        public void setVertical_renew(int vertical_renew) {
            this.vertical_renew = vertical_renew;
        }

        public int getIspact() {
            return ispact;
        }

        public void setIspact(int ispact) {
            this.ispact = ispact;
        }

        public int getDesktop() {
            return desktop;
        }

        public void setDesktop(int desktop) {
            this.desktop = desktop;
        }

        public int getWall_renew() {
            return wall_renew;
        }

        public void setWall_renew(int wall_renew) {
            this.wall_renew = wall_renew;
        }

        public float getRenewal() {
            return renewal;
        }

        public void setRenewal(float renewal) {
            this.renewal = renewal;
        }

        public int getWall() {
            return wall;
        }

        public void setWall(int wall) {
            this.wall = wall;
        }

        public float getWithdrawal_total_amount() {
            return withdrawal_total_amount;
        }

        public void setWithdrawal_total_amount(float withdrawal_total_amount) {
            this.withdrawal_total_amount = withdrawal_total_amount;
        }

        public float getBy_tkr_rebates() {
            return by_tkr_rebates;
        }

        public void setBy_tkr_rebates(float by_tkr_rebates) {
            this.by_tkr_rebates = by_tkr_rebates;
        }

        public float getTotal_money() {
            return total_money;
        }

        public void setTotal_money(float total_money) {
            this.total_money = total_money;
        }
    }
}
