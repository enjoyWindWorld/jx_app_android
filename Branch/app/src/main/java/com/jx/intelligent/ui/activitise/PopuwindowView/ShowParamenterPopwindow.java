package com.jx.intelligent.ui.activitise.PopuwindowView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.result.ProductDetailResult;
import com.jx.intelligent.ui.activitise.payment.NewProductDetailActivity;

public class ShowParamenterPopwindow extends PopupWindow implements View.OnClickListener {
    private View mRootView;
    private Activity activity;

    private ProductDetailResult mPopuDatas;
    TextView txt_model, txt_voltage_frequency, txt_power, txt_pressure, txt_temperature, txt_flow, txt_mode,
            txt_range, txt_quality, txt_gross_weight, txt_weight, txt_size, txt_packing_size;

    private Button mParmeter_btn;

    public ShowParamenterPopwindow(Activity activity, ProductDetailResult mPopuDatas) {
        this.activity =activity;
        this.mPopuDatas = mPopuDatas;
        initView(activity);
    }


    public void show(){
        Rect rect = new Rect();
          /*
           * getWindow().getDecorView()得到的View是Window中的最顶层View，可以从Window中获取到该View，
           * 然后该View有个getWindowVisibleDisplayFrame()方法可以获取到程序显示的区域，
           * 包括标题栏，但不包括状态栏。
           */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = activity.getWindow().getDecorView().getHeight();
        this.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void initView(Activity activity) {
        //设置按钮监听
//        this.listener = listener;
        initViewSetting(activity);
        //设置SelectPicPopupWindow的View
        this.setContentView(mRootView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mRootView.findViewById(R.id.pop_container).getTop();
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

    //初始化 操作
    private void initViewSetting(Activity context) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.detail_parameter, null);


        //型号，下面的都是产品参数
        txt_model = (TextView) mRootView.findViewById(R.id.txt_model);
        //加热功率
        txt_power = (TextView) mRootView.findViewById(R.id.txt_power);
        //产品毛重
        txt_gross_weight = (TextView) mRootView.findViewById(R.id.txt_gross_weight);
        //产品净重
        txt_weight = (TextView) mRootView.findViewById(R.id.txt_weight);
        //产品尺寸
        txt_size = (TextView) mRootView.findViewById(R.id.txt_size);
        //包装尺寸
        txt_packing_size = (TextView) mRootView.findViewById(R.id.txt_packing_size);
        //额定电压/频率：
        txt_voltage_frequency = (TextView) mRootView.findViewById(R.id.txt_voltage_frequency);
        //进水压力
        txt_pressure = (TextView) mRootView.findViewById(R.id.txt_pressure);
        //环境温度
        txt_temperature = (TextView) mRootView.findViewById(R.id.txt_temperature);
        //净水流量
        txt_flow = (TextView) mRootView.findViewById(R.id.txt_flow);
        //过滤方式
        txt_mode = (TextView) mRootView.findViewById(R.id.txt_mode);
        //适用水范围
        txt_range = (TextView) mRootView.findViewById(R.id.txt_range);
        //出水水质
        txt_quality = (TextView) mRootView.findViewById(R.id.txt_quality);
        //产品参数popuwindow的 确定按钮
        mParmeter_btn = (Button) mRootView.findViewById(R.id.parmeter_btn);

        if (mPopuDatas != null && mPopuDatas.getData().size() > 0) {
            //详情页面的所有数据
            ProductDetailResult.Data DetaiAllDatas = mPopuDatas.getData().get(0);
            //产品详情的数据
            ProductDetailResult.Data.DetailInfo detailInfo = DetaiAllDatas.getDetail().get(0);
            //显示数据
            txt_model.setText(detailInfo.getTypename());
            txt_voltage_frequency.setText(detailInfo.getPROD_HZ());
            txt_power.setText(detailInfo.getPROD_W());
            txt_pressure.setText(detailInfo.getPROD_MPA());
            txt_temperature.setText(detailInfo.getPROD_C());
            txt_flow.setText(detailInfo.getPROD_HL());
            txt_mode.setText(detailInfo.getPROD_FL());
            txt_range.setText(detailInfo.getPROD_WT());
            txt_quality.setText(detailInfo.getPROD_IW());
            txt_gross_weight.setText(detailInfo.getPROD_WX());
            txt_weight.setText(detailInfo.getPROD_WD());
            txt_size.setText(detailInfo.getPROD_SZ());
            txt_packing_size.setText(detailInfo.getPROD_SZI());

        }

        //产品参数popuwindow的 确定按钮
        mParmeter_btn.setOnClickListener(this);



    }

    public void onClick(View view) {

        NewProductDetailActivity ac = new NewProductDetailActivity();

        switch (view.getId()) {
            case R.id.parmeter_btn:
                dismiss();
                break;


        }
    }
}
