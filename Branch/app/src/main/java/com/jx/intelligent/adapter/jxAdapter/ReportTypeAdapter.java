package com.jx.intelligent.adapter.jxAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.bean.ReportTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 韦飞 on 2017/6/28 0028.
 * 举报的类型
 */

public class ReportTypeAdapter extends BaseAdapter {
    private Context mContext;
    public List<ReportTypeBean> typeList = new ArrayList<ReportTypeBean>();
    private LayoutInflater mInflater;


    public ReportTypeAdapter(Context mContext, List<ReportTypeBean> datas) {
        super(datas);
        this.mContext = mContext;
        typeList = datas;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDatas(List<ReportTypeBean> datas) {
        this.typeList.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_report_type_item,
                    null);
            holder.img_check = (ImageView) convertView
                    .findViewById(R.id.img_check);
            holder.txt_type = (TextView) convertView
                    .findViewById(R.id.txt_type);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_type.setText(typeList.get(position).getType());

        if(typeList.get(position).getFlag() == 1)
        {
            holder.txt_type.setTextColor(mContext.getResources().getColor(R.color.color_399f4a));
            holder.img_check.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txt_type.setTextColor(mContext.getResources().getColor(R.color.color_666666));
            holder.img_check.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView img_check;
        private TextView txt_type;
    }
}
