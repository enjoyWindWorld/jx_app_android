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
import com.jx.maneger.fragments.SubordinateFragment;

/**
 * 下级界面
 * Created by Administrator on 2017/8/11.
 */

public class SubordinateManegeActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "SubordinateManegeActivity";
    private RadioButton direct_subordinate;
    private RadioButton indirect_subordinate;
    private RadioGroup subordinate_radiogroup;
    private int fragmentPosition;
    private ImageView titlebar_left_iv;
    private RelativeLayout titlebar_left_rl;

    private SubordinateFragment directSubordinateFragment;
    private SubordinateFragment indirectSubordinateFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subordinate);
        ((TextView) findViewById(R.id.titlebar_center_tv)).setText("我的e家");
        titlebar_left_iv = (ImageView)findViewById(R.id.titlebar_left_iv) ;
        titlebar_left_iv.setVisibility(View.VISIBLE);
        titlebar_left_rl = (RelativeLayout)findViewById(R.id.titlebar_left_rl);
        titlebar_left_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubordinateManegeActivity.this.finish();
            }
        });
        subordinate_radiogroup = (RadioGroup) findViewById(R.id.subordinate_radiogroup);
        direct_subordinate = (RadioButton) findViewById(R.id.direct_subordinate);
        indirect_subordinate = (RadioButton) findViewById(R.id.indirect_subordinate);
        subordinate_radiogroup.setOnCheckedChangeListener(this);
        switch (fragmentPosition)
        {
            case 0:
                direct_subordinate.setChecked(true);
                break;
            case 1:
                indirect_subordinate.setChecked(true);
                break;
        }
        setFragment();
    }

    /**
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
        if (directSubordinateFragment != null) {
            trans.hide(directSubordinateFragment);
        }
        if (indirectSubordinateFragment != null) {
            trans.hide(indirectSubordinateFragment);
        }
    }

    private void setFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        setAnimation(fragmentPosition, transaction);
        hideFragments(transaction);
        switch (fragmentPosition) {
            case 0:
                if (directSubordinateFragment == null) {
                    directSubordinateFragment = new SubordinateFragment();
                    directSubordinateFragment.setTag("0");
                    transaction.add(R.id.Fm_container, directSubordinateFragment);
                } else {
                    transaction.show(directSubordinateFragment);
                }
                break;
            case 1:
                if (indirectSubordinateFragment == null) {
                    indirectSubordinateFragment = new SubordinateFragment();
                    indirectSubordinateFragment.setTag("1");
                    transaction.add(R.id.Fm_container, indirectSubordinateFragment);
                } else {
                    transaction.show(indirectSubordinateFragment);
                }
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
        SubordinateManegeActivity.this.finish();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId) {
            //已支付订单
            case R.id.direct_subordinate:
                fragmentPosition = 0;
                setFragment();
                break;
            //已绑定订单
            case R.id.indirect_subordinate:
                fragmentPosition = 1;
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
                    if (directSubordinateFragment != null) {
                        directSubordinateFragment.freshView();
                    }
                    break;
                case 1:
                    if (indirectSubordinateFragment != null) {
                        indirectSubordinateFragment.freshView();
                    }
                    break;
            }
        }
    }
}
