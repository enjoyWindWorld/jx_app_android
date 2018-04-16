package com.jx.intelligent.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jx.intelligent.R;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;

/**
 * Glide加载图片帮助类
 */

public class GlideHelper {
    private static Animation mAnimation;

    /**
     * 加载动画
     *
     * @param context
     * @param url
     * @param targetImageView  成功显示的图片
     * @param loadingImageView 加载动画的imageView
     */
    public static void setImageView(Context context, String url, final ImageView targetImageView, final ImageView loadingImageView, final int imgRes) {
        if (mAnimation == null) {
            mAnimation = AnimationUtils.loadAnimation(UIUtil.getContext(), R.anim.loading_anim);
            LinearInterpolator interpolator = new LinearInterpolator();
            mAnimation.setInterpolator(interpolator);
        }

        if(loadingImageView != null)
        {
            loadingImageView.setVisibility(View.VISIBLE);
            loadingImageView.startAnimation(mAnimation);
        }


        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new GlideDrawableImageViewTarget(targetImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //图片加载成功,可做相应操作
                        if(loadingImageView != null) {
                            loadingImageView.clearAnimation();
                            loadingImageView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        LogUtil.e("加载失败");
                        if(loadingImageView != null) {
                            loadingImageView.clearAnimation();
                            loadingImageView.setVisibility(View.GONE);
                        }

                        if(imgRes > -1)
                        {
                            targetImageView.setImageResource(imgRes);
                        }

                    }
                });
    }

    /**
     * 加载普通图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void setImageView(Context context, String url, ImageView imageView) {
        if(!StringUtil.isEmpty(url))
        {
            if(url.contains("data.jx-inteligent.tech:15010/jx"))
            {
                url = url.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
            }
        }
        Glide.with(context).load(url)
                .placeholder(R.color.color_e0e0e0)
                .error(R.mipmap.big_empty_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 加载指定宽高压缩后的图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param width     指定压缩的宽度
     * @param height    指定压缩的高度
     */
    public static void setImageView(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context).load(url)
                .placeholder(R.color.color_e0e0e0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(width, height)
                .into(imageView);
    }
}
