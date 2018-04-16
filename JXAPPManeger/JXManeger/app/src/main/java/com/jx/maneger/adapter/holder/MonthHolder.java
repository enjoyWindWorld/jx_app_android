package com.jx.maneger.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.adapter.holder.bean.MonthBean;
import com.jx.maneger.util.LayoutUtil;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.UIUtil;


public class MonthHolder extends BaseHolder<MonthBean> {

    private TextView mDayTV;

    @Override
    public View initHolderView() {
        View dateView = LayoutUtil.inflate(R.layout.item_date_picker);
        mDayTV = (TextView) dateView.findViewById(R.id.day_tv);
        return dateView;
    }

    @Override
    public void setDataAndRefreshUI(MonthBean data) {
        if (StringUtil.isEmpty(data.getDayOfMonth())) {
            int day = data.getDay();
            if (day == 0) {
                mDayTV.setText("");
            }else{
                mDayTV.setText("" + data.getDay());
            }
        } else
            mDayTV.setText("" + data.getDayOfMonth());
        if (data.isChecked()) {
            LogUtil.e("shape_circle_yellow");
            mDayTV.setBackgroundResource(R.drawable.shape_circle_yellow);
        } else {
            if (data.isCheckCurrTime()) {
                LogUtil.e("shape_circle_red");
                mDayTV.setBackgroundResource(R.drawable.shape_circle_red);
            } else {
                mDayTV.setBackgroundColor(UIUtil.getColor(R.color.white));
            }
        }


    }
}
