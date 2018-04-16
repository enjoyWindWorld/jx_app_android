package com.jx.intelligent.ui.activitise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.SplashAdapter;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.util.SharedPreferencesUtil;
import com.jx.intelligent.util.UIUtil;

import java.util.ArrayList;

/**
 * Created by 王云 on 2017/5/18 0018.
 */

public class GuideUi extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager mSplash_pager;
    private int ImageDatas[] = {R.mipmap.splash1, R.mipmap.splash2, R.mipmap.splash3};
    private ArrayList<ImageView> mIcons;
    private ImageView mPagerPic;
    private View mDots;
    private LinearLayout mPoint_container;
    private int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        init();
        initData();
    }

    protected void init() {
        mSplash_pager = (ViewPager) findViewById(R.id.guide_pager);
        mPoint_container = (LinearLayout) findViewById(R.id.point_container);
        mPoint_container.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mIcons = new ArrayList<>();
        for (int i = 0; i < ImageDatas.length; i++) {
            mPagerPic = new ImageView(UIUtil.getContext());
            mDots = new View(UIUtil.getContext());
            //初始化小圆点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(5, 0, 5, 0);
            //设置小圆点的参数
            mDots.setLayoutParams(params);
            if(i==0){
                mDots.setBackgroundResource(R.mipmap.selecteddot);
            }else {
                mDots.setBackgroundResource(R.mipmap.normaldot);
            }
            mPagerPic.setBackgroundResource(ImageDatas[i]);
            mPagerPic.setOnClickListener(GuideUi.this);
            mIcons.add(mPagerPic);
            mPoint_container.addView(mDots);

        }

        //给viewpager设置adapter
        SplashAdapter adapter = new SplashAdapter(mIcons);
        mSplash_pager.setAdapter(adapter);
        //给viewpager设置滑动监听事件
        mSplash_pager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.postion = position;
    }

    @Override
    public void onPageSelected(int position) {
        //如果点这个view不为空 那么就给他设置背景为默认背景
        if(mDots != null){
            mDots.setBackgroundResource(R.mipmap.normaldot);
        }
        //拿到当前装点的容器中 选择的第position个点
        View mChildDot = mPoint_container.getChildAt(position);
        //让选中的那个点设置背景为 选中背景
        mChildDot.setBackgroundResource(R.mipmap.selecteddot);

        mDots=mChildDot;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * GuideUi界面中开始体验按钮的点击监听事件
     */
    @Override
    public void onClick(View v) {
        if(postion == ImageDatas.length -1)
        {
            SharedPreferencesUtil.putBoolean(GuideUi.this, "notOpenFistTime", true);
            setResult(Constant.SHOW_GUIDE);
            finish();
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再点击一次将退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent();
            setResult(Constant.EXIT_APP, intent);
            finish();
        }
    }
}

