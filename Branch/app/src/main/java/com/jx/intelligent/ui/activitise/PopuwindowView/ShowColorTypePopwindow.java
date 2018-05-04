package com.jx.intelligent.ui.activitise.PopuwindowView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.dao.AddToShoppingCartDao;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.AddToShopPingCartResult;
import com.jx.intelligent.result.ParmentDetailsListResult;
import com.jx.intelligent.result.ProductDetailResult;
import com.jx.intelligent.ui.activitise.payment.NewProductDetailActivity;
import com.jx.intelligent.ui.activitise.payment.WyPaymentDetailActivity;
import com.jx.intelligent.util.MsgIntEvent;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.view.NumberAddSubView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.jx.intelligent.R.id.color_pop_red;
import static com.jx.intelligent.util.UIUtil.getResources;

public class ShowColorTypePopwindow extends PopupWindow implements View.OnClickListener {
    private View mRootView;
    private Activity activity;
    private ImageView mPop_img;
    private TextView mPop_price;
    private TextView mPop_title;
    private TextView mPop_type;
    private Button mPop_red_btn;
    private Button mPop_golden_btn;
    private Button mPop_pearl_btn;
    private ImageView mPop_year_buy;
    private ImageView mPop_flow_buy;
    private LinearLayout mYear_linearLayout;
    private LinearLayout mFlow_linearLayout;
    private ProductDetailResult mPopuDatas;
    private Button mAdd_cart;
    private Button mIdl_buy;
    private  String mColor;
    private static int mType =0;
    private  String mUrl;

    private NumberAddSubView mYear_addSubView;
    private NumberAddSubView mFlow_addSubView;
    private NumberAddSubView mBuy_number_addSubView;


    private int mYearNumber=1;
    private int mFlowNumber=1;
    private int mBuyNumber=1;
    private String mTypeName;

    private int ppdnum=1;
    private Button mPop_black_btn;
    private String mRedUrl;
    private String mPrice;
    private String mUserId;
    private String mProid;
    private String mNormalUrl;
    private String name;
    private ProductDetailResult.Data.DetailInfo mPop_detailInfo;
    private ProductDetailResult.Data.PayType mPop_payType;
    private ProductDetailResult.Data mPop_detaiAllDatas;
    private List<ParmentDetailsListResult.DataBean> data;
    private RelativeLayout mFlow_btn_show;
    private final String userId;
    private ProductDetailResult.Data.ColorInfo mPop_colorInfo;

    public ShowColorTypePopwindow(Activity activity, ProductDetailResult mPopuDatas) {
        this.activity =activity;
        this.mPopuDatas = mPopuDatas;
        SesSharedReferences sp = new SesSharedReferences();
        userId = sp.getUserId(activity);
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
        mRootView = inflater.inflate(R.layout.detail_popupwindow, null);

        //颜色类型popuwindow 中的图片img
        mPop_img = (ImageView) mRootView.findViewById(R.id.color_pop_img);
        //img右边的价格
        mPop_price = (TextView) mRootView.findViewById(R.id.color_pop_price);
        //img右边的净水机详细参数标题
        mPop_title = (TextView) mRootView.findViewById(R.id.color_pop_title);
        //类型
        mPop_type = (TextView) mRootView.findViewById(R.id.color_pop_type);
        //红色
        mPop_red_btn = (Button) mRootView.findViewById(color_pop_red);
        //土豪金
        mPop_golden_btn = (Button) mRootView.findViewById(R.id.color_pop_golden);
        //珍珠白
        mPop_pearl_btn = (Button) mRootView.findViewById(R.id.color_pop_pearl);
        //珍珠黑
        mPop_black_btn = (Button) mRootView.findViewById(R.id.color_pop_black);

        //包年购买的选择图标
        mPop_year_buy = (ImageView) mRootView.findViewById(R.id.year_buy_img);
        //流量购买的选择图标
        mPop_flow_buy = (ImageView) mRootView.findViewById(R.id.flow_buy_img);

        //包年购买和 包流量购买的 选择LinearLayout  这样比较方便点击 比较大
        mYear_linearLayout = (LinearLayout) mRootView.findViewById(R.id.year_linerar);
        mFlow_linearLayout = (LinearLayout) mRootView.findViewById(R.id.flow_linerar);

        //加入购物车 和立即购买按钮
        mAdd_cart = (Button) mRootView.findViewById(R.id.addto_shopping_cart);
        mIdl_buy = (Button) mRootView.findViewById(R.id.immediately_buy);

        //包流量栏目的隐藏与现实 （测试账号显示包流量功能，正式账号不显示）
        mFlow_btn_show = (RelativeLayout) mRootView.findViewById(R.id.flow_btn_show);
        if("121".equals(userId)){
            mFlow_btn_show.setVisibility(View.VISIBLE);

        }else {
            mFlow_btn_show.setVisibility(View.GONE);
        }


        //3个加减框自定义控件
        mYear_addSubView = (NumberAddSubView) mRootView.findViewById(R.id.year_addsubview);
        mFlow_addSubView = (NumberAddSubView) mRootView.findViewById(R.id.folw_addsubview);
        mBuy_number_addSubView = (NumberAddSubView) mRootView.findViewById(R.id.buy_number_addsubview);
        //默认情况下选择的是包年购买，所以要给流量购买设置不可点击
        mFlow_addSubView.ClickFalse();


        //popuwindow里面的 点击事件
        mPop_red_btn.setOnClickListener(this);
        mPop_golden_btn.setOnClickListener(this);
        mPop_pearl_btn.setOnClickListener(this);
        mPop_black_btn.setOnClickListener(this);
        mYear_linearLayout.setOnClickListener(this);
        mFlow_linearLayout.setOnClickListener(this);
        mAdd_cart.setOnClickListener(this);
        mIdl_buy.setOnClickListener(this);

        /**
         * 包年自定义控件的 监听器
         */
        mYear_addSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View v, int value) {
                mYearNumber = mYear_addSubView.getValue();
                Log.e("包年总数量",mYearNumber+"");

                if(mYearNumber == 2)
                {
                    mYearNumber = 3;
                    mYear_addSubView.setValue(mYearNumber);
                }
            }

            @Override
            public void onButtonSubClick(View v, int value) {
                mYearNumber = mYear_addSubView.getValue();
                Log.e("包年总数量",mYearNumber+"");

                if(mYearNumber == 2)
                {
                    mYearNumber = 1;
                    mYear_addSubView.setValue(mYearNumber);
                }
            }


        });
        /**
         * 包流量自定义控件的 监听器
         */
        mFlow_addSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View v, int value) {

                mFlowNumber=mFlow_addSubView.getValue();
                Log.e("包流量总数量",mFlowNumber+"");


            }

            @Override
            public void onButtonSubClick(View v, int value) {
                mFlowNumber=mFlow_addSubView.getValue();
                Log.e("包流量总数量",mFlowNumber+"");

            }


        });

        /**
         * 购买数量自定义控件的 监听器
         */
        mBuy_number_addSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View v, int value) {
                mBuyNumber =mBuy_number_addSubView.getValue();
                Log.e("购买总数量",mBuyNumber+"");
            }

            @Override
            public void onButtonSubClick(View v, int value) {
                mBuyNumber =mBuy_number_addSubView.getValue();
                Log.e("购买总数量",mBuyNumber+"");
            }


        });



        //显示popwindow里面的数据
        if (mPopuDatas != null && mPopuDatas.getData().size() > 0) {
            //详情页面的所有数据
            mPop_detaiAllDatas = mPopuDatas.getData().get(0);
            //产品详情的detailInfo数据
            mPop_detailInfo = mPop_detaiAllDatas.getDetail().get(0);
            List<ProductDetailResult.Data.ColorInfo> color = mPop_detaiAllDatas.getColor();


            //产品详情的colorInfo数据
            mPop_colorInfo = mPop_detaiAllDatas.getColor().get(0);
            //净水器的类型 JXxxxxxd等
            mTypeName = mPop_detailInfo.getId();
            //popuwindow第一次进去初始化的时候 显示默认的img和 price 和 title
            String url =  mPop_colorInfo.getUrl();
            name = mPop_detailInfo.getName();
            NewProductDetailActivity ac = new NewProductDetailActivity();
            mNormalUrl = ac.SplitUrl(url);
            if(!StringUtil.isEmpty(mNormalUrl))
            {
                if(mNormalUrl.contains("data.jx-inteligent.tech:15010/jx"))
                {
                    mNormalUrl = mNormalUrl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
                }
            }
            Picasso.with(UIUtil.getContext()).load(mNormalUrl).into(mPop_img);

            //产品详情的 payType 数据
            mPop_payType = mPop_detaiAllDatas.getPaytype().get(0);

            //商品单价
            mPrice = mPop_payType.getPrice();
            mPop_price.setText("￥ "+ mPrice);

            //popuwindow 设置默认 URL和 颜色
            mUrl=mNormalUrl;
            mColor="中国红";

            
            mPop_title.setText(mPop_detailInfo.getName()+" "+ mPop_colorInfo.getPic_color()+" 包年购买");
            //商品ID  1 2 3
            mProid = mPop_detailInfo.getId();
            mUrl= mNormalUrl;
            mType=0;
            if (mProid.equals("1")){
                mPop_type.setText("壁挂式净水机");
                mPop_red_btn.setVisibility(View.VISIBLE);
                mPop_golden_btn.setVisibility(View.VISIBLE);
                mPop_pearl_btn.setVisibility(View.VISIBLE);
                mPop_black_btn.setVisibility(View.GONE);
            }else if (mProid.equals("2")){
                mPop_type.setText("台式净水机");
                mPop_red_btn.setVisibility(View.VISIBLE);
                mPop_golden_btn.setVisibility(View.GONE);
                mPop_pearl_btn.setVisibility(View.GONE);
                mPop_black_btn.setVisibility(View.VISIBLE);
            }else if (mProid.equals("3")){
                mPop_type.setText("立式净水机");
                mPop_red_btn.setVisibility(View.VISIBLE);
                mPop_golden_btn.setVisibility(View.GONE);
                mPop_pearl_btn.setVisibility(View.GONE);
                mPop_black_btn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onClick(View view) {

        NewProductDetailActivity ac = new NewProductDetailActivity();

        switch (view.getId()) {
            //中国红按钮
            case R.id.color_pop_red:
                mColor="中国红";
                Log.e("111", "中国红按钮");
                mPop_black_btn.setBackground(getResources().getDrawable(R.mipmap.pearl_normal));
                mPop_red_btn.setBackground(getResources().getDrawable(R.mipmap.red_selected));
                mPop_golden_btn.setBackground(getResources().getDrawable(R.mipmap.golden_normal));
                mPop_pearl_btn.setBackground(getResources().getDrawable(R.mipmap.pearl_normal));
                ProductDetailResult.Data.DetailInfo reddetailsInfo = mPopuDatas.getData().get(0).getDetail().get(0);
                ProductDetailResult.Data.ColorInfo redcolorInfo = mPopuDatas.getData().get(0).getColor().get(0);
                String redUrl = redcolorInfo.getUrl();
                mRedUrl = ac.SplitUrl(redUrl);
                if(!StringUtil.isEmpty(mRedUrl))
                {
                    if(mRedUrl.contains("data.jx-inteligent.tech:15010/jx"))
                    {
                        mRedUrl = mRedUrl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
                    }
                }
                Picasso.with(UIUtil.getContext()).load(mRedUrl).into(mPop_img);
                mUrl= mRedUrl;
                if(mType==0){
                    mPop_title.setText(reddetailsInfo.getName() + " " + redcolorInfo.getPic_color()+" 包年购买");
                }else if(mType==1){
                    mPop_title.setText(reddetailsInfo.getName() + " " + redcolorInfo.getPic_color()+" 流量购买");
                }

                break;
            //popupwindow里面的 土豪金按钮
            case R.id.color_pop_golden:
                mColor="土豪金";
                mPop_red_btn.setBackground(getResources().getDrawable(R.mipmap.red_normal));
                mPop_golden_btn.setBackground(getResources().getDrawable(R.mipmap.golden_selected));
                mPop_pearl_btn.setBackground(getResources().getDrawable(R.mipmap.pearl_normal));
                Log.e("111", "土豪金按钮");
                ProductDetailResult.Data.DetailInfo goldendetailsInfo = mPopuDatas.getData().get(0).getDetail().get(0);
                ProductDetailResult.Data.ColorInfo goldencolorInfo = mPopuDatas.getData().get(0).getColor().get(1);
                String goldenUrl = goldencolorInfo.getUrl();
                String mGoldenUrl = ac.SplitUrl(goldenUrl);
                //因为换域名了所以 所有的 图片URL 都要经过切割再重新拼接
                if(!StringUtil.isEmpty(mGoldenUrl))
                {
                    if(mGoldenUrl.contains("data.jx-inteligent.tech:15010/jx"))
                    {
                        mGoldenUrl = mGoldenUrl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
                    }
                }
                Picasso.with(UIUtil.getContext()).load(mGoldenUrl).into(mPop_img);
                mUrl=mGoldenUrl;
                if(mType==0){
                    mPop_title.setText(goldendetailsInfo.getName() + " " + goldencolorInfo.getPic_color()+" 包年购买");
                }else if(mType==1){
                    mPop_title.setText(goldendetailsInfo.getName() + " " + goldencolorInfo.getPic_color()+" 流量购买");
                }

                break;
            //popupwindow里面的 珍珠白按钮
            case R.id.color_pop_pearl:

                mColor="珍珠白";
                Log.e("111", "珍珠白按钮");
                mPop_red_btn.setBackground(getResources().getDrawable(R.mipmap.red_normal));
                mPop_golden_btn.setBackground(getResources().getDrawable(R.mipmap.golden_normal));
                mPop_pearl_btn.setBackground(getResources().getDrawable(R.mipmap.pearl_selected));

                ProductDetailResult.Data.DetailInfo pearldetailsInfo = mPopuDatas.getData().get(0).getDetail().get(0);
                ProductDetailResult.Data.ColorInfo pearlcolorInfo = mPopuDatas.getData().get(0).getColor().get(2);
                String pearlUrl = pearlcolorInfo.getUrl();
                String mPearlUrl = ac.SplitUrl(pearlUrl);
                if(!StringUtil.isEmpty(mPearlUrl))
                {
                    if(mPearlUrl.contains("data.jx-inteligent.tech:15010/jx"))
                    {
                        mPearlUrl = mPearlUrl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
                    }
                }
                Picasso.with(UIUtil.getContext()).load(mPearlUrl).into(mPop_img);
                mUrl=mPearlUrl;
                if(mType==0){
                    mPop_title.setText(pearldetailsInfo.getName() + " " + pearlcolorInfo.getPic_color()+" 包年购买");
                }else if(mType==1){
                    mPop_title.setText(pearldetailsInfo.getName() + " " + pearlcolorInfo.getPic_color()+" 流量购买");
                }
                break;
            //popupwindow里面的 珍珠黑按钮
            case R.id.color_pop_black:
                mColor="珍珠黑";
                mPop_red_btn.setBackground(getResources().getDrawable(R.mipmap.red_normal));
                mPop_black_btn.setBackground(getResources().getDrawable(R.mipmap.pearl_selected));
                ProductDetailResult.Data.DetailInfo blackdetailsInfo = mPopuDatas.getData().get(0).getDetail().get(0);
                ProductDetailResult.Data.ColorInfo blackcolorInfo = mPopuDatas.getData().get(0).getColor().get(1);
                String blackUrl = blackcolorInfo.getUrl();
                String mBlackUrl = ac.SplitUrl(blackUrl);
                if(!StringUtil.isEmpty(mBlackUrl))
                {
                    if(mBlackUrl.contains("data.jx-inteligent.tech:15010/jx"))
                    {
                        mBlackUrl = mBlackUrl.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
                    }
                }
                Picasso.with(UIUtil.getContext()).load(mBlackUrl).into(mPop_img);
                mUrl=mBlackUrl;
                if(mType==0){
                    mPop_title.setText(blackdetailsInfo.getName() + " " + blackcolorInfo.getPic_color()+" 包年购买");
                }else if(mType==1){
                    mPop_title.setText(blackdetailsInfo.getName() + " " + blackcolorInfo.getPic_color()+" 流量购买");
                }
                break;

            //包年购买的选择
            case R.id.year_linerar:
                mType =0;
                mPop_flow_buy.setBackground(getResources().getDrawable(R.mipmap.trade_type_normal));
                mPop_year_buy.setBackground(getResources().getDrawable(R.mipmap.trade_type_selected));
               //选择包年购买的时候 设置 流量购买的 加减框不可点击
                mYear_addSubView.ClickTrue();
                mFlow_addSubView.ClickFalse();
                //设置包流量购买的数量归位 也就是回到原来的1
                mFlow_addSubView.setValue(1);

                //如果是包年取的是包年的单价
                //产品详情的 payType 数据
                mPop_payType = mPop_detaiAllDatas.getPaytype().get(0);
                //商品单价
                mPrice = mPop_payType.getPrice();
                //重新设置 显示的单价
                mPop_price.setText("￥ "+ mPrice);
                mPop_title.setText(mPop_detailInfo.getName()+" "+mPop_colorInfo.getPic_color()+" 包年购买");

                break;
            //流量购买的选择
            case R.id.flow_linerar:
                mType =1;
                mPop_flow_buy.setBackground(getResources().getDrawable(R.mipmap.trade_type_selected));
                mPop_year_buy.setBackground(getResources().getDrawable(R.mipmap.trade_type_normal));
                //选择包流量购买的时候设置 包年购买的 加减框不可点击
                mYear_addSubView.ClickFalse();
                mFlow_addSubView.ClickTrue();
                //设置包年购买的数量归位 也就是回到原来的1
                mYear_addSubView.setValue(1);

                //如果是包流量取的是包流量的单价
                //产品详情的 payType 数据
                mPop_payType = mPop_detaiAllDatas.getPaytype().get(1);
                //商品单价
                mPrice = mPop_payType.getPrice();
                //重新设置 显示的单价
                mPop_price.setText("￥ "+ mPrice);
                mPop_title.setText(mPop_detailInfo.getName()+" "+mPop_colorInfo.getPic_color()+" 流量购买");
                break;
            //  加入购物车
            case R.id.addto_shopping_cart:
                SesSharedReferences sp = new SesSharedReferences();
                mUserId = sp.getUserId(UIUtil.getContext());
                //包年或者包流量的倍数
                if(mType==0){
                    ppdnum=mYearNumber;
                }else if(mType==1){
                    ppdnum=mFlowNumber;
                }

                //去加入购物车
                AddToShoppingCart(name,mPrice,mUrl,mBuyNumber+"",ppdnum+"",mColor,mUserId,mProid,mType+"");

                break;
            //立即购买
            case R.id.immediately_buy:
                //包年或者包流量的倍数
                if(mType==0){
                   ppdnum=mYearNumber;
                }else if(mType==1){
                    ppdnum=mFlowNumber;
                }
                Intent intent = new Intent(activity,WyPaymentDetailActivity.class);
                intent.putExtra("number",mBuyNumber);
                intent.putExtra("ppdnum",ppdnum);
                intent.putExtra("type",mType);
                intent.putExtra("color",mColor);
                intent.putExtra("url",mUrl);
                intent.putExtra("typename",mTypeName);
                intent.putExtra("proid",mProid);
                activity.startActivity(intent);
                dismiss();
                break;

        }
    }

    /**
     * 加入购物车的网络请求
     */
    public void AddToShoppingCart(String name,String price,String url,String number ,String ppdnum,String color,String userid, String proid,String type){

        AddToShoppingCartDao dao = new AddToShoppingCartDao();
       dao.getAddShoppingCartTask(name,price,url,number, ppdnum, color, userid, proid, type, new ResponseResult() {
           @Override
           public void resSuccess(Object object) {
               AddToShopPingCartResult mCartAmountDatas= (AddToShopPingCartResult) object;

               int sum = mCartAmountDatas.getData().get(0).getSum();
               EventBus.getDefault().post(new MsgIntEvent(sum));


               dismiss();
           }

           @Override
           public void resFailure(String message) {

               ToastUtil.showToast(message);

           }
       });

    }

}
