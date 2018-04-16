package com.jx.maneger.adapter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;


import com.jx.maneger.R;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;

import java.util.List;

public abstract class RecycleAdapter<T> extends RecycleCommonAdapter<T> {
    public RecycleAdapter(Context context, List<T> datas, int layoutId, SwipeRefreshLayout refreshLayout) {
        super(context, datas, layoutId, refreshLayout);
    }

    public RecycleAdapter(Context context, List<T> datas, boolean loadMore, int layoutId, SwipeRefreshLayout refreshLayout) {
        super(context, datas, loadMore, layoutId, refreshLayout);
    }

    @Override
    public abstract void convert(RecycleCommonViewHolder holder, T t);

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return super.getItemViewType(position);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(RecycleCommonViewHolder holder, int position) {
        if (getItemViewType(position) == R.layout.title_bar) {
            opHeader(holder);
        }
        else if (getItemViewType(position) == R.layout.item_rcy_footer) {
            checkLoadStatus(holder);
        }
        else {
            convert(holder, mDatas.get(position)); //顶部有一个不同的ViewType，角标需要-1
        }
    }

    protected abstract void opHeader(RecycleCommonViewHolder holder);
}
