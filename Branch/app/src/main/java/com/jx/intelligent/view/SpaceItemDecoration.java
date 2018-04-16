package com.jx.intelligent.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecycleView的条目间隔
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first layout_wd_dd_item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = space;
    }

}
