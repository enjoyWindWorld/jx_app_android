package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.HomeProductDao;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.PlaceOrderResult;
import com.jx.intelligent.result.ReNewPayOrderDetailResult;
import com.jx.intelligent.ui.activitise.personalcenter.HomeAddrActivity;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

/**
 * 设置续费的订单信息
 * Created by Administrator on 2016/11/15 0015.
 */
public class SetReNewInfoActivity extends RHBaseActivity {

    Button zf_txzfxx_zf_btn;
    TextView zf_txzfxx_bddsjhm, txt_person, txt_product_name, txt_phone, txt_address, txt_year_price, txt_flow_price;
    LinearLayout zf_txzfxx_bdxsjhm, layout_year, layout_flow;
    RelativeLayout zf_txzfxx_txshdz_jr;
    ImageView img_product;
    HomeProductDao homeProductDao;
    UserCenter userCenter;
    ProgressWheelDialog dialog;
    CheckBox year_price_checkb, flow_price_checkb;
    private String ord_no, payType, year_price, flow_price, proName, productId, isAgain;

    @Override
    protected void init() {
        if (getIntent() != null) {
            ord_no = getIntent().getStringExtra("ord_no");
            payType = getIntent().getStringExtra("payType");
            productId = getIntent().getStringExtra("productId");
        }
        homeProductDao = new HomeProductDao();
        userCenter = new UserCenter();
        getProductDetail();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_renew_info;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText(getResources().getString(R.string.pay_info_title))
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        dialog = new ProgressWheelDialog(SetReNewInfoActivity.this);
        zf_txzfxx_txshdz_jr = (RelativeLayout) contentView.findViewById(R.id.zf_txzfxx_txshdz_jr);
        zf_txzfxx_bddsjhm = (TextView) contentView.findViewById(R.id.zf_txzfxx_bddsjhm);
        zf_txzfxx_bdxsjhm = (LinearLayout) contentView.findViewById(R.id.zf_txzfxx_bdxsjhm);
        layout_year = (LinearLayout) contentView.findViewById(R.id.layout_year);
        layout_flow = (LinearLayout) contentView.findViewById(R.id.layout_flow);
        txt_person = (TextView) contentView.findViewById(R.id.txt_person);
        txt_product_name = (TextView) contentView.findViewById(R.id.txt_product_name);
        txt_phone = (TextView) contentView.findViewById(R.id.txt_phone);
        txt_address = (TextView) contentView.findViewById(R.id.txt_address);
        txt_year_price = (TextView) contentView.findViewById(R.id.txt_year_price);
        txt_flow_price = (TextView) contentView.findViewById(R.id.txt_flow_price);
        zf_txzfxx_zf_btn = (Button) contentView.findViewById(R.id.zf_txzfxx_zf_btn);
        img_product = (ImageView) contentView.findViewById(R.id.img_product);
        year_price_checkb = (CheckBox) contentView.findViewById(R.id.year_price_checkb);
        flow_price_checkb = (CheckBox) contentView.findViewById(R.id.flow_price_checkb);

        zf_txzfxx_zf_btn.setOnClickListener(this);
//        zf_txzfxx_txshdz_jr.setOnClickListener(this);
        zf_txzfxx_bdxsjhm.setOnClickListener(this);
        layout_year.setOnClickListener(this);
        layout_flow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SesSharedReferences sp = new SesSharedReferences();
        String userId = sp.getUserId(UIUtil.getContext());
        Intent intent;
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.zf_txzfxx_zf_btn:

                if(payType.equals("0"))
                {
                    //续费下单 包年下单
                    //这里测试账号要重新设置 0元
                    if("121".equals(userId)){
                        reNewPlaceOrder(ord_no, payType + "", year_price, proName);
                    }else {
                        reNewPlaceOrder(ord_no, payType + "", year_price, proName);
                    }
                }
                else
                {
                    //续费下单 包流量下单
                    //这里测试账号要重新设置 0元
                    if("121".equals(userId)){
                        reNewPlaceOrder(ord_no, payType + "", flow_price, proName);
                    }else {
                        reNewPlaceOrder(ord_no, payType + "", flow_price, proName);
                    }
                }
                break;
            case R.id.zf_txzfxx_txshdz_jr:
                intent = new Intent(SetReNewInfoActivity.this, HomeAddrActivity.class);
                intent.putExtra("flag", Constant.GET_HOME_ADDR_FLAG);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            case R.id.zf_txzfxx_bdxsjhm:

                break;
            case R.id.layout_year:
                year_price_checkb.setChecked(true);
                flow_price_checkb.setChecked(false);
                payType = "0";
                break;
            case R.id.layout_flow:
                payType = "1";
                year_price_checkb.setChecked(false);
                flow_price_checkb.setChecked(true);
                break;
        }
    }

    //下单
    void reNewPlaceOrder(String ord_no, String paytype, String price, String proname)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        homeProductDao.getReNewPlaceOrderTask(ord_no, paytype, price, proname, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                PlaceOrderResult placeOrderResult = (PlaceOrderResult) object;
                if(placeOrderResult != null && placeOrderResult.getData().size()>0)
                {
                    Intent intent = new Intent(SetReNewInfoActivity.this, DetailsPaymentActivity.class);
                    intent.putExtra("ord_no", placeOrderResult.getData().get(0).getOrdNo());
                    intent.putExtra("context", placeOrderResult.getData().get(0).getContext());
                    intent.putExtra("price", placeOrderResult.getData().get(0).getPrice());
                    intent.putExtra("paytype", placeOrderResult.getData().get(0).getPaytype());
                    intent.putExtra("isAgain", "1");
                    startActivityForResult(intent, Constant.REQUEST_CODE);
                }
                else
                {
                    ToastUtil.showToast("续费下单失败");
                }
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                dialog.dismiss();
            }
        });
    }

    void getProductDetail()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        homeProductDao.getOrdeAgainDetailTask(ord_no, productId, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ReNewPayOrderDetailResult reNewPayOrderDetailResult = (ReNewPayOrderDetailResult)object;
                if(reNewPayOrderDetailResult!=null && reNewPayOrderDetailResult.getData().size()>0)
                {
                    showOrderDetail(reNewPayOrderDetailResult.getData().get(0));
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

    void showOrderDetail(ReNewPayOrderDetailResult.Data data)
    {
        ReNewPayOrderDetailResult.Data.OrderDetail orderDetail = data.getOrderdetail().get(0);

        GlideHelper.setImageView(SetReNewInfoActivity.this, orderDetail.getUrl(), img_product);
        txt_person.setText(orderDetail.getName());
        txt_address.setText(orderDetail.getAddress());
        txt_phone.setText(Utils.encodePhoneNum(orderDetail.getPhone()));
        txt_product_name.setText(orderDetail.getProname()+"("+orderDetail.getColor()+")");
        proName = orderDetail.getProname();
//        isAgain = orderDetail.getIsagain();

        for (ReNewPayOrderDetailResult.Data.Paytype paytype : data.getPaytype()){
            if(paytype.getPaytype().equals("0"))
            {
                txt_year_price.setText("￥"+paytype.getPrice()+"元");
                year_price = paytype.getPrice();
            }
            else
            {
                txt_flow_price.setText("￥"+paytype.getPrice()+"元");
                flow_price = paytype.getPrice();
            }
        }

        if(payType.equals("0"))
        {
            zf_txzfxx_bddsjhm.setText("包年费用"+orderDetail.getPrice()+"元");
            year_price_checkb.setChecked(true);
        }
        else
        {
            zf_txzfxx_bddsjhm.setText("流量费用"+orderDetail.getPrice()+"元");
            flow_price_checkb.setChecked(true);
        }
    }

}
