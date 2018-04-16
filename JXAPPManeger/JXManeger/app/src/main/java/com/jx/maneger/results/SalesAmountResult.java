package com.jx.maneger.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class SalesAmountResult extends BaseResult {

    List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable
    {
        private int vertical;//立式
        private int desktop;//台式
        private int wall;//壁挂式
        private int wall_renew;//壁挂式续费
        private int vertical_renew;//立式续费
        private int desktop_renew;//台式续费
        private float withdrawal_total_amount;//提现总金额
        private float installation;//安装费返利
        private float lower_rebate;//下级返利
        private int isshop;//是否建店：0：否 1：是
        private float service_fee;//服务费返利
        private int ispact;//是否按照合同
        private float build_store;//建店返利
        private float renewal ;//服务费续费返利
        private int state;//0：审核操作， -1：提现操作
        private float wdl_fee;//返利比例
        private float rwl_install;//安装费比例
        private String withdrawal_order_no;//提现单号
        private String user_number;//提现人编号
        private String sales_time;//提现时间
        private float buy_combined;//购买型合计（去押金）
        private float renewal_combined;//续费型合计（去押金）


        private List<Subordinate> direct_subordinates = new ArrayList<>();

        public List<Subordinate> getDirect_subordinates() {
            return direct_subordinates;
        }

        public void setDirect_subordinates(List<Subordinate> direct_subordinates) {
            this.direct_subordinates = direct_subordinates;
        }

        public int getVertical() {
            return vertical;
        }

        public void setVertical(int vertical) {
            this.vertical = vertical;
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

        public float getWithdrawal_total_amount() {
            return withdrawal_total_amount;
        }

        public void setWithdrawal_total_amount(float withdrawal_total_amount) {
            this.withdrawal_total_amount = withdrawal_total_amount;
        }

        public float getInstallation() {
            return installation;
        }

        public void setInstallation(float installation) {
            this.installation = installation;
        }

        public float getLower_rebate() {
            return lower_rebate;
        }

        public void setLower_rebate(float lower_rebate) {
            this.lower_rebate = lower_rebate;
        }

        public int getDesktop_renew() {
            return desktop_renew;
        }

        public void setDesktop_renew(int desktop_renew) {
            this.desktop_renew = desktop_renew;
        }

        public int getIsshop() {
            return isshop;
        }

        public void setIsshop(int isshop) {
            this.isshop = isshop;
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

        public float getBuild_store() {
            return build_store;
        }

        public void setBuild_store(float build_store) {
            this.build_store = build_store;
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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public float getWdl_fee() {
            return wdl_fee;
        }

        public void setWdl_fee(float wdl_fee) {
            this.wdl_fee = wdl_fee;
        }

        public float getRwl_install() {
            return rwl_install;
        }

        public void setRwl_install(float rwl_install) {
            this.rwl_install = rwl_install;
        }

        public String getWithdrawal_order_no() {
            return withdrawal_order_no;
        }

        public void setWithdrawal_order_no(String withdrawal_order_no) {
            this.withdrawal_order_no = withdrawal_order_no;
        }

        public String getUser_number() {
            return user_number;
        }

        public void setUser_number(String user_number) {
            this.user_number = user_number;
        }

        public String getSales_time() {
            return sales_time;
        }

        public void setSales_time(String sales_time) {
            this.sales_time = sales_time;
        }

        public float getBuy_combined() {
            return buy_combined;
        }

        public void setBuy_combined(float buy_combined) {
            this.buy_combined = buy_combined;
        }

        public float getRenewal_combined() {
            return renewal_combined;
        }

        public void setRenewal_combined(float renewal_combined) {
            this.renewal_combined = renewal_combined;
        }

        public class Subordinate implements Serializable {
            private String id;
            private String number;
            private String name;
            private float money;
            private float rebates;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public float getMoney() {
                return money;
            }

            public void setMoney(float money) {
                this.money = money;
            }

            public float getRebates() {
                return rebates;
            }

            public void setRebates(float rebates) {
                this.rebates = rebates;
            }
        }
    }
}
