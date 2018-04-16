package com.kxw.smarthome.entity;

import java.util.List;

/**
 * 对应机器的滤芯寿命和对应省份的滤芯寿命
 * @author Administrator
 *
 */
public class OldFilterLifeInfo {
	List<FilterLifeInfo> code;
	List<FilterLifeInfo> pro_no;
	public List<FilterLifeInfo> getCode() {
		return code;
	}
	public void setCode(List<FilterLifeInfo> code) {
		this.code = code;
	}
	public List<FilterLifeInfo> getPro_no() {
		return pro_no;
	}
	public void setPro_no(List<FilterLifeInfo> pro_no) {
		this.pro_no = pro_no;
	}
}
