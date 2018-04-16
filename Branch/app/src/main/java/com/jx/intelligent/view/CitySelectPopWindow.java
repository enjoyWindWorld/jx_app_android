package com.jx.intelligent.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.bean.CcityBean;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.util.AddressData;
import com.jx.intelligent.util.DensityUtil;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.view.loopview.LoopListener;
import com.jx.intelligent.view.loopview.LoopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class CitySelectPopWindow extends PopupWindow {

    private static final String TAG = "CitySelectPopWindow";
    private View mMenuView;

    private String cityTxt;
    int pPWindowSelect;
    TextView grxx_xbxz_qx, grxx_xbxz_qd;

    private RelativeLayout.LayoutParams layoutParams;

    private Activity mContext;

    private String sheng = "", shi = "", qu = "";

    public CitySelectPopWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.ppwidow_city_selection, null);
        grxx_xbxz_qx = (TextView) mMenuView.findViewById(R.id.grxx_xbxz_qx);
        grxx_xbxz_qd = (TextView) mMenuView.findViewById(R.id.grxx_xbxz_qd);


        layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);


        initWheelCityView(mMenuView);
        //取消按钮
        grxx_xbxz_qx.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        grxx_xbxz_qd.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationBottomTo);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
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

    List<CcityBean> mAllCities;
    DBManager dbManager;

    LoopView country;
    LoopView city;
    LoopView ccity;

    List<String> shengF;
    List<CcityBean> shengFBean;
    List<String> di;
    List<CcityBean> diBean;
    List<String> xian;
    List<CcityBean> xianBean;

    String shengStr;
    String diStr;

    private void initWheelCityView(View contentView) {

        shengF = new ArrayList<String>();
        di = new ArrayList<String>();
        xian = new ArrayList<String>();
        shengFBean = new ArrayList<CcityBean>();
        diBean = new ArrayList<CcityBean>();
        xianBean = new ArrayList<CcityBean>();
        country = (LoopView) contentView
                .findViewById(R.id.wheelcity_country);
        final String countries[] = AddressData.PROVINCES;
//        CountryAdapter conuAda = new CountryAdapter(mContext);
//        conuAda.setTextSize(DensityUtil.px2sp(34));

        country.setTextSize(DensityUtil.px2sp(getScaleTxt()));
        country.setNotLoop();

//        country.setViewAdapter(conuAda);

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        city = (LoopView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setTextSize(DensityUtil.px2sp(getScaleTxt()));
        city.setNotLoop();
        // 地区选择
        ccity = (LoopView) contentView
                .findViewById(R.id.wheelcity_ccity);
        ccity.setTextSize(DensityUtil.px2sp(getScaleTxt()));
        ccity.setNotLoop();


        dbManager = new DBManager(mContext);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCitiesSF();


        for (int i = 0; i < mAllCities.size(); i++) {

            if (mAllCities.get(i).level == 1) {
                shengFBean.add(mAllCities.get(i));
                shengF.add(mAllCities.get(i).name);
            }
            if (mAllCities.get(i).level == 2 && mAllCities.get(i).sheng.equals("11")) {
                diBean.add(mAllCities.get(i));
                di.add(mAllCities.get(i).name);
            }
            if (mAllCities.get(i).level == 3 && mAllCities.get(i).sheng.equals("11") && mAllCities.get(i).di.equals("01")) {
                xianBean.add(mAllCities.get(i));
                xian.add(mAllCities.get(i).name);
            }


        }

        Log.e(TAG, "~~~di.sz=" + di.size());
        city.setArrayList(di);
        Log.e(TAG, "~~~xian.sz=" + xian.size());
        ccity.setArrayList(xian);
        updateCountries(country, shengF);


        country.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {


                updateCities(city, item);

            }
        });

        city.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                updatecCities(ccity,
                        item);

            }
        });

        ccity.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {

            }
        });


        country.setCurrentItem(0);// 设置北京

        city.setCurrentItem(0);
        ccity.setCurrentItem(0);


    }

    /**
     * Updates the city wheel
     */
    private void updateCountries(LoopView city, List<String> cities) {


        city.setArrayList(cities);
        city.setCurrentItem(0);
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(LoopView city, int index) {

        if (shengFBean.size() < country.getCurrentItem()) {
            return;
        }


        shengStr = shengFBean.get(country.getCurrentItem()).sheng;

        di.clear();
        diBean.clear();
        for (int i = 0; i < mAllCities.size(); i++) {

            if (mAllCities.get(i).sheng.equals(shengStr) && mAllCities.get(i).level == 2) {

                di.add(mAllCities.get(i).name);
                diBean.add(mAllCities.get(i));


            }

        }

        if (di.size() == 0) {
            di.add("-");

            city.setArrayList(di);
        } else {

            city.setArrayList(di);
        }


        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(LoopView ccity, int index) {


        if (di.get(city.getCurrentItem()).equals("-")) {
            xian.clear();
        } else {
            diStr = diBean.get(city.getCurrentItem()).di;


            xian.clear();
            xianBean.clear();
            for (int i = 0; i < mAllCities.size(); i++) {

                if (mAllCities.get(i).sheng.equals(shengStr) && mAllCities.get(i).di.equals(diStr) && mAllCities.get(i).level == 3) {


                    xianBean.add(mAllCities.get(i));
                    xian.add(mAllCities.get(i).name);
                }

            }
        }


        if (xian.size() == 0) {
            xian.add("-");
            ccity.setArrayList(xian);
        } else {

            ccity.setArrayList(xian);
        }


        ccity.setCurrentItem(0);
    }


    public List<Map<String, String>> getPpWindowSelect() {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();


        Map<String, String> map;

        String cityTxt;

        if (di.get(city.getCurrentItem()).equals("-") && xian.get(ccity.getCurrentItem()).equals("-")) {
            cityTxt = shengF.get(country.getCurrentItem());
            map = new HashMap<String, String>();
            map.put("addrCode", shengFBean.get(country.getCurrentItem()).code);
            list.add(map);

            sheng = shengF.get(country.getCurrentItem());

        } else if (xian.get(ccity.getCurrentItem()).equals("-") && !di.get(city.getCurrentItem()).equals("-")) {
            cityTxt = shengF.get(country.getCurrentItem())
                    +"-"
                    + di.get(city.getCurrentItem());
            map = new HashMap<String, String>();
            map.put("addrCode", diBean.get(city.getCurrentItem()).code);
            list.add(map);

            sheng = shengF.get(country.getCurrentItem());
            shi = di.get(city.getCurrentItem());

        } else {
            cityTxt = shengF.get(country.getCurrentItem())
                    +"-"
                    + di.get(city.getCurrentItem())
                    +"-"
                    + xian.get(ccity.getCurrentItem());
            map = new HashMap<String, String>();
            map.put("addrCode", xianBean.get(ccity.getCurrentItem()).code);
            list.add(map);

            sheng = shengF.get(country.getCurrentItem());
            shi = di.get(city.getCurrentItem());
            qu = xian.get(ccity.getCurrentItem());
        }

        map = new HashMap<String, String>();
        map.put("addrStr", cityTxt);


        list.add(map);

        return list;

    }

    int getScaleTxt()
    {
        int screenWidth = ScreenSizeUtil.getInstance(mContext).getScreenWidth();
        int scaleTxt = 0;

        if (screenWidth <= 360)
        {
            scaleTxt = 25;
        }
        else if (screenWidth <= 720 && screenWidth > 360)
        {
            scaleTxt = 35;
        }
        else if(screenWidth > 720)
        {
            scaleTxt = 45;
        }

        return scaleTxt;
    }

    public String getSheng() {
        return sheng;
    }

    public String getShi() {
        return shi;
    }

    public String getQu() {
        return qu;
    }
}
