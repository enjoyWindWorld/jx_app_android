package com.jx.intelligent.ui.activitise.customerService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ServicePhotoAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.bean.ServicePhotoBean;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.dao.CustomerServiceDao;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.UploadImgResult;
import com.jx.intelligent.ui.activitise.service.ClipViewActivity;
import com.jx.intelligent.ui.activitise.service.ServiceShowImgActivity;
import com.jx.intelligent.util.CommonUtils;
import com.jx.intelligent.util.SDCardUtils;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.MyGridView;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddServiceEvaluateActivity extends RHBaseActivity implements AdapterView.OnItemClickListener {

    private TextView txt_task_type;
    private EditText edit_master_worker, edit_master_worker_phone, edit_evaluate, edit_user_name, edit_user_phone;
    private MyGridView grid_photo;
    private CheckBox checkbox_wear_cards, checkbox_wear_overalls, checkbox_anonymous;
    private XLHRatingBar ratingBar_myd, ratingBar_td;
    private LinearLayout layout_wear_cards, layout_wear_overalls, layout_anonymous;
    private Button btn_evaluate;

    private Uri mImageUri, uri;
    private String logo;
    private ServicePhotoAdapter servicePhotoAdapter;
    private List<ServicePhotoBean> photoInfos = new ArrayList<ServicePhotoBean>();
    private int deleteIndex;
    private NormalAlertDialog dialog2;
    private ProgressWheelDialog dialog;
    private CustomerServiceDao dao;
    String id, pro_no, ord_no, service_type, service_master, service_master_phone, evaluation_people, evaluation_people_phnoe, content, appraise_url, is_badge, is_overalls, is_anonymous, satisfaction, service_attitude, ord_managerno;

    @Override
    protected void init() {
        dao = new CustomerServiceDao();
        if(getIntent() != null)
        {
            id = getIntent().getStringExtra("id");
            pro_no = getIntent().getStringExtra("pro_no");
            ord_no = getIntent().getStringExtra("ord_no");
            service_type = getIntent().getStringExtra("service_type");
            ord_managerno = getIntent().getStringExtra("ord_managerno");

            if("1".equals(service_type))
            {
                txt_task_type.setText("滤芯更换");
            }
            else if("2".equals(service_type))
            {
                txt_task_type.setText("设备报修");
            }
            else if("3".equals(service_type))
            {
                txt_task_type.setText("其他");
            }
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_add_service_evaluate;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(AddServiceEvaluateActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("售后评价")
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(final View contentView) {
        txt_task_type = (TextView)contentView.findViewById(R.id.txt_task_type);
        edit_master_worker = (EditText)contentView.findViewById(R.id.edit_master_worker);
        edit_master_worker_phone = (EditText)contentView.findViewById(R.id.edit_master_worker_phone);
        edit_evaluate = (EditText)contentView.findViewById(R.id.edit_evaluate);
        edit_user_name = (EditText)contentView.findViewById(R.id.edit_user_name);
        edit_user_phone = (EditText)contentView.findViewById(R.id.edit_user_phone);
        grid_photo = (MyGridView)contentView.findViewById(R.id.grid_photo);

        checkbox_wear_cards = (CheckBox) contentView.findViewById(R.id.checkbox_wear_cards);
        checkbox_wear_overalls = (CheckBox)contentView.findViewById(R.id.checkbox_wear_overalls);
        checkbox_anonymous = (CheckBox)contentView.findViewById(R.id.checkbox_anonymous);

        ratingBar_myd = (XLHRatingBar)contentView.findViewById(R.id.ratingBar_myd);
        ratingBar_td = (XLHRatingBar)contentView.findViewById(R.id.ratingBar_td);

        layout_wear_cards = (LinearLayout) contentView.findViewById(R.id.layout_wear_cards);
        layout_wear_overalls = (LinearLayout) contentView.findViewById(R.id.layout_wear_overalls);
        layout_anonymous = (LinearLayout) contentView.findViewById(R.id.layout_anonymous);

        btn_evaluate = (Button) contentView.findViewById(R.id.btn_evaluate);

        btn_evaluate.setOnClickListener(this);
        layout_wear_cards.setOnClickListener(this);
        layout_wear_overalls.setOnClickListener(this);
        layout_anonymous.setOnClickListener(this);

        grid_photo.setOnItemClickListener(this);

        ratingBar_myd.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                satisfaction = countSelected+"";
            }
        });

        ratingBar_td.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                service_attitude = countSelected+"";
            }
        });
        dialog = new ProgressWheelDialog(AddServiceEvaluateActivity.this);
        initDialog();
        showServicePhoto();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.btn_evaluate:
                service_master = edit_master_worker.getText().toString();
                service_master_phone = edit_master_worker_phone.getText().toString();
                evaluation_people = edit_user_name.getText().toString();
                evaluation_people_phnoe = edit_user_phone.getText().toString();
                content = edit_evaluate.getText().toString();
                evaluate();
                break;
            case R.id.layout_wear_cards:
                if(checkbox_wear_cards.isChecked())
                {
                    checkbox_wear_cards.setChecked(false);
                    is_badge = "false";
                }
                else
                {
                    checkbox_wear_cards.setChecked(true);
                    is_badge = "true";
                }
                break;
            case R.id.layout_wear_overalls:
                if(checkbox_wear_overalls.isChecked())
                {
                    checkbox_wear_overalls.setChecked(false);
                    is_overalls = "false";
                }
                else
                {
                    checkbox_wear_overalls.setChecked(true);
                    is_overalls = "true";
                }
                break;
            case R.id.layout_anonymous:
                if(checkbox_anonymous.isChecked())
                {
                    checkbox_anonymous.setChecked(false);
                    is_anonymous = "false";
                    edit_user_name.setEnabled(true);
                    edit_user_phone.setEnabled(true);
                }
                else
                {
                    checkbox_anonymous.setChecked(true);
                    is_anonymous = "true";
                    evaluation_people = "";
                    evaluation_people_phnoe = "";
                    edit_user_name.setText("");
                    edit_user_phone.setText("");
                    edit_user_name.setEnabled(false);
                    edit_user_phone.setEnabled(false);
                }
                break;
        }
    }

    private void initDialog() {
        dialog2 = new NormalAlertDialog.Builder(AddServiceEvaluateActivity.this)
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

        File file = new File(SDCardUtils.getAbsRootDir(AddServiceEvaluateActivity.this) + File.separator
                + "ImageAuth");

        if (!file.exists()) {
            file.mkdir();
        } else {

        }
        mImageUri = Uri.parse("file://" + SDCardUtils.getAbsRootDir(AddServiceEvaluateActivity.this)
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
                String[] urls = appraise_url.split(",");
                appraise_url = "";
                for (int i = 0; i < urls.length; i ++)
                {
                    if(i != deleteIndex)
                    {
                        if(StringUtil.isEmpty(appraise_url))
                        {
                            appraise_url = urls[i] +",";
                        }
                        else
                        {
                            appraise_url += urls[i] + ",";
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
                            if(StringUtil.isEmpty(appraise_url))
                            {
                                appraise_url = data.getData().get(0).getImgUrl() +",";
                            }
                            else
                            {
                                appraise_url += data.getData().get(0).getImgUrl() + ",";
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
        servicePhotoAdapter = new ServicePhotoAdapter(AddServiceEvaluateActivity.this, photoInfos);
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

    void evaluate()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        dao.evaluateCustomerService(id, pro_no, ord_no, service_type, service_master, service_master_phone, evaluation_people, evaluation_people_phnoe, SesSharedReferences.getUserId(AddServiceEvaluateActivity.this), content, appraise_url, is_badge, is_overalls, is_anonymous, satisfaction, service_attitude, ord_managerno, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("评价成功！");
                setResult(Constant.EVALUATE_OK);
                finish();
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }
}
