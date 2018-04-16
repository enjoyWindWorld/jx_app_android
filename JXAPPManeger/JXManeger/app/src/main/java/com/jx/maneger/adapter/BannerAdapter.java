package com.jx.maneger.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.jx.maneger.constant.Constant;
import com.jx.maneger.helper.GlideHelper;
import com.jx.maneger.intf.ViewPagerOnItemClickListener;

import java.util.List;

/**
 * Banner适配器
 */

public class BannerAdapter extends PagerAdapter {
    private List<String> mList;
    private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;
    private int[] mImages;
    private Context mContext;


    public BannerAdapter(Context context, List<String> list){
        this.mList = list;
        this.mContext = context;
    }

    public BannerAdapter(Context context, int[] images){
        this.mImages = images;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(mList != null){
            return mList.size();
        }else if(mImages != null){
            return mImages.length;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(mList != null){
            String s = mList.get(position);
            //因为换域名了所以 所有的 图片URL 都要经过切割再重新拼接
            String[] split = s.split("jx/");
            String mNew_Url = split[1];
            String imgurl = Constant.DOMAIN_URL+ mNew_Url;
            GlideHelper.setImageView(mContext,imgurl,imageView);
        }else if(mImages != null){
            imageView.setImageResource(mImages[position]);
        }

        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = imageView.getParent();
        if (vp != null)
        {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mViewPagerOnItemClickListener != null)
                {
                    mViewPagerOnItemClickListener.onItemClick(position);
                }
            }
        });

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public void setViewPagerOnItemClickListener(ViewPagerOnItemClickListener mViewPagerOnItemClickListener)
    {

        this.mViewPagerOnItemClickListener = mViewPagerOnItemClickListener;

    }
}
