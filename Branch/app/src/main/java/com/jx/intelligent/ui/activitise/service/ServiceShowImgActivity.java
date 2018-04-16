package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.ui.activitise.personalcenter.PerInfoActivity;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.XBitmapUtils;
import com.jx.intelligent.view.CommonClipImageView;

/**
 * Created by 韦飞 on 2017/6/29 0029.
 */

public class ServiceShowImgActivity extends RHBaseActivity {

    private ImageView img;
    String imgUrl;
    Bitmap bitmap;
    int size = 800;
    private TitleBarHelper titleBarHelper;

    private void getPreData() {
        // TODO Auto-generated method stub
        imgUrl = getIntent().getStringExtra("value");
    }


    @Override
    protected void init() {
        getPreData();
        if (!TextUtils.isEmpty(imgUrl)) {
            if(imgUrl.contains("http"))
            {
                GlideHelper.setImageView(ServiceShowImgActivity.this, imgUrl, img);
            }
            else
            {
                bitmap = XBitmapUtils.decodeFile(imgUrl, size);
                img.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_delete_photo;
    }
    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(ServiceShowImgActivity.this);
        titleBarHelper.setMiddleTitleText("");
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBarHelper.setRightText("删除");
        titleBarHelper.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void findView(View contentView) {
        img = (ImageView) contentView.findViewById(R.id.img);
    }
}
