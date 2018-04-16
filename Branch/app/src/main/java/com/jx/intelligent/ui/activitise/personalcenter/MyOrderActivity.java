package com.jx.intelligent.ui.activitise.personalcenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.MyOrderAdapter;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetOrderListResult;
import com.jx.intelligent.ui.activitise.MainActivity;
import com.jx.intelligent.ui.activitise.payment.DetailsOrderActivity;
import com.jx.intelligent.ui.fragments.MyOrderFragment;
import com.jx.intelligent.ui.fragments.MyWaterPurifierFragment;
import com.jx.intelligent.util.DensityUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/16 0016.
 * 我的订单列表
 */
public class MyOrderActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MyOrderActivity";
    private String state;
    private RadioButton mAll_order;
    private RadioButton mPay_order;
    private RadioButton mBind_order;
    private RadioButton mXufei_order;
    private RadioButton mNo_pay_order;
    private RadioGroup mRadio_group;
    private int fragmentPosition;
    private boolean isComeFromUserCenter;
    private ImageView titlebar_left_iv;
    private RelativeLayout titlebar_left_rl;

    private MyOrderFragment mAllOrderFragment;
    private MyOrderFragment mNoPayOrderFragment;
    private MyOrderFragment mPayOrderFragment;
    private MyOrderFragment mBindOrderFragment;
    private MyOrderFragment mXufeiOrderFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //获取state  根据传递过来的state值 进行判断到底 展示什么内容
        state = getIntent().getStringExtra("state");
        fragmentPosition = getIntent().getIntExtra("postion", 0);
        isComeFromUserCenter = getIntent().getBooleanExtra("isComeFromUserCenter", false);
        ((TextView) findViewById(R.id.titlebar_center_tv)).setText("我的订单");
        titlebar_left_iv = (ImageView)findViewById(R.id.titlebar_left_iv) ;
        titlebar_left_iv.setVisibility(View.VISIBLE);
        titlebar_left_rl = (RelativeLayout)findViewById(R.id.titlebar_left_rl);
        titlebar_left_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isComeFromUserCenter)
                {
                    MyOrderActivity.this.finish();
                }
                else
                {
                    Intent intent_main = new Intent(MyOrderActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    MyOrderActivity.this.finish();
                }
            }
        });

        //我的订单列表中上面的 5个 title
        mRadio_group = (RadioGroup) findViewById(R.id.Order_RadioGroup);
        mAll_order = (RadioButton) findViewById(R.id.all_Order);
        mPay_order = (RadioButton) findViewById(R.id.pay_Order);
        mBind_order = (RadioButton) findViewById(R.id.bind_Order);
        mXufei_order = (RadioButton) findViewById(R.id.xufei_Order);
        mNo_pay_order = (RadioButton) findViewById(R.id.no_pay_Order);
        mRadio_group.setOnCheckedChangeListener(this);
        switch (fragmentPosition)
        {
            case 0:
                mAll_order.setChecked(true);
                break;
            case 1:
                mNo_pay_order.setChecked(true);
                break;
            case 2:
                mPay_order.setChecked(true);
                break;
            case 3:
                mBind_order.setChecked(true);
                break;
            case 4:
                mXufei_order.setChecked(true);
                break;
        }
    }

    /**
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
        if (mAllOrderFragment != null) {
            trans.hide(mAllOrderFragment);
        }
        if (mNoPayOrderFragment != null) {
            trans.hide(mNoPayOrderFragment);
        }
        if (mPayOrderFragment != null) {
            trans.hide(mPayOrderFragment);
        }
        if (mBindOrderFragment != null) {
            trans.hide(mBindOrderFragment);
        }
        if (mXufeiOrderFragment != null) {
            trans.hide(mXufeiOrderFragment);
        }
    }

    private void setFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        setAnimation(fragmentPosition, transaction);
        hideFragments(transaction);
        switch (fragmentPosition) {
            case 0:
                if (mAllOrderFragment == null) {
                    mAllOrderFragment = new MyOrderFragment();
                    transaction.add(R.id.Fm_container, mAllOrderFragment);
                } else {
                    transaction.show(mAllOrderFragment);
                }
                mAllOrderFragment.setState(state);
                break;
            case 1:
                if (mNoPayOrderFragment == null) {
                    mNoPayOrderFragment = new MyOrderFragment();
                    transaction.add(R.id.Fm_container, mNoPayOrderFragment);
                } else {
                    transaction.show(mNoPayOrderFragment);
                }
                mNoPayOrderFragment.setState(state);
                break;
            case 2:
                if (mPayOrderFragment == null) {
                    mPayOrderFragment = new MyOrderFragment();
                    transaction.add(R.id.Fm_container, mPayOrderFragment);
                } else {
                    transaction.show(mPayOrderFragment);
                }
                mPayOrderFragment.setState(state);
                break;
            case 3:
                if (mBindOrderFragment == null) {
                    mBindOrderFragment = new MyOrderFragment();
                    transaction.add(R.id.Fm_container, mBindOrderFragment);
                } else {
                    transaction.show(mBindOrderFragment);
                }
                mBindOrderFragment.setState(state);
                break;
            case 4:
                if (mXufeiOrderFragment == null) {
                    mXufeiOrderFragment = new MyOrderFragment();
                    transaction.add(R.id.Fm_container, mXufeiOrderFragment);
                } else {
                    transaction.show(mXufeiOrderFragment);
                }
                mXufeiOrderFragment.setState(state);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * @param position
     * @param transaction
     */
    private void setAnimation(int position, FragmentTransaction transaction) {

        if (position > fragmentPosition) {
//            transaction.setCustomAnimations(R.anim.slide_in_from_left,
//                    R.anim.abc_fade_out);

        } else if (position < fragmentPosition) {
//            transaction.setCustomAnimations(R.anim.slide_in_from_right,
//                    R.anim.abc_fade_out);
        }

        fragmentPosition = position;
    }

    @Override
    public void onBackPressed() {
        if(isComeFromUserCenter)
        {
            MyOrderActivity.this.finish();
        }
        else
        {
            Intent intent_main = new Intent(MyOrderActivity.this, MainActivity.class);
            startActivity(intent_main);
            MyOrderActivity.this.finish();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId) {
            //全部订单
            case R.id.all_Order:
                state="";
                fragmentPosition = 0;
                setFragment();
                break;
            //未支付订单
            case R.id.no_pay_Order:
                state="0";
                fragmentPosition = 1;
                setFragment();
                break;
            //已支付订单
            case R.id.pay_Order:
                state="1";
                fragmentPosition = 2;
                setFragment();
                break;
            //已绑定订单
            case R.id.bind_Order:
                state="3";
                fragmentPosition = 3;
                setFragment();
                break;
            //续费订单
            case R.id.xufei_Order:
                state="4,5";
                fragmentPosition = 4;
                setFragment();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.DELETE_ORDER)
        {
            switch (fragmentPosition) {
                case 0:
                    if (mAllOrderFragment != null) {
                        mAllOrderFragment.freshView();
                    }
                    break;
                case 1:
                    if (mNoPayOrderFragment != null) {
                        mNoPayOrderFragment.freshView();
                    }
                    break;
                case 2:
                    if (mPayOrderFragment != null) {
                        mPayOrderFragment.freshView();
                    }
                    break;
                case 3:
                    if (mBindOrderFragment != null) {
                        mBindOrderFragment.freshView();
                    }
                    mBindOrderFragment.setState(state);
                    break;
                case 4:
                    if (mXufeiOrderFragment != null) {
                        mXufeiOrderFragment.freshView();
                    }
                    break;
            }
        }
    }
}
