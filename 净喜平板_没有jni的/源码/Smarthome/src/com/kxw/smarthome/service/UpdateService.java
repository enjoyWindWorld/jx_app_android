package com.kxw.smarthome.service;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;

import com.kxw.smarthome.R;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;


public class UpdateService extends Service {

    private String TAG = "UpdateService";
    private Context mContext;
    private long downloadID = -1;
    private boolean isDownLoading;
    private DownloadManager downloadManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLogger.getInstance().d("onCreate()");

        mContext = this;
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        initBroadcastReciever();
    }

    @SuppressLint("InlinedApi")
	private void initBroadcastReciever() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @SuppressLint("NewApi")
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
                	MyLogger.getInstance().i("ACTION_NOTIFICATION_CLICKED");

                    queryStatus(downloadID);

                    if (isDownLoading) {
                    	MyToast.getManager(getApplicationContext()).show(getString(R.string.downloading));
                    }
                }

                // 下载完成，进行安装
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                	MyLogger.getInstance().i("ACTION_DOWNLOAD_COMPLETE");
                    isDownLoading = false;
                    long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    String savePath = querySavePath(downloadID);

                    if (!TextUtils.isEmpty(savePath)) {
                        Uri uri = Uri.parse(savePath);
                        File file = new File(uri.getPath());
                        if (file.exists()) {
                            installApk(uri);
                        }
                        // OperateFile.openFile(file, context);
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
    }

    /**
     * 根据下载ID查询下载保存的路径
     * 
     * @param downloadID
     * @return
     */
    @SuppressLint("NewApi")
    private String querySavePath(long downloadID) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);

        Cursor cursor = downloadManager.query(query);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        String savePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        cursor.close();
        return savePath;
    }

    /**
     * 安装apk
     * 
     * @param uri
     */
    private void installApk(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLogger.getInstance().d("onStartCommand()");

        if (downloadID != -1) {
            queryStatus(downloadID);
        }
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String url = bundle.getString("DownloadURL");
                String  apkName = bundle.getString("apkName");

                if (!TextUtils.isEmpty(url)) {
                    if (isDownLoading) {
                    	MyToast.getManager(getApplicationContext()).show(getString(R.string.downloading_wait));
                    } else {
                        startDownload(url,apkName);
                        MyToast.getManager(getApplicationContext()).show(getString(R.string.start_download));
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        MyLogger.getInstance().d( "onBind()");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        MyLogger.getInstance().d( "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLogger.getInstance().d( "onDestroy()");
    }

    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    private void startDownload(String url,String apkName) {
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        // 设置允许�???启下载的网络类型
        // Restrict the types of networks over which this download may proceed.
        // By default, all network types are allowed
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        // Set whether this download may proceed over a roaming connection. By
        // default, roaming is allowed
        // mRequest.setAllowedOverRoaming(true);

        // 设置下载地址
        String fileName = url.substring(url.lastIndexOf(File.separator) + 1);

        // 父目录是否存在，没有就创建
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!file.exists()) {
            file.mkdirs();
        }

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        // file:///storage/sdcard0/Download/AdobeReader.apk

        // If no title is given, a default one will be assigned based on the
        // download filename, once the download starts.
        request.setTitle(apkName);

        // Set a description of this download, to be displayed in notifications
        // (if enabled)
        request.setDescription("正在更新，请稍后");

        // 在�?�知栏显示进�???
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // By default, a notification is shown. If set to false, this
            // requires the permission
            // android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
            request.setShowRunningNotification(true);
        } else {
            // By default, a notification is shown only when the download is in
            // progress.

            // It can take the following values: VISIBILITY_HIDDEN,
            // VISIBILITY_VISIBLE, VISIBILITY_VISIBLE_NOTIFY_COMPLETED.

            // If set to VISIBILITY_HIDDEN, this requires the permission
            // android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }

        // Set whether this download should be displayed in the system's
        // Downloads UI. True by default.
        request.setVisibleInDownloadsUi(true);

        // 执行下载
        downloadID = downloadManager.enqueue(request);
        MyLogger.getInstance().d( "id: " + downloadID);

        // mHandler.postDelayed(runnable, 1000);
    }

    /**
     * 查询下载状态
     * 
     * @param downloadID
     */
    @SuppressLint("NewApi")
    private void queryStatus(long downloadID) {
        String TAG = "queryStatus";
        MyLogger.getInstance().d( "-----------------queryStatus()--------------------");

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);

        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();

        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

        switch (status) {
            case DownloadManager.STATUS_PENDING:
                // the download is waiting to start.
                isDownLoading = false;
                MyLogger.getInstance().d( "STATUS_PENDING");
                break;

            case DownloadManager.STATUS_RUNNING:
                isDownLoading = true;
                MyLogger.getInstance().d( "STATUS_RUNNING");
                break;

            case DownloadManager.STATUS_PAUSED:
                isDownLoading = false;
                MyLogger.getInstance().w( "STATUS_PAUSED");
                queryPause(downloadID);// 查询暂停原因
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                isDownLoading = false;
                MyLogger.getInstance().i( "STATUS_SUCCESSFUL");
                break;

            case DownloadManager.STATUS_FAILED:
                isDownLoading = false;
                MyLogger.getInstance().e( "STATUS_FAILED");
                queryFail(downloadID);// 查询下载失败原因
                break;
        }

        cursor.close();
    }

    /**
     * 查询下载停止原因
     * 
     * @param downloadID
     */
    @SuppressLint("NewApi")
    private void queryPause(long downloadID) {
        String TAG = "Pause";
        MyLogger.getInstance().d( "-----------------queryPause()--------------------");

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_PAUSED);

        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();

        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));

        switch (status) {
            case DownloadManager.PAUSED_WAITING_TO_RETRY:
                MyLogger.getInstance().w( "PAUSED_WAITING_TO_RETRY");
                break;
            case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                MyLogger.getInstance().w( "PAUSED_WAITING_FOR_NETWORK");
                break;
            case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                MyLogger.getInstance().w( "PAUSED_QUEUED_FOR_WIFI");
                break;
            case DownloadManager.PAUSED_UNKNOWN:
                MyLogger.getInstance().w( "PAUSED_UNKNOWN");
                break;
        }

        cursor.close();
    }

    /**
     * 查询下载失败原因
     * 
     * @param downloadID
     */
    @SuppressLint("NewApi")
    private void queryFail(long downloadID) {
        String TAG = "Fail";
        MyLogger.getInstance().d( "-----------------queryFail()--------------------");

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_FAILED);

        Cursor cursor = downloadManager.query(query);
        cursor.moveToFirst();

        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));// 注意这个参数

        switch (status) {
            case DownloadManager.ERROR_CANNOT_RESUME:
                MyLogger.getInstance().e( "ERROR_CANNOT_RESUME");
                break;
            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                MyLogger.getInstance().e( "ERROR_DEVICE_NOT_FOUND");
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                MyLogger.getInstance().e( "ERROR_FILE_ALREADY_EXISTS");
                break;
            case DownloadManager.ERROR_FILE_ERROR:
                MyLogger.getInstance().e( "ERROR_FILE_ERROR");
                break;
            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                MyLogger.getInstance().e( "ERROR_HTTP_DATA_ERROR");
                break;
            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                MyLogger.getInstance().e( "ERROR_INSUFFICIENT_SPACE");
                break;
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                MyLogger.getInstance().e( "ERROR_TOO_MANY_REDIRECTS");
                break;
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                MyLogger.getInstance().e( "ERROR_UNHANDLED_HTTP_CODE");
                break;
            case DownloadManager.ERROR_UNKNOWN:
                MyLogger.getInstance().e("ERROR_UNKNOWN");
                break;
        }

        cursor.close();
    }
}
