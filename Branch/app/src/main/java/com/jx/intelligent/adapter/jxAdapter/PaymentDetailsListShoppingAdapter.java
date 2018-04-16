package com.jx.intelligent.adapter.jxAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.ShopPingCartResult;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 填写支付信息中的listView  Adapter
 * Created by 王云 on 2017/6/14 0027.
 */

public class PaymentDetailsListShoppingAdapter extends BaseAdapter {





    public PaymentDetailsListShoppingAdapter(ArrayList<ShopPingCartResult.DataBean.ListBean> listBean) {
        this.listBean = listBean;
    }

    ArrayList<ShopPingCartResult.DataBean.ListBean> listBean;
    @Override
    public int getCount() {
        if (listBean.size() != 0) {
            return listBean.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (listBean != null) {
            return listBean.get(position);
        }
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(UIUtil.getContext()).inflate(R.layout.payment_detail_list_item, null);
            holder.mDetails_list_title = (TextView) convertView.findViewById(R.id.details_list_title);
            holder.mYear_flow_number = (TextView) convertView.findViewById(R.id.year_flow_number);
            holder.mPayment_detail_img = (ImageView) convertView.findViewById(R.id.payment_detail_img);
            holder.mUnit_price = (TextView) convertView.findViewById(R.id.unit_price);
            holder.mDetails_list_weight = (TextView) convertView.findViewById(R.id.details_list_weight);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imgurl = listBean.get(position).getUrl();
        if(!StringUtil.isEmpty(imgurl))
        {
            if(imgurl.contains("data.jx-inteligent.tech:15010/jx"))
            {
                imgurl = imgurl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
            }
        }
        holder.mDetails_list_title.setText(listBean.get(position).getName() + "(" +listBean.get(position).getColor()+")" );
        holder.mYear_flow_number.setText(listBean.get(position).getYearsorflow());
        Picasso.with(UIUtil.getContext()).load(imgurl).into(holder.mPayment_detail_img);
        float price = (float) listBean.get(position).getPrice();
        float totalPrice = (float) listBean.get(position).getTotalPrice();
        //取小数点后一位
        String format = DecimalFormat.getInstance().format(totalPrice);

        holder.mUnit_price.setText("¥：" + format + "元");

        holder.mDetails_list_weight.setText(" x " +  listBean.get(position).getNumber());


        return convertView;
    }

    static class ViewHolder {

        public ImageView mPayment_detail_img;
        public TextView mDetails_list_title;
        public TextView mYear_flow_number;
        public TextView mUnit_price;
        public TextView mDetails_list_weight;

    }
}
