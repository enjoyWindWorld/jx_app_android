package com.kxw.smarthome.entity;

import java.util.List;


public class FreeWeatherInfo {
	
	private Data data;
	private int status;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	public class Data
	{
		private List<Forecast> forecast;
		
		public List<Forecast> getForecast() {
			return forecast;
		}

		public void setForecast(List<Forecast> forecast) {
			this.forecast = forecast;
		}
	}
	
	public class Forecast
	{
		private String date;
		private String high;
		private String fengli;
		private String low;
		private String fengxiang;
		private String type;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getHigh() {
			return high;
		}
		public void setHigh(String high) {
			this.high = high;
		}
		public String getFengli() {
			return fengli;
		}
		public void setFengli(String fengli) {
			this.fengli = fengli;
		}
		public String getLow() {
			return low;
		}
		public void setLow(String low) {
			this.low = low;
		}
		public String getFengxiang() {
			return fengxiang;
		}
		public void setFengxiang(String fengxiang) {
			this.fengxiang = fengxiang;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
