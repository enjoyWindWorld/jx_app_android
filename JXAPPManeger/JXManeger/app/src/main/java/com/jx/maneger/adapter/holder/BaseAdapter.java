package com.jx.maneger.adapter.holder;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    public List<T> mDatas;

    public BaseAdapter(List<T> datas) {
        mDatas = datas;
    }


    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
