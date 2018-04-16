package com.jx.maneger.adapter.holder;

import android.view.View;

/**
 * 抽取一个基类的holder
 */
public abstract class BaseHolder<HOLDERTYPE> {

    public View mHolderView;

    public BaseHolder() {
        mHolderView = initHolderView();
        mHolderView.setTag(this);
    }

    /**
     * 初始化HolderView,留给子类实现
     *
     * @return
     */
    public abstract View initHolderView() ;

    /**
     * 用于设置数据和刷新UI，供子类实现
     * @param data
     */
    public abstract void setDataAndRefreshUI(HOLDERTYPE data);

}
