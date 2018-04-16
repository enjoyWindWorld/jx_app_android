package com.jx.intelligent.adapter.jxAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.bean.City;
import com.jx.intelligent.adapter.holder.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1 0001.
 * 城市选择上面的区域
 */
public class AreaAdapter extends BaseAdapter {
    private Context mContext;
    public List<City> areaList = new ArrayList<City>();

    private int selectItem;


    public AreaAdapter(Context mContext, List<City> datas) {
        super(datas);
        this.mContext = mContext;
        areaList = datas;
    }

    public void setDatas(List<City> serviceFlLis) {

        this.areaList = serviceFlLis;


    }


    @Override
    public int getCount() {

        return areaList.size();
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

        grid_title.setText(areaList.get(position).getInitials());

        return convertView;
    }


}
