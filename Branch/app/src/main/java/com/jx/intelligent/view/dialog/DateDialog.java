package com.jx.intelligent.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.MonthAdapter;
import com.jx.intelligent.bean.MonthBean;
import com.jx.intelligent.util.DateUtil;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期选择对话框
 */
public class DateDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "DateDialog";
    private ImageView previousIV;
    private ImageView nextIV;
    private TextView tvMonthYear;
    private GridView gridView;
    private List<MonthBean> data;
    private MonthAdapter adapter;
    private int             currentYear;
    private int             currentMonth;
    private int             currentDay;
    private boolean         mIsClick;
    private int             mIndex;
    private int             year;
    private int             month;
    private int             day;
    private int             todayPosition;//今天所对应的角标
    private int notClickPosition = 6;//不能点击的角标

    public DateDialog(@NonNull Context context) {
        this(context,R.style.DialogStyle);
        init(context);
    }

    public DateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public DateDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public DateDialog(@NonNull Context context, int year, int monthOfYear, int dayOfMonth) {
        this(context);
        currentYear = year;
        currentMonth = monthOfYear;
        currentDay = dayOfMonth;
    }

    public DateDialog(int year, int monthOfYear, int dayOfMonth) {
        this(UIUtil.getContext(), year, monthOfYear, dayOfMonth);
    }

    private void init(@NonNull Context context) {
        data = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        initData();
        findView();
        initView();
    }

    private void initData() {
        adapter = new MonthAdapter(data);
        //        adapter.setData(data);
        currentYear = DateUtil.getCurrentYear();
        currentMonth = DateUtil.getCurrentMonth();
        currentDay = DateUtil.getCurrentDay();
        year = currentYear;
        month = currentMonth;
        day = currentDay;
        getMonthDays(currentYear, currentMonth);
    }

    private void findView() {
        gridView = (GridView)findViewById(R.id.gridView);
        tvMonthYear = (TextView)findViewById(R.id.tv_month_year);
        previousIV = (ImageView)findViewById(R.id.btn_previous_month);
        nextIV = (ImageView)findViewById(R.id.btn_next_month);
    }

    private void initView() {
        tvMonthYear.setText(currentYear+"年"+currentMonth + "月" + currentDay + "日" );
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.setHorizontalSpacing(5);
        previousIV.setOnClickListener(this);
        nextIV.setOnClickListener(this);
    }

    public void getMonthDays(int currentYear, int currentMonth) {
        currentDay = day;

        if (mIsClick) {
            tvMonthYear.setText(currentYear+"年"+currentMonth + "月");
        }
        data.clear();
        // 添加顶部星期
        data.add(new MonthBean(UIUtil.getString(R.string.text_m), false));
        data.add(new MonthBean(UIUtil.getString(R.string.text_th), false));
        data.add(new MonthBean(UIUtil.getString(R.string.text_w), false));
        data.add(new MonthBean(UIUtil.getString(R.string.text_t), false));
        data.add(new MonthBean(UIUtil.getString(R.string.text_f), false));
        data.add(new MonthBean(UIUtil.getString(R.string.text_s), false));
        data.add(new MonthBean(UIUtil.getString(R.string.text_su), false));

        int week = DateUtil.getWeekOfMonth(currentYear, currentMonth - 1, 1);//获得每月1号所对应的星期
        switch (week) {
            case 1://星期天
                LogUtil.e("星期天");
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                todayPosition = todayPosition + 6;
                notClickPosition = notClickPosition + 6;
                break;
            case 2://星期一
                LogUtil.e("星期一");
                break;
            case 3://星期二
                LogUtil.e("星期二");
                data.add(new MonthBean("", false));
                todayPosition = todayPosition + 1;
                notClickPosition = notClickPosition + 1;
                break;
            case 4://星期三
                LogUtil.e("星期三");
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                todayPosition = todayPosition + 2;
                notClickPosition = notClickPosition + 2;
                break;
            case 5://星期四
                LogUtil.e("星期四");
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                todayPosition = todayPosition + 3;
                notClickPosition = notClickPosition + 3;
                break;
            case 6://星期五
                LogUtil.e("星期五");
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                todayPosition = todayPosition + 4;
                notClickPosition = notClickPosition + 4;
                break;
            case 7://星期六
                LogUtil.e("星期六");
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                data.add(new MonthBean("", false));
                todayPosition = todayPosition + 5;
                notClickPosition = notClickPosition + 5;
                break;
        }
        // 获取指定年份下的月份下的天数
        final int days = DateUtil.getDayOfMonth(currentYear, currentMonth);
        LogUtil.e(TAG, currentYear + "年" + currentMonth + "月份一共有" + days + "天");
        for (int j = 1; j <= days; j++) {
            boolean flag = false;
            // 判断是否为今天，是则选中
            if (j == currentDay && year == currentYear && month == currentMonth) {
                flag = true;
                todayPosition = j + 6;
            }
            // 添加到数据源用于GridView显示
            data.add(new MonthBean(j, false, flag));
        }
    }

    @Override
    public void onClick(View v) {
        mIsClick = true;
        // 解决切换到上一个月或下一个月时，会选中相同日期（今天）问题
        currentDay = 0;
        notClickPosition = 6;
        if (v.getId() == R.id.btn_previous_month) {
            --currentMonth;
            if (currentMonth < 1) {
                currentMonth = 12;
                currentYear--;
            }

            getMonthDays(currentYear, currentMonth);

        } else if (v.getId() == R.id.btn_next_month) {
            ++currentMonth;
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }

            getMonthDays(currentYear, currentMonth);
        }
        if (year == currentYear && month == currentMonth) { //判断是否是当前的时间
            data.get(todayPosition).setCheckCurrTime(true);
            todayPosition = 0;
            tvMonthYear.setText(year + "年"+ month + "月" + day + "日" );
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mIndex != 0) {
            data.get(mIndex).setChecked(false);
        }

        if (position <= notClickPosition) {
            return;
        }
        LogUtil.e("position::" + position);
        mIndex = position;
        final MonthBean monthBean = data.get(position);
        if (position > 6 && position != todayPosition) {
            tvMonthYear.setText(currentYear+"年"+currentMonth + "月" + monthBean.getDay() + "日");
            monthBean.setChecked(true);
            adapter.notifyDataSetChanged();

        }

    }
}