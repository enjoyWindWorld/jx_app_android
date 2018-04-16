package com.jx.intelligent.ui.activitise.service;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ServiceFlAdapter;
import com.jx.intelligent.adapter.jxAdapter.ServicePhotoAdapter;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.bean.ServicePhotoBean;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetHomeServiceTypeResult;
import com.jx.intelligent.result.UploadImgResult;
import com.jx.intelligent.util.CommonUtils;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.util.SDCardUtils;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.MyGridView;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceAddContentActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText edt_content;
    private Button btn_ok;
    private NormalAlertDialog dialog2;
    private ProgressWheelDialog dialog;

    private Uri mImageUri, uri;
    private String logo, uploadUrl, categoryid, content;
    private MyGridView service_fl_gridview, grid_photo;
    private ServiceFlAdapter serviceFlAdapter;
    private List<GetHomeServiceTypeResult.ServiceFlBean> serviceFlBeanList = new ArrayList<GetHomeServiceTypeResult.ServiceFlBean>();
    private CommunityService communityService;
    private ServicePhotoAdapter servicePhotoAdapter;
    private List<ServicePhotoBean> photoInfos = new ArrayList<ServicePhotoBean>();
    private int deleteIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_add_content);
        communityService = new CommunityService();
        new TitleBarHelper(ServiceAddContentActivity.this)
                .setMiddleTitleText(R.string.service_selector_type_title)
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this);
        service_fl_gridview = (MyGridView) findViewById(R.id.service_fl_gridview);
        grid_photo = (MyGridView) findViewById(R.id.grid_photo);
        serviceFlAdapter = new ServiceFlAdapter(ServiceAddContentActivity.this, serviceFlBeanList);
        service_fl_gridview.setAdapter(serviceFlAdapter);
        dialog = new ProgressWheelDialog(ServiceAddContentActivity.this);
        getServiceType();

        service_fl_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                serviceFlAdapter.setSelectItem(i);
                serviceFlAdapter.notifyDataSetChanged();
                categoryid = serviceFlBeanList.get(i).getId();
            }
        });

        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        KeyboardUtil.hideSoftKeyboard(edt_content);

        btn_ok.setOnClickListener(this);
        grid_photo.setOnItemClickListener(this);
        initDialog();

        if(getIntent() != null)
        {
            uploadUrl = getIntent().getStringExtra("url");
            content = getIntent().getStringExtra("content");
            categoryid = getIntent().getStringExtra("categoryid");

            if(!StringUtil.isEmpty(uploadUrl))
            {
                String[] urls =  uploadUrl.split(",");
                for (String s : urls)
                {
                    ServicePhotoBean servicePhotoBean = new ServicePhotoBean();
                    servicePhotoBean.setImg_url(s);
                    photoInfos.add(servicePhotoBean);
                }
            }
            edt_content.setText(content);
        }

        showServicePhoto();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.btn_ok:
                if(StringUtil.isEmpty(uploadUrl))
                {
                    ToastUtil.showToast("请上传图片");
                }
                else if(StringUtil.isEmpty(categoryid))
                {
                    ToastUtil.showToast("请选择发布类型");
                }
                else if(StringUtil.isEmpty(edt_content.getText().toString()))
                {
                    ToastUtil.showToast(R.string.service_release_hit_add_content);
                }
                else if(Utils.filterEmoji(edt_content.getText().toString()))
                {
                    ToastUtil.showToast("请勿输入表情符号");
                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("url", uploadUrl);
                    intent.putExtra("categoryid", categoryid);
                    intent.putExtra("content", edt_content.getText().toString());
                    setResult(Constant.GET_SERVICE_CONTENT_OK, intent);
                    finish();
                }
                break;
        }
    }

    private void initDialog() {
        dialog2 = new NormalAlertDialog.Builder(ServiceAddContentActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("请选择照片")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("相册")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("拍照")
                .setCancelable(true)
                .setRightButtonTextColor(R.color.color_000000)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        getPhotoByLocal();
                        dialog2.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        createPicture();
                        dialog2.dismiss();
                    }
                })
                .build();
    }

    protected void getPhotoByLocal() {

        getRootDir();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.TAKE_LOCAL_PICTURE);

    }

    private void getRootDir() {

        File file = new File(SDCardUtils.getAbsRootDir(ServiceAddContentActivity.this) + File.separator
                + "ImageAuth");

        if (!file.exists()) {
            file.mkdir();
        } else {

        }
        mImageUri = Uri.parse("file://" + SDCardUtils.getAbsRootDir(ServiceAddContentActivity.this)
                + File.separator + "ImageAuth" + File.separator + "content.jpg");
    }

    protected void createPicture() {
        getRootDir();
        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(camera, Constant.TAKE_PHOTO);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        if (uri == null) {
            return;
        }

        this.uri = uri;

        if (weHavePermissionToReadContacts()) {
            logo = CommonUtils.getPath(this, uri);
            gotoClipViewActivity(logo);
        } else {
            requestReadContactsPermissionFirst();
        }
    }

    /**
     * 跳转到裁剪页面
     *
     * @param url
     */
    private void gotoClipViewActivity(String url) {
        Intent intent = new Intent(this, ClipViewActivity.class);
        intent.putExtra("value", url);
        startActivityForResult(intent, Constant.CLIP_CODE);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, final Intent data) {

        super.onActivityResult(requestcode, resultcode, data);

        try
        {
            if (resultcode != RESULT_OK) {
                return;
            }
            if (requestcode == Constant.TAKE_LOCAL_PICTURE) {
                startPhotoZoom(data.getData());
            }
            if (requestcode == Constant.TAKE_LOCAL_PICTURE_CROP) {
                disPlayImg(logo);
            }
            if (requestcode == Constant.TAKE_PHOTO) {
                startPhotoZoom(mImageUri);
            }
            if (requestcode == Constant.SHOW_PHOTO)
            {
                //删除掉图片路径
                String[] urls = uploadUrl.split(",");
                uploadUrl = "";
                for (int i = 0; i < urls.length; i ++)
                {
                    if(i != deleteIndex)
                    {
                        if(StringUtil.isEmpty(uploadUrl))
                        {
                            uploadUrl = urls[i] +",";
                        }
                        else
                        {
                            uploadUrl += urls[i] + ",";
                        }
                    }
                }
                photoInfos.remove(deleteIndex);
                showServicePhoto();
            }

            //图片剪切时的返回
            if (requestcode == Constant.CLIP_CODE) {
                if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                {
                    return;
                }
                //将图片上传到服务器
                logo = data.getStringExtra("value");
                dialog.show();
                new CommunityService().upload(logo, new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        dialog.dismiss();
                        UploadImgResult data = (UploadImgResult)object;
                        if(data != null && data.getData().size()>0)
                        {
                            //收集图片路径
                            if(StringUtil.isEmpty(uploadUrl))
                            {
                                uploadUrl = data.getData().get(0).getImgUrl() +",";
                            }
                            else
                            {
                                uploadUrl += data.getData().get(0).getImgUrl() + ",";
                            }
                            disPlayImg(logo);
                        }
                        else
                        {
                            ToastUtil.showToast("图片上传失败");
                        }
                    }

                    @Override
                    public void resFailure(String message) {
                        dialog.dismiss();
                        ToastUtil.showToast(message);
                    }
                });
            }
        }
        catch (Exception e)
        {

        }
    }

    protected void disPlayImg(String strUrl) {
        for (ServicePhotoBean servicePhotoBean : photoInfos)
        {
            if(StringUtil.isEmpty(servicePhotoBean.getImg_url()))
            {
                servicePhotoBean.setImg_url(strUrl);
            }
        }
        showServicePhoto();
    }

    //==========申请权限的==========
    public boolean weHavePermissionToReadContacts() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestReadContactsPermissionFirst() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestForResultContactsPermission();
        } else {
            requestForResultContactsPermission();
        }
    }

    public void requestForResultContactsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            logo = CommonUtils.getPath(this, uri);
            gotoClipViewActivity(logo);
        }
    }
    //==========申请权限的==========


    void getServiceType() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        communityService.getServiceTypeTask(new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                GetHomeServiceTypeResult getHomeServiceTypeResult = (GetHomeServiceTypeResult) object;
                if(getHomeServiceTypeResult != null)
                {
                    serviceFlBeanList.addAll(getHomeServiceTypeResult.getData());
                    if (serviceFlBeanList.size() > 0) {
                        serviceFlAdapter.setDatas(serviceFlBeanList);
                        if(StringUtil.isEmpty(categoryid))
                        {
                            serviceFlAdapter.setSelectItem(0);
                        }
                        else
                        {
                            for (int i = 0; i < serviceFlBeanList.size(); i++){
                                if(serviceFlBeanList.get(i).getId().equals(categoryid))
                                {
                                    serviceFlAdapter.setSelectItem(i);
                                }
                            }
                        }
                        serviceFlAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    void showServicePhoto()
    {
        for (ServicePhotoBean bean : photoInfos)
        {
            if(StringUtil.isEmpty(bean.getImg_url()))
            {
                photoInfos.remove(bean);
            }
        }

        if(photoInfos.size() < 4)
        {
            ServicePhotoBean servicePhotoBean = new ServicePhotoBean();
            photoInfos.add(servicePhotoBean);
        }
        servicePhotoAdapter = new ServicePhotoAdapter(ServiceAddContentActivity.this, photoInfos);
        grid_photo.setAdapter(servicePhotoAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(StringUtil.isEmpty(photoInfos.get(i).getImg_url()))
        {
            dialog2.show();
        }
        else
        {
            deleteIndex = i;
            Intent intent = new Intent(this, ServiceShowImgActivity.class);
            intent.putExtra("value", photoInfos.get(i).getImg_url());
            startActivityForResult(intent, Constant.SHOW_PHOTO);
        }
    }
}
