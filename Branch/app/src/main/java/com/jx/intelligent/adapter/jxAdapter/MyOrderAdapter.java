/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jx.intelligent.adapter.jxAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.GetOrderListResult;
import com.jx.intelligent.intf.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 * 我的订单的适配器
 */
public class MyOrderAdapter extends SwipeMenuAdapter<MyOrderAdapter.DefaultViewHolder> {

    private List<GetOrderListResult.Data> mOrderBeanList;
    private OnItemClickListener mOnItemClickListener;

    public MyOrderAdapter(List<GetOrderListResult.Data> orderBeanList) {
        this.mOrderBeanList = orderBeanList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void add (List<GetOrderListResult.Data> orderBeanList)
    {
        mOrderBeanList = orderBeanList;
    }

    public void clear()
    {
        mOrderBeanList.clear();
        this.notifyDataSetChanged();
    }

    public GetOrderListResult.Data getItem(int position)
    {
        return mOrderBeanList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mOrderBeanList.get(position).getStatus();
    }

    @Override
    public int getItemCount() {
        return mOrderBeanList == null ? 0 : mOrderBeanList.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wd_dd_item, parent, false);
    }

    @Override
    public MyOrderAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MyOrderAdapter.DefaultViewHolder holder, int position) {
        holder.setData(mOrderBeanList.get(position));
        holder.setOnItemClickListener(mOnItemClickListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView grxx_wddd_ddm;
        TextView grxx_wddd_ddje;
        TextView grxx_wddd_ddrq;
        TextView grxx_wddd_ddzfzt;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            grxx_wddd_ddm = (TextView) itemView.findViewById(R.id.grxx_wddd_ddm);
            grxx_wddd_ddje = (TextView) itemView.findViewById(R.id.grxx_wddd_ddje);
            grxx_wddd_ddrq = (TextView) itemView.findViewById(R.id.grxx_wddd_ddrq);
            grxx_wddd_ddzfzt = (TextView) itemView.findViewById(R.id.grxx_wddd_ddzfzt);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(GetOrderListResult.Data orderBean) {

            this.grxx_wddd_ddm.setText(orderBean.getName());
            this.grxx_wddd_ddje.setText(orderBean.getPrice());
            this.grxx_wddd_ddrq.setText(orderBean.getAddtime());
            if(orderBean.getStatus() == 0)
            {
                this.grxx_wddd_ddzfzt.setText("未支付");
            }
            else if(orderBean.getStatus() == 1)
            {
                this.grxx_wddd_ddzfzt.setText("已支付");
            }
            else if(orderBean.getStatus() == 3)
            {
                this.grxx_wddd_ddzfzt.setText("已绑定");
            }
            else if(orderBean.getStatus() == 4)
            {
                this.grxx_wddd_ddzfzt.setText("续费未使用");
            }
            else if(orderBean.getStatus() == 5)
            {
                this.grxx_wddd_ddzfzt.setText("续费已使用");
            }

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
