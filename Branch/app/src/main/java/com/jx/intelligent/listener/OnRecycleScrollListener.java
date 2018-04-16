package com.jx.intelligent.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 *  recycle的滑动监听，上拉加载更多操作
 */

public abstract class OnRecycleScrollListener extends RecyclerView.OnScrollListener {

    private int mLastVisibleItem = 0;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public OnRecycleScrollListener(RecyclerView.Adapter mAdapter, LinearLayoutManager layoutManager){
        this.mAdapter = mAdapter;
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition(); //最后条目可见的角标
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //如果滚动到底部，就获取更多的数据
        if (mAdapter != null && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
            loadMore();
        }
    }

    protected abstract void loadMore();
}
