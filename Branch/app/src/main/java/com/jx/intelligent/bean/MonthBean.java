package com.jx.intelligent.bean;

/**
 *日期选择控件的封装类
 */
public class MonthBean{
    private String dayOfMonth;
    private int day;
    private boolean checked;
    private boolean checkCurrTime;

    public MonthBean(int day, boolean checked , boolean checkCurrTime) {
        this.day = day;
        this.checked = checked;
        this.checkCurrTime = checkCurrTime;
    }

    public boolean isCheckCurrTime() {
        return checkCurrTime;
    }

    public void setCheckCurrTime(boolean checkCurrTime) {
        this.checkCurrTime = checkCurrTime;
    }

    public MonthBean(String dayOfMonth, boolean checked) {
        this.dayOfMonth = dayOfMonth;
        this.checked = checked;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "MonthBean{" +
                "dayOfMonth='" + dayOfMonth + '\'' +
                ", day=" + day +
                ", checked=" + checked +
                '}';
    }
}