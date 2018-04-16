package com.jx.intelligent.adapter.jxAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.HomeWaterResult;
import com.jx.intelligent.util.UIUtil;

import java.util.List;

/**
 * 今日饮水量的ListView 的adapter
 * Created by 王云 on 2017/5/27 0027.
 */

public class WaterListAdapter extends BaseAdapter {

    private HomeWaterResult.DataBean dataBean;


    public WaterListAdapter(List<HomeWaterResult.DataBean.WaterQualityBean> mWaterBean) {
        this.mWaterBean = mWaterBean;
    }

    private List<HomeWaterResult.DataBean.WaterQualityBean> mWaterBean;
    @Override
    public int getCount() {
        if (mWaterBean.size() != 0) {

            return mWaterBean.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mWaterBean != null) {
            return mWaterBean.get(position);
        }
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

    ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(UIUtil.getContext()).inflate(R.layout.home_water_list_item, null);
            holder.mWaterTv = (TextView) convertView.findViewById(R.id.water_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeWaterResult.DataBean.WaterQualityBean waterQualityBean = mWaterBean.get(position);
        String mWaterReport = waterQualityBean.getWater_quality();
        Log.e("饮水量", mWaterReport);
        holder.mWaterTv.setText(mWaterReport);

        return convertView;
    }


    static class ViewHolder {
        public TextView mWaterTv;
    }
}
