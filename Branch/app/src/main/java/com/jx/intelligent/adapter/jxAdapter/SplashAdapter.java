package com.jx.intelligent.adapter.jxAdapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by 王云 on 2017/5/17 0017.
 */

public class SplashAdapter extends PagerAdapter {
    private ArrayList<ImageView> Datas ;

    public SplashAdapter(ArrayList<ImageView> datas) {
        Datas = datas;
    }





    @Override
    public int getCount() {
        if (Datas.size()!=0){
            return Datas.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = Datas.get(position);

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);

        container.removeView((View) object);
    }
}
