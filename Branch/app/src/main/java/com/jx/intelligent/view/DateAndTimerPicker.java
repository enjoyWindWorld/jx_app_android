package com.jx.intelligent.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.jx.intelligent.R;
import com.jx.intelligent.enums.DateShowType;
import com.jx.intelligent.view.loopview.LoopListener;
import com.jx.intelligent.view.loopview.LoopView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @创建者 zhangbo
 * @创建时间 2016/6/6 9:21
 * @描述 ${TODO} 日期和时间选择器
 * @更新者 $author$
 * @更新时间
 * @更新描述 ${TODO}
 */
public class DateAndTimerPicker extends Dialog {

    private static final int MIN_YEAR = 1970;
    private Params params;

    public DateAndTimerPicker(Context context) {
        super(context);
    }

    public DateAndTimerPicker(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(Params params) {//设置参数
        this.params = params;
    }

    public interface OnDateAndTimeSelectedListener { //对选中的条目的监听
        void onDateAndTimeSelected(String[] dates);
    }

    private static final class Params {
        private boolean shadow    = true;
        private boolean canCancel = true;
        private LoopView loopYear, loopMonth, loopDay, loopHour, loopMin;
        private OnDateAndTimeSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final Params params;
        private DateShowType dateShowType;//是否显示时分控件，默认为显示

        public Builder(Context context, DateShowType dateShowType) {
            this.context = context;
            this.dateShowType = dateShowType;
            params = new Params();
        }

        /**
         * 获取当前选择的日期及时间
         * @return int[]数组形式返回。
         */
        private final String[] getCurrDateValues() {

            String currYear = params.loopYear.getCurrentItemValue();
            String currMonth = params.loopMonth.getCurrentItemValue();
            String currDay = params.loopDay.getCurrentItemValue();
            String currHour = params.loopHour.getCurrentItemValue();
            String currMin = params.loopMin.getCurrentItemValue();
            return new String[]{currYear,currMonth,currDay,currHour, currMin};
        }


        public Builder setOnDateAndTimeSelectedListener(OnDateAndTimeSelectedListener onDateAndTimeSelectedListener) {
            params.callback = onDateAndTimeSelectedListener;
            return this;
        }


        public DateAndTimerPicker create() {
            final DateAndTimerPicker dialog = new DateAndTimerPicker(context,params.shadow?R.style.CustomDialogStyle:R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_date_and_time, null);

            FrameLayout yearFrame = (FrameLayout)view.findViewById(R.id.year);
            FrameLayout monthFrame = (FrameLayout)view.findViewById(R.id.month);
            FrameLayout dayFrame = (FrameLayout)view.findViewById(R.id.day);
            FrameLayout hourFrame = (FrameLayout)view.findViewById(R.id.hour);
            FrameLayout secondFrame = (FrameLayout)view.findViewById(R.id.second);

            switch (dateShowType)
            {
                case YMDHM:
                    yearFrame.setVisibility(View.VISIBLE);
                    monthFrame.setVisibility(View.VISIBLE);
                    dayFrame.setVisibility(View.VISIBLE);
                    secondFrame.setVisibility(View.VISIBLE);
                    hourFrame.setVisibility(View.VISIBLE);
                    break;
                case YMD:
                    yearFrame.setVisibility(View.VISIBLE);
                    monthFrame.setVisibility(View.VISIBLE);
                    dayFrame.setVisibility(View.VISIBLE);
                    secondFrame.setVisibility(View.GONE);
                    hourFrame.setVisibility(View.GONE);
                    break;
                case MDHM:
                    yearFrame.setVisibility(View.GONE);
                    monthFrame.setVisibility(View.VISIBLE);
                    dayFrame.setVisibility(View.VISIBLE);
                    secondFrame.setVisibility(View.VISIBLE);
                    hourFrame.setVisibility(View.VISIBLE);
                    break;
                case HM:
                    yearFrame.setVisibility(View.GONE);
                    monthFrame.setVisibility(View.GONE);
                    dayFrame.setVisibility(View.GONE);
                    secondFrame.setVisibility(View.VISIBLE);
                    hourFrame.setVisibility(View.VISIBLE);
                    break;
            }

            final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
            final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
            final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
            final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);
            final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);



            loopDay.setArrayList(d(1, 30)); //设置默认的天数是30天
            loopDay.setNotLoop(); //设置不循环滚动

            Calendar c = Calendar.getInstance(); //获取当前的日期
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int mintue = c.get(Calendar.MINUTE);


            loopDay.setCurrentItem(day - 1);
            loopYear.setArrayList(d(MIN_YEAR, year - MIN_YEAR + 15));
            loopYear.setCurrentItem(year - MIN_YEAR);
            loopYear.setNotLoop();

            loopMonth.setArrayList(d(1, 12));
            loopMonth.setCurrentItem(month);
            loopMonth.setNotLoop();

            final LoopListener maxDaySyncListener = new LoopListener() {
                @Override
                public void onItemSelect(int item) { //监听相应的年月的改变随之改变天数
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(loopYear.getCurrentItemValue()), Integer.parseInt(loopMonth.getCurrentItemValue()) - 1, 1);
                    c.roll(Calendar.DATE, false);
                    int maxDayOfMonth = c.get(Calendar.DATE);
                    int fixedCurr = loopDay.getCurrentItem();
                    loopDay.setArrayList(d(1, maxDayOfMonth));
                    // 修正被选中的日期最大值
                    if (fixedCurr > maxDayOfMonth) fixedCurr = maxDayOfMonth - 1;
                    loopDay.setCurrentItem(fixedCurr);
                }
            };
            loopYear.setListener(maxDaySyncListener);
            loopMonth.setListener(maxDaySyncListener);


            loopHour.setArrayList(d(0, 24));//设置时间为24小时

            loopHour.setCurrentItem(hour); //默认选中12点
            loopHour.setCyclic(true); //设置循环滚动


            loopMin.setArrayList(d(0, 60));
            loopMin.setCurrentItem(mintue);

            view.findViewById(R.id.layout_ensure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDateAndTimeSelected(getCurrDateValues());
                }
            });

            view.findViewById(R.id.layout_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);

            dialog.setContentView(view);
//            dialog.setCanceledOnTouchOutside(params.canCancel); //点击外部dialog消失
//            dialog.setCancelable(params.canCancel);
            //设置对应的参数
            params.loopYear = loopYear;
            params.loopMonth = loopMonth;
            params.loopDay = loopDay;
            params.loopHour = loopHour;
            params.loopMin = loopMin;

            dialog.setParams(params);
            return dialog;
        }



        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;
                values[i - startNum] = tempValue;
            }
            return Arrays.asList(values);
        }
    }
}
