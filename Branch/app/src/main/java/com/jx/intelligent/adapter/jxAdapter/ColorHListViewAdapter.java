package com.jx.intelligent.adapter.jxAdapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;

/**
 * 商品详情轮播图片的颜色按钮
 */
public class ColorHListViewAdapter extends BaseAdapter {
    private String[] colorStrVal;
    private String[] colorName;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;


    public ColorHListViewAdapter(Context context, String[] colorName, String[] colorStrVal) {
        this.mContext = context;
        this.colorStrVal = colorStrVal;
        this.colorName = colorName;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return colorStrVal.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater
                    .inflate(R.layout.layout_color_select_item, null);
            holder.mImage = (ImageView) convertView
                    .findViewById(R.id.h_lv_img);
            holder.mTitle = (TextView) convertView
                    .findViewById(R.id.h_lv_tex);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mImage.setBackgroundColor(Color.parseColor(colorStrVal[position]));
//        holder.mTitle.setTextColor(Color.parseColor(colorStrVal[position]));
        holder.mTitle.setText(colorName[position]);
        if (position == selectIndex) {

            holder.mImage.setImageResource(R.mipmap.icon_selected);

            convertView.setSelected(true);
        } else {
            convertView.setSelected(false);
        }


        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle;
        private ImageView mImage;
    }


    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}