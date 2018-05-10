package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.PaymentDetailsListAdapter;
import com.jx.intelligent.adapter.jxAdapter.PaymentDetailsListShoppingAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.HomeProductDao;
import com.jx.intelligent.dao.PamentDetailListDao;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.enums.DateShowType;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetAddrResult;
import com.jx.intelligent.result.NewPlaceOrderResult;
import com.jx.intelligent.result.ParmentDetailsListResult;
import com.jx.intelligent.result.ShopPingCartResult;
import com.jx.intelligent.ui.activitise.personalcenter.HomeAddrActivity;
import com.jx.intelligent.util.DateUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.DateAndTimerPicker;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 新版填写支付信息页面
 * Created by 王云 on 2017/6/12 0012.
 */

public class WyPaymentDetailActivity extends RHBaseActivity implements View.OnLayoutChangeListener {


    private LinearLayout mPayment_address;
    String adrid;
    private TextView mAddress_name;
    private TextView mAddress_phone;
    private TextView mDetails_address;
    private TextView mBuy_details;
    private TextView tv_no_addr;
    private CheckBox mCheckBox;
    private EditText mDetails_time;
    private LinearLayout mDetails_time_click;
    TitleBarHelper titleBarHelper;
    private EditText mPM_number_edit;
    private RelativeLayout layout_details_address;


    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private LinearLayout mDetails_bottom;
    private String number;
    private String type;
    private String color;
    private String url;
    private String ppdnum;
    private String typename;
    private ListView mDetails_listView;
    private TextView mEquipment_number;
    private TextView mPledge;
    private TextView mSubtotal_price;
    private TextView mAmounts_price;
    private Button mBtn_confirm;
    private String mProid;
    private String userId;
    private ArrayList<ShopPingCartResult.DataBean.ListBean> listBean;
    private int mNowsc_id;
    private ProgressWheelDialog dialog;
    private String mSc_id_all;
    GetAddrResult getAddrResult;
    UserCenter userCenter;
    private HomeProductDao dao;

    @Override
    protected void init() {
        number = getIntent().getIntExtra("number", 1) + "";
        type = getIntent().getIntExtra("type", 0) + "";
        color = getIntent().getStringExtra("color");
        url = getIntent().getStringExtra("url");
        ppdnum = getIntent().getIntExtra("ppdnum", 1) + "";
        typename = getIntent().getStringExtra("typename");
        mProid = getIntent().getStringExtra("proid");
        SesSharedReferences sp = new SesSharedReferences();
        userId = sp.getUserId(UIUtil.getContext());

        userCenter = new UserCenter();


        //购物车传递过来的 选中的全部商品item集合
        listBean = (ArrayList<ShopPingCartResult.DataBean.ListBean>) getIntent().getSerializableExtra("listBean");
        //购物车传递过来的 选择的全部商品的 scid(购物车ID)
        mSc_id_all = getIntent().getStringExtra("mSc_id_All");



        if (listBean != null) {
            //获取缓存地址
            getDefaultAddr();
            PaymentDetailsListShoppingAdapter adapter = new PaymentDetailsListShoppingAdapter(listBean);
            mDetails_listView.setAdapter(adapter);
            ShowCartView(listBean);
        } else {
            //获取缓存地址
            getDefaultAddr();
            LoadDatasFromNet(number, type, color, url, ppdnum, typename, userId, mProid);
        }


    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_paymentdetail;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("填写支付信息")
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


    }

    @Override
    protected void findView(View contentView) {
        tv_no_addr = (TextView) contentView.findViewById(R.id.tv_no_addr);
        layout_details_address = (RelativeLayout) contentView.findViewById(R.id.layout_details_address);

        dialog = new ProgressWheelDialog(WyPaymentDetailActivity.this);
        //新版支付详情页面 地址栏 右边的箭头
        mPayment_address = (LinearLayout) contentView.findViewById(R.id.details_address);
        //地址栏中的名字
        mAddress_name = (TextView) contentView.findViewById(R.id.address_name);
        //地址栏中的电话
        mAddress_phone = (TextView) contentView.findViewById(R.id.address_phone);
        //地址栏中的详细地址
        mDetails_address = (TextView) contentView.findViewById(R.id.detailed_address);
        //购买须知
        mBuy_details = (TextView) contentView.findViewById(R.id.buy_details_tv);
        mCheckBox = (CheckBox) contentView.findViewById(R.id.checkBox);
        //自定义的时间控件 EdiText
        mDetails_time = (EditText) contentView.findViewById(R.id.details_time_edit);
        //时间控件的父类linnarlayout
        mDetails_time_click = (LinearLayout) contentView.findViewById(R.id.details_time_edit_click);
        //产品经理编号的 EdiText
        mPM_number_edit = (EditText) contentView.findViewById(R.id.pm_number_edit);
        //最外侧的布局
        activityRootView = contentView.findViewById(R.id.details_relative);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //底部合计：和确定按钮的整个 LinearLayout  需要在软键盘弹出或隐身的时候 显示或不显示
        mDetails_bottom = (LinearLayout) contentView.findViewById(R.id.details_bottom);

        //填写支付信息中的 ListView
        mDetails_listView = (ListView) contentView.findViewById(R.id.payment_detail_listView);

        //设备数量 共计多少台设备
        mEquipment_number = (TextView) contentView.findViewById(R.id.equipment_number);
        //押金
        mPledge = (TextView) contentView.findViewById(R.id.pledge);
        //小计金额
//        mSubtotal_price = (TextView) contentView.findViewById(R.id.subtotal_price);
        //合计  也就是最下面的 下单按钮左边的  总金额
        mAmounts_price = (TextView) contentView.findViewById(R.id.amounts_price);
        //确定按钮  也就是去下单的按钮
        mBtn_confirm = (Button) contentView.findViewById(R.id.btn_confirm);


        //地址栏的监听事件
        layout_details_address.setOnClickListener(this);
        //购买须知的点击事件
        mBuy_details.setOnClickListener(this);
        //时间自定义控件 为了方便点到 用的父类linnarlayout做的点击事件
        mDetails_time_click.setOnClickListener(this);
        //下单按钮的点击事件
        mBtn_confirm.setOnClickListener(this);
    }

    /**
     * 这个方法和监听软键盘弹出消失有关系
     * 是用来添加最外侧布局大小改变的监听
     * 用最外侧布局大小的改变 来判断 软件盘是否弹出
     */
    @Override
    public void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
    }

    /**
     * 各种点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //家庭地址
            case R.id.layout_details_address:
                Intent intent = new Intent(this, HomeAddrActivity.class);
                intent.putExtra("flag", Constant.GET_HOME_ADDR_FLAG);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            //购买详情
            case R.id.buy_details_tv:
                Intent BuyIntent = new Intent(this, BuyDetailsActivity.class);
                startActivityForResult(BuyIntent, Constant.BUY_DETAILS);
                break;
            //时间自定义控件 为了方便点到 用的父类linnarlayout做的点击事件
            case R.id.details_time_edit_click:
                showDatePicker();
                break;
            //填写支付信息页面的 确定按钮， 也就是下单按钮
            case R.id.btn_confirm:
                if (StringUtil.isEmpty(mDetails_address.getText().toString())) {
                    ToastUtil.showToast("请选择家庭住址");
                } else if (StringUtil.isEmpty(mPM_number_edit.getText().toString())) {
                    ToastUtil.showToast("请输入产品经理编号");
                } else if(Utils.filterEmoji(mPM_number_edit.getText().toString())) {
                    ToastUtil.showToast("请勿输入表情符号");
                }else if (!DateUtil.judgeCurrTime(mDetails_time.getText().toString())) {
                    ToastUtil.showToast("安装时间要大于当前时间");
                } else if (!mCheckBox.isChecked()) {
                    //在这里添加判断是否勾选了购买详情的选项  勾选则跳转支付
                    ToastUtil.showToast("请阅读购买须知");
                } else if (StringUtil.isEmpty(mSc_id_all)) {
                    //如果传递过来的 全部ScID 是个空 则代表是从立即支付过来的 没有走购物车
                    //这是从立即支付过去支付的
                    placeOrder(mNowsc_id + "", mPM_number_edit.getText().toString(), mDetails_time.getText().toString(), adrid);
                } else {
                    //如果传递过来的 全部ScID 不为空 则代表是从购物车过来的
                    //scid 换成 购物车 选中的全部scid 也就是 mSc_id_all
                    //这是从购物车结算支付过去的
                    placeOrder(mSc_id_all, mPM_number_edit.getText().toString(), mDetails_time.getText().toString(), adrid);

                }

                break;


        }

    }

    /**
     * 下单网络请求
     */
    public void placeOrder(String id, String managerNo, String settime, String adrid) {
        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
            return;
        }
        dialog.show();
        dao = new HomeProductDao();
        dao.getNewPlaceOrderTask(id, managerNo, settime, adrid, new ResponseResult() {

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                NewPlaceOrderResult result = (NewPlaceOrderResult) object;

                if (result != null && result.getData().size() > 0) {
                    Intent intent = new Intent(WyPaymentDetailActivity.this, DetailsPaymentActivity.class);
                    intent.putExtra("ord_no", result.getData().get(0).getOrd_no());
                    intent.putExtra("context", result.getData().get(0).getContext());
                    intent.putExtra("price", result.getData().get(0).getPrice());
                    intent.putExtra("pay_price", result.getData().get(0).getPay_price());
                    intent.putExtra("paytype", result.getData().get(0).getPaytype());
                    intent.putExtra("tag", result.getData().get(0).getTag());
                    intent.putExtra("isAgain", "0");
                    startActivityForResult(intent, Constant.CART_ZHIFU);
                } else {
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

    /**
     * 显示时间控件
     */
    public void showDatePicker() {
        DateAndTimerPicker.Builder builder = new DateAndTimerPicker.Builder(this, DateShowType.YMDHM);

        DateAndTimerPicker picker = builder.setOnDateAndTimeSelectedListener(new DateAndTimerPicker.OnDateAndTimeSelectedListener() {
            @Override
            public void onDateAndTimeSelected(String[] dates) {
                String time = dates[0] + "/" + dates[1] + "/" + dates[2] + " " + dates[3] + ":" + dates[4];
                mDetails_time.setText(time);
            }
        }).create();
        picker.show();

    }

    /**
     * 去网络请求填写支付信息中listView的数据
     * 这里是从立即支付过来的数据
     *
     * @param number
     * @param type
     * @param color
     * @param url
     * @param ppdnum
     * @param typename
     */
    public void LoadDatasFromNet(String number, String type, String color, String url, String ppdnum, String typename, String userid, String proid) {
        PamentDetailListDao dao = new PamentDetailListDao();

        dao.getPamentDetailsLisTask(number, type, color, url, ppdnum, typename, proid, userid, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                ParmentDetailsListResult getParmentDetailsListResult = (ParmentDetailsListResult) object;
                //填写支付信息页面的总数据
                List<ParmentDetailsListResult.DataBean> data = getParmentDetailsListResult.getData();

                //ListView的adapter
                PaymentDetailsListAdapter adapter = new PaymentDetailsListAdapter(data);

                mDetails_listView.setAdapter(adapter);

                //去显示数据
                ShowDataView(data);

            }

            @Override
            public void resFailure(String message) {

            }
        });
    }

    /**
     * 显示数据
     *
     * @param data
     */
    public void ShowDataView(List<ParmentDetailsListResult.DataBean> data) {

        if (data!=null&&data.size()>0) {
            ParmentDetailsListResult.DataBean dataBean = data.get(0);
            mPledge.setText(dataBean.getPledge()+"");
            mEquipment_number.setText(dataBean.getNumber());
//            mSubtotal_price.setText("金额:" + "¥ " + dataBean.getTotalPrice() + "");
            //最下面的合计金额
            //取小数点后一位
            String format = DecimalFormat.getInstance().format(dataBean.getTotalPrice());
            mAmounts_price.setText("合计：" + "¥" +format +"元");
            //立即支付过去的 购物车ID
            mNowsc_id = dataBean.getSc_id();
        }
    }
    /**
     * 显示购物车数据
     *
     * @param
     */
    public void ShowCartView(ArrayList<ShopPingCartResult.DataBean.ListBean> listBean) {

        if (listBean!=null) {
            int sum = 0;
            int Pledge = 0;
            float mTotalPrice = 0;
            for (int i = 0; i < listBean.size(); i++) {
                int number = listBean.get(i).getNumber();
                float TotalPrice = listBean.get(i).getTotalPrice();
                int pd = listBean.get(i).getPledge();

                mTotalPrice+=TotalPrice;

                sum += number;
                Pledge+=pd;
            }
            mPledge.setText(Pledge+"");
            mEquipment_number.setText(sum+"");


            //最下面的合计金额
            mAmounts_price.setText("合计：" + "¥" +  DecimalFormat.getInstance().format(mTotalPrice)+"元");
        }

    }

    /**
     * 地址栏的数据返回 和 阅读购买须知返回的状态
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.GET_HOME_ADDR_OK && data != null) {
            GetAddrResult.HomeAddrBean homeAddrBean = (GetAddrResult.HomeAddrBean) data.getSerializableExtra("obj");
            mAddress_name.setText(homeAddrBean.getName());
            mAddress_phone.setText(Utils.encodePhoneNum(homeAddrBean.getPhone()));
            mDetails_address.setText(homeAddrBean.getArea() + homeAddrBean.getDetail());
            mPayment_address.setVisibility(View.VISIBLE);
            tv_no_addr.setVisibility(View.GONE);
            adrid = homeAddrBean.getId() + "";
        }
        else if (resultCode == Constant.BUY_DETAILS)
        {
            mCheckBox.setChecked(true);
        }
        else if(resultCode==Constant.CART_ZHIFU)
        {
            String msg = data.getStringExtra("queren");
            Intent intent = new Intent(WyPaymentDetailActivity.this, ShoppingCartActivity.class);
            intent.putExtra("quxiao", msg);
            setResult(Constant.CART_JIESUAN, intent);
            finish();
        }
        else if(resultCode == Constant.ALL_HOME_ADDR_DELETE)
        {
            mAddress_name.setText("");
            mAddress_phone.setText("");
            mDetails_address.setText("");
            mPayment_address.setVisibility(View.GONE);
            tv_no_addr.setVisibility(View.VISIBLE);
            adrid = null;
        }
    }

    /**
     *
     */
    void getDefaultAddr() {

        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
            return;
        }

        dialog.show();
        userCenter.getHomeAddrListTask(SesSharedReferences.getUserId(WyPaymentDetailActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {

                dialog.dismiss();
                getAddrResult = (GetAddrResult) object;
                if (getAddrResult != null) {
                    for (GetAddrResult.HomeAddrBean homeAddrBean : getAddrResult.getData()) {
                        if (homeAddrBean.getIsdefault().equals("0")) {
                            mAddress_name.setText(homeAddrBean.getName());
                            mAddress_phone.setText(Utils.encodePhoneNum(homeAddrBean.getPhone()));
                            mDetails_address.setText(homeAddrBean.getArea() + homeAddrBean.getDetail());
                            mPayment_address.setVisibility(View.VISIBLE);
                            tv_no_addr.setVisibility(View.GONE);
                            adrid = homeAddrBean.getId() + "";
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
     * 监听软键盘是否弹出的方法
     * 并在这个方法中设置 底部 那一条的显示或隐藏
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            mDetails_bottom.setVisibility(View.GONE);

//            Toast.makeText(WyPaymentDetailActivity.this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            mDetails_bottom.setVisibility(View.VISIBLE);
//            Toast.makeText(WyPaymentDetailActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();

        }
    }

}
