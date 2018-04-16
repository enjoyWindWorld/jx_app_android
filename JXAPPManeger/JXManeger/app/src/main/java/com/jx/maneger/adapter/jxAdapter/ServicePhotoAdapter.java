package com.jx.maneger.adapter.jxAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.jx.maneger.R;
import com.jx.maneger.adapter.holder.BaseAdapter;
import com.jx.maneger.bean.ServicePhotoBean;
import com.jx.maneger.helper.GlideHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1 0001.
 * 发布服务的照片
 */
public class ServicePhotoAdapter extends BaseAdapter {
    private Context mContext;
    public List<ServicePhotoBean> photoList = new ArrayList<ServicePhotoBean>();
    private LayoutInflater mInflater;


    public ServicePhotoAdapter(Context mContext, List<ServicePhotoBean> datas) {
        super(datas);
        this.mContext = mContext;
        photoList = datas;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDatas(List<ServicePhotoBean> serviceFlLis) {
        this.photoList.addAll(serviceFlLis);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return photoList.size();
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
            convertView = mInflater.inflate(R.layout.layout_service_photo_item,
                    null);
            holder.my_img = (ImageView) convertView
                    .findViewById(R.id.my_img);
            holder.layout_top = (LinearLayout) convertView
                    .findViewById(R.id.layout_top);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if(TextUtils.isEmpty(photoList.get(position).getImg_url()))
        {
            holder.layout_top.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.layout_top.setVisibility(View.GONE);
            GlideHelper.setImageView(mContext, photoList.get(position).getImg_url(), holder.my_img);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView my_img;
        private LinearLayout layout_top;
    }
}
