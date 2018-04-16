package com.jx.intelligent.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.helper.DiffViewHelper;
import com.jx.intelligent.util.LayoutUtil;
import com.jx.intelligent.util.StatusBarUtil;
import com.jx.intelligent.util.UIUtil;


public abstract class BaseToolBarActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private FrameLayout mContentFl;
    private TextView mTitleTv;
    private NavigationView mNavigationView;
    public Context mContext;
    public DiffViewHelper mDiffViewHelper;
    private ActivityManager mActivityManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_and_drawerlaout);
        StatusBarUtil.setColor(this, UIUtil.getColor(R.color.color_15aa5a));
        mContext = this;
        initView();
        init();

        mActivityManager = ActivityManager.getAppManager();
        mActivityManager.addActivity(this);
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.custom_tl);
        mTitleTv = (TextView) findViewById(R.id.custom_title_tv);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContentFl = (FrameLayout) findViewById(R.id.content_fl);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (getTargetView() != null) {
            mDiffViewHelper = new DiffViewHelper.Builder()
                    .setDataView(getTargetView())
                    .setEmptyView(LayoutUtil.inflate(mContext, R.layout.layout_emptyview))
                    .setErrorView(LayoutUtil.inflate(mContext, R.layout.layout_errorview))
                    .setLoadingView(LayoutUtil.inflate(mContext, R.layout.layout_loadingview))
                    .setRefreshListener(this)
                    .build();
        }

        initTitlebar(mToolBar, mDrawerLayout,mTitleTv,mNavigationView);

    }

    public View getTargetView() {
        if (setContentLayout() == 0) {
            return null;
        } else {
            View contentView = LayoutUtil.inflate(mContext, setContentLayout());
            mContentFl.addView(contentView);
            findView(contentView);
            return mContentFl;
        }
    }

    protected abstract void init();

    protected abstract void findView(View contentView);

    protected abstract int setContentLayout();


    /**
     * 继承该activity获取对应控件进行操作
     * @param toolBar
     * @param drawerLayout
     * @param titleTv
     */
    protected abstract void initTitlebar(Toolbar toolBar, DrawerLayout drawerLayout, TextView titleTv, NavigationView navigationView);


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
     * 错误视图刷新的监听，子类只需重写该方法
     */
    private void errorRetryRefreshListener() {

    }

    /**
     * 空视图刷新的监听,子类只需重写该方法
     */
    private void emptyRetryRefreshListener() {

    }

    /**
     * 申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDiffViewHelper != null) {
            mDiffViewHelper.releaseVaryView(); //销毁后释放View
        }

        mActivityManager.finishActivity();
    }
}
