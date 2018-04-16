package com.jx.intelligent.adapter;

import android.view.View;
import android.view.ViewGroup;


import com.jx.intelligent.adapter.jxAdapter.BaseAdapter;
import com.jx.intelligent.adapter.holder.BaseHolder;

import java.util.List;


public abstract class SuperBaseAdapter<T> extends BaseAdapter {

    public SuperBaseAdapter(List datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder baseHolder = null;
        if(convertView == null){
            baseHolder = getSpecialHolder();
        }else{
            baseHolder = (BaseHolder) convertView.getTag();
        }
        baseHolder.setDataAndRefreshUI(mDatas.get(position));

        return baseHolder.mHolderView;
    }

    protected abstract BaseHolder getSpecialHolder();
}
