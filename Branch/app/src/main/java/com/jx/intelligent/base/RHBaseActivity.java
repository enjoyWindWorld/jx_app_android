package com.jx.intelligent.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.helper.DiffViewHelper;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.util.LayoutUtil;
import com.jx.intelligent.util.StatusBarUtil;
import com.jx.intelligent.util.UIUtil;


/**
 * activity基类，实现通用简单功能
 */

public abstract class RHBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mContentFl;
    public DiffViewHelper mDiffViewHelper;
    private ActivityManager mActivityManager;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        StatusBarUtil.setColor(this, UIUtil.getColor(R.color.color_1bb6ef));
        this.mContext = this;
        initTitle();
        mContentFl = (FrameLayout) findViewById(R.id.base_content_fl);

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
        mActivityManager = ActivityManager.getAppManager();
        mActivityManager.addActivity(this);
    }

    /**
     * 初始化操作
     */
    protected abstract void init();

    @CallSuper
    @Override
    public void onResume() {
        // 竖屏显示
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onResume();
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

    /**
     * 设置内容布局文件资源文件ID
     *
     * @return
     */
    protected abstract int setContentLayout();

    /**
     * 初始化顶部标题状态栏
     */
    protected abstract void initTitle();

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


    protected void hideSoftKeyboard(final EditText view)
    {
        view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                KeyboardUtil.hideSoftKeyboard(view);
                return false;
            }
        });
    }
}
