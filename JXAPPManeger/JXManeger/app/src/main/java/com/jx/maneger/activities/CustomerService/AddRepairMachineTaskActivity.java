package com.jx.maneger.activities.CustomerService;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.adapter.jxAdapter.ServicePhotoAdapter;
import com.jx.maneger.bean.ServicePhotoBean;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.results.CustomerServiceTaskDetailResult;
import com.jx.maneger.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class AddRepairMachineTaskActivity extends Activity implements View.OnClickListener {

    private RelativeLayout  layout_fault_phenomenon;
    private TextView txt_equipment, txt_fault_phenomenon, txt_fault_description, txt_date, txt_pcd;
    private EditText edit_name, edit_phone, edit_addr;
    private MyGridView grid_photo;
    private List<ServicePhotoBean> photoInfos = new ArrayList<ServicePhotoBean>();
    private int type = 1;
    private ServicePhotoAdapter servicePhotoAdapter;
    private CustomerServiceTaskDetailResult.Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repair_machine_task);

        type = getIntent().getIntExtra("type", 1);
        data = (CustomerServiceTaskDetailResult.Data)getIntent().getSerializableExtra("obj");

        layout_fault_phenomenon = (RelativeLayout) findViewById(R.id.layout_fault_phenomenon);

        txt_equipment = (TextView) findViewById(R.id.txt_equipment);
        txt_fault_phenomenon = (TextView) findViewById(R.id.txt_fault_phenomenon);
        txt_fault_description = (TextView) findViewById(R.id.txt_fault_description);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_pcd = (TextView) findViewById(R.id.txt_pcd);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_addr = (EditText) findViewById(R.id.edit_addr);

        grid_photo = (MyGridView) findViewById(R.id.grid_photo);
        if(type == 1)
        {
            layout_fault_phenomenon.setVisibility(View.VISIBLE);
        }

        if(data != null)
        {
            showTaskDetail();
        }

        new TitleBarHelper(AddRepairMachineTaskActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("设备报修")
                .setLeftClickListener(this);

        showServicePhoto();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }

    void showServicePhoto()
    {
        for (ServicePhotoBean bean : photoInfos)
        {
            if(TextUtils.isEmpty(bean.getImg_url()))
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

    void showTaskDetail()
    {
        if(!TextUtils.isEmpty(data.getFautl_url()))
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
