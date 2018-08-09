package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.DetailPagerAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CartAmountDao;
import com.jx.intelligent.dao.HomeProductDao;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.CartAmountResult;
import com.jx.intelligent.result.ProductDetailResult;
import com.jx.intelligent.ui.activitise.PopuwindowView.ShowColorTypePopwindow;
import com.jx.intelligent.ui.activitise.PopuwindowView.ShowParamenterPopwindow;
import com.jx.intelligent.util.MsgIntEvent;
import com.jx.intelligent.util.NetUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王云 on 2017/6/1 0001.
 * 新版产品详情页面
 */

public class NewProductDetailActivity extends RHBaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    TitleBarHelper titleBarHelper;
    private ViewPager mDetair_ViewPager;
    private TextView mDetair_price;
    private TextView mDetair_introduce;
    private RelativeLayout mColor;
    private RelativeLayout mTrade;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout mService;
    HomeProductDao dao;

    private View mRootView;
    private PopupWindow window;
    private ArrayList<String> mUrlList;
    private LinearLayout mDetair_bg;
    TextView txt_model, txt_voltage_frequency, txt_power, txt_pressure, txt_temperature, txt_flow, txt_mode,
            txt_range, txt_quality, txt_gross_weight, txt_weight, txt_size, txt_packing_size;
    private FrameLayout layout_dot;
    private Button mParmeter_btn;
    private ImageView mPop_img;
    private TextView mPop_price;
    private TextView mPop_title;
    private TextView mPop_type;
    private Button mPop_red_btn;
    private Button mPop_golden_btn;
    private Button mPop_pearl_btn;
    private ImageView mPop_year_buy;
    private ImageView mPop_flow_buy;
    private ImageView mPop_year_subtract;
    private ImageView mPop_year_add;
    private TextView mPop_year_number;
    private ImageView mPop_flow_subtract;
    private ImageView mPop_flow_add;
    private TextView mPop_flow_number;
    private ImageView mPop_buy_subtract;
    private ImageView mPop_buy_add;
    private TextView mPop_buy_number;
    private ArrayList<String> mPopUrlList;
    private ProductDetailResult mPopuDatas;
    private LinearLayout mYear_linearLayout;
    private LinearLayout mFlow_linearLayout;
    private RelativeLayout mParmeter;
    private Button mDetails_add_cart;
    private Button mGoTo_buy;
    private ImageView mShopPing_cart;
    private TextView mCart_amount;
    private int mAmount_sum;
    private boolean flag = false;
    private String userId;
    private int id;
    private ProgressWheelDialog dialog;
    public void getPopDatas(ProductDetailResult mPopuDatas) {
        this.mPopuDatas = mPopuDatas;
    }


    @Override
    protected void init() {
        if (getIntent() != null) {
            id = getIntent().getIntExtra("type_id", 0);
            Log.e("111", id + "");
            dao = new HomeProductDao();
            SesSharedReferences sp = new SesSharedReferences();
            userId = sp.getUserId(UIUtil.getContext());
            LoadDataFromNet(id);
            initCartAmount(userId);
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 忘记这个是从哪里传过来的了！！！！下次接手的哥们自己找哈
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgIntEvent event) {

        int sum = event.getIntMsg();

        if (sum > 0) {
            layout_dot.setVisibility(View.VISIBLE);
            mCart_amount.setText(sum + "");
        } else {
            layout_dot.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    protected int setContentLayout() {
        return R.layout.activity_product;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(NewProductDetailActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setMiddleTitleText("商品详情");
        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void findView(View contentView) {
        dialog =  new ProgressWheelDialog(UIUtil.getContext());
        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        //阴影部分的背景 popuwindow上面那个阴影部分
        mDetair_bg = (LinearLayout) contentView.findViewById(R.id.detail_bg);

        //商品图片
        mDetair_ViewPager = (ViewPager) contentView.findViewById(R.id.detail_center_img);
        //商品价格
        mDetair_price = (TextView) contentView.findViewById(R.id.detail_price);
        //商品介绍信息
        mDetair_introduce = (TextView) contentView.findViewById(R.id.details_introduce);
        //选择颜色
        mColor = (RelativeLayout) contentView.findViewById(R.id.color_seleted);
        //产品参数
        mParmeter = (RelativeLayout) contentView.findViewById(R.id.Product_Parameter);
        //净水器介绍及服务
        mService = (RelativeLayout) contentView.findViewById(R.id.details_service);
        //加入购物车
        mDetails_add_cart = (Button) contentView.findViewById(R.id.details_add_cart);
        //立即购买
        mGoTo_buy = (Button) contentView.findViewById(R.id.goto_buy);
        //购物车图标
        mShopPing_cart = (ImageView) contentView.findViewById(R.id.shopping_cart_img);
        //购物车上面的 圆圈 商品数量 textview
        mCart_amount = (TextView) contentView.findViewById(R.id.txt_amount);

        layout_dot = (FrameLayout) contentView.findViewById(R.id.layout_dot);

        //选择颜色
        mColor.setOnClickListener(this);
        //产品参数
        mParmeter.setOnClickListener(this);
        //净水器介绍及服务
        mService.setOnClickListener(this);
        //立即购买
        mGoTo_buy.setOnClickListener(this);
        //加入购物车
        mDetails_add_cart.setOnClickListener(this);
        //购物车点击事件
        mShopPing_cart.setOnClickListener(this);


    }

    /**
     * 选择颜色  交易类型 以及 产品参数 这3个条目点击弹出的popuwindow 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //颜色 机型等的popuWindow
            case R.id.color_seleted:


                if (mPopuDatas != null && mPopuDatas.getData().size() > 0) {
                    //显示颜色类别选择的popuWindow
                    ShowColorTypePopwindow mColorWindow = new ShowColorTypePopwindow(this, mPopuDatas);
                    mColorWindow.show();
                }
                break;
            //产品参数的popuWindow
            case R.id.Product_Parameter:

                if (mPopuDatas != null && mPopuDatas.getData().size() > 0) {
                    //显示popuWindow
                    ShowParamenterPopwindow mParamentWindow = new ShowParamenterPopwindow(this, mPopuDatas);
                    mParamentWindow.show();

                }
                break;
            //净水器服务详情与介绍
            case R.id.details_service:
                //跳转浏览器
//                goWebJX(Constant.PRODUCT_INTRODUCE_AND_SERVICE_UTL);
                Intent details_service_intent = new Intent(NewProductDetailActivity.this, BuyDetailsActivity.class);
                details_service_intent.putExtra("flag", 2);
                startActivity(details_service_intent);
                break;
            //立即购买弹出 选择类型颜色的 popuWindow
            case R.id.goto_buy:

                if (mPopuDatas != null && mPopuDatas.getData().size() > 0) {
                    //显示颜色类别选择的popuWindow
                    ShowColorTypePopwindow mColorWindow = new ShowColorTypePopwindow(this, mPopuDatas);
                    mColorWindow.show();

                }
                break;
            case R.id.details_add_cart:
                if (mPopuDatas != null && mPopuDatas.getData().size() > 0) {
                    //显示颜色类别选择的popuWindow
                    ShowColorTypePopwindow mColorWindow = new ShowColorTypePopwindow(this, mPopuDatas);
                    mColorWindow.show();

                }
                break;
            case R.id.shopping_cart_img:
                Intent intent = new Intent(this, ShoppingCartActivity.class);
                startActivityForResult(intent, Constant.SHOPPINGCART_NUMBER);

                break;


        }
    }

    /**
     * 详情页面的ViewPager的Adapter
     *
     * @param mUrlList imgUrl的list集合
     */
    public void initViewPager(ArrayList<String> mUrlList) {

        DetailPagerAdapter adapter = new DetailPagerAdapter(mUrlList);
        mDetair_ViewPager.setAdapter(adapter);
    }

    /**
     * 根据产品选购页面传递过来的 产品ID去网络获取对应产品的数据
     *
     * @param id
     */
    public void LoadDataFromNet(int id) {
        getPreCache(id);

        if (!Utils.isNetworkAvailable(UIUtil.getContext())) {
            return;
        }
            dialog.show();

        //产品详情页面的数据请求
        dao.getHomeProdustDetailTask(id, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();

                ProductDetailResult productDetailResultBean = (ProductDetailResult) object;
                //拿到了数据去显示View
                ShowDetailView(productDetailResultBean);
                //给popuwindow需要的数据赋值
                getPopDatas(productDetailResultBean);
            }

            @Override
            public void resFailure(String message) {
//                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });


    }

    public void initCartAmount(String userid) {
        //产品详情页面 购物车中有多少商品数量的请求
        CartAmountDao mCartDao = new CartAmountDao();
        mCartDao.getCartAmountTask(userid, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                CartAmountResult getCartAmount = (CartAmountResult) object;

                mAmount_sum = getCartAmount.getData().get(0).getSum();
                if (mAmount_sum > 0) {
                    layout_dot.setVisibility(View.VISIBLE);
                    mCart_amount.setText(mAmount_sum + "");
                } else {
                    layout_dot.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void resFailure(String message) {

            }
        });
    }

    /**
     * 先获取缓存数据
     */
    public void getPreCache(int id) {
        DBManager dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id + "");
        String js = dbManager.getUrlJsonData(Constant.HOME_PRODUCT_DETAIL_URL + StringUtil.obj2JsonStr(map));
        if (!StringUtil.isEmpty(js)) {
            ProductDetailResult mCacheProductDetailResult = (ProductDetailResult) new Gson().fromJson(js, ProductDetailResult.class);

            //用缓存数据去显示商品详情
            ShowDetailView(mCacheProductDetailResult);
            //用缓存数据去给popuwindow设置数据
            getPopDatas(mCacheProductDetailResult);
        }
    }

    /**
     * 显示商品详情页面的数据
     *
     * @param productDetailResultBean
     */
    public void ShowDetailView(ProductDetailResult productDetailResultBean) {

        if (productDetailResultBean != null && productDetailResultBean.getData().size() > 0) {
            //详情页面的所有数据
            ProductDetailResult.Data DetaiAllDatas = productDetailResultBean.getData().get(0);
            //colorInfoList是3种颜色的集合 里面有 中国红 ，土豪金，珍珠白，以及其对应的其他参数的集合
            List<ProductDetailResult.Data.ColorInfo> colorInfoList = DetaiAllDatas.getColor();

            if(colorInfoList != null && colorInfoList.size() > 0)
            {
                ArrayList<String> mUrlList = new ArrayList<>();
                for (ProductDetailResult.Data.ColorInfo colorInfo : colorInfoList)
                {
                    mUrlList.add(colorInfo.getUrl());
                }
                //初始化商品详情页面的 ViewPager
                initViewPager(mUrlList);
            }

            //TODO: 这里需要注意  get(0)是正常账号的价格，get(1)是测试账号的价格
            //这是价格部分的数据
            String price = DetaiAllDatas.getPaytype().get(0).getPay_price();
            String flow_price = DetaiAllDatas.getPaytype().get(1).getPay_price();
            if (price.equals(flow_price)) {
                mDetair_price.setText(price + " 元");
            } else if (!price.equals(flow_price)) {
                mDetair_price.setText(flow_price + " - " + price + " 元");
            }


            //这是详情部分的数据
            ProductDetailResult.Data.DetailInfo detailInfo = DetaiAllDatas.getDetail().get(0);
            //净水机的名字
            String name = detailInfo.getName();
//            //净水机型号
            String typename = detailInfo.getTypename();
//            //反渗透
            String prod_fl = detailInfo.getPROD_FL();
            //商品介绍信息的TextView
            mDetair_introduce.setText(name);

        }

    }

    /**
     * 拼接URL集合
     */
    private ArrayList<String> SelectedUrl(String url) {
        String[] url_arr = url.split(",");
        mUrlList = new ArrayList<>();
        for (int i = 0; i < url_arr.length; i++) {
            String imgUrl = url_arr[i];
            mUrlList.add(imgUrl);
        }
        return mUrlList;
    }

    /**
     * 颜色 类型popuwindow中的 突出来的img 图片的URL的切割
     *
     * @param url
     * @return
     */
    public String SplitUrl(String url) {
        String[] mPopurl_arr = url.split(",");
        mPopUrlList = new ArrayList<>();
        for (int i = 0; i < mPopurl_arr.length; i++) {
            String SplitUrl = mPopurl_arr[i];
            mPopUrlList.add(SplitUrl);
        }
        return mPopUrlList.get(0);
    }

    /**
     * 跳转到浏览器界面
     */
    private void goWebJX(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.CART_NUMBER) {
            initCartAmount(userId);
        } else if (resultCode == Constant.SHOPPINGCART_NUMBER) {
            initCartAmount(userId);
        }
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //                去网络加载一次数据
                        LoadDataFromNet(id);
                        //去网络拿过数据以后  则显示 刷新完成
                mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


            }
        }).start();

    }
}
