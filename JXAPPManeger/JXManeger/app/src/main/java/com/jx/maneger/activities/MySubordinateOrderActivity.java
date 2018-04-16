package com.jx.maneger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.fragments.MyOrderFragment;
import com.jx.maneger.fragments.MySubordinateOrderFragment;
import com.jx.maneger.util.StringUtil;

/**
 * 下属的订单列表
 */
public class MySubordinateOrderActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MyOrderActivity";
    private String state;
    private String ord_managerno;
    private String name;
    private RadioButton mPay_order;
    private RadioButton mBind_order;
    private RadioButton mXufei_order;
    private RadioGroup mRadio_group;
    private int fragmentPosition;
    private ImageView titlebar_left_iv;
    private RelativeLayout titlebar_left_rl;

    private MySubordinateOrderFragment mPayOrderFragment;
    private MySubordinateOrderFragment mBindOrderFragment;
    private MySubordinateOrderFragment mXufeiOrderFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        if(getIntent() != null)
        {
            name = getIntent().getStringExtra("name");
            ord_managerno = getIntent().getStringExtra("ord_managerno");
        }

        if(StringUtil.isEmpty(name))
        {
            ((TextView) findViewById(R.id.titlebar_center_tv)).setText("我的订单");
        }
        else
        {
            ((TextView) findViewById(R.id.titlebar_center_tv)).setText(name + "的订单");
        }
        titlebar_left_iv = (ImageView)findViewById(R.id.titlebar_left_iv) ;
        titlebar_left_iv.setVisibility(View.VISIBLE);
        titlebar_left_rl = (RelativeLayout)findViewById(R.id.titlebar_left_rl);
        titlebar_left_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySubordinateOrderActivity.this.finish();
            }
        });
        mRadio_group = (RadioGroup) findViewById(R.id.Order_RadioGroup);
        mPay_order = (RadioButton) findViewById(R.id.pay_Order);
        mBind_order = (RadioButton) findViewById(R.id.bind_Order);
        mXufei_order = (RadioButton) findViewById(R.id.xufei_Order);
        mRadio_group.setOnCheckedChangeListener(this);
        switch (fragmentPosition)
        {
            case 0:
                mPay_order.setChecked(true);
                break;
            case 1:
                mBind_order.setChecked(true);
                break;
            case 2:
                mXufei_order.setChecked(true);
                break;
        }
    }

    /**
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
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
                if (mPayOrderFragment == null) {
                    mPayOrderFragment = new MySubordinateOrderFragment();
                    transaction.add(R.id.Fm_container, mPayOrderFragment);
                } else {
                    transaction.show(mPayOrderFragment);
                }
                mPayOrderFragment.setState(state);
                mPayOrderFragment.setOrd_managerno(ord_managerno);
                break;
            case 1:
                if (mBindOrderFragment == null) {
                    mBindOrderFragment = new MySubordinateOrderFragment();
                    transaction.add(R.id.Fm_container, mBindOrderFragment);
                } else {
                    transaction.show(mBindOrderFragment);
                }
                mBindOrderFragment.setState(state);
                mBindOrderFragment.setOrd_managerno(ord_managerno);
                break;
            case 2:
                if (mXufeiOrderFragment == null) {
                    mXufeiOrderFragment = new MySubordinateOrderFragment();
                    transaction.add(R.id.Fm_container, mXufeiOrderFragment);
                } else {
                    transaction.show(mXufeiOrderFragment);
                }
                mXufeiOrderFragment.setState(state);
                mXufeiOrderFragment.setOrd_managerno(ord_managerno);
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
        MySubordinateOrderActivity.this.finish();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId) {
            //已支付订单
            case R.id.pay_Order:
                state="1";
                fragmentPosition = 0;
                setFragment();
                break;
            //已绑定订单
            case R.id.bind_Order:
                state="3";
                fragmentPosition = 1;
                setFragment();
                break;
            //续费订单
            case R.id.xufei_Order:
                state="4,5";
                fragmentPosition = 2;
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
                    if (mPayOrderFragment != null) {
                        mPayOrderFragment.freshView();
                    }
                    break;
                case 1:
                    if (mBindOrderFragment != null) {
                        mBindOrderFragment.freshView();
                    }
                    break;
                case 2:
                    if (mXufeiOrderFragment != null) {
                        mXufeiOrderFragment.freshView();
                    }
                    break;
            }
        }
    }
}
