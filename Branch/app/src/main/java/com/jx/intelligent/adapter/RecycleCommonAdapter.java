package com.jx.intelligent.adapter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * recycleview 通用的Adapter,只支持单一布局,支持上拉加载更多
 */

public abstract class RecycleCommonAdapter<T> extends RecyclerView.Adapter<RecycleCommonViewHolder>{

    public List<T> mDatas = new ArrayList<T>();
    //布局id
    private int     mLayoutId;

    protected boolean mLoadMore;
    public boolean mLoadFinish; // 加载完成
    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isHavefooter = true;//设置底部布局显示

    /**
     * 构造函数,传入数据的list和布局,默认不支持上拉加载更多
     * @param datas
     * @param layoutId
     */
    public RecycleCommonAdapter(Context context, List<T> datas, int layoutId, SwipeRefreshLayout refreshLayout) {
        this(context,datas,false,layoutId,refreshLayout);
    }

    /**
     * 构造函数,传入数据的list和布局
     *
     * @param datas
     * @param layoutId
     */
    public RecycleCommonAdapter(Context context, List<T> datas, boolean loadMore, int layoutId, SwipeRefreshLayout refreshLayout) {
        this.mContext = context;
        mDatas = datas;
        this.mLayoutId = layoutId;
        this.mLoadMore = loadMore;
        this.mRefreshLayout = refreshLayout;
    }

    /**
     * 清除数据
     */
    public void clean(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecycleCommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecycleCommonViewHolder.getViewHolder(mContext,parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecycleCommonViewHolder holder, int position) {
        if(getItemViewType(position) == R.layout.item_rcy_footer){
            checkLoadStatus(holder);
        }else{
            convert(holder, mDatas.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        int layoutId = 0;

        if(mLoadMore && position + 1 == getItemCount() && isHavefooter){
            layoutId = R.layout.item_rcy_footer;
        }else{
            layoutId = mLayoutId;
        }
        return layoutId;
    }


    @Override
    public int getItemCount() {
        if(mDatas != null)
        {
            if(isHavefooter)
            {
                return mLoadMore?mDatas.size()+1:mDatas.size();
            }
            else
            {
                return mDatas.size();
            }
        }

        return 0;
    }

    /**
     * 留给调用者去实现
     *
     * @param holder
     * @param t
     */
    public abstract void convert(RecycleCommonViewHolder holder, T t) ;

    /**
     * 设置每个条目占用的列数
     * @param recyclerView recycleView
     */
    public void setSpanCount(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    // 若是最后一个 且需要加载更多，则强制让最后一个条目占满横屏
                    if (type == R.layout.item_rcy_footer) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public void checkLoadStatus(RecycleCommonViewHolder holder) {
        TextView tv = holder.getView(R.id.tv_item_footer_load_more);
        ProgressBar pb = holder.getView(R.id.pb_item_footer_loading);
        if(mLoadFinish){
            tv.setText("已经到底了");
            tv.setVisibility(View.VISIBLE);

            pb.setVisibility(View.GONE);
        }else if(mRefreshLayout != null && !mRefreshLayout.isRefreshing()){ //没有正在下拉刷新数据
            if(getItemCount()> 1 && mLoadMore){
                tv.setText("正在加载中...");
                tv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.VISIBLE);
            }else {
                tv.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 加载更多
     * @param datas
     */
    public void loadMore(List<T> datas){
        if(mLoadMore && !mLoadFinish){
            if(datas == null || datas.size() == 0){ // 结束标志
                mLoadFinish = true;
            }else {
                mDatas.addAll(datas);
            }
            notifyDataSetChanged();
        }
    }

    public boolean isLoadFinish() {
        return mLoadFinish;
    }

    /**
     * 设置显示不够一页时到底
     * @param mLoadFinish
     */
    public void setmLoadFinish(boolean mLoadFinish) {
        this.mLoadFinish = mLoadFinish;
    }

    public boolean isHavefooter() {
        return isHavefooter;
    }

    public void setHavefooter(boolean havefooter) {
        isHavefooter = havefooter;
    }
}
