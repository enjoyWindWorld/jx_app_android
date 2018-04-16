package com.jx.intelligent.ui.activitise.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import com.jx.intelligent.R;
import com.jx.intelligent.ui.activitise.navi.RideRouteCalculateActivity;
import com.jx.intelligent.ui.activitise.navi.SingleRouteCalculateActivity;
import com.jx.intelligent.ui.activitise.navi.WalkRouteCalculateActivity;


/**
 * Created by 韦飞 on 2016/11/16 0016.
 * 导航方式选择控件
 */
public class SelectNaviPopWindow extends PopupWindow {

    private View mMenuView;
    TextView txt_walk, txt_bike, txt_car;

    Activity mContext;
    private RelativeLayout.LayoutParams layoutParams;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SelectNaviPopWindow(Activity context, final NaviLatLng starN, final NaviLatLng endN) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.ppwidow_navi_selection, null);
        txt_walk = (TextView) mMenuView.findViewById(R.id.txt_walk);
        txt_bike = (TextView) mMenuView.findViewById(R.id.txt_bike);
        txt_car = (TextView) mMenuView.findViewById(R.id.txt_car);

        layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);


        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationBottomTo);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txt_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WalkRouteCalculateActivity.class);
                intent.putExtra("starN", starN);
                intent.putExtra("endN", endN);
                mContext.startActivity(intent);
                dismiss();
            }
        });

        txt_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RideRouteCalculateActivity.class);
                intent.putExtra("starN", starN);
                intent.putExtra("endN", endN);
                mContext.startActivity(intent);
                dismiss();
            }
        });

        txt_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SingleRouteCalculateActivity.class);
                intent.putExtra("starN", starN);
                intent.putExtra("endN", endN);
                mContext.startActivity(intent);
                dismiss();
            }
        });
    }
}
