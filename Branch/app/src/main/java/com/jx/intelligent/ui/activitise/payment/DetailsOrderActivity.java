package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.OrderDetailResult;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单详情
 * Created by 王云 on 2016/11/15 0015.
 */
public class DetailsOrderActivity extends RHBaseActivity {

    ImageView zf_zt_icon;
    Button zf_ljzf_btn, zf_qxzf_btn;
    TextView zf_zt_text, zf_dd_bt_text, zf_dd_jg_text, zf_sp_spm, zf_sp_spsj, zf_sp_ddzffs, zf_sp_ddhm, txt_person, txt_address, zf_status, zf_dd_jg_title_text, zf_zt_title_text;
    LinearLayout zfbtn_layout, layout_date;
    String orderId, ord_no, context, price, isAgain, tag;
    UserCenter dao;
    private ProgressWheelDialog dialog;
    private NormalAlertDialog normalAlertDialog;
    private DBManager dbManager;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            orderId = getIntent().getStringExtra("orderId");
        }
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new UserCenter();
        getOrderDetail();
        initDialog();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_datails_order;
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
        txt_person = (TextView) contentView.findViewById(R.id.txt_person);
        txt_address = (TextView) contentView.findViewById(R.id.txt_address);
        zf_status = (TextView) contentView.findViewById(R.id.zf_status);
        zf_dd_jg_title_text = (TextView) contentView.findViewById(R.id.zf_dd_jg_title_text);
        zf_zt_title_text = (TextView) contentView.findViewById(R.id.zf_zt_title_text);
        zfbtn_layout = (LinearLayout) contentView.findViewById(R.id.zfbtn_layout);
        layout_date = (LinearLayout) contentView.findViewById(R.id.layout_date);

        zf_ljzf_btn = (Button) contentView.findViewById(R.id.zf_ljzf_btn);
        zf_ljzf_btn.setOnClickListener(this);
        zf_qxzf_btn = (Button) contentView.findViewById(R.id.zf_qxzf_btn);
        zf_qxzf_btn.setOnClickListener(this);

        dialog = new ProgressWheelDialog(DetailsOrderActivity.this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.titlebar_left_rl:
                    finish();
                    break;
                case R.id.zf_ljzf_btn:
                    // 提交立即支付请求
                    if(Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                    {
                        intent =  new Intent(DetailsOrderActivity.this, DetailsPaymentActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("ord_no", ord_no);
                        intent.putExtra("context", context);
                        intent.putExtra("price", price);
                        intent.putExtra("isAgain", isAgain);
                        intent.putExtra("tag", tag);
                        startActivity(intent);
                    }
                    break;
                case R.id.zf_qxzf_btn:
                    // 提交取消订单请求
                    //删除订单
                    if(Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                    {
                        normalAlertDialog.show();
                    }
                    break;
            }
        }


    void getOrderDetail()
    {
        getPreOrderDetailData();
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
                showOrderDetailData(orderDetailResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的订单详情的数据
     */
    void getPreOrderDetailData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ord_no", orderId);

        String js = dbManager.getUrlJsonData(Constant.USER_GET_ORD_DET_WP + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            OrderDetailResult orderDetailResult = new Gson().fromJson(js, OrderDetailResult.class);
            showOrderDetailData(orderDetailResult);
        }
    }

    /**
     * 显示订单详情信息
     * @param orderDetailResult
     */
    void showOrderDetailData(OrderDetailResult orderDetailResult)
    {
        if(orderDetailResult!=null && orderDetailResult.getData().size()>0)
        {
            showOrderDetail(orderDetailResult.getData().get(0));
        }
        else
        {
            ToastUtil.showToast("获取订单详情失败");
        }
    }


    void showOrderDetail(OrderDetailResult.Data data)
    {
        GlideHelper.setImageView(DetailsOrderActivity.this, data.getUrl(),zf_zt_icon);
        zf_zt_text.setText(data.getPrice()+"元");
        zf_dd_bt_text.setText(data.getName()+"（"+data.getColor()+"）");
        zf_dd_jg_text.setText(data.getPrice()+"元");
        zf_sp_spm.setText(data.getPhone());

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
        txt_person.setText(data.getUname());
        txt_address.setText(data.getAddress());

        if("0".equals(data.getStatus()))
        {
            zfbtn_layout.setVisibility(View.VISIBLE);
            zf_status.setText("未支付");
        }
        else if("1".equals(data.getStatus()))
        {
            zfbtn_layout.setVisibility(View.GONE);
            zf_status.setText("已支付");
        }
        else if("3".equals(data.getStatus()))
        {
            zfbtn_layout.setVisibility(View.GONE);
            zf_status.setText("已绑定");
        }
        else if("4".equals(data.getStatus()))
        {
            zfbtn_layout.setVisibility(View.GONE);
            zf_status.setText("续费未使用");
        }
        else if("5".equals(data.getStatus()))
        {
            zfbtn_layout.setVisibility(View.GONE);
            zf_status.setText("续费已使用");
        }

        if(!StringUtil.isEmpty(data.getPaytype()) && "1".equals(data.getPaytype()))//流量
        {
            if("0".equals(isAgain))//不续费的
            {
                zf_zt_title_text.setText("流量购买:");
            }
            else
            {
                zf_zt_title_text.setText("流量续费:");
            }
//            zf_dd_jg_title_text.setText("续费支付");
            layout_date.setVisibility(View.GONE);
        }
        else//包年
        {
            if("0".equals(isAgain))
            {
                zf_zt_title_text.setText("服务费包年购买:");
            }
            else
            {
                zf_zt_title_text.setText("服务费包年续费:");
            }
//            zf_dd_jg_title_text.setText("全额支付");
            layout_date.setVisibility(View.VISIBLE);
            zf_sp_spsj.setText(data.getSerttime());
        }

        isAgain = data.getIsagain();
        ord_no = data.getOrdNo();
        context = data.getName();
        price = data.getPrice()+"";
        tag = data.getTag();
    }

    /**
     * 删除我的订单
     */
    public void deleteMyOrder(String orderId) {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.myOrderDeleteTask(orderId, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("删除我的订单列表成功");
                setResult(Constant.DELETE_ORDER);
                DetailsOrderActivity.this.finish();
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    private void initDialog()
    {
        normalAlertDialog = new NormalAlertDialog.Builder(DetailsOrderActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否要删除订单")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("确定")
                .setRightButtonTextColor(R.color.color_000000)
                .setCancelable(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        deleteMyOrder(orderId);
                    }
                })
                .build();
    }
}
