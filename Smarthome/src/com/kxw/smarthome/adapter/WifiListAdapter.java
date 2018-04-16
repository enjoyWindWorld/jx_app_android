package com.kxw.smarthome.adapter;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kxw.smarthome.R;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.WifiUtils;

public class WifiListAdapter extends BaseAdapter {

	private Context context;
	private List<ScanResult> wifiList;
	private Handler setWifiHandler = null;

	public WifiListAdapter(Context context, List<ScanResult> wifiList,
			Handler setWifiHandler) {
		this.context = context;
		this.wifiList = wifiList;
		this.setWifiHandler = setWifiHandler;
	}

	public WifiListAdapter(Context context, List<ScanResult> wifiList) {
		this.context = context;
		this.wifiList = wifiList;
	}

	@Override
	public int getCount() {
		return wifiList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.wifi_list_item, null);
		}
		final ScanResult childData = wifiList.get(position);

		/**
		 * 加载资源
		 */
		TextView wifi_name_tv = (TextView) convertView.findViewById(R.id.wifi_name_tv);
		ImageView wifi_level_iv = (ImageView) convertView.findViewById(R.id.wifi_level_iv);
		ImageView wifi_lock_iv = (ImageView) convertView.findViewById(R.id.wifi_lock_iv);
		wifi_name_tv.setText(childData.SSID); 
		

		if (childData.capabilities.contains("WPA2-PSK") || childData.capabilities.contains("WPA-PSK")
				|| childData.capabilities.contains("WPA-EAP") || childData.capabilities.contains("WEP")) {
			wifi_lock_iv.setVisibility(View.VISIBLE);
		} else {
			// 无密码
			wifi_lock_iv.setVisibility(View.INVISIBLE);
		}

		WifiUtils linkWifi = new WifiUtils(context);
		WifiManager wifiManager = (WifiManager)context.getSystemService(Service.WIFI_SERVICE);

		if (linkWifi.IsExsits(childData.SSID) != null) {
			wifi_lock_iv.setImageResource(R.drawable.wifi_is_connected);
		}else{
			wifi_lock_iv.setImageResource(R.drawable.wifi_have_pwd);
		}
/*

		// 点击的话，中继该无线
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (setWifiHandler != null) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = childData;
					setWifiHandler.sendMessage(msg);
				}
			}
		});

		convertView.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					arg0.setBackgroundColor(0xaa333333);
				} else {
					arg0.setBackgroundColor(0x00ffffff);
				}

				return false; // 表示继续传递该消息，如果返回true则表示该消息不再被传递
			}
		});*/

		if (childData.level < -85) {	
			wifi_level_iv.setImageResource(R.drawable.wifi_level_0);
		} else if (childData.level < -70 && childData.level >= -85) {
			wifi_level_iv.setImageResource(R.drawable.wifi_level_1);
		} else if (childData.level < -60  && childData.level >= -70) {	
			wifi_level_iv.setImageResource(R.drawable.wifi_level_2);
		} else if (childData.level >= -60) {
			wifi_level_iv.setImageResource(R.drawable.wifi_level_3);
		}

		convertView.setTag("wifi_" + childData.BSSID);

		return convertView;
	}

}
