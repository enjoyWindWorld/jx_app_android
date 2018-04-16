package com.kxw.smarthome.entity;

import java.util.List;

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
	
	private List<VideoInfo> video_list;
	
	
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

	public List<VideoInfo> getVideo_list() {
		return video_list;
	}

	public void setVideo_list(List<VideoInfo> video_list) {
		this.video_list = video_list;
	}

	@Override
	public String toString() {
		int size = 0;
		if(video_list != null)
		{
			size = video_list.size();
		}
		return "AdvInfo [id=" + id + ", adv_name=" + adv_name + ", adv_url="
				+ adv_url + ", adv_imgurl=" + adv_imgurl + ", adv_type="
				+ adv_type + "video_list.size=" + size + "]";
	}
}