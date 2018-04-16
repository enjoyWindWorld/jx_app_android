package com.jx.maneger.activities.Withdrawals;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.joanzapata.pdfview.PDFView;
import com.jx.maneger.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RulesActivity extends Activity implements View.OnClickListener{

    private PDFView mPdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rules);
        initView();
        initData();
    }


    private void initView() {
        mPdfView = (PDFView) findViewById(R.id.pdfView);
    }
    private void initData() {
        loadBuyNotice("http://www.szjxzn.tech:8080/jx/pdf/cashrule.pdf", "cashrule.pdf");
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
