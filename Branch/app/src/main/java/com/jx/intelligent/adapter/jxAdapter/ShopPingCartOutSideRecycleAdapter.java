package com.jx.intelligent.adapter.jxAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.intf.AddButtonClickListener;
import com.jx.intelligent.intf.SubButtonClickListener;
import com.jx.intelligent.result.ShopPingCartResult;
import com.jx.intelligent.util.UIUtil;

import java.util.List;


/**
 *
 * Created by 王云 on 2017/5/27 0027.
 */

public class
ShopPingCartOutSideRecycleAdapter extends RecyclerView.Adapter<ShopPingCartOutSideRecycleAdapter.CartOutSideHolder>{


    private List<ShopPingCartResult.DataBean.ListBean> list;

    public ShopPingCartOutSideRecycleAdapter(List<ShopPingCartResult.DataBean> mOutside_Datas) {
        this.mOutside_Datas = mOutside_Datas;
    }

    List<ShopPingCartResult.DataBean> mOutside_Datas;
    //定义一个条目点击事件的接口




    public CartOutSideHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopping_cart_outside_item, viewGroup, false);
        CartOutSideHolder holder = new CartOutSideHolder(view);

           return holder;
    }

    @Override
    public void onBindViewHolder(CartOutSideHolder holder, final int position2) {
        final ShopPingCartResult.DataBean dataBean = mOutside_Datas.get(position2);
        String name = dataBean.getName().get(0).getName();
        list = dataBean.getList();
        holder.mOutside_tv_name.setText(name);

        ShopPingCartInSideRecycleAdapter adapter = new ShopPingCartInSideRecycleAdapter(list);
        holder.mRecycle_inside.setAdapter(adapter);
        Boolean Flag = dataBean.ischeck();

        adapter.setSubButtonClickListener(new SubButtonClickListener() {
            @Override
            public void SubButtonClickListener(View v, int value) {
                SubButtonClickListener.SubButtonClickListener(v,value);
            }
        });

        adapter.setAddButtonClickListener(new AddButtonClickListener() {
            @Override
            public void AddButtonClickListener(View v, int value) {
                AddButtonClickListener.AddButtonClickListener(v,value);
            }
        });




        holder.mCart_outside_checkBox.setChecked(dataBean.ischeck());


        holder.mCart_outside_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //将店铺的checkbox的点击变化事件进行回调
                if (mCallBack != null) {
                    mCallBack.OnCheckListener(isChecked, position2);
                }
            }
        });
        //实现第二层RecyclerView的回调接口
        adapter.setCallBack(new ShopPingCartInSideRecycleAdapter.allCheck() {
            @Override
            public void OnCheckListener(boolean isChecked, int childpostion) {
                //将店铺商品的checkbox的点击变化事件进行回调
                if (mCallBack != null) {
                    mCallBack.OnItemCheckListener(isChecked, position2, childpostion);
                }
            }


        });



    }




    @Override
    public int getItemCount() {

        return mOutside_Datas.size();

    }



    public class CartOutSideHolder extends RecyclerView.ViewHolder {
       private  TextView mOutside_tv_name;
       private  RecyclerView mRecycle_inside;
       private  LinearLayoutManager manager;
       private  CheckBox mCart_outside_checkBox;

       public CartOutSideHolder(View itemView) {
            super(itemView);
           //标题 商铺的checkBox
            mCart_outside_checkBox = (CheckBox) itemView.findViewById(R.id.cart_outside_checkbox);
            mOutside_tv_name = (TextView) itemView.findViewById(R.id.otside_tv_name);
            mRecycle_inside = (RecyclerView) itemView.findViewById(R.id.cart_recycle_inside);
            manager = new LinearLayoutManager(UIUtil.getContext());
            mRecycle_inside.setLayoutManager(manager);

            //优化性能
            mRecycle_inside.setHasFixedSize(true);



        }
       public CheckBox getCheckBox() {

           return mCart_outside_checkBox;
       }


    }

    private allCheck mCallBack;

    public void setCallBack(allCheck callBack) {
        mCallBack = callBack;
    }

    public interface allCheck{
        //回调函数 将店铺的checkbox的点击变化事件进行回调
        public void OnCheckListener(boolean isSelected,int position);
        //回调函数 将店铺商品的checkbox的点击变化事件进行回调
        public void OnItemCheckListener(boolean isSelected,int parentposition,int chaildposition);
    }

    AddButtonClickListener AddButtonClickListener;

    public void setAddButtonClickListener(AddButtonClickListener AddButtonClickListener){

        this.AddButtonClickListener=AddButtonClickListener;
    }

    SubButtonClickListener SubButtonClickListener;

    public void setSubButtonClickListener(SubButtonClickListener SubButtonClickListener){

        this.SubButtonClickListener=SubButtonClickListener;
    }
}



