package com.jx.intelligent.ui.activitise.personalcenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.jx.intelligent.R;
import com.jx.intelligent.util.DensityUtil;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.view.loopview.LoopListener;
import com.jx.intelligent.view.loopview.LoopView;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/11/16 0016.
 * 性别选择控件
 */
public class SexSelectPopWindow extends PopupWindow {

    private RelativeLayout rootview;
    private View mMenuView;


    int pPWindowSelect;
    TextView grxx_xbxz_qx, grxx_xbxz_qd;

    Activity mContext;
    private RelativeLayout.LayoutParams layoutParams;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public SexSelectPopWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.ppwidow_sex_selection, null);
        grxx_xbxz_qx = (TextView) mMenuView.findViewById(R.id.grxx_xbxz_qx);
        grxx_xbxz_qd = (TextView) mMenuView.findViewById(R.id.grxx_xbxz_qd);


        layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        rootview = (RelativeLayout) mMenuView.findViewById(R.id.rootview);

//
        LoopView loopView = (LoopView) mMenuView.findViewById(R.id.loop_year);


        ArrayList<String> list = new ArrayList();
        list.add("女");
        list.add("男");
        list.add("保密");
        // 设置是否循环播放
        loopView.setNotLoop();


        // 滚动监听
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                pPWindowSelect = item;
            }
        });


        // 设置原始数据
        loopView.setArrayList(list);
        // 设置初始位置
//        loopView.setPosition(1);
        // 设置字体大小
        loopView.setTextSize(DensityUtil.px2sp(getScaleTxt()));


        //取消按钮
        grxx_xbxz_qx.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        grxx_xbxz_qd.setOnClickListener(itemsOnClick);
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
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    int getScaleTxt()
    {
        int screenWidth = ScreenSizeUtil.getInstance(mContext).getScreenWidth();
        int scaleTxt = 0;

        if (screenWidth <= 360)
        {
            scaleTxt = 25;
        }
        else if (screenWidth <= 720 && screenWidth > 360)
        {
            scaleTxt = 35;
        }
        else if(screenWidth > 720)
        {
            scaleTxt = 45;
        }

        return scaleTxt;
    }


    public int getPpWindowSelect() {
        return pPWindowSelect;
    }


}
