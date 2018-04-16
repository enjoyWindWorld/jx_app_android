package com.jx.intelligent.adapter.jxAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.intf.AddButtonClickListener;
import com.jx.intelligent.intf.SubButtonClickListener;
import com.jx.intelligent.result.ShopPingCartResult;
import com.jx.intelligent.util.MsgIntEvent;
import com.jx.intelligent.util.MsgScidEvent;
import com.jx.intelligent.util.SharedPreferencesUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.view.NumberAddSubView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 首页产品选购RecycleView的Adapter
 * Created by 王云 on 2017/5/27 0027.
 */

public class ShopPingCartInSideRecycleAdapter extends RecyclerView.Adapter<ShopPingCartInSideRecycleAdapter.CartInSideHolder> {
    private List<ShopPingCartResult.DataBean.ListBean> mList;

    private int sc_id;
    private ShopPingCartResult.DataBean.ListBean listBean;


    private boolean flag= true;

    public ShopPingCartInSideRecycleAdapter(List<ShopPingCartResult.DataBean.ListBean> list) {
        this.mList = list;
    }



    public CartInSideHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopping_cart_inside_item, viewGroup, false);
        CartInSideHolder holder = new CartInSideHolder(view);

           return holder;

    }


    @Override
    public void onBindViewHolder(final CartInSideHolder holder, final int position) {

        listBean = mList.get(position);
        String sc_name = listBean.getName();
        String sc_imgurl = listBean.getUrl();
        sc_id = listBean.getSc_id();

        int sc_number = listBean.getNumber();
        float mPrice = listBean.getPrice();
        float totalPrice = listBean.getTotalPrice();
        //取小数点后一位
        String format = DecimalFormat.getInstance().format(totalPrice);


        //购买的倍数
        int pro_multiple = listBean.getPpdnum();
        //sc_type 如果是 0 则表示包年，如果是 1 则是包流量
        int sc_type = listBean.getType();
        String sc_color = listBean.getColor();
        //显示item的数据


        SharedPreferencesUtil sp = new SharedPreferencesUtil();
        boolean flag = sp.getBoolean(UIUtil.getContext(), Constant.FIRST_SHOW);

        if(flag){
            holder.mShop_addSubView.setVisibility(View.GONE);

        }else {

            holder.mShop_addSubView.setVisibility(View.VISIBLE);
        }



        holder.mSc_title_name.setText(sc_name+"("+sc_color+")");
        holder.mSc_title_price.setText("¥: "+format+"元");
        holder.mSc_title_number.setText("×"+sc_number);
        holder.mShop_addSubView.setValue(sc_number);

        holder.mSc_year_flow.setText(listBean.getYearsorflow());

        if(!StringUtil.isEmpty(sc_imgurl))
        {
            if(sc_imgurl.contains("data.jx-inteligent.tech:15010/jx"))
            {
                sc_imgurl = sc_imgurl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
            }
        }
        Picasso.with(UIUtil.getContext()).load(sc_imgurl).into(holder.mSc_title_img);
        holder.mCart_inside_checkBox.setChecked(listBean.ischeck());
        holder.mShop_addSubView.setTag(R.id.tag_first,sc_id);

        holder.mCart_inside_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //将商品的checkbox的点击变化事件进行回调给第一个Recyclerview
                if (mCallBack != null) {
                    mCallBack.OnCheckListener(isChecked, position);
                }
            }
        });

        /**
         * 加减框的点击事件
         */
        holder.mShop_addSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonSubClick(View v, int value) {

                int Addnumber = holder.mShop_addSubView.getValue();


                int scid = (int)holder.mShop_addSubView.getTag(R.id.tag_first);

                EventBus.getDefault().post(new MsgIntEvent(Addnumber));

                EventBus.getDefault().post(new MsgScidEvent(scid));

                SubButtonClickListener.SubButtonClickListener(v,value);

            }

            @Override
            public void onButtonAddClick(View v, int value) {

                int SubNumber = holder.mShop_addSubView.getValue();


                int scid = (int)holder.mShop_addSubView.getTag(R.id.tag_first);

                EventBus.getDefault().post(new MsgIntEvent(SubNumber));

                EventBus.getDefault().post(new MsgScidEvent(scid));

                AddButtonClickListener.AddButtonClickListener(v,value);

            }
        });


        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);


    }




    @Override
    public int getItemCount() {

        return mList.size();
    }



    class CartInSideHolder extends RecyclerView.ViewHolder {

        private  ImageView mSc_title_img;
        private  TextView mSc_title_name;
        private  TextView mSc_title_price;
        private  TextView mSc_title_number;
        private  TextView mSc_year_flow;
        private  CheckBox mCart_inside_checkBox;
        private  NumberAddSubView mShop_addSubView;

        public CartInSideHolder(View itemView) {
            super(itemView);
            //净水机的图片
            mSc_title_img = (ImageView) itemView.findViewById(R.id.sc_title_img);
            //净水机的名字  壁挂式。。立式。。台式 这里要把颜色也要设置进去
            mSc_title_name = (TextView) itemView.findViewById(R.id.sc_title_name);
            //商品单价
            mSc_title_price = (TextView) itemView.findViewById(R.id.sc_title_price);
            //用户购买的商品总数
            mSc_title_number = (TextView) itemView.findViewById(R.id.sc_title_number);
            //包年购买 或者 包流量购买的倍数
            mSc_year_flow = (TextView) itemView.findViewById(R.id.sc_title_year_or_flow);
            //每个商品的 checkBox
            mCart_inside_checkBox = (CheckBox) itemView.findViewById(R.id.cart_inside_checkbox);
            //商品的加减框
            mShop_addSubView = (NumberAddSubView) itemView.findViewById(R.id.shop_addsubview);



        }
    }
    private allCheck mCallBack;

    public void setCallBack(allCheck callBack) {
        mCallBack = callBack;
    }

    public interface allCheck {
        //回调函数 将店铺商品的checkbox的点击变化事件进行回调
        public void OnCheckListener(boolean isChecked, int childpostion);
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



