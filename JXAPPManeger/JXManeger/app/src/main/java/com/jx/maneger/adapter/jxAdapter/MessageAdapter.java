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
package com.jx.maneger.adapter.jxAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jx.maneger.R;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.results.MessageResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 * 消息中心的适配器
 */
public class MessageAdapter extends SwipeMenuAdapter<MessageAdapter.DefaultViewHolder> {

    private List<MessageResult.Data> mMessageList;
    private OnItemClickListener mOnItemClickListener;
    private static Context mContext;

    public MessageAdapter(List<MessageResult.Data> messageList, Context context) {
        this.mMessageList = messageList;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void add (List<MessageResult.Data> messageList)
    {
        mMessageList = messageList;
    }

    public void clear()
    {
        mMessageList.clear();
    }

    public MessageResult.Data getItem(int position)
    {
        return mMessageList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mMessageList == null ? 0 : mMessageList.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_item, parent, false);
    }

    @Override
    public MessageAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.DefaultViewHolder holder, int position) {
        holder.setData(mMessageList.get(position));
        holder.setOnItemClickListener(mOnItemClickListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_red_dot;
        ImageView my_img;
        TextView tv_title;
        TextView tv_date;
        TextView tv_content;
        RelativeLayout layout_delete;
        ImageView img_delete;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            my_img = (ImageView) itemView.findViewById(R.id.my_img);
            img_red_dot = (ImageView) itemView.findViewById(R.id.img_red_dot);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            layout_delete = (RelativeLayout) itemView.findViewById(R.id.layout_delete);
            layout_delete.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(MessageResult.Data msg) {
            this.tv_title.setText(msg.getP_title());
            this.tv_content.setText(msg.getP_content());

            if(!StringUtil.isEmpty(msg.getP_isread())&&msg.getP_isread().equals("0"))
            {
                this.img_red_dot.setVisibility(View.VISIBLE);
            }
            else
            {
                this.img_red_dot.setVisibility(View.GONE);
            }

            if(msg.isDelete())
            {
                layout_delete.setVisibility(View.VISIBLE);
                if(msg.isCheck())
                {
                    img_delete.setBackgroundResource(R.mipmap.cart_choose_selected);
                }
                else
                {
                    img_delete.setBackgroundResource(R.mipmap.cart_choose_normal);
                }
            }
            else
            {
                img_delete.setBackgroundResource(R.mipmap.cart_choose_normal);
                layout_delete.setVisibility(View.GONE);
            }

            this.tv_date.setText(msg.getMessage_time());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
