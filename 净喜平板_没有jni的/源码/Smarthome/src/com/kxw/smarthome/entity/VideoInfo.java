package com.kxw.smarthome.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "AdvVideoInfo")
public class VideoInfo {
	@Column(name = "id",isId=true,autoGen=false)
	public int id;
	
	@Column(name = "video_url")
	public String video_url;
	
	@Column(name = "adv_url")
	public String adv_url;
		
	@Column(name = "is_accord")
	public int is_accord;
	
	@Column(name = "sup_id")
	public String sup_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getAdv_url() {
		return adv_url;	
	}

	public void setAdv_url(String adv_url) {
		this.adv_url = adv_url;
	}

	public int getIs_accord() {
		return is_accord;
	}

	public void setIs_accord(int is_accord) {
		this.is_accord = is_accord;
	}

	public String getSup_id() {
		return sup_id;
	}

	public void setSup_id(String sup_id) {
		this.sup_id = sup_id;
	}
	
	@Override
	public String toString() {
		return "AdvVideoInfo [id=" + id + ", video_url=" + video_url + ", adv_url="
				+ adv_url + ", is_accord=" + is_accord + ", sup_id="
				+ sup_id + "]";
	}
}
