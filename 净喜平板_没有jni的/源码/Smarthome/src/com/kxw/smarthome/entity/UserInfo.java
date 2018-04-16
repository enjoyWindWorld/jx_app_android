package com.kxw.smarthome.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "UserInfo")
public class UserInfo {
	
	@Column(name = "_id",isId = true, autoGen = true)
	public int _id;
	
	@Column(name = "pro_no")
	public String pro_no; //设备码，即UUID
	
	@Column(name = "order_no")
	public String order_no;
	
	@Column(name = "pay_proid")
	public int pay_proid;
	
	@Column(name = "quantity")
	public double quantity;

	@Column(name = "day")
	public int day;

	@Column(name = "end")
	public int end;
	
	@Column(name = "now")
	public String now;
	
	@Column(name = "proname")
	public String proname;
	
	public String getPro_no() {
		return pro_no;
	}

	public void setPro_no(String pro_no) {
		this.pro_no = pro_no;
	}

	public String getOrder_no() {
		return order_no;
	}
	
	
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	
	public int getPay_proid() {
		return pay_proid;
	}

	public void setPay_proid(int pay_proid) {
		this.pay_proid = pay_proid;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public String getNow() {
		return now;
	}

	public void setNow(String now) {
		this.now = now;
	}

	@Override
	public String toString() {
		return "UserInfo [_id=" + _id + ", pro_no=" + pro_no + ", order_no="
				+ order_no + ", pay_proid=" + pay_proid + ", quantity="
				+ quantity + ", end=" + end + ", now=" + now + ", proname=" + proname + "]";
	}
	
}
