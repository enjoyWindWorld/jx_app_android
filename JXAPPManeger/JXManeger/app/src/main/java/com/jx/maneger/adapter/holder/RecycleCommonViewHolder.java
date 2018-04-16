package com.jx.maneger.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.maneger.helper.GlideHelper;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.util.LayoutUtil;


/**
 *recycleview的通用ViewHolder
 */

public class RecycleCommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * 这个稀疏数组，可以提高效率的
     */
    private final SparseArray<View> views;
    private View convertView;
    private OnItemClickListener mOnItemClickListener;

    /**
     * 如果用到了，比如我们用glide加载图片的时候，还有其他的需要用到上下文的时候
     */
    private Context mContext;

    public RecycleCommonViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        convertView = itemView;
        mContext = itemView.getContext();
        convertView.setOnClickListener(this);
    }

    public static RecycleCommonViewHolder getViewHolder(Context context, ViewGroup parent, int layountId){
        View itemView = LayoutUtil.inflate(context,parent,layountId);
        RecycleCommonViewHolder holder = new RecycleCommonViewHolder(itemView);
        return holder;
    }

    /**
     * 返回一个具体的view对象 ,借鉴的base-adapter-helper中的方法
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本数据
     * @param resId
     * @param text
     * @return
     */
    public RecycleCommonViewHolder setText(int resId, String text) {
        TextView view = getView(resId);
        view.setText(text);
        return this;
    }

    /**
     * 加载显示图片的
     * @param resId
     * @param url
     */
    public RecycleCommonViewHolder setImageByUrl(int resId, final String url, int loadId, int imgRes) {
        final ImageView imageView = getView(resId);
        final ImageView loadingIv = getView(loadId);

        GlideHelper.setImageView(mContext,url,imageView,loadingIv,imgRes);
        return this;
    }

    /**
     * 加载显示图片的
     * @param resId
     * @param resourceId
     * @param loadId
     * @param imgRes
     * @return
     */
    public RecycleCommonViewHolder setImageById(int resId, final Integer resourceId, int loadId, int imgRes) {
        final ImageView imageView = getView(resId);
        final ImageView loadingIv = getView(loadId);

        GlideHelper.setImageView(mContext,resourceId,imageView,loadingIv,imgRes);
        return this;
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    /**
     * 设置recycleView条目的点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
}
