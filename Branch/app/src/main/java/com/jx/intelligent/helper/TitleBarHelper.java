package com.jx.intelligent.helper;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;


/**
 * 顶部标题状态栏的设置
 */

public class TitleBarHelper {

    private ImageView mLeftIv;
    private ImageView mLeftHorizontalIv;
    private ImageView mLeftVerticalIv;
    private ImageView mCenterIv;
    private ImageView mRightIv;
    private ImageView mRightVerticalIv;
    private TextView mLeftTv;
    private TextView mLeftHorizontalTv;
    private TextView mLeftVerticalTv;
    private TextView mRightTv;
    private TextView mRightVerticalTv;
    private TextView mCenterTv;
    private RelativeLayout mLeftRl;
    private RelativeLayout mRightRl;
    private RelativeLayout mTitleRl;
    private RelativeLayout mCenterRl;
    private LinearLayout mLeftVerticalL;
    private LinearLayout mLeftHorizontalL;


    public TitleBarHelper(Activity activity) {

        mTitleRl = (RelativeLayout) activity.findViewById(R.id.titlebar_rl);
        mLeftIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_left_iv);
        mLeftVerticalIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_left_vertical_iv);
        mLeftHorizontalIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_left_horizontal_iv);
        mCenterIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_center_iv);
        mRightIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_right_iv);
        mRightVerticalIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_right_vertical_iv);

        mLeftTv = (TextView) mTitleRl.findViewById(R.id.titlebar_left_tv);
        mLeftVerticalTv = (TextView) mTitleRl.findViewById(R.id.titlebar_left_vertical_tv);
        mLeftHorizontalTv = (TextView) mTitleRl.findViewById(R.id.titlebar_left_horizontal_tv);

        mRightTv = (TextView) mTitleRl.findViewById(R.id.titlebar_right_tv);
        mRightVerticalTv = (TextView) mTitleRl.findViewById(R.id.titlebar_right_vertical_tv);
        mCenterTv = (TextView) mTitleRl.findViewById(R.id.titlebar_center_tv);

        mLeftRl = (RelativeLayout) mTitleRl.findViewById(R.id.titlebar_left_rl);
        mRightRl = (RelativeLayout) mTitleRl.findViewById(R.id.titlebar_right_rl);

        mLeftRl = (RelativeLayout) mTitleRl.findViewById(R.id.titlebar_left_rl);
        mRightRl = (RelativeLayout) mTitleRl.findViewById(R.id.titlebar_right_rl);
        mCenterRl = (RelativeLayout) mTitleRl.findViewById(R.id.layout_center);

        mLeftVerticalL = (LinearLayout) mTitleRl.findViewById(R.id.titlebar_left_vertical_ll);
        mLeftHorizontalL = (LinearLayout) mTitleRl.findViewById(R.id.titlebar_left_horizontal_ll);
    }

    public TitleBarHelper(View view) {

        mTitleRl = (RelativeLayout) view.findViewById(R.id.titlebar_rl);
        mLeftIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_left_iv);
        mLeftVerticalIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_left_vertical_iv);
        mLeftHorizontalIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_left_horizontal_iv);
        mCenterIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_center_iv);
        mRightIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_right_iv);
        mRightVerticalIv = (ImageView) mTitleRl.findViewById(R.id.titlebar_right_vertical_iv);

        mLeftTv = (TextView) mTitleRl.findViewById(R.id.titlebar_left_tv);
        mLeftVerticalTv = (TextView) mTitleRl.findViewById(R.id.titlebar_left_vertical_tv);
        mLeftHorizontalTv = (TextView) mTitleRl.findViewById(R.id.titlebar_left_horizontal_tv);

        mRightTv = (TextView) mTitleRl.findViewById(R.id.titlebar_right_tv);
        mRightVerticalTv = (TextView) mTitleRl.findViewById(R.id.titlebar_right_vertical_tv);
        mCenterTv = (TextView) mTitleRl.findViewById(R.id.titlebar_center_tv);

        mLeftRl = (RelativeLayout) mTitleRl.findViewById(R.id.titlebar_left_rl);
        mRightRl = (RelativeLayout) mTitleRl.findViewById(R.id.titlebar_right_rl);
        mCenterRl = (RelativeLayout) mTitleRl.findViewById(R.id.layout_center);

        mLeftVerticalL = (LinearLayout) mTitleRl.findViewById(R.id.titlebar_left_vertical_ll);
        mLeftHorizontalL = (LinearLayout) mTitleRl.findViewById(R.id.titlebar_left_horizontal_ll);
    }

    /**
     * 隐藏标题状态栏
     *
     */
    public void setHideTitleBar(){
        mTitleRl.setVisibility(View.GONE);
    }

    /**
     * title 的背景色
     */

    public TitleBarHelper setTitleBgRes(int resid) {

        mTitleRl.setBackgroundResource(resid);

        return this;
    }
    /**
     * title的文本
     *
     * @param text
     * @return
     */
    public TitleBarHelper setMiddleTitleText(String text) {
        mCenterTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        mCenterTv.setText(text);
        return this;
    }

    /**
     * title的文本
     * @param text
     * @return
     */
    public TitleBarHelper setMiddleTitleText(int text) {
        mCenterTv.setVisibility(text < 0 ? View.GONE
                : View.VISIBLE);
        mCenterTv.setText(text);
        return this;
    }

    public TitleBarHelper setMiddleImageRes(int resId){
        mCenterIv.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mCenterIv.setImageResource(resId);
        return this;
    }

    /**
     * 左边的图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBarHelper setLeftImageRes(int resId) {

        mLeftIv.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mLeftIv.setBackgroundResource(resId);
        return this;
    }

    /**
     * 左边边竖直文本
     *
     * @param resId
     * @return
     */
    public TitleBarHelper setLeftImageVerticalRes(int resId){

        mLeftVerticalIv.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mLeftVerticalIv.setBackgroundResource(resId);
        return this;
    }

    /**
     * 左边文字按钮
     *
     * @param text
     * @return
     */
    public TitleBarHelper setLeftText(String text) {

        mLeftTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        mLeftTv.setText(text);

        return this;
    }

    /**
     * 左边竖直文本
     *
     * @param text
     * @return
     */
    public TitleBarHelper setLeftVerticalText(String text){
        mLeftVerticalL.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        mLeftVerticalTv.setText(text);

        return this;
    }

    /**
     * 左边横行文本
     *
     * @param text
     * @return
     */
    public TitleBarHelper setLeftHorizontalText(String text){
        mLeftHorizontalL.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        mLeftHorizontalTv.setText(text);

        return this;
    }


    /**
     * 右边的图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBarHelper setRightImageRes(int resId) {

        mRightIv.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mRightIv.setBackgroundResource(resId);

        return this;
    }

    /**
     * 右边竖直文本
     *
     * @param resId
     * @return
     */
    public TitleBarHelper setRightImageVerticalRes(int resId){

        mRightVerticalIv.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mRightVerticalIv.setBackgroundResource(resId);
        return this;
    }

    /**
     * 右边文字按钮
     *
     * @param text
     * @return
     */
    public TitleBarHelper setRightText(String text) {

        mRightTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        mRightTv.setText(text);

        return this;
    }

    /**
     * 右边竖直文本
     *
     * @param text
     * @return
     */
    public TitleBarHelper setRightVerticalText(String text){

        mRightVerticalTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE: View.VISIBLE);
        mRightVerticalTv.setText(text);

        return this;
    }

    /**
     * 设置左边点击事件
     */
    public TitleBarHelper setLeftClickListener(View.OnClickListener listener){
        mLeftRl.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右边点击事件
     */
    public TitleBarHelper setRightClickListener(View.OnClickListener listener){
        mRightRl.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置中部标题的点击事件
     * @param listener
     * @return
     */
    public TitleBarHelper setMiddleClickListener(View.OnClickListener listener){
        mCenterRl.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置中部标题能否点击
     * @param enable
     */
    public void centerRlEnable(boolean enable)
    {
        mCenterRl.setEnabled(enable);
    }
}
