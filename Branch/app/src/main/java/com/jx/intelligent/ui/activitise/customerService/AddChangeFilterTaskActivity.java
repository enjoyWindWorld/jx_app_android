package com.jx.intelligent.ui.activitise.customerService;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CustomerServiceDao;
import com.jx.intelligent.enums.DateShowType;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.CustomerServiceTaskDetailResult;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.CitySelectPopWindow;
import com.jx.intelligent.view.DateAndTimerPicker;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */

public class AddChangeFilterTaskActivity extends RHBaseActivity {

    private RelativeLayout layout_equipment, layout_filter, layout_date, layout_pcd, layout_fault_description;
    private EditText edit_name, edit_phone, edit_addr;
    private TextView txt_equipment, txt_filter, txt_date, txt_pcd, txt_fault_description;
    private CustomerServiceDao dao;
    private ProgressWheelDialog dialog;
    private CitySelectPopWindow menuWindow;
    private String pro_id, pro_name, ord_color, make_time, contact_person, contact_way, user_address, address_details, proflt_life, filter_name, pro_no, ord_managerno, ord_no, specific_reason, sheng = "", shi = "", qu = "";;
    private CustomerServiceTaskDetailResult.Data data;

    @Override
    protected void init() {
        dao = new CustomerServiceDao();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_add_change_filter_task;
    }

    @Override
    protected void initTitle() {
        data = (CustomerServiceTaskDetailResult.Data)getIntent().getSerializableExtra("obj");
        new TitleBarHelper(AddChangeFilterTaskActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("滤芯更换")
                .setRightText(data == null ? "发布" : data.getFas_state().equals("200") ? "" : "验收")
                .setLeftClickListener(this)
                .setRightClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        layout_equipment = (RelativeLayout) contentView.findViewById(R.id.layout_equipment);
        layout_filter = (RelativeLayout) contentView.findViewById(R.id.layout_filter);
        layout_date = (RelativeLayout) contentView.findViewById(R.id.layout_date);
        layout_pcd = (RelativeLayout) contentView.findViewById(R.id.layout_pcd);
        layout_fault_description = (RelativeLayout) contentView.findViewById(R.id.layout_fault_description);

        edit_name = (EditText) contentView.findViewById(R.id.edit_name);
        edit_phone = (EditText) contentView.findViewById(R.id.edit_phone);
        edit_addr = (EditText) contentView.findViewById(R.id.edit_addr);

        txt_equipment = (TextView) contentView.findViewById(R.id.txt_equipment);
        txt_filter = (TextView) contentView.findViewById(R.id.txt_filter);
        txt_date = (TextView) contentView.findViewById(R.id.txt_date);
        txt_pcd = (TextView) contentView.findViewById(R.id.txt_pcd);
        txt_fault_description = (TextView) contentView.findViewById(R.id.txt_fault_description);

        dialog = new ProgressWheelDialog(AddChangeFilterTaskActivity.this);
        if(data != null)
        {
            showTaskDetail();
        }
        else
        {
            layout_equipment.setOnClickListener(this);
            layout_filter.setOnClickListener(this);
            layout_date.setOnClickListener(this);
            layout_pcd.setOnClickListener(this);
            layout_fault_description.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.layout_fault_description:
                Intent intent_description = new Intent(AddChangeFilterTaskActivity.this, AddFaultDescriptionActivity.class);
                intent_description.putExtra("description", specific_reason);
                startActivityForResult(intent_description, Constant.REQUEST_CODE);
                break;
            case R.id.layout_equipment:
                Intent intent_equipment = new Intent(AddChangeFilterTaskActivity.this, RepairEquipmentListActivity.class);
                startActivityForResult(intent_equipment, Constant.REQUEST_CODE);
                break;
            case R.id.layout_filter:
                if(!StringUtil.isEmpty(pro_no))
                {
                    Intent intent = new Intent(AddChangeFilterTaskActivity.this, RepairFilterDetailActivity.class);
                    intent.putExtra("pro_no", pro_no);
                    startActivityForResult(intent, Constant.REQUEST_CODE);
                }
                break;
            case R.id.layout_date:
                showDatePicker();
                break;
            case R.id.layout_pcd:
                menuWindow = new CitySelectPopWindow(AddChangeFilterTaskActivity.this, itemsOnClick);
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(AddChangeFilterTaskActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                KeyboardUtil.hideSoftKeyboard(txt_pcd);
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
                    else if(StringUtil.isEmpty(proflt_life) || StringUtil.isEmpty(filter_name))
                    {
                        ToastUtil.showToast("请选择更换的滤芯");
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
                    Intent intent = new Intent(AddChangeFilterTaskActivity.this, AddServiceEvaluateActivity.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("pro_no", data.getPro_no());
                    intent.putExtra("ord_no", data.getOrd_no());
                    intent.putExtra("service_type", data.getFas_type());
                    intent.putExtra("ord_managerno", data.getOrd_managerno());
                    startActivityForResult(intent, Constant.REQUEST_CODE);
                }
                break;
        }
    }

    void addTask()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        dao.changeFilterTask(SesSharedReferences.getUserId(AddChangeFilterTaskActivity.this), pro_id, pro_name, ord_color, make_time, contact_person, contact_way, user_address, address_details, proflt_life, filter_name, pro_no, ord_managerno, ord_no, specific_reason, new ResponseResult() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.SELECT_EQUIPMENT)
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
        else if(resultCode == Constant.SELECT_FILTER)
        {
            proflt_life = data.getStringExtra("proflt_life");
            filter_name = data.getStringExtra("filter_name");
            txt_filter.setText(filter_name);
            txt_filter.setTextColor(getResources().getColor(R.color.color_333333));
        }
        else if(resultCode == Constant.ADD_FAULT_DESCRIPTION)
        {
            specific_reason = data.getStringExtra("description");
            txt_fault_description.setText(specific_reason);
            txt_fault_description.setTextColor(getResources().getColor(R.color.color_333333));
        }
        else if(resultCode == Constant.EVALUATE_OK)
        {
            finish();
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

    void showTaskDetail()
    {
        edit_name.setText(data.getContact_person());
        edit_phone.setText(data.getContact_way());
        edit_addr.setText(data.getAddress_details());
        txt_equipment.setText(data.getPro_name()+"（"+data.getOrd_no()+"）");
        txt_filter.setText(data.getFilter_name());
        txt_date.setText(data.getMake_time());
        txt_pcd.setText(data.getUser_address());
        txt_fault_description.setText(data.getSpecific_reason());
        txt_equipment.setTextColor(getResources().getColor(R.color.color_333333));
        txt_filter.setTextColor(getResources().getColor(R.color.color_333333));
        txt_date.setTextColor(getResources().getColor(R.color.color_333333));
        txt_pcd.setTextColor(getResources().getColor(R.color.color_333333));
        txt_fault_description.setTextColor(getResources().getColor(R.color.color_333333));
        edit_name.setEnabled(false);
        edit_phone.setEnabled(false);
        edit_addr.setEnabled(false);
        edit_name.setTextColor(getResources().getColor(R.color.color_333333));
        edit_phone.setTextColor(getResources().getColor(R.color.color_333333));
        edit_addr.setTextColor(getResources().getColor(R.color.color_333333));
    }

}
