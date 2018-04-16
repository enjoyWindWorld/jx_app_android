package com.jx.intelligent.ui.activitise.personalcenter;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 意见反馈
 * Created by Administrator on 2016/11/15 0015.
 */
public class FeedbackActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;

    EditText grxx_yjfk_srk;
    Button grxx_yjfk_tjan;

    UserCenter userCenterDao;
    ProgressWheelDialog dialog;


    @Override
    protected void init() {

        userCenterDao = new UserCenter();

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("意见反馈")
                .setRightText("")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        grxx_yjfk_srk = (EditText) contentView.findViewById(R.id.grxx_yjfk_srk);
        grxx_yjfk_tjan = (Button) contentView.findViewById(R.id.grxx_yjfk_tjan);

        grxx_yjfk_tjan.setOnClickListener(this);
        dialog = new ProgressWheelDialog(FeedbackActivity.this);

        hideSoftKeyboard(grxx_yjfk_srk);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:

                finish();

                break;

            case R.id.grxx_yjfk_tjan:

                //TODO 提交意见反馈

                if (StringUtil.isEmpty(grxx_yjfk_srk.getText().toString().trim())) {
                   ToastUtil.showToast("请输入意见反馈");
                }
                else if(Utils.filterEmoji(grxx_yjfk_srk.getText().toString().trim())) {
                    ToastUtil.showToast("请勿输入表情符号");
                }
                else {
                    commitFeedback();
                }


                break;


        }


    }


    /**
     * 提交意见反馈
     */
    public void commitFeedback() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenterDao.commitFeedbackTask(SesSharedReferences.getPhoneNum(FeedbackActivity.this),
                grxx_yjfk_srk.getText().toString().trim(), new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        dialog.dismiss();
                        ToastUtil.showToast("提交反馈成功");
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
