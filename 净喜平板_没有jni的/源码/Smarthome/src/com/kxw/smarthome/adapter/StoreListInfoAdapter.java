package com.kxw.smarthome.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kxw.smarthome.MyApplication;
import com.kxw.smarthome.R;
import com.kxw.smarthome.entity.StoreListInfo;
import com.kxw.smarthome.utils.DBUtils;

public class StoreListInfoAdapter extends BaseAdapter {

	private Context context;
	private List<StoreListInfo> list;
	private Handler setWifiHandler = null;

	public StoreListInfoAdapter(Context context, List<StoreListInfo> list,
			Handler setWifiHandler) {
		this.context = context;
		this.list = list;
		this.setWifiHandler = setWifiHandler;
	}

	public StoreListInfoAdapter(Context context, List<StoreListInfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
			convertView = inflater.inflate(R.layout.store_info_item, null);
		}
		final StoreListInfo storeInfo = list.get(position);

		/**
		 * 加载资源
		 */

		ImageView store_url_iv = (ImageView) convertView
				.findViewById(R.id.store_url_iv);
		TextView store_seller_tv = (TextView) convertView
				.findViewById(R.id.store_seller_tv);
		TextView store_content_tv = (TextView) convertView
				.findViewById(R.id.store_content_tv);
		TextView store_address_tv = (TextView) convertView
				.findViewById(R.id.store_address_tv);
		TextView vaild_time_tv = (TextView) convertView
				.findViewById(R.id.vaild_time_tv);
		TextView invild_time_tv = (TextView) convertView
				.findViewById(R.id.invild_time_tv);

		String[] str_arr = storeInfo.url.split(",");
		
		if(str_arr.length > 0)
		{
			MyApplication.getInstance().display(store_url_iv, str_arr[0]);
		}
		
		store_seller_tv.setText(storeInfo.seller);
		store_content_tv.setText(storeInfo.content);
		store_address_tv.setText(storeInfo.address);
		vaild_time_tv.setText(storeInfo.vaildtime);
		invild_time_tv.setText(storeInfo.invildtime);

		convertView.setTag("store_info" + storeInfo.invildtime);

		return convertView;
	}

}
