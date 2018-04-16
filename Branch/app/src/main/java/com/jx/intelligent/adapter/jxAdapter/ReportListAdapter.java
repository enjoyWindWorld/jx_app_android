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

public class ReportListAdapter extends BaseAdapter {


    private HomeWaterResult.DataBean.CurrentExponentBean report;


    public ReportListAdapter(List<HomeWaterResult.DataBean.CurrentExponentBean> ReportBean) {
        this.ReportBean = ReportBean;
    }

    private List<HomeWaterResult.DataBean.CurrentExponentBean> ReportBean;
    @Override
    public int getCount() {
        if (ReportBean.size() != 0) {

            return ReportBean.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (ReportBean != null) {
            return ReportBean.get(position);
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
            convertView = LayoutInflater.from(UIUtil.getContext()).inflate(R.layout.home_report_list_item, null);
            holder.mReportTv = (TextView) convertView.findViewById(R.id.report_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        report = ReportBean.get(position);
        String mReport = report.getCurrent_exponent();
        Log.e("饮水量", mReport);
        holder.mReportTv.setText(mReport);

        return convertView;
    }


    static class ViewHolder {
        public TextView mReportTv;
    }
}
