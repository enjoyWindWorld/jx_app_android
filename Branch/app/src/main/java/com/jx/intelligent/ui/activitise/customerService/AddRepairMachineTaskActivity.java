package com.jx.intelligent.ui.activitise.customerService;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ServicePhotoAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.bean.ServicePhotoBean;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.dao.CustomerServiceDao;
import com.jx.intelligent.enums.DateShowType;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.CustomerServiceTaskDetailResult;
import com.jx.intelligent.result.FaultTypeResult;
import com.jx.intelligent.result.UploadImgResult;
import com.jx.intelligent.ui.activitise.service.ClipViewActivity;
import com.jx.intelligent.ui.activitise.service.ServiceShowImgActivity;
import com.jx.intelligent.util.CommonUtils;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.util.SDCardUtils;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.CitySelectPopWindow;
import com.jx.intelligent.view.DateAndTimerPicker;
import com.jx.intelligent.view.MyGridView;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10.
 */

public class AddRepairMachineTaskActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RelativeLayout layout_equipment, layout_fault_phenomenon, layout_fault_description, layout_date, layout_pcd;
    private TextView txt_equipment, txt_fault_phenomenon, txt_fault_description, txt_date, txt_pcd;
    private EditText edit_name, edit_phone, edit_addr;
    private MyGridView grid_photo;
    private NormalAlertDialog dialog2;
    private ProgressWheelDialog dialog;
    private List<ServicePhotoBean> photoInfos = new ArrayList<ServicePhotoBean>();
    private int deleteIndex, type = 1;
    private Uri mImageUri, uri;
    private String logo;
    private ServicePhotoAdapter servicePhotoAdapter;
    private CitySelectPopWindow menuWindow;
    private CustomerServiceDao dao;
    private String pro_id, pro_name, ord_color, make_time, contact_person, contact_way, user_address, address_details, filter_name, fault_cause, specific_reason, fautl_url, pro_no, ord_managerno, ord_no, sheng, shi, qu;
    private CustomerServiceTaskDetailResult.Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repair_machine_task);

        type = getIntent().getIntExtra("type", 1);
        data = (CustomerServiceTaskDetailResult.Data)getIntent().getSerializableExtra("obj");

        layout_equipment = (RelativeLayout) findViewById(R.id.layout_equipment);
        layout_fault_phenomenon = (RelativeLayout) findViewById(R.id.layout_fault_phenomenon);
        layout_fault_description = (RelativeLayout) findViewById(R.id.layout_fault_description);
        layout_date = (RelativeLayout) findViewById(R.id.layout_date);
        layout_pcd = (RelativeLayout) findViewById(R.id.layout_pcd);

        txt_equipment = (TextView) findViewById(R.id.txt_equipment);
        txt_fault_phenomenon = (TextView) findViewById(R.id.txt_fault_phenomenon);
        txt_fault_description = (TextView) findViewById(R.id.txt_fault_description);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_pcd = (TextView) findViewById(R.id.txt_pcd);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_addr = (EditText) findViewById(R.id.edit_addr);

        grid_photo = (MyGridView) findViewById(R.id.grid_photo);
        dialog = new ProgressWheelDialog(AddRepairMachineTaskActivity.this);
        grid_photo.setOnItemClickListener(this);

        dao = new CustomerServiceDao();
        if(type == 1)
        {
            layout_fault_phenomenon.setVisibility(View.VISIBLE);
        }

        if(data != null)
        {
            showTaskDetail();
        }
        else
        {
            layout_equipment.setOnClickListener(this);
            layout_fault_phenomenon.setOnClickListener(this);
            layout_fault_description.setOnClickListener(this);
            layout_date.setOnClickListener(this);
            layout_pcd.setOnClickListener(this);
        }

        new TitleBarHelper(AddRepairMachineTaskActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("设备报修")
                .setRightText(data == null ? "发布" : data.getFas_state().equals("200") ? "" : "验收")
                .setLeftClickListener(this)
                .setRightClickListener(this);

        initDialog();
        showServicePhoto();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_equipment:
                Intent intent_equipment = new Intent(AddRepairMachineTaskActivity.this, RepairEquipmentListActivity.class);
                startActivityForResult(intent_equipment, Constant.REQUEST_CODE);
                break;
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.titlebar_right_rl:
                if(data == null)
                {
                    contact_person = edit_name.getText().toString();
                    contact_way = edit_phone.getText().toString();
                    address_details  = edit_addr.getText().toString();
                    if(StringUtil.isEmpty(pro_no))
                    {
                        ToastUtil.showToast("请选择设备");
                    }
                    else if(StringUtil.isEmpty(fault_cause) && type == 1)
                    {
                        ToastUtil.showToast("请选择故障现象");
                    }
                    else if(StringUtil.isEmpty(specific_reason))
                    {
                        ToastUtil.showToast("请输入具体说明");
                    }
                    else if(StringUtil.isEmpty(fautl_url))
                    {
                        ToastUtil.showToast("上传故障照片");
                    }
                    else if(StringUtil.isEmpty(make_time))
                    {
                        ToastUtil.showToast("请选择预约时间");
                    }
                    else if(StringUtil.isEmpty(contact_person))
                    {
                        ToastUtil.showToast("请填写联系人");
                    }
                    else if(StringUtil.isEmpty(contact_way))
                    {
                        ToastUtil.showToast("请填写联系方式");
                    }
                    else if(StringUtil.isEmpty(user_address))
                    {
                        ToastUtil.showToast("请选择省市区");
                    }
                    else if(StringUtil.isEmpty(address_details))
                    {
                        ToastUtil.showToast("请填写详细地址");
                    }
                    else
                    {
                        addTask();
                    }
                }
                else
                {
                    Intent intent = new Intent(AddRepairMachineTaskActivity.this, AddServiceEvaluateActivity.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("pro_no", data.getPro_no());
                    intent.putExtra("ord_no", data.getOrd_no());
                    intent.putExtra("service_type", data.getFas_type());
                    intent.putExtra("ord_managerno", data.getOrd_managerno());
                    startActivityForResult(intent, Constant.REQUEST_CODE);
                }
                break;
            case R.id.layout_date:
                showDatePicker();
                break;
            case R.id.layout_fault_phenomenon:
                Intent intent_fault = new Intent(AddRepairMachineTaskActivity.this, FaultTypeActivity.class);
                startActivityForResult(intent_fault, Constant.REQUEST_CODE);
                break;
            case R.id.layout_fault_description:
                Intent intent_description = new Intent(AddRepairMachineTaskActivity.this, AddFaultDescriptionActivity.class);
                intent_description.putExtra("description", specific_reason);
                startActivityForResult(intent_description, Constant.REQUEST_CODE);
                break;
            case R.id.layout_pcd:
                menuWindow = new CitySelectPopWindow(AddRepairMachineTaskActivity.this, itemsOnClick);
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(AddRepairMachineTaskActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                KeyboardUtil.hideSoftKeyboard(txt_pcd);
                break;
        }
    }

    private void initDialog() {
        dialog2 = new NormalAlertDialog.Builder(AddRepairMachineTaskActivity.this)
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

        File file = new File(SDCardUtils.getAbsRootDir(AddRepairMachineTaskActivity.this) + File.separator
                + "ImageAuth");

        if (!file.exists()) {
            file.mkdir();
        } else {

        }
        mImageUri = Uri.parse("file://" + SDCardUtils.getAbsRootDir(AddRepairMachineTaskActivity.this)
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
             if(resultcode == Constant.SELECT_EQUIPMENT)
            {
                pro_id = data.getStringExtra("pro_id");
                pro_name = data.getStringExtra("pro_name");
                ord_color = data.getStringExtra("ord_color");
                pro_no = data.getStringExtra("pro_no");
                ord_no = data.getStringExtra("ord_no");
                ord_managerno = data.getStringExtra("ord_managerno");
                txt_equipment.setText(pro_name+"("+ord_no+")");
                txt_equipment.setTextColor(getResources().getColor(R.color.color_333333));
            }
            else if(resultcode == Constant.ADD_FAULT_DESCRIPTION)
            {
                specific_reason = data.getStringExtra("description");
                txt_fault_description.setText(specific_reason);
                txt_fault_description.setTextColor(getResources().getColor(R.color.color_333333));
            }
            else if(resultcode == Constant.SELECT_FAULT_TYPE)
            {
                fault_cause = data.getStringExtra("name");
                txt_fault_phenomenon.setText(data.getStringExtra("name"));
                txt_fault_phenomenon.setTextColor(getResources().getColor(R.color.color_333333));
            }
             else if(resultcode == Constant.EVALUATE_OK)
             {
                 finish();
             }
            else if (requestcode == Constant.TAKE_LOCAL_PICTURE) {
                startPhotoZoom(data.getData());
            }
            else if (requestcode == Constant.TAKE_LOCAL_PICTURE_CROP) {
                disPlayImg(logo);
            }
            else if (requestcode == Constant.TAKE_PHOTO) {
                startPhotoZoom(mImageUri);
            }
            else if (requestcode == Constant.SHOW_PHOTO)
            {
                //删除掉图片路径
                String[] urls = fautl_url.split(",");
                fautl_url = "";
                for (int i = 0; i < urls.length; i ++)
                {
                    if(i != deleteIndex)
                    {
                        if(StringUtil.isEmpty(fautl_url))
                        {
                            fautl_url = urls[i] +",";
                        }
                        else
                        {
                            fautl_url += urls[i] + ",";
                        }
                    }
                }
                photoInfos.remove(deleteIndex);
                showServicePhoto();
            }
            //图片剪切时的返回
           else if (requestcode == Constant.CLIP_CODE) {
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
                            if(StringUtil.isEmpty(fautl_url))
                            {
                                fautl_url = data.getData().get(0).getImgUrl() +",";
                            }
                            else
                            {
                                fautl_url += data.getData().get(0).getImgUrl() + ",";
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
        servicePhotoAdapter = new ServicePhotoAdapter(AddRepairMachineTaskActivity.this, photoInfos);
        grid_photo.setAdapter(servicePhotoAdapter);
    }

    /**
     * 显示时间控件
     */
    public void showDatePicker(){
        DateAndTimerPicker.Builder builder = new DateAndTimerPicker.Builder(this, DateShowType.YMDHM);

        DateAndTimerPicker picker = builder.setOnDateAndTimeSelectedListener(new DateAndTimerPicker.OnDateAndTimeSelectedListener() {
            @Override
            public void onDateAndTimeSelected(String[] dates) {
                make_time = dates[0]+"-"+dates[1]+"-"+dates[2]+" "+dates[3]+":"+dates[4]+":00";
                txt_date.setText(make_time);
                txt_date.setTextColor(getResources().getColor(R.color.color_333333));
            }
        }).create();
        picker.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(data == null)
        {
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

    //为弹出窗口实现监听类
    public View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.grxx_xbxz_qd:
                    List<Map<String, String>> datas = menuWindow.getPpWindowSelect();
                    user_address = datas.get(1).get("addrStr");
                    txt_pcd.setText(user_address);
                    txt_pcd.setTextColor(getResources().getColor(R.color.color_333333));
                    sheng = menuWindow.getSheng();
                    shi = menuWindow.getShi();
                    if(shi.equals("市辖区") || shi.equals("县"))
                    {
                        shi = "";
                    }
                    qu = menuWindow.getQu();
                    break;
            }
        }
    };

    void addTask()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        if(type == 1)
        {
            dao.machineRepairTask(SesSharedReferences.getUserId(AddRepairMachineTaskActivity.this), pro_id, pro_name, ord_color, make_time, contact_person, contact_way, user_address, address_details, filter_name, fault_cause, specific_reason, fautl_url, pro_no, ord_managerno, ord_no, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    dialog.dismiss();
                    ToastUtil.showToast("任务发布成功！");
                    finish();
                }

                @Override
                public void resFailure(String message) {
                    dialog.dismiss();
                    ToastUtil.showToast(message);
                }
            });
        }
        else
        {
            dao.otherTask(SesSharedReferences.getUserId(AddRepairMachineTaskActivity.this), pro_id, pro_name, ord_color, make_time, contact_person, contact_way, user_address, address_details, filter_name, specific_reason, fautl_url, pro_no, ord_managerno, ord_no, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    dialog.dismiss();
                    ToastUtil.showToast("任务发布成功！");
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

    void showTaskDetail()
    {
        if(!StringUtil.isEmpty(data.getFautl_url()))
        {
            String[] urls =  data.getFautl_url().split(",");
            for (String s : urls)
            {
                ServicePhotoBean servicePhotoBean = new ServicePhotoBean();
                servicePhotoBean.setImg_url(s);
                photoInfos.add(servicePhotoBean);
            }
        }

        edit_name.setText(data.getContact_person());
        edit_phone.setText(data.getContact_way());
        edit_addr.setText(data.getAddress_details());
        txt_equipment.setText(data.getPro_name()+"（"+data.getOrd_no()+"）");
        txt_date.setText(data.getMake_time());
        txt_pcd.setText(data.getUser_address());
        txt_fault_description.setText(data.getSpecific_reason());
        txt_fault_phenomenon.setText(data.getFault_cause());
        txt_equipment.setTextColor(getResources().getColor(R.color.color_333333));
        txt_date.setTextColor(getResources().getColor(R.color.color_333333));
        txt_pcd.setTextColor(getResources().getColor(R.color.color_333333));
        txt_fault_description.setTextColor(getResources().getColor(R.color.color_333333));
        txt_fault_phenomenon.setTextColor(getResources().getColor(R.color.color_333333));
        edit_name.setEnabled(false);
        edit_phone.setEnabled(false);
        edit_addr.setEnabled(false);
        edit_name.setTextColor(getResources().getColor(R.color.color_333333));
        edit_phone.setTextColor(getResources().getColor(R.color.color_333333));
        edit_addr.setTextColor(getResources().getColor(R.color.color_333333));
    }
}
