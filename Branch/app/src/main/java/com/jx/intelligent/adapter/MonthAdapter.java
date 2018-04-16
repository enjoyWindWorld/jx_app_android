package com.jx.intelligent.adapter;


import com.jx.intelligent.adapter.holder.BaseHolder;
import com.jx.intelligent.adapter.holder.MonthHolder;

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
