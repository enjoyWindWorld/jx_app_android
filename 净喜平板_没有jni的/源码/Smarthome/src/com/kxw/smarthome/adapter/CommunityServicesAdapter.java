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
import com.kxw.smarthome.entity.CommunityServicesInfo;
import com.kxw.smarthome.entity.StoreListInfo;
import com.kxw.smarthome.utils.DBUtils;

public class CommunityServicesAdapter extends BaseAdapter {

	private Context context;
	private List<CommunityServicesInfo> list;
	private Handler setWifiHandler = null;

	public CommunityServicesAdapter(Context context, List<CommunityServicesInfo> list,
			Handler setWifiHandler) {
		this.context = context;
		this.list = list;
		this.setWifiHandler = setWifiHandler;
	}

	public CommunityServicesAdapter(Context context, List<CommunityServicesInfo> list) {
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
			convertView = inflater.inflate(R.layout.grid_item, null);
		}
		final CommunityServicesInfo communityServicesInfo = list.get(position);

		/**
		 * 加载资源
		 */

		ImageView menu_icon = (ImageView) convertView
				.findViewById(R.id.item_iv);
		TextView menu_name = (TextView) convertView
				.findViewById(R.id.item_tv);


		MyApplication.getInstance().display(menu_icon, communityServicesInfo.menu_icourl);
		menu_name.setText(communityServicesInfo.menu_name);


		convertView.setTag("communityServicesInfo" + communityServicesInfo.id);

		return convertView;
	}

}
