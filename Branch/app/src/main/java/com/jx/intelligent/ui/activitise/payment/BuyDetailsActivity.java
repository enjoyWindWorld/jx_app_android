package com.jx.intelligent.ui.activitise.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.joanzapata.pdfview.PDFView;
import com.jx.intelligent.R;
import com.jx.intelligent.constant.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 王云 on 2017/4/13 0010.
 * 购买详情PDF页面
 */


public class BuyDetailsActivity extends Activity implements View.OnClickListener{


    private PDFView mPdfView;
    private Button mBtn_buy;
    private int flag; //区分是否是发布服务界面过来的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buy_datails);

        flag = getIntent().getIntExtra("flag", 0);

        initView();
        initData();
    }


    private void initView() {

        mPdfView = (PDFView) findViewById(R.id.pdfView);
        mBtn_buy = (Button) findViewById(R.id.btn_buydetails);
        mBtn_buy.setOnClickListener(this);
    }
    private void initData() {
        //从assets目录读取pdf
        if(flag == 1)
        {
            mBtn_buy.setVisibility(View.GONE);
            loadBuyNotice("http://www.szjxzn.tech:8080/old_jx/4/5/071310460511831_publishagreement.pdf", "publishagreement.pdf");
        }
        else
        {
            loadBuyNotice("http://www.szjxzn.tech:8080/old_jx/pdf/agreement.pdf", "agreement.pdf");
        }
    }

    private void displayFromFile(File file) {
        mPdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(0)         //设置默认显示第1页
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( false )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻页
                // .pages( 2 , 3 , 4 , 5  )  //把2 , 3 , 4 , 5 过滤掉
                .load();
    }


    /**
     * 阅读协议按钮的点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
         Intent intent = new Intent();
        setResult(Constant.BUY_DETAILS, intent);
        finish();
    }

    // 创建okHttpClient对象
    OkHttpClient mOkHttpClient = new OkHttpClient();

    String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private void loadBuyNotice(String url, final String fileName)
    {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("h_bl", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                    displayFromFile(file);
                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}
