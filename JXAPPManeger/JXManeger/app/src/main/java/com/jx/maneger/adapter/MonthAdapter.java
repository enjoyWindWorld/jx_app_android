package com.jx.maneger.adapter;


import com.jx.maneger.adapter.holder.BaseHolder;
import com.jx.maneger.adapter.holder.MonthHolder;

import java.util.List;


public class MonthAdapter<MonthBean> extends SuperBaseAdapter<MonthBean> {
    public MonthAdapter(List datas) {
        super(datas);
    }

    @Override
    protected BaseHolder getSpecialHolder() {
        return new MonthHolder();
    }
}
