package com.jx.intelligent.adapter.jxAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.GetAddrResult;
import com.jx.intelligent.util.Utils;

import java.util.List;


/**
 * 比原来的多了getItemViewType和getViewTypeCount这两个方法，
 */
public class HomeAddrAdapter extends BaseAdapter {


    public static final String TAG = "HomeAddrAdapter";
    public static final String KEY = "key";
    public static final String VALUE = "value";

    public static final int VALUE_TIME_TIP = 0;// 7种不同的布局
    public static final int VALUE_LEFT_TEXT = 1;

    private LayoutInflater mInflater;

    private List<GetAddrResult.HomeAddrBean> myList;

//    private int selectIndex;

    private myOnClickListen szmrdzOnClickListener;
    private myOnClickListen scOnClickListener;
    private myOnClickListen xgOnClickListener;


    public HomeAddrAdapter(Context context, List<GetAddrResult.HomeAddrBean> myList) {
        this.myList = myList;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        GetAddrResult.HomeAddrBean msg = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderAddrInfoLayout viewHolderAddrInfoLayout = null;
        ViewHolderAddAddrLayout viewHolderAddAddrLayout = null;


        if (convertView == null) {

            viewHolderAddrInfoLayout = new ViewHolderAddrInfoLayout();
            convertView = mInflater.inflate(R.layout.layout_home_addr_item,
                    null);
            viewHolderAddrInfoLayout.grxx_jtdz_yhm = (TextView) convertView
                    .findViewById(R.id.grxx_jtdz_yhm);
            viewHolderAddrInfoLayout.grxx_jtdz_mrcheck_lr = (LinearLayout) convertView
                    .findViewById(R.id.grxx_jtdz_mrcheck_lr);
            viewHolderAddrInfoLayout.grxx_jtdz_xg_lr = (LinearLayout) convertView
                    .findViewById(R.id.grxx_jtdz_xg_lr);
            viewHolderAddrInfoLayout.grxx_jtdz_sc_lr = (LinearLayout) convertView
                    .findViewById(R.id.grxx_jtdz_sc_lr);


            viewHolderAddrInfoLayout.grxx_jtdz_mrcheck = (ImageView) convertView
                    .findViewById(R.id.grxx_jtdz_mrcheck);
            viewHolderAddrInfoLayout.grxx_jtdz_phone = (TextView) convertView
                    .findViewById(R.id.grxx_jtdz_phone);
            viewHolderAddrInfoLayout.grxx_jtdz_dz = (TextView) convertView
                    .findViewById(R.id.grxx_jtdz_dz);


            convertView.setTag(viewHolderAddrInfoLayout);


        } else {

            viewHolderAddrInfoLayout = (ViewHolderAddrInfoLayout) convertView.getTag();


        }

        viewHolderAddrInfoLayout.grxx_jtdz_mrcheck_lr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                selectIndex = position;
//                notifyDataSetChanged();
                szmrdzOnClickListener.onItemClick(position);
            }
        });

        viewHolderAddrInfoLayout.grxx_jtdz_xg_lr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xgOnClickListener.onItemClick(position);
            }
        });
        viewHolderAddrInfoLayout.grxx_jtdz_sc_lr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scOnClickListener.onItemClick(position);
            }
        });

        viewHolderAddrInfoLayout.grxx_jtdz_yhm.setText(msg.name);


        viewHolderAddrInfoLayout.grxx_jtdz_dz.setText(msg.getArea() + msg.getDetail());

        viewHolderAddrInfoLayout.grxx_jtdz_phone.setText(Utils.encodePhoneNum(msg.getPhone()));

//        if (selectIndex == position) {
//            viewHolderAddrInfoLayout.grxx_jtdz_mrcheck.setImageResource(R.mipmap.icon_select);
//        } else {
//            viewHolderAddrInfoLayout.grxx_jtdz_mrcheck.setImageResource(R.mipmap.icon_select_f);
//        }


        if (msg.getIsdefault().equals("0")) {
            viewHolderAddrInfoLayout.grxx_jtdz_mrcheck.setImageResource(R.mipmap.icon_select);
        } else {
            viewHolderAddrInfoLayout.grxx_jtdz_mrcheck.setImageResource(R.mipmap.icon_select_f);
        }

        return convertView;
    }

    public void setData(List<GetAddrResult.HomeAddrBean> list) {
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getIsdefault().equals("0")) {
//                selectIndex = i;
//            }
//        }
        this.myList = list;
    }


    public interface myOnClickListen {

        void onItemClick(int item);

    }

    public void setItemSzmrdzOnclickListen(myOnClickListen szmrdzOnClickListen) {

        this.szmrdzOnClickListener = szmrdzOnClickListen;


    }

    public void setItemScOnclickListen(myOnClickListen scOnClickListen) {

        this.scOnClickListener = scOnClickListen;


    }

    public void setItemXgOnclickListen(myOnClickListen xgOnClickListen) {

        this.xgOnClickListener = xgOnClickListen;


    }


    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    class ViewHolderAddrInfoLayout {
        private TextView grxx_jtdz_yhm;
        private ImageView grxx_jtdz_mrcheck;
        private LinearLayout grxx_jtdz_mrcheck_lr;
        private LinearLayout grxx_jtdz_xg_lr;
        private LinearLayout grxx_jtdz_sc_lr;
        private TextView grxx_jtdz_dz;
        private TextView grxx_jtdz_phone;
    }

    class ViewHolderAddAddrLayout {
        private ImageView ivRightIcon;// 右边的头像
        private Button btnRightText;// 右边的文本
    }


}
