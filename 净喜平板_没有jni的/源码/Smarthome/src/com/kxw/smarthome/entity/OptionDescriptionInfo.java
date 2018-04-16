package com.kxw.smarthome.entity;

import java.io.Serializable;

public class OptionDescriptionInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String param;
	private String netDate;
	private String localDate;
	private String option;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getNetDate() {
		return netDate;
	}
	public void setNetDate(String netDate) {
		this.netDate = netDate;
	}
	public String getLocalDate() {
		return localDate;
	}
	public void setLocalDate(String localDate) {
		this.localDate = localDate;
	}
	
}
