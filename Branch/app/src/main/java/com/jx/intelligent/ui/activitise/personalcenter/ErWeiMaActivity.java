package com.jx.intelligent.ui.activitise.personalcenter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.util.Des;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.UIUtil;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class ErWeiMaActivity extends RHBaseActivity {
    TitleBarHelper titleBarHelper;
    private String input;
    private ImageView mImg;
    private String mDes_userid;
    private String initkey;
    private String mInput;

    @Override
    protected void init() {
        SesSharedReferences sp = new SesSharedReferences();

        String userId = sp.getUserId(UIUtil.getContext());

//        input = "jxsmart://promotion://" + userId;

        String mDes_Userid = ToDesUrl(userId);

        mInput = "jxsmart://promotion://" + mDes_Userid + initkey;

        //去生成二维码
        make(mInput);


    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_erweima;
    }

    @Override
    protected void initTitle() {

        titleBarHelper = new TitleBarHelper(ErWeiMaActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setMiddleTitleText("我的二维码");


        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void findView(View contentView) {
        mImg = (ImageView) contentView.findViewById(R.id.img);


    }


    private String ToDesUrl(String userid) {
        Des des = new Des();
        //随机生成的加密的 密码
        initkey = des.initkey();


        //拿这个加密的 密码去 对 userid 进行加密
        try {

            mDes_userid = des.encryptDES(userid, initkey);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDes_userid;

    }


    /**
     * 生成二维码
     */
    public void make(String input) {
        Log.e("mInput::", input);
        //生成二维码，然后为二维码增加logo
        EncodingHandler handler = new EncodingHandler();
        //二维码中间加log
//        isLogo.isChecked()? BitmapFactory.decodeResource(getResources(),
//                R.mipmap.ic_launcher)
        Bitmap bitmap = null;
        try {
            bitmap = handler.createQRCode(input, 500);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mImg.setImageBitmap(bitmap);
    }

}
