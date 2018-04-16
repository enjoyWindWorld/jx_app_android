package com.jx.intelligent.adapter.jxAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class DetailPagerAdapter extends PagerAdapter {
    private ArrayList<String> mUrlList;
    public DetailPagerAdapter(ArrayList<String> mUrlList) {
        this.mUrlList = mUrlList;
    }
    @Override
    public int getCount() {
        if(mUrlList.size()!=0){
            return mUrlList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = mUrlList.get(position);
        if(!StringUtil.isEmpty(url))
        {
            if(url.contains("data.jx-inteligent.tech:15010/jx"))
            {
                url = url.replace("data.jx-inteligent.tech:15010/jx", "www.szjxzn.tech:8080/old_jx");
            }
        }
        ImageView img = new ImageView(UIUtil.getContext());
        Picasso.with(UIUtil.getContext()).load(url).into(img);
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);

    }
}
