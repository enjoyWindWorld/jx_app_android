package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.OrderDetailResult;
import com.jx.intelligent.ui.activitise.personalcenter.MyOrderActivity;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

/**
 * 支付成功界面
 * Created by Administrator on 2016/11/15 0015.
 */
public class SuccessPaymentActivity extends RHBaseActivity {

    ImageView zf_zt_icon;
    Button zf_ljzf_btn, zf_qxzf_btn;
    TextView zf_zt_text, zf_dd_bt_text, zf_dd_jg_text, zf_sp_spm, zf_sp_spsj, zf_sp_ddzffs, zf_sp_ddhm;
    private ProgressWheelDialog dialog;
    UserCenter dao;
    private String orderId;

    @Override
    protected void init() {
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
            getOrderDetail();
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_success_payment;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText(getResources().getString(R.string.order_detail_title))
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {

        zf_zt_icon = (ImageView) contentView.findViewById(R.id.zf_zt_icon);
        zf_zt_text = (TextView) contentView.findViewById(R.id.zf_zt_text);
        zf_dd_bt_text = (TextView) contentView.findViewById(R.id.zf_dd_bt_text);
        zf_dd_jg_text = (TextView) contentView.findViewById(R.id.zf_dd_jg_text);
        zf_sp_spm = (TextView) contentView.findViewById(R.id.zf_sp_spm);
        zf_sp_spsj = (TextView) contentView.findViewById(R.id.zf_sp_spsj);
        zf_sp_ddzffs = (TextView) contentView.findViewById(R.id.zf_sp_ddzffs);
        zf_sp_ddhm = (TextView) contentView.findViewById(R.id.zf_sp_ddhm);

        zf_ljzf_btn = (Button) contentView.findViewById(R.id.zf_ljzf_btn);
        zf_ljzf_btn.setOnClickListener(this);
        zf_qxzf_btn = (Button) contentView.findViewById(R.id.zf_qxzf_btn);
        zf_qxzf_btn.setOnClickListener(this);

        dialog = new ProgressWheelDialog(SuccessPaymentActivity.this);
        dao = new UserCenter();
    }

    /**
     *
     */
    public void setPageStat(int orderType) {

        switch (orderType) {

            case Constant.QXDD:
                zf_zt_icon.setImageResource(R.mipmap.qxzf_icon);
                zf_zt_text.setText("取消订单");
                zf_zt_text.setTextColor(getResources().getColor(R.color.color_fd6212));
                zf_ljzf_btn.setVisibility(View.GONE);
                zf_qxzf_btn.setVisibility(View.GONE);
                break;

            case Constant.JYXQ:
                zf_zt_icon.setImageResource(R.mipmap.qrzf_icon);
                zf_zt_text.setText("交易详情");
                zf_zt_text.setTextColor(getResources().getColor(R.color.color_5bc137));
                zf_ljzf_btn.setVisibility(View.GONE);
                zf_qxzf_btn.setVisibility(View.GONE);
                break;

            case Constant.DDZF:
                zf_zt_icon.setImageResource(R.mipmap.qrzf_icon);
                zf_zt_text.setText("等待支付");
                zf_zt_text.setTextColor(getResources().getColor(R.color.color_5bc137));
                zf_ljzf_btn.setVisibility(View.GONE);
                zf_qxzf_btn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuccessPaymentActivity.this, MyOrderActivity.class);
        intent.putExtra("state","");
        intent.putExtra("postion", 0);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                Intent intent = new Intent(SuccessPaymentActivity.this, MyOrderActivity.class);
                intent.putExtra("state","");
                intent.putExtra("postion", 0);
                startActivity(intent);
                break;
            case R.id.zf_ljzf_btn:
                //TODO 提交立即支付请求
                break;
            case R.id.zf_qxzf_btn:
                //TODO 提交取消订单请求
                break;
        }

    }

    void getOrderDetail()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.getMyOrderDetailTask(orderId, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                OrderDetailResult orderDetailResult = (OrderDetailResult)object;
                if(orderDetailResult != null && orderDetailResult.getData().size()>0)
                {
                    showOrderDetail(orderDetailResult.getData().get(0));
                }
                else
                {
                    ToastUtil.showToast("获取订单详情失败");
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    private void showOrderDetail(OrderDetailResult.Data data) {

        zf_dd_jg_text.setText("￥"+data.getPrice());
        if("0".equals(data.getPaytype()))
        {
            zf_sp_spm.setText(data.getName()+"（"+data.getColor()+"）"+"包年费用");
        }
        else
        {
            zf_sp_spm.setText(data.getName()+"（"+data.getColor()+"）"+"流量费用");
        }
        zf_sp_spsj.setText(data.getOrd_modtime());
        if("0".equals(data.getWay()))
        {
            zf_sp_ddzffs.setText("支付宝");
        }
        else if("1".equals(data.getWay()))
        {
            zf_sp_ddzffs.setText("微信");
        }
        else if("2".equals(data.getWay()))
        {
            zf_sp_ddzffs.setText("银联");
        }
        else
        {
            zf_sp_ddzffs.setText("网上支付");
        }
        zf_sp_ddhm.setText(data.getOrdNo());
        if("0".equals(data.getStatus()))
        {
            setPageStat(Constant.DDZF);
        }
        else
        {
            setPageStat(Constant.JYXQ);
        }
    }
}
