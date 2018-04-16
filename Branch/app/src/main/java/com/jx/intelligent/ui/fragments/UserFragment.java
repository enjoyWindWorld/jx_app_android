package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.LoginResult;
import com.jx.intelligent.result.MessageNoReadResult;
import com.jx.intelligent.ui.activitise.customerService.TaskManageActivity;
import com.jx.intelligent.ui.activitise.payment.ShoppingCartActivity;
import com.jx.intelligent.ui.activitise.personalcenter.FeedbackActivity;
import com.jx.intelligent.ui.activitise.personalcenter.GeneraLizeActivity;
import com.jx.intelligent.ui.activitise.personalcenter.HomeAddrActivity;
import com.jx.intelligent.ui.activitise.personalcenter.MerchantReleaseActivity;
import com.jx.intelligent.ui.activitise.personalcenter.MessageActivity;
import com.jx.intelligent.ui.activitise.personalcenter.MyOrderActivity;
import com.jx.intelligent.ui.activitise.personalcenter.MyWaterPurifierManageActivity;
import com.jx.intelligent.ui.activitise.personalcenter.PerInfoActivity;
import com.jx.intelligent.ui.activitise.personalcenter.SettingActivity;
import com.jx.intelligent.ui.activitise.personalcenter.SharedBindingsActivity;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.XRoundAngleImageView;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建者： weifei
 * @项目名: jx
 * @包名: com.jx.intelligent.ui.fragments
 * @创建时间： 2016/11/10 5:17
 * @描述： ${TODO} 用户中心
 */

public class UserFragment extends RHBaseFragment {

    private static final String TAG = "UserFragment";
    LinearLayout wd_sz, wd_yjfk, wd_wdjsq, wd_wddd, wd_sjfb, wd_fxbd, wd_shpj;
    MyOnclickList myOnclickList;
    UserCenter userCenterDao;
    TextView wd_nickname_text, wd_sign_text, txt_amount;
    FrameLayout layout_msg, layout_dot;
    XRoundAngleImageView wd_yhtx_img;
    ProgressWheelDialog dialog;
    private DBManager dbManager;
    private LinearLayout mNon_payMent;
    private LinearLayout mHave_paid;
    private LinearLayout mIs_binding;
    private LinearLayout mReNew;
    private LinearLayout mMy_address;
    private LinearLayout mGeneralize;
    private LinearLayout mGo_shopping_cart;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        userCenterDao = new UserCenter();
        if (SesSharedReferences.getUsrLoginState(getActivity())) {
            gerUserInfo();
            getMessageList();
        }
    }


    @Override
    protected int setContentLayout() {
        return R.layout.userfragment_layout;
    }

    @Override
    protected void initTitle(View titleView) {
        titleView.findViewById(R.id.titlebar_rl).setVisibility(View.GONE);
    }


    @Override
    protected void findView(View contentView) {
        myOnclickList = new MyOnclickList();
        wd_wdjsq = (LinearLayout) contentView.findViewById(R.id.wd_wdjsq);
        wd_wdjsq.setOnClickListener(myOnclickList);
        wd_wddd = (LinearLayout) contentView.findViewById(R.id.wd_wddd);
        wd_wddd.setOnClickListener(myOnclickList);
        wd_sjfb = (LinearLayout) contentView.findViewById(R.id.wd_sjfb);
        wd_sjfb.setOnClickListener(myOnclickList);
        wd_sz = (LinearLayout) contentView.findViewById(R.id.wd_sz);
        wd_sz.setOnClickListener(myOnclickList);
        wd_fxbd = (LinearLayout) contentView.findViewById(R.id.wd_fxbd);
        wd_fxbd.setOnClickListener(myOnclickList);
        wd_yjfk = (LinearLayout) contentView.findViewById(R.id.wd_yjfk);
        wd_yjfk.setOnClickListener(myOnclickList);
        wd_shpj= (LinearLayout) contentView.findViewById(R.id.customer_service);
        wd_shpj.setOnClickListener(myOnclickList);

        wd_nickname_text = (TextView) contentView.findViewById(R.id.wd_nickname_text);
        txt_amount = (TextView) contentView.findViewById(R.id.txt_amount);
        wd_sign_text = (TextView) contentView.findViewById(R.id.wd_sign_text);
        wd_yhtx_img = (XRoundAngleImageView) contentView.findViewById(R.id.wd_yhtx_img);
        layout_msg = (FrameLayout) contentView.findViewById(R.id.layout_msg);
        layout_dot = (FrameLayout) contentView.findViewById(R.id.layout_dot);
        dialog = new ProgressWheelDialog(getActivity());
        //未支付按钮
        mNon_payMent = (LinearLayout) contentView.findViewById(R.id.non_payment);
        mNon_payMent.setOnClickListener(myOnclickList);
        //已支付按钮
        mHave_paid = (LinearLayout) contentView.findViewById(R.id.have_paid);
        mHave_paid.setOnClickListener(myOnclickList);
        //已绑定
        mIs_binding = (LinearLayout) contentView.findViewById(R.id.Is_binding);
        mIs_binding.setOnClickListener(myOnclickList);
        //续费
        mReNew = (LinearLayout) contentView.findViewById(R.id.renew);
        mReNew.setOnClickListener(myOnclickList);
        //我的地址
        mMy_address = (LinearLayout) contentView.findViewById(R.id.user_address);
        mMy_address.setOnClickListener(myOnclickList);
        //我的推广
        mGeneralize = (LinearLayout) contentView.findViewById(R.id.generalize);
        mGeneralize.setOnClickListener(myOnclickList);
        //我的页面购物车栏目
        mGo_shopping_cart = (LinearLayout) contentView.findViewById(R.id.Go_shopping_car);

        layout_msg.setOnClickListener(myOnclickList);
        wd_yhtx_img.setOnClickListener(myOnclickList);
        wd_nickname_text.setOnClickListener(myOnclickList);
        wd_sign_text.setOnClickListener(myOnclickList);
        mGo_shopping_cart.setOnClickListener(myOnclickList);

    }

    public void home(View v) {

    }


    class MyOnclickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.wd_yhtx_img:
                case R.id.wd_nickname_text:
                case R.id.wd_sign_text:
                    goPerInfo();
                    break;
                case R.id.wd_wdjsq:
                    //TODO 跳转到我的净水器界面
                    goMyWaterPurifier();
                    break;
                case R.id.wd_wddd:
                    //TODO 跳转到我的订单界面
                    goMyOrder();
                    break;
                    //我的发布
                case R.id.wd_sjfb:
                    //TODO 跳转到商家发布
                    goMerchantRelease();
                    break;
                case R.id.wd_yjfk:
                    goFeedback();
                    break;
                case R.id.wd_sz:
                    goSetting();
                    break;
                case R.id.wd_fxbd:
                    goSharedBindings();
                    break;
                case R.id.layout_msg:
                    goMessageLists();
                    break;
                    //未支付
                case R.id.non_payment:
                    Intent intent = new Intent(getActivity(),MyOrderActivity.class);
                    intent.putExtra("state",0+"");
                    intent.putExtra("postion", 1);
                    intent.putExtra("isComeFromUserCenter", true);
                    getActivity().startActivity(intent);
                    break;
                    //已支付
                case R.id.have_paid:
                    Intent intent2 = new Intent(getActivity(),MyOrderActivity.class);
                    intent2.putExtra("state",1+"");
                    intent2.putExtra("postion", 2);
                    intent2.putExtra("isComeFromUserCenter", true);
                    getActivity().startActivity(intent2);
                    break;
                    //已绑定
                case R.id.Is_binding:
                    Intent intent3 = new Intent(getActivity(),MyOrderActivity.class);
                    intent3.putExtra("state",3+"");
                    intent3.putExtra("postion", 3);
                    intent3.putExtra("isComeFromUserCenter", true);
                    getActivity().startActivity(intent3);
                    break;
                    //续费
                case R.id.renew:
                    Intent intent4 = new Intent(getActivity(),MyOrderActivity.class);
                    intent4.putExtra("state","4,5");
                    intent4.putExtra("postion", 4);
                    intent4.putExtra("isComeFromUserCenter", true);
                    getActivity().startActivity(intent4);
                    break;
                    //我的地址
                case R.id.user_address:
                    Intent intent5 = new Intent(getActivity(), HomeAddrActivity.class);
                    getActivity().startActivity(intent5);
                    break;
                    //我的推广
                case R.id.generalize:
                    Intent intent6 = new Intent(getActivity(), GeneraLizeActivity.class);
                    getActivity().startActivity(intent6);
                    break;
                case R.id.Go_shopping_car:
                    Intent intent7 = new Intent(getActivity(), ShoppingCartActivity.class);
                    getActivity().startActivity(intent7);
                    break;
                case R.id.customer_service:
                    Intent intent8 = new Intent(getActivity(), TaskManageActivity.class);
                    getActivity().startActivity(intent8);
                    break;
            }
        }
    }


    /**
     * 跳转到用户信息界面
     */
    private void goPerInfo() {
        Intent intent = new Intent(getActivity(), PerInfoActivity.class);
        getActivity().startActivity(intent);
    }


    /**
     * 跳转到我的净水器界面
     */
    private void goMyWaterPurifier() {
        Intent intent = new Intent(getActivity(), MyWaterPurifierManageActivity.class);
        getActivity().startActivity(intent);

    }

    /**
     * 跳转到我的订单界面
     */
    private void goMyOrder() {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        intent.putExtra("state","");
        intent.putExtra("postion", 0);
        intent.putExtra("isComeFromUserCenter", true);
        getActivity().startActivity(intent);

    }

    /**
     * 跳转到我的发布
     */
    private void goMerchantRelease() {
        Intent intent = new Intent(getActivity(), MerchantReleaseActivity.class);
        getActivity().startActivity(intent);

    }

    /**
     * 跳转到设置界面
     */
    private void goSetting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        getActivity().startActivity(intent);
        onDestroy();

    }

    /**
     * 跳转分享绑定界面
     */
    private void goSharedBindings() {
        Intent intent = new Intent(getActivity(), SharedBindingsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("FenX", bundle);
        getActivity().startActivity(intent);
    }

    /**
     * 跳转消息列表界面
     */
    private void goMessageLists() {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        getActivity().startActivity(intent);

    }

    /**
     * 跳转到意见反馈界面
     */
    private void goFeedback() {
        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
        getActivity().startActivity(intent);

    }


    /**
     * 获取用户信息（包括头像、昵称、个性签名）
     */
    public void gerUserInfo() {
        getPreUserInfoData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        userCenterDao.getUserInfoTask(SesSharedReferences.getUserId(getActivity()), new ResponseResult() {

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                LoginResult loginResult = (LoginResult) object;

                showDataView(loginResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的服务类型的数据
     */
    void getPreUserInfoData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", SesSharedReferences.getUserId(getActivity()));
        String js = dbManager.getUrlJsonData(Constant.USER_GET_MY_INFO_WP + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            LoginResult loginResult = new Gson().fromJson(js, LoginResult.class);
            showDataView(loginResult);
        }
    }

    /**
     * 显示用户信息
     * @param loginResult
     */
    void showDataView(LoginResult loginResult)
    {

        if(loginResult != null && loginResult.getData().size() > 0)
        {

            RHBaseApplication.getInstance().setLoginResult(loginResult);
            wd_nickname_text.setText(loginResult.getData().get(0).getNickname());
            wd_sign_text.setText(loginResult.getData().get(0).getSign());
            GlideHelper.setImageView(getContext(), loginResult.getData().get(0).getUserImg(), wd_yhtx_img, null, R.mipmap.img_head_empty);
        }
        else
        {
            ToastUtil.showToast("获取用户信息失败");
        }
    }

    /**
     * 获取未读消息
     */
    public void getMessageList() {
        if(!StringUtil.isEmpty(SesSharedReferences.getUserId(getActivity())))
        {
            if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
            {
                return;
            }

            userCenterDao.getNoReadMsgTask(SesSharedReferences.getUserId(getActivity()),  new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    MessageNoReadResult messageNoReadResult = (MessageNoReadResult)object;
                    if(messageNoReadResult != null && messageNoReadResult.getData().size()>0)
                    {
                        RHBaseApplication.getInstance().setNotReadMsg(messageNoReadResult.getData().get(0).getNumber());
                        RHBaseApplication.getMainActivity().changeMineBtn(RHBaseApplication.getMainActivity().mian_rb_mine.isChecked());
                        if(messageNoReadResult.getData().get(0).getNumber() > 0)
                        {
                            layout_dot.setVisibility(View.VISIBLE);
                            if(messageNoReadResult.getData().get(0).getNumber() > 99)
                            {
                                txt_amount.setText("99+");
                            }
                            else
                            {
                                txt_amount.setText(messageNoReadResult.getData().get(0).getNumber() +"");
                            }
                        }
                        else
                        {
                            layout_dot.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void resFailure(String message) {
                }
            });
        }
    }

    public void refActivityData() {

        gerUserInfo();
        getMessageList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SesSharedReferences.getUsrLoginState(getActivity())) {
            getMessageList();
        }
    }
}
