package com.jx.intelligent.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jx.intelligent.intf.ISwitchView;


/**
 *  切换布局,用一个新的View覆盖原先的View
 */

public class OverlayViewHelper implements ISwitchView {
    public ISwitchView mSwitchView;
    public View mDataView;

    public OverlayViewHelper(View view){
        this.mDataView = view;

        /*找到父View*/
        ViewGroup parent;
        if (view.getParent() != null) {//判断是否有父view
            parent = (ViewGroup) view.getParent();
        } else {
            parent = (ViewGroup) view.getRootView().findViewById(android.R.id.content);
        }


        /*记录要显示的View在父View中的位置*/
        int childIndex = 0;
        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            if (view == parent.getChildAt(index)) {
                childIndex = index;
                break;
            }
        }

        /*重新将一个frameLayout添加进原来的View的位子中*/
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        FrameLayout frameLayout = new FrameLayout(view.getContext());
        parent.removeViewAt(childIndex);
        parent.addView(frameLayout, childIndex, layoutParams);

        /*在这个frameLayout中实现将新的View覆盖在原来的view上*/
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View floatView = new View(view.getContext());
        frameLayout.addView(view, params);
        frameLayout.addView(floatView, params);
        mSwitchView = new ReplaceViewHelper(floatView);
    }
    @Override
    public Context getContext() {
        return mSwitchView.getContext();
    }

    @Override
    public View getDataView() {
        return mDataView;
    }

    @Override
    public View getCurrentView() {
        return mSwitchView.getCurrentView();
    }

    @Override
    public void showSwitchLayout(View view) {
        mSwitchView.showSwitchLayout(view);
    }

    @Override
    public void showSwitchLayout(int layoutId) {
        showSwitchLayout(inflate(layoutId));
    }

    @Override
    public void restoreLayout() {
        mSwitchView.restoreLayout();
    }

    @Override
    public View inflate(int layoutId) {
        return mSwitchView.inflate(layoutId);
    }
}
