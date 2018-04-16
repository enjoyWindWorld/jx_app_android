package com.jx.intelligent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.jx.intelligent.R;
import com.jx.intelligent.adapter.BannerAdapter;
import com.jx.intelligent.intf.ViewPagerOnItemClickListener;
import com.jx.intelligent.ui.activitise.service.ServiceDetailActivity;
import com.jx.intelligent.util.DensityUtil;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.util.StringUtil;

import java.util.List;

/**
 * 自定义Banner无限轮播控件
 */

public class BannerView extends RelativeLayout implements ViewPager.OnPageChangeListener, ViewPagerOnItemClickListener {
    //选中显示Indicator
    private int mSelectRes = R.mipmap.icon_banner_ellipse;

    //非选中显示Indicator
    private int mUnSelcetRes = R.mipmap.icon_banner_circle;
    private AutoScrollLoopViewPager mViewPager;
    private LinearLayout mPoiontsContainer;
    private Context mContext;
    private ViewPagerOnItemClickListener mListener;
    private int mLength;
    private BannerAdapter mAdapter;
    private boolean mIsLoopScroll;
    private String mDelayTime;
    private int[] mImages;
    private List<String> mImageUrls;
    private boolean mIsCycle;

    View view;

    public BannerView(Context context) {

        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        //默认自动轮播
        mIsLoopScroll = typedArray.getBoolean(R.styleable.BannerView_isLoopSrcoll, true);
        mDelayTime = typedArray.getString(R.styleable.BannerView_delay_time);
        if (StringUtil.isEmpty(mDelayTime)) {
            mDelayTime = "3000";
        }
        mIsCycle = typedArray.getBoolean(R.styleable.BannerView_isCycle, true);//默认可循环
    }

    public void setImagesUrl(List<String> imagesUrl) {
        initLayout();
        initImgFromNet(imagesUrl);
        setData();
    }

    public void setImagesRes(int[] imagesRes) {
        initLayout();
        initImgFromRes(imagesRes);
        setData();
    }


    private void initLayout() {
        view = View.inflate(mContext, R.layout.layout_custom_banner, this);
        mViewPager = (AutoScrollLoopViewPager) view.findViewById(R.id.banner_viewpager);
        mPoiontsContainer = (LinearLayout) view.findViewById(R.id.banner_points_container);
        mPoiontsContainer.removeAllViews();
    }

    private void initImgFromNet(List<String> imagesUrl) {
        this.mImageUrls = imagesUrl;
        mLength = imagesUrl.size();

        for (int i = 0; i < mLength; i++) {
            View dotView = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dp2px(15),
                    DensityUtil.dp2px(5));
            params.leftMargin = 2;
            params.rightMargin = 2;
            dotView.setBackgroundResource(mSelectRes);
            if (i != 0) {
                params.leftMargin = DensityUtil.dp2px(2);
                dotView.setBackgroundResource(mUnSelcetRes);
            }
            mPoiontsContainer.addView(dotView, params);
        }
    }

    private void initImgFromRes(int[] imagesRes) {
        this.mImages = imagesRes;
        mLength = imagesRes.length;

        for (int i = 0; i < mLength; i++) {
            View dotView = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dp2px(15),
                    DensityUtil.dp2px(5));
            params.leftMargin = 2;
            params.rightMargin = 2;
            dotView.setBackgroundResource(mSelectRes);
            if (i != 0) {
                params.leftMargin = DensityUtil.dp2px(2);
                dotView.setBackgroundResource(mUnSelcetRes);
            }
            mPoiontsContainer.addView(dotView, params);
        }
    }

    private void setData() {
//        if (mAdapter == null) {
            if (mImages != null && mImages.length > 0) {
                mAdapter = new BannerAdapter(mContext, mImages);
            } else {
                mAdapter = new BannerAdapter(mContext, mImageUrls);
            }
//        }

        mViewPager.setAdapter(mAdapter);
        if (mIsLoopScroll) {
            mViewPager.setInterval(Long.parseLong("3000"));
            mViewPager.startAutoScroll();
        }

        mViewPager.setCycle(mIsCycle);
        mViewPager.addOnPageChangeListener(this);

        mAdapter.setViewPagerOnItemClickListener(this);

        if(mAdapter.getCount() > 1)
        {
            mPoiontsContainer.setVisibility(VISIBLE);
            mViewPager.setCanScroll(true);
        }
        else
        {
            mPoiontsContainer.setVisibility(INVISIBLE);
            mViewPager.setCanScroll(false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mIsCycle) {
            for (int i = 0; i < mLength; i++) {
                View childView = mPoiontsContainer.getChildAt(i);
                if (position == i + 1) {
                    childView.setBackgroundResource(R.mipmap.icon_banner_ellipse);
                } else {
                    childView.setBackgroundResource(R.mipmap.icon_banner_circle);
                }
            }
        } else {
            for (int i = 0; i < mLength; i++) {
                View childView = mPoiontsContainer.getChildAt(i);
                if (position == i) {
                    childView.setBackgroundResource(R.mipmap.icon_banner_ellipse);
                } else {
                    childView.setBackgroundResource(R.mipmap.icon_banner_circle);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置ViewPager的Item点击回调事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        if (mListener != null) {
            mListener.onItemClick(position);
        }
    }

    /**
     * 设置ViewPager的Item点击回调监听
     */
    public void setViewpagerItemClickListener(ViewPagerOnItemClickListener listener) {
        this.mListener = listener;
    }

    public void startAutoScroll() {
        mViewPager.startAutoScroll();
    }

    public void stopAutoScroll() {
        mViewPager.stopAutoScroll();
    }

    public void setViewSize(int width, int heigth)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, heigth);
        mViewPager.setLayoutParams(params);
    }
}
