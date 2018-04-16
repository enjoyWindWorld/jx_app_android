package com.jx.intelligent.adapter.jxAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.HomeBannerResult;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 社区服务排行的recycleView的adapter
 * Created by 王云 on 2017/5/27 0027.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener  = null;
    private Context mContext;
    private List<HomeBannerResult.DataBean.RankingListBean> mServiceListData;

    //定义一个条目点击事件的接口
    public static interface OnItemClickListener {
        void onItemClick2(View view , int position,List<HomeBannerResult.DataBean.RankingListBean> mServiceListData);
    }

    public ServiceListAdapter(Context context, List<HomeBannerResult.DataBean.RankingListBean> mServiceListData) {
        this.mServiceListData = mServiceListData;
        mContext = context;
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.servicelist_recycle_item,viewGroup,false);
        RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtil.getInstance(mContext).getScreenWidth() / 3;
        view.setLayoutParams(params);

        ServiceHolder holder =  new ServiceHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {

        HomeBannerResult.DataBean.RankingListBean rankingListBean = mServiceListData.get(position);
        String imgurl = rankingListBean.getAdv_imgurl();
        String ServiceName = rankingListBean.getAdv_name();
        if(!StringUtil.isEmpty(imgurl))
        {
            if(imgurl.contains("data.jx-inteligent.tech:15010/jx"))
            {
                imgurl = imgurl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
            }
        }
        holder.mServiceName.setText(ServiceName);
        Picasso.with(UIUtil.getContext()).load(imgurl).into(holder.mServiceImg);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mServiceListData.size();
    }
    @Override
    public void onClick(View view) {
        if(mOnItemClickListener!=null){
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick2(view, (int) view.getTag(),mServiceListData);
        }
    }
    public void setOnItemClickListener(ServiceListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}


class  ServiceHolder extends  RecyclerView.ViewHolder{

    public TextView mServiceName;
    public ImageView mServiceImg;

    public ServiceHolder(View itemView) {
        super(itemView);
        mServiceImg = (ImageView) itemView.findViewById(R.id.servicelist_recycle_img);
        mServiceName = (TextView) itemView.findViewById(R.id.servicelist_recycle_txt);
    }
}
