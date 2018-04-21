package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ColorHListViewAdapter;
import com.jx.intelligent.adapter.jxAdapter.ProductImageGroupAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.HomeProductDao;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ProductDetailResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.HorizontalListView;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14 0014.
 * 商品详情
 */

public class ProductDetailActivity extends RHBaseActivity {

    private static final String TAG = "ProductDetailActivity";
    TextView txt_product_name, txt_year_price, txt_flow_price, txt_model, txt_voltage_frequency, txt_power, txt_pressure, txt_temperature, txt_flow, txt_mode,
            txt_range, txt_quality, txt_gross_weight, txt_weight, txt_size, txt_packing_size, txt_color;
    RelativeLayout layout_introduce, layout_price, layout_service, layout_promise;
    private ViewPager viewPager;
    private List<View> listViews = null;
    private ProductImageGroupAdapter adapter;
    HorizontalListView pro_info_color_selet;
    Button btn_pay;
    String imgUrls, imgUrl, proName, color, proid;
    TitleBarHelper titleBarHelper;
    double year_money = 0, flow_money = 0;
    int id, payType;
    ColorHListViewAdapter colorHListViewAdapter;
    HomeProductDao dao;
    ProductDetailResult.Data.DetailInfo detailInfo;
    List<ProductDetailResult.Data.ColorInfo> colorInfos;
    ProgressWheelDialog dialog;
    int pledge;
    private TextView mPledge_tv;

    @Override
    protected void init() {
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            colorInfos = new ArrayList<ProductDetailResult.Data.ColorInfo>();
            dao = new HomeProductDao();
            getProductDetail(id);
        }

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_pro_detail;
    }

    @Override
    protected void initTitle() {

        titleBarHelper = new TitleBarHelper(ProductDetailActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void findView(View contentView) {
        txt_color = (TextView) contentView.findViewById(R.id.txt_color);
        txt_year_price = (TextView) contentView.findViewById(R.id.txt_year_price);
        txt_flow_price = (TextView) contentView.findViewById(R.id.txt_flow_price);
        //型号
        txt_model = (TextView) contentView.findViewById(R.id.txt_model);
        //加热功率
        txt_power = (TextView) contentView.findViewById(R.id.txt_power);
        //产品毛重
        txt_gross_weight = (TextView) contentView.findViewById(R.id.txt_gross_weight);
        //产品净重
        txt_weight = (TextView) contentView.findViewById(R.id.txt_weight);
        //产品尺寸
        txt_size = (TextView) contentView.findViewById(R.id.txt_size);
        //包装尺寸
        txt_packing_size = (TextView) contentView.findViewById(R.id.txt_packing_size);
        //额定电压/频率：
        txt_voltage_frequency = (TextView) contentView.findViewById(R.id.txt_voltage_frequency);
        //进水压力
        txt_pressure = (TextView) contentView.findViewById(R.id.txt_pressure);
        //环境温度
        txt_temperature = (TextView) contentView.findViewById(R.id.txt_temperature);
        //净水流量
        txt_flow = (TextView) contentView.findViewById(R.id.txt_flow);
        //过滤方式
        txt_mode = (TextView) contentView.findViewById(R.id.txt_mode);
        //适用水范围
        txt_range = (TextView) contentView.findViewById(R.id.txt_range);
        //出水水质
        txt_quality = (TextView) contentView.findViewById(R.id.txt_quality);


        txt_product_name = (TextView) contentView.findViewById(R.id.txt_product_name);
        txt_year_price = (TextView) contentView.findViewById(R.id.txt_year_price);
        txt_flow_price = (TextView) contentView.findViewById(R.id.txt_flow_price);

        btn_pay = (Button) contentView.findViewById(R.id.btn_pay);
        layout_introduce = (RelativeLayout) contentView.findViewById(R.id.layout_introduce);
        layout_price = (RelativeLayout) contentView.findViewById(R.id.layout_price);
        layout_service = (RelativeLayout) contentView.findViewById(R.id.layout_service);
        layout_promise = (RelativeLayout) contentView.findViewById(R.id.layout_promise);
        viewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        pro_info_color_selet = (HorizontalListView) contentView.findViewById(R.id.pro_info_color_selet);

        dialog = new ProgressWheelDialog(ProductDetailActivity.this);
        //判断userID
        //这个地方是用来判断登录的账号是否是 测试账号
        if(SesSharedReferences.getUserId(UIUtil.getContext()).equals("121")){
//            txt_year_price.setVisibility(View.GONE);
            payType = 1;
        }else {
            txt_flow_price.setVisibility(View.GONE);
            payType = 0;
        }

        //押金textView
        mPledge_tv = (TextView) contentView.findViewById(R.id.txt_flow_pledge);

//        radioGroup.setOnCheckedChangeListener(this);
        btn_pay.setOnClickListener(this);
        layout_introduce.setOnClickListener(this);
        layout_price.setOnClickListener(this);
        layout_service.setOnClickListener(this);
        layout_promise.setOnClickListener(this);
        txt_year_price.setOnClickListener(this);
        txt_flow_price.setOnClickListener(this);



        pro_info_color_selet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                colorHListViewAdapter.setSelectIndex(i);
                colorHListViewAdapter.notifyDataSetChanged();
                imgUrls = colorInfos.get(i).getUrl();
                color = colorInfos.get(i).getPic_color();
                if (!StringUtil.isEmpty(imgUrls)) {
                    showImgGroup(imgUrls);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_pay:
                if (Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) && detailInfo != null) {
//                    if (SesSharedReferences.getUsrLoginState(ProductDetailActivity.this)) {
//                        if (payType == 0) {
//                            goSetPayInfo(year_money);
//                        } else {
//                            goSetPayInfo(flow_money);
//                        }
//                    } else {
//                        intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
//                        intent.putExtra("flag", Constant.LOGIN_FLAG);
//                        startActivityForResult(intent, Constant.REQUEST_CODE);
//                    }
                    //流量或者是包年的选择跳转
                    if (payType == 0) {
                        goSetPayInfo(year_money);
                    } else {

                        goSetPayInfo(flow_money);
                    }
                }
                break;
            case R.id.layout_introduce:
                goWebJX(Constant.PRODUCT_INTRODUCE_AND_SERVICE_UTL);
                break;
            case R.id.txt_year_price:
                payType = 0;
                //王云
                //如果选择的是包年 那么 包流量的费用就隐藏
//                txt_flow_price.setVisibility(View.GONE);
                txt_year_price.setBackground(getResources().getDrawable(R.mipmap.img_btn_selector));
                txt_year_price.setTextColor(getResources().getColor(R.color.color_fd6212));
                txt_flow_price.setBackground(getResources().getDrawable(R.mipmap.img_btn_normal));
                txt_flow_price.setTextColor(getResources().getColor(R.color.color_777777));
                break;
            case R.id.txt_flow_price:
                payType = 1;
                //如果选择的是包流量 那么 包年的费用就隐藏
//                txt_year_price.setVisibility(View.GONE);
                txt_year_price.setBackground(getResources().getDrawable(R.mipmap.img_btn_normal));
                txt_year_price.setTextColor(getResources().getColor(R.color.color_777777));
                txt_flow_price.setBackground(getResources().getDrawable(R.mipmap.img_btn_selector));
                txt_flow_price.setTextColor(getResources().getColor(R.color.color_fd6212));
                break;
        }
    }

    void initViewPage(List<ProductDetailResult.Data.ColorInfo> colorInfos) {
        String[] colorArr = new String[colorInfos.size()];
        String[] colorNameArr = new String[colorInfos.size()];
        for (int i = 0; i < colorInfos.size(); i++) {
            if (!StringUtil.isEmpty(colorInfos.get(i).getPic_color())) {
                colorArr[i] = colorInfos.get(i).getPic_color();
            } else {
                colorArr[i] = "无";
            }

            if (!StringUtil.isEmpty(colorInfos.get(i).getTone())) {
                colorNameArr[i] = colorInfos.get(i).getTone();
            } else {
                colorNameArr[i] = "#000000";
            }
        }

        colorHListViewAdapter = new ColorHListViewAdapter(ProductDetailActivity.this, colorArr, colorNameArr);
        pro_info_color_selet.setAdapter(colorHListViewAdapter);

        colorHListViewAdapter.setSelectIndex(0);
        colorHListViewAdapter.notifyDataSetChanged();
        color = colorInfos.get(0).getPic_color();
        imgUrls = colorInfos.get(0).getUrl();
        if (!StringUtil.isEmpty(imgUrls)) {
            showImgGroup(imgUrls);
        }
    }

    void showImgGroup(final String imgUrls) {
        String[] img_arr = imgUrls.split(",");
        listViews = new ArrayList<View>();
        for (int i = 0; i < img_arr.length; i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.layout_prodount_goup_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.img_product);
            GlideHelper.setImageView(ProductDetailActivity.this, img_arr[i], iv);
            listViews.add(view);
        }

        imgUrl = img_arr[0];

        adapter = new ProductImageGroupAdapter(listViews);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void goSetPayInfo(double money) {
        Intent intent = new Intent(ProductDetailActivity.this, SetPayInfoActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("proName", proName);
        intent.putExtra("proid", proid);
        intent.putExtra("color", color);
        intent.putExtra("payType", payType);
        //添加一个押金字段
        intent.putExtra("pledge", pledge);
        startActivityForResult(intent, Constant.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.LOGIN_OK) {
//            goSetPayInfo();
        }
    }

    void getProductDetail(int id) {
        getPreCache(id);
        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
            return;
        }

        dialog.show();
        dao.getHomeProdustDetailTask(id, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ProductDetailResult productDetailResult = (ProductDetailResult) object;
                showDataView(productDetailResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    void showDataView(ProductDetailResult productDetailResult) {
        if (productDetailResult != null && productDetailResult.getData().size() > 0) {
            ProductDetailResult.Data data = productDetailResult.getData().get(0);
            if (data.getColor().size() > 0) {
                colorInfos = data.getColor();
                initViewPage(colorInfos);
            }

            if (data.getDetail().size() > 0) {
                detailInfo = data.getDetail().get(0);
                showProDetail();
            }
            //王云  押金数据在这里
            pledge = data.getPaytype().get(0).getPay_pledge();

           mPledge_tv.setText("履约金:￥"+pledge+""+"元");


            for (ProductDetailResult.Data.PayType payType : data.getPaytype()) {
                if (payType.getPaytype().equals("0")) {
                    if (!StringUtil.isEmpty(payType.getPrice())) {
                        year_money = Double.parseDouble(payType.getPrice());
                        txt_year_price.setText("包年费用:￥" + payType.getPrice() + "元");
                    }
                } else if (payType.getPaytype().equals("1")) {
                    if (!StringUtil.isEmpty(payType.getPrice())) {
                        flow_money = Double.parseDouble(payType.getPrice());
                        txt_flow_price.setText("流量预付:￥" + payType.getPrice() + "元");
                    }
                }
            }
        } else {
            ToastUtil.showToast("获取产品详情失败");
        }
    }

    void showProDetail() {
        txt_product_name.setText(detailInfo.getName());
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
        txt_color.setText(colorInfos.get(0).getPic_color());
        titleBarHelper.setMiddleTitleText(detailInfo.getName());
        proName = detailInfo.getName();
        proid = detailInfo.getId();
        imgUrls = colorInfos.get(0).getUrl();
        color = colorInfos.get(0).getPic_color();

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
            ProductDetailResult productDetailResult = (ProductDetailResult) new Gson().fromJson(js, ProductDetailResult.class);
            showDataView(productDetailResult);
        }
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
}
