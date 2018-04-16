package com.jx.intelligent.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jx.intelligent.R;
import com.jx.intelligent.helper.DiffViewHelper;
import com.jx.intelligent.util.LayoutUtil;


/**
 * fragment基类，实现通用简单功能
 */

public abstract class RHBaseFragment extends Fragment implements View.OnClickListener {
    private FrameLayout mContentFl;
    public DiffViewHelper mDiffViewHelper;
    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
//        View view = LayoutUtil.inflate(mContext, R.layout.base_layout);
        View view=inflater.inflate(R.layout.base_layout, container, false);
        mContentFl = (FrameLayout) view.findViewById(R.id.base_content_fl);
        initTitle(view);

        if (getTargetView() != null) {
            mDiffViewHelper = new DiffViewHelper.Builder()
                    .setDataView(getTargetView())
                    .setEmptyView(LayoutUtil.inflate(mContext, R.layout.layout_emptyview))
                    .setErrorView(LayoutUtil.inflate(mContext, R.layout.layout_errorview))
                    .setLoadingView(LayoutUtil.inflate(mContext, R.layout.layout_loadingview))
                    .setRefreshListener(this)
                    .build();
        }


        init();
        return view;
    }

    protected abstract void init();


    public View getTargetView() {
        View contentView = LayoutUtil.inflate(mContext, setContentLayout());
        mContentFl.addView(contentView);
        findView(contentView);
        return mContentFl;
    }


    /**
     * 设置内容布局文件资源文件ID
     *
     * @return
     */
    protected abstract int setContentLayout();

    /**
     * 初始化顶部标题状态栏
     *
     * @param titleView
     */
    protected abstract void initTitle(View titleView);

    /**
     * 查找控件
     *
     * @param contentView
     */
    protected abstract void findView(View contentView);

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.refresh_empty_view_ll) {
            emptyRetryRefreshListener();
        } else if (id == R.id.refresh_error_view_ll) {
            errorRetryRefreshListener();
        }
    }

    /**
     * 空视图刷新的监听,子类只需重写该方法
     */
    public void emptyRetryRefreshListener() {

    }

    /**
     * 错误视图刷新的监听，子类只需重写该方法
     */
    public void errorRetryRefreshListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDiffViewHelper != null) {
            mDiffViewHelper.releaseVaryView(); //销毁后释放View
        }
    }
}
