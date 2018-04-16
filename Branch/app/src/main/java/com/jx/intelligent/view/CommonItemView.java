package com.jx.intelligent.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;

/**
 * 自定义共用条目样式 ，左右支持图片和文本
 */

public class CommonItemView extends RelativeLayout {

    private View mRootView;
    private ImageView mLeftIv;
    private ImageView mRightIv;
    private TextView mLeftTv;
    private TextView mRightTv;

    public CommonItemView(Context context) {
        this(context,null);
    }

    public CommonItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRootView = View.inflate(context, R.layout.common_item_view, this);
        initView();
        initAttrs(context,attrs);
    }
    private void initView() {
        mLeftIv = (ImageView) mRootView.findViewById(R.id.left_iv);
        mRightIv = (ImageView) mRootView.findViewById(R.id.right_iv);
        mLeftTv = (TextView) mRootView.findViewById(R.id.left_tv);
        mRightTv = (TextView) mRootView.findViewById(R.id.right_tv);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonItemView);
        int leftIvResId = typedArray.getResourceId(R.styleable.CommonItemView_left_iv, 0);
        int leftTvResId = typedArray.getResourceId(R.styleable.CommonItemView_left_tv, 0);
        int leftTvSizeResId = typedArray.getResourceId(R.styleable.CommonItemView_left_tv_size, 0);
        int leftTvColorResId = typedArray.getResourceId(R.styleable.CommonItemView_left_tv_color, 0);

        int rightIvResId = typedArray.getResourceId(R.styleable.CommonItemView_right_iv, 0);
        int rightTvResId = typedArray.getResourceId(R.styleable.CommonItemView_right_tv, 0);
        int rightTvSizeResId = typedArray.getResourceId(R.styleable.CommonItemView_right_tv_size, 0);
        int rightTvColorResId = typedArray.getResourceId(R.styleable.CommonItemView_right_tv_color, 0);

        int bgColorId = typedArray.getResourceId(R.styleable.CommonItemView_bg_color, 0);
        Resources resources = context.getResources();
        if(leftIvResId != 0){
            Drawable leftDrawable = resources.getDrawable(leftIvResId);
            mLeftIv.setImageDrawable(leftDrawable);
        }

        if(leftTvResId != 0){
            String leftString = resources.getString(leftTvResId);
            mLeftTv.setText(leftString);
        }

        if(leftTvSizeResId != 0){
            float leftSize = resources.getDimension(leftTvSizeResId);
            mLeftTv.setTextSize(leftSize);
        }

        if(leftTvColorResId != 0){
            int leftColor = resources.getColor(leftTvColorResId);
            mLeftTv.setTextColor(leftColor);
        }

        if(rightIvResId != 0){
            Drawable rightDrawable = resources.getDrawable(rightIvResId);
            mRightIv.setImageDrawable(rightDrawable);
        }

        if(rightTvResId != 0){
            String rightString = resources.getString(rightTvResId);
            mRightTv.setText(rightString);
        }

        if(rightTvSizeResId != 0){
            float rightSize = resources.getDimension(rightTvSizeResId);
            mRightTv.setTextSize(rightSize);
        }

        if(rightTvColorResId != 0){
            int rightColor = resources.getColor(rightTvColorResId);
            mRightTv.setTextColor(rightColor);
        }

        if(bgColorId != 0){
            int bgColor = resources.getColor(bgColorId);
            setBackgroundColor(bgColor);
        }

        typedArray.recycle();

    }

    public void setRightText(String text){
        mRightTv.setText(text);
    }


}
