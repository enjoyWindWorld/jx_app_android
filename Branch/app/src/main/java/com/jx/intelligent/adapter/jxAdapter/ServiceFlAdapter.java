package com.jx.intelligent.adapter.jxAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.holder.SuperViewHolder;
import com.jx.intelligent.result.GetHomeServiceTypeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1 0001.
 * 社区服务的子类目适配器
 */
public class ServiceFlAdapter extends BaseAdapter {
    private final String TAG = "ServiceFlAdapter";
    private Context mContext;
    public List<GetHomeServiceTypeResult.ServiceFlBean> serviceFlList = new ArrayList<GetHomeServiceTypeResult.ServiceFlBean>();

    private int selectItem;


    public ServiceFlAdapter(Context mContext, List<GetHomeServiceTypeResult.ServiceFlBean> datas) {
        super(datas);
        this.mContext = mContext;
        serviceFlList = datas;
    }

    public void setDatas(List<GetHomeServiceTypeResult.ServiceFlBean> serviceFlLis) {

        this.serviceFlList = serviceFlLis;


    }


    @Override
    public int getCount() {

        return serviceFlList.size();
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

    public int getSelectItem(int selectItem) {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_ser_fl_grid_item, parent, false);
        }
        TextView grid_title = SuperViewHolder.get(convertView, R.id.grid_title);

        if (selectItem == position) {


            grid_title.setBackgroundResource(R.drawable.corner_blue_bg_white);
            grid_title.setTextColor(mContext.getResources().getColor(R.color.color_1bb6ef));
        } else {

            grid_title.setBackgroundResource(R.drawable.corner_gray_bg_white);
            grid_title.setTextColor(mContext.getResources().getColor(R.color.color_8e95a1));
        }


        grid_title.setText(serviceFlList.get(position).getMenu_name());


        return convertView;
    }


}
