package com.kxw.smarthome.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "AdvUrlInfo")
public class AdvInfo {
	
	@Column(name = "id",isId=true,autoGen=false)
	public int id;
	
	@Column(name = "adv_name")
	public String adv_name;
	
	@Column(name = "adv_url")
	public String adv_url;
		
	@Column(name = "adv_imgurl")
	public String adv_imgurl;
	
	@Column(name = "adv_type")
	public int adv_type;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id=id; 
	}
	
	public String getAdv_name() {
		return adv_name;
	}

	public void setAdv_name(String adv_name) {
		this.adv_name=adv_name; 
	}
	
	public String getAdv_imgurl() {
		return adv_imgurl;
	}

	public void setAdv_imgurl(String adv_imgurl) {
		this.adv_imgurl=adv_imgurl; 
	}
	
	public void setAdv_url(String adv_url) {
		this.adv_url=adv_url; 
	}
	
	public String getAdv_url() {
		return adv_url;
	}
	
	public int getAdv_type() {
		return adv_type;
	}

	public void setAdv_type(int adv_type) {
		this.adv_type=adv_type; 
	}

	@Override
	public String toString() {
		return "AdvInfo [id=" + id + ", adv_name=" + adv_name + ", adv_url="
				+ adv_url + ", adv_imgurl=" + adv_imgurl + ", adv_type="
				+ adv_type + "]";
	}
}