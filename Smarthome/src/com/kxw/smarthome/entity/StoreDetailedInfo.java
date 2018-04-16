package com.kxw.smarthome.entity;



public class StoreDetailedInfo {
	
	public String url;
	
	public String name;
	
	public String content;
	
	public String vaildtime;
	
	public String invildtime;
	 
	public String address;
	
	public String phoneNum;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url=url; 
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name; 
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content=content; 
	}
	
	public String getVaildtime() {
		return vaildtime;
	}

	public void setVaildtime(String vaildtime) {
		this.vaildtime=vaildtime; 
	}
	
	public String getInvildtime() {
		return invildtime;
	}

	public void setInvildtime(String invildtime) {
		this.invildtime=invildtime; 
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address=address; 
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum=phoneNum; 
	}

	@Override
	public String toString() {
		return "StoreInfo [url=" + url + ", name=" + name + ", content="
				+ content + ", vaildtime=" + vaildtime + ", invildtime="
				+ invildtime + ", address=" + address + ", phoneNum=" + phoneNum
				+ "]";
	}
	
	
}