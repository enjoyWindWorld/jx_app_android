package com.jx.maneger.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jx.maneger.intf.ISwitchView;
import com.jx.maneger.util.LayoutUtil;


/**
 * 切换布局，用一个新的View替换原先的View
 */

public class ReplaceViewHelper implements ISwitchView {
    public View mDataView;
    public View mCurrentView;

    public ViewGroup mParentView;
    public ViewGroup.LayoutParams mLayoutParams;
    public int                    mViewIndex;

    public ReplaceViewHelper(View dataView) {

        /*记录显示数据的View*/
        this.mDataView = dataView;

        mLayoutParams = dataView.getLayoutParams();

        /*记录父View*/
        if (dataView.getParent() != null) {
            mParentView = (ViewGroup) dataView.getParent();
        } else {
            mParentView = (ViewGroup) dataView.getRootView();
        }

        /*记录要显示的View在父View中的位置*/
        int childCount = mParentView.getChildCount();
        for (int index = 0; index < childCount; index++) {
            if (dataView == mParentView.getChildAt(index)) {
                mViewIndex = index;
                break;
            }
        }

        this.mCurrentView = dataView;

    }


    @Override
    public Context getContext() {
        return mDataView.getContext();
    }

    @Override
    public View getDataView() {
        return mDataView;
    }

    @Override
    public View getCurrentView() {
        return mCurrentView;//dateview
    }

    @Override
    public void showSwitchLayout(View view) {
        if (view == null) {
            return;
        }
        this.mCurrentView = view;

        /*如果要显示的View跟已显示View一样，就不用切换了*/
        if (mParentView.getChildAt(mViewIndex) != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            /*切换的时候，先移除原先显示的View,再显示需要的View*/
            mParentView.removeViewAt(mViewIndex);
            mParentView.addView(view, mViewIndex, mLayoutParams);
        }
    }

    @Override
    public void showSwitchLayout(int layoutId) {

    }

    @Override
    public void restoreLayout() {
        showSwitchLayout(mDataView);
    }

    @Override
    public View inflate(int layoutId) {
        return LayoutUtil.inflate(mDataView.getContext(),layoutId);
    }
}
