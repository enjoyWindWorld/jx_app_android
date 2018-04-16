package com.jx.maneger.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.OrderDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.GlideHelper;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.OrderDetailResult;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.NormalAlertDialog;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单详情
 */
public class DetailsOrderActivity extends RHBaseActivity {

    ImageView zf_zt_icon;
    TextView zf_zt_text, zf_dd_bt_text, zf_dd_jg_text, zf_sp_spm, zf_sp_spsj, zf_sp_ddzffs, zf_sp_ddhm, txt_person, txt_address, zf_status, zf_dd_jg_title_text, zf_zt_title_text;
    LinearLayout  layout_date;
    String orderId, ord_no, context, price, isAgain, tag;
    OrderDao dao;
    private ProgressWheelDialog dialog;
    private DBManager dbManager;
    private DecimalFormat decimalFormat;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            orderId = getIntent().getStringExtra("orderId");
        }
        decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new OrderDao();
        getOrderDetail();
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
        layout_date = (LinearLayout) contentView.findViewById(R.id.layout_date);

        dialog = new ProgressWheelDialog(DetailsOrderActivity.this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left_rl:
                    finish();
                    break;
            }
        }


    void getOrderDetail()
    {
        getPreOrderDetailData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(DetailsOrderActivity.this)))
        {
            return;
        }

        dialog.show();
        dao.getMyOrderDetailTask(orderId, SesSharedReferences.getSafetyMark(DetailsOrderActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                OrderDetailResult orderDetailResult = (OrderDetailResult)object;
                showOrderDetailData(orderDetailResult);
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else
                {
                    ToastUtil.showToast(message);
                }
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
        map.put("safetyMark", SesSharedReferences.getSafetyMark(DetailsOrderActivity.this));

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
        zf_zt_text.setText(decimalFormat.format(data.getPrice())+"元");
        zf_dd_bt_text.setText(data.getName()+"（"+data.getColor()+"）");
        zf_dd_jg_text.setText(decimalFormat.format(data.getPrice())+"元");
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
            zf_status.setText("未支付");
        }
        else if("1".equals(data.getStatus()))
        {
            zf_status.setText("已支付");
        }
        else if("3".equals(data.getStatus()))
        {
            zf_status.setText("已绑定");
        }
        else if("4".equals(data.getStatus()))
        {
            zf_status.setText("续费未使用");
        }
        else if("5".equals(data.getStatus()))
        {
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
            layout_date.setVisibility(View.GONE);
        }
        else//包年
        {
            if("0".equals(isAgain))
            {
                zf_zt_title_text.setText("包年购买:");
            }
            else
            {
                zf_zt_title_text.setText("包年续费:");
            }
            layout_date.setVisibility(View.VISIBLE);
            zf_sp_spsj.setText(data.getSerttime());
        }

        isAgain = data.getIsagain();
        ord_no = data.getOrdNo();
        context = data.getName();
        price = data.getPrice()+"";
        tag = data.getTag();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            getOrderDetail();
        }
    }
}
