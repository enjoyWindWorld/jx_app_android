package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.util.AppUtil;

/**
 * 关于
 * Created by Administrator on 2016/11/15 0015.
 */
public class AboutJXActivity extends AppCompatActivity {

    ImageView titlebar_left_vertical_iv, titlebar_left_iv, gy_sp_jr, gy_sp_jr1;
    MyOnclickList myOnclickList;
    RelativeLayout titlebar_left_rl;
    TextView gy_gw_jr, txt_version;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_jx);
        ((TextView) findViewById(R.id.titlebar_center_tv)).setText("关于净喜");

        myOnclickList = new MyOnclickList();

        titlebar_left_iv = (ImageView) findViewById(R.id.titlebar_left_iv);
        titlebar_left_iv.setVisibility(View.VISIBLE);

        titlebar_left_rl = (RelativeLayout) findViewById(R.id.titlebar_left_rl);
        titlebar_left_rl.setOnClickListener(myOnclickList);

        txt_version = (TextView) findViewById(R.id.txt_version);
        gy_gw_jr = (TextView) findViewById(R.id.gy_gw_jr);
        gy_gw_jr.setOnClickListener(myOnclickList);


        gy_sp_jr = (ImageView) findViewById(R.id.gy_sp_jr);
        gy_sp_jr1 = (ImageView) findViewById(R.id.gy_sp_jr1);

        gy_sp_jr.setOnClickListener(myOnclickList);
        gy_sp_jr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goVideoPlay();
            }
        });

        txt_version.setText("版本（"+AppUtil.getVersionName()+"）");
    }


    class MyOnclickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left_rl:
                    finish();
                    break;
                case R.id.gy_sp_jr:
                case R.id.gy_sp_jr1:
                    goVideoPlay();
                    break;
                case R.id.gy_gw_jr:
                    goWebJX();
                    break;
            }
        }
    }


    /**
     * 跳转到播放视频界面
     */
    private void goVideoPlay() {
        Intent intent = new Intent(AboutJXActivity.this, VideoPlayActivity.class);
        intent.putExtra("play_url", Constant.IP + "pic/jingxi1010.mp4");
        intent.putExtra("title", "关于净喜");
        startActivity(intent);

    }

    /**
     * 跳转到净喜官网界面
     */
    private void goWebJX() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://www.szjxzn.cn/index.php");
        intent.setData(content_url);
        startActivity(intent);
    }

}
