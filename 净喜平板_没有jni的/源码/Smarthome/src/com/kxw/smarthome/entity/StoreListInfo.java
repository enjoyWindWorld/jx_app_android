package com.kxw.smarthome.entity;

import java.io.Serializable;


@SuppressWarnings("serial")
public class StoreListInfo implements Serializable {
	
	public String url;
	
	public String seller;
	
	public String content;
	
	public String vaildtime;
	
	public String invildtime;
	 
	public String address;
	
	public int pubId;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url=url; 
	}
	
	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller=seller; 
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
	
	public int getPubId() {
		return pubId;
	}

	public void setPubId(int pubId) {
		this.pubId=pubId; 
	}

	@Override
	public String toString() {
		return "StoreInfo [url=" + url + ", seller=" + seller + ", content="
				+ content + ", vaildtime=" + vaildtime + ", invildtime="
				+ invildtime + ", address=" + address + ", pubId=" + pubId
				+ "]";
	}
	
	
}