package com.jx.maneger.helper;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.util.DensityUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.view.ProgressWheel;


/**
 * 帮助切换错误，数据为空，正在加载的页面
 */

public class DiffViewHelper {
    /**
     * 切换不同视图的帮助类
     */
    private OverlayViewHelper mViewHelper;
    /**
     * 错误页面
     */
    private View mErrorView;
    /**
     * 正在加载页面
     */
    private View mLoadingView;
    /**
     * 数据为空的页面
     */
    private View mEmptyView;
    /**
     * 正在加载页面的进度环
     */
    private ProgressWheel mProgressWheel;
    private AnimationDrawable mAnimationIvDrawable;
    private ImageView mEmptyIv;
    private TextView mEmptyTv;
    private TextView mLoadingPromotTv;

    public DiffViewHelper(View view) {
        this(new OverlayViewHelper(view));
    }

    public DiffViewHelper(OverlayViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    /**
     * 设置加载中的视图
     *
     * @param view
     */
    void setLoadingView(View view) {
        mLoadingView = view;
        mLoadingView.setClickable(true);
        mLoadingPromotTv = (TextView) view.findViewById(R.id.loading_promot_tv);
        ProgressHelper progressHelper = new ProgressHelper(UIUtil.getContext());
        progressHelper.setProgressWheel((ProgressWheel) view.findViewById(R.id.loading_progress));
        progressHelper.setBarWidth(DensityUtil.dp2px(2));
        mProgressWheel = progressHelper.getProgressWheel();
        progressHelper.setBarColor(UIUtil.getColor(R.color.color_1bb6ef));

      /*  ImageView animationIv = (ImageView) view.findViewById(R.id.animation_iv);
        animationIv.setImageResource(R.drawable.animation_list_loading);
        mAnimationIvDrawable = (AnimationDrawable) animationIv.getDrawable();
        mAnimationIvDrawable.start();*/
    }

    /**
     * 设置错误视图，加载失败可重新点击刷新
     *
     * @param view
     * @param listener
     */
    void setErrorView(View view, View.OnClickListener listener) {
        mErrorView = view;
        mErrorView.setClickable(true);
        view.findViewById(R.id.refresh_error_view_ll).setOnClickListener(listener);
    }

    void setEmptyView(View view, View.OnClickListener listener) {
        mEmptyView = view;
        mEmptyView.setClickable(true);
        mEmptyIv = (ImageView) view.findViewById(R.id.refresh_empty_iv);
        mEmptyTv = (TextView) view.findViewById(R.id.refresh_empty_tv);
        view.findViewById(R.id.refresh_empty_view_ll).setOnClickListener(listener);
    }

    public void showEmptyView() {
        mViewHelper.showSwitchLayout(mEmptyView);
        stopProgressLoading();
    }

    public void showErrorView() {
        mViewHelper.showSwitchLayout(mErrorView);
        stopProgressLoading();
    }

    public void showLoadingView() {
        mViewHelper.showSwitchLayout(mLoadingView);
        startProgressLoading();
    }

    public void showDataView() {
        mViewHelper.restoreLayout();
        stopProgressLoading();
    }

    public static class Builder {
        private View mErrorView;
        private View mLoadingView;
        private View mEmptyView;
        private View mDataView;
        private View.OnClickListener mRefreshListener;

        public Builder setLoadingView(View loadingView) {
            mLoadingView = loadingView;
            return this;
        }

        public Builder setErrorView(View errorView) {
            mErrorView = errorView;
            return this;
        }

        public Builder setEmptyView(View emptyView) {
            mEmptyView = emptyView;
            return this;
        }

        public Builder setDataView(View dataView) {
            mDataView = dataView;
            return this;
        }

        public Builder setRefreshListener(View.OnClickListener refreshListener) {
            mRefreshListener = refreshListener;
            return this;
        }

        public DiffViewHelper build() {
            DiffViewHelper diffViewHelper = new DiffViewHelper(mDataView);

            if (mLoadingView != null) {
                diffViewHelper.setLoadingView(mLoadingView);
            }

            if (mErrorView != null) {
                diffViewHelper.setErrorView(mErrorView, mRefreshListener);
            }

            if (mEmptyView != null) {
                diffViewHelper.setEmptyView(mEmptyView, mRefreshListener);
            }

            return diffViewHelper;
        }
    }


    private void stopProgressLoading() {
        if (mProgressWheel != null && mProgressWheel.isSpinning()) {
            mProgressWheel.stopSpinning();
        }

        /*if(mAnimationIvDrawable != null && mAnimationIvDrawable.isRunning()){
            mAnimationIvDrawable.stop();
        }*/
    }

    private void startProgressLoading() {
        if (mProgressWheel != null && !mProgressWheel.isSpinning()) {
            mProgressWheel.spin();
        }

       /* if(mAnimationIvDrawable != null && !mAnimationIvDrawable.isRunning()){
            mAnimationIvDrawable.start();
        }*/
    }

    public void setLoadingPromotText(String text) {
        mLoadingPromotTv.setText(text);
    }

    public void setEmptyViewShowImage(int resId) {
        mEmptyIv.setImageResource(resId);
    }

    public void setEmptyViewShowText(String text) {
        mEmptyTv.setText(text);
    }

    public void releaseVaryView() {
        mErrorView = null;
        mLoadingView = null;
        mEmptyView = null;
    }
}
