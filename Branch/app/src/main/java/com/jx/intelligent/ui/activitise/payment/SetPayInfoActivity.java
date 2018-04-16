package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.HomeProductDao;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.enums.DateShowType;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetAddrResult;
import com.jx.intelligent.result.PlaceOrderResult;
import com.jx.intelligent.ui.activitise.personalcenter.HomeAddrActivity;
import com.jx.intelligent.util.DateUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.DateAndTimerPicker;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

/**
 * 填写支付信息
 * Created by 王云 on 2016/11/15 0015.
 */
public class SetPayInfoActivity extends RHBaseActivity {

    Button zf_txzfxx_zf_btn;
    TextView zf_txzfxx_bddsjhm, zf_txzfxx_fyje, zf_txzfxx_fyms, txt_person, txt_product_name, txt_phone, txt_address;
    EditText zf_txzfxx_xzfwsj_edit;
    LinearLayout zf_txzfxx_xzfwsj,zf_txzfxx_txshdz_jr, zf_txzfxx_bdxsjhm;
    EditText zf_txzfxx_txcpjlbh;
    ImageView img_product;
    double money;
    String imgUrl, proName, color, proid, adrid;
    int payType;
    HomeProductDao homeProductDao;
    UserCenter userCenter;
    ProgressWheelDialog dialog;
    GetAddrResult getAddrResult;
    private int mPledge;
    private CheckBox mCheckBox;
    private TextView mBuy_tv;
    private  int price;
    private TextView mZfxx_pledge_tv;
    private TextView mZfxx_total_tv;
    private float mPrice;


    @Override
    protected void init() {
        if (getIntent() != null) {
            money = getIntent().getDoubleExtra("money", 0);
            imgUrl = getIntent().getStringExtra("imgUrl");
            proName = getIntent().getStringExtra("proName");
            color = getIntent().getStringExtra("color");
            proid = getIntent().getStringExtra("proid");
            payType = getIntent().getIntExtra("payType", 0);
            mPledge = getIntent().getIntExtra("pledge",0);
            txt_product_name.setText(proName+"("+color+")");
            zf_txzfxx_fyje.setText("￥" + money + "元");
            //押金TextView显示
            mZfxx_pledge_tv.setText("￥" +mPledge+"元");
            //总计金额
            mPrice = (float) (money+mPledge);
            //这里拿到总计金额  并显示出来
            mZfxx_total_tv.setText("￥" +mPrice+""+"元");

            GlideHelper.setImageView(SetPayInfoActivity.this, imgUrl, img_product);

            if(payType==1)
            {
                zf_txzfxx_fyms.setText("流量金额：");
                zf_txzfxx_bddsjhm.setText("流量费用"+money+"元");
            }
            else
            {
                zf_txzfxx_fyms.setText("包年费用：");
                zf_txzfxx_bddsjhm.setText("包年费用"+money+"元");
            }
        }
        homeProductDao = new HomeProductDao();
        userCenter = new UserCenter();
        getDefaultAddr();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_pay_info;
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
        dialog = new ProgressWheelDialog(SetPayInfoActivity.this);
        zf_txzfxx_txshdz_jr = (LinearLayout) contentView.findViewById(R.id.zf_txzfxx_txshdz_jr);
        zf_txzfxx_txcpjlbh = (EditText) contentView.findViewById(R.id.zf_txzfxx_txcpjlbh);
        zf_txzfxx_xzfwsj = (LinearLayout) contentView.findViewById(R.id.zf_txzfxx_xzfwsj);
        zf_txzfxx_xzfwsj_edit = (EditText) contentView.findViewById(R.id.zf_txzfxx_xzfwsj_edit);
        zf_txzfxx_bddsjhm = (TextView) contentView.findViewById(R.id.zf_txzfxx_bddsjhm);
        zf_txzfxx_bdxsjhm = (LinearLayout) contentView.findViewById(R.id.zf_txzfxx_bdxsjhm);
        zf_txzfxx_fyms = (TextView) contentView.findViewById(R.id.zf_txzfxx_fyms);
        zf_txzfxx_fyje = (TextView) contentView.findViewById(R.id.zf_txzfxx_fyje);
        txt_person = (TextView) contentView.findViewById(R.id.txt_person);
        txt_product_name = (TextView) contentView.findViewById(R.id.txt_product_name);
        txt_phone = (TextView) contentView.findViewById(R.id.txt_phone);
        txt_address = (TextView) contentView.findViewById(R.id.txt_address);
        zf_txzfxx_zf_btn = (Button) contentView.findViewById(R.id.zf_txzfxx_zf_btn);
        img_product = (ImageView) contentView.findViewById(R.id.img_product);
        //王云
        mCheckBox = (CheckBox) contentView.findViewById(R.id.checkBox);
        //购买详情textview
        mBuy_tv = (TextView) contentView.findViewById(R.id.buy_details_tv);
        //押金的textview
        mZfxx_pledge_tv = (TextView) contentView.findViewById(R.id.zf_txzfxx_pledge);
        //总计金额的textview
        mZfxx_total_tv = (TextView) contentView.findViewById(R.id.zf_txzfxx_total);




        zf_txzfxx_zf_btn.setOnClickListener(this);
        zf_txzfxx_txshdz_jr.setOnClickListener(this);
        zf_txzfxx_xzfwsj.setOnClickListener(this);
        zf_txzfxx_bdxsjhm.setOnClickListener(this);
        //购买详情TextView的点击事件
        mBuy_tv.setOnClickListener(this);
        hideSoftKeyboard(zf_txzfxx_txcpjlbh);

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
                if (StringUtil.isEmpty(txt_person.getText().toString())) {
                    ToastUtil.showToast("请选择家庭住址");
                } else if (StringUtil.isEmpty(zf_txzfxx_xzfwsj_edit.getText().toString())) {
                    ToastUtil.showToast("请选择安装时间");
                } else if (StringUtil.isEmpty(zf_txzfxx_txcpjlbh.getText().toString())) {
                    ToastUtil.showToast("请输入产品经理编号");
                } else if (money == 0) {
                    ToastUtil.showToast("价格不能为0");
                } else if(!DateUtil.judgeCurrTime(zf_txzfxx_xzfwsj_edit.getText().toString())) {
                    ToastUtil.showToast("安装时间要大于当前时间");
                }else if(!mCheckBox.isChecked()){
                    //在这里添加判断是否勾选了购买详情的选项  勾选则跳转支付
                    ToastUtil.showToast("请阅读购买须知");

                }
                else {
                    if("121".equals(userId)){
                        //这里是下单  结算的是总计金额                                                                                                               下单测试价格修改成0.01
                        placeOrder(payType + "", adrid, proid, proName, zf_txzfxx_txcpjlbh.getText().toString(), zf_txzfxx_xzfwsj_edit.getText().toString(), 0 + "", imgUrl, color);
                    }else {
                        //mPrice
                        placeOrder(payType + "", adrid, proid, proName, zf_txzfxx_txcpjlbh.getText().toString(), zf_txzfxx_xzfwsj_edit.getText().toString(), mPrice + "", imgUrl, color);
                    }
                }
                break;
            case R.id.zf_txzfxx_txshdz_jr:
                intent = new Intent(SetPayInfoActivity.this, HomeAddrActivity.class);
                intent.putExtra("flag", Constant.GET_HOME_ADDR_FLAG);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            case R.id.zf_txzfxx_xzfwsj:
                showDatePicker();
                break;
            case R.id.zf_txzfxx_bdxsjhm:

                break;
            //购买详情TextView的点击
            case R.id.buy_details_tv:
                Intent BuyIntent = new Intent(SetPayInfoActivity.this,BuyDetailsActivity.class);
                startActivityForResult(BuyIntent, Constant.BUY_DETAILS);
                break;
        }
    }

    //下单
    void placeOrder(String paytype, String adrid, String proid, String proname, String managerNo, String settime, String price, String url, String color)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        homeProductDao.getPlaceOrderTask(paytype, SesSharedReferences.getUserId(SetPayInfoActivity.this), adrid, proid, proname, managerNo, settime, price, url, color, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                PlaceOrderResult placeOrderResult = (PlaceOrderResult) object;
                if(placeOrderResult != null && placeOrderResult.getData().size()>0)
                {
                    Intent intent = new Intent(SetPayInfoActivity.this, DetailsPaymentActivity.class);
                    intent.putExtra("ord_no", placeOrderResult.getData().get(0).getOrdNo());
                    intent.putExtra("context", placeOrderResult.getData().get(0).getContext());
                    intent.putExtra("price", placeOrderResult.getData().get(0).getPrice());
                    intent.putExtra("paytype", placeOrderResult.getData().get(0).getPaytype());
                    intent.putExtra("isAgain", "0");
                    startActivityForResult(intent, Constant.REQUEST_CODE);
                }
                else
                {
                    ToastUtil.showToast("下单失败");
                }

            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                dialog.dismiss();
            }
        });
    }

    void getDefaultAddr()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenter.getHomeAddrListTask(SesSharedReferences.getUserId(SetPayInfoActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                getAddrResult = (GetAddrResult) object;
                if(getAddrResult != null)
                {
                    for (GetAddrResult.HomeAddrBean homeAddrBean : getAddrResult.getData())
                    {
                        if(homeAddrBean.getIsdefault().equals("0"))
                        {
                            txt_person.setText(homeAddrBean.getName());
                            txt_phone.setText(Utils.encodePhoneNum(homeAddrBean.getPhone()));
                            txt_address.setText(homeAddrBean.getArea()+homeAddrBean.getDetail());
                            adrid = homeAddrBean.getId()+"";
                            return;
                        }
                    }
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });


    }

    /**
     * 显示时间控件
     */
    public void showDatePicker() {
        DateAndTimerPicker.Builder builder = new DateAndTimerPicker.Builder(this, DateShowType.YMDHM);

        DateAndTimerPicker picker = builder.setOnDateAndTimeSelectedListener(new DateAndTimerPicker.OnDateAndTimeSelectedListener() {
            @Override
            public void onDateAndTimeSelected(String[] dates) {
                String time = dates[0] + "/" + dates[1] + "/" + dates[2]+" "+dates[3]+":"+dates[4];
                zf_txzfxx_xzfwsj_edit.setText(time);
            }
        }).create();
        picker.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.GET_HOME_ADDR_OK && data != null) {
            GetAddrResult.HomeAddrBean homeAddrBean = (GetAddrResult.HomeAddrBean)data.getSerializableExtra("obj");
            txt_person.setText(homeAddrBean.getName());
            txt_phone.setText(Utils.encodePhoneNum(homeAddrBean.getPhone()));
            txt_address.setText(homeAddrBean.getArea()+homeAddrBean.getDetail());
            adrid = homeAddrBean.getId()+"";
        }else if(resultCode== Constant.BUY_DETAILS){
            mCheckBox.setChecked(true);
        }
    }
}
