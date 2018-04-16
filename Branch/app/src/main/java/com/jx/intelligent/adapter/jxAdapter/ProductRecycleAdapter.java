package com.jx.intelligent.adapter.jxAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.result.GetMainHomeProductsResult;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 首页产品选购RecycleView的Adapter
 * Created by 王云 on 2017/5/27 0027.
 */

public class ProductRecycleAdapter extends RecyclerView.Adapter<ProductHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private List<GetMainHomeProductsResult.Data> productData;
    private GetMainHomeProductsResult.Data data;


    //定义一个条目点击事件的接口
    public static interface OnItemClickListener {
        void onItemClick(View view , int position,List<GetMainHomeProductsResult.Data> productData);
    }

    public ProductRecycleAdapter(Context context, List<GetMainHomeProductsResult.Data> productData) {
        this.productData = productData;
        mContext = context;
    }

    public ProductHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_recycle_item, viewGroup, false);
        RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtil.getInstance(mContext).getScreenWidth() / 3;
        view.setLayoutParams(params);
        ProductHolder holder = new ProductHolder(view);

        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {

        data = productData.get(position);
        holder.mName.setText(data.getName());
        String imgurl = data.getPic_url();
        if(!StringUtil.isEmpty(imgurl))
        {
            if(imgurl.contains("data.jx-inteligent.tech:15010/jx"))
            {
                imgurl = imgurl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
            }
        }
        Picasso.with(UIUtil.getContext()).load(imgurl).into(holder.mProImg);
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }
    @Override
    public void onClick(View v) {
        if(mOnItemClickListener!=null){
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag(),productData);
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }
}
   class ProductHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public ImageView mProImg;
        public ProductHolder(View itemView) {
            super(itemView);
            mProImg = (ImageView) itemView.findViewById(R.id.home_recycle_img);
            mName = (TextView) itemView.findViewById(R.id.home_recycle_text);
        }
    }



