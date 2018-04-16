package com.jx.maneger.activities.CustomerService;

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
import com.jx.maneger.fragments.RepairTaskListFragment;

public class TaskListActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RepairTaskListFragment repairingFragment;
    private RepairTaskListFragment finishFragment;
    private int fragmentPosition;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    private RadioButton btn_repairing;
    private RadioButton btn_finish;
    private RadioGroup my_radioGroup;
    private RelativeLayout titlebar_left_rl;
    private ImageView titlebar_left_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ((TextView) findViewById(R.id.titlebar_center_tv)).setText("当前任务");
        my_radioGroup = (RadioGroup) findViewById(R.id.my_radioGroup);
        btn_repairing = (RadioButton) findViewById(R.id.btn_repairing);
        btn_finish = (RadioButton) findViewById(R.id.btn_finish);
        my_radioGroup.setOnCheckedChangeListener(this);
        titlebar_left_iv = (ImageView)findViewById(R.id.titlebar_left_iv) ;
        titlebar_left_iv.setVisibility(View.VISIBLE);
        titlebar_left_rl = (RelativeLayout)findViewById(R.id.titlebar_left_rl);
        titlebar_left_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_repairing.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            //已支付订单
            case R.id.btn_repairing:
                fragmentPosition = 0;
                setFragment(0);
                break;
            //已绑定订单
            case R.id.btn_finish:
                fragmentPosition = 1;
                setFragment(1);
                break;
        }
    }

    /**
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
        if (repairingFragment != null) {
            trans.hide(repairingFragment);
        }
        if (finishFragment != null) {
            trans.hide(finishFragment);
        }
    }

    private void setFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        setAnimation(position, transaction);
        hideFragments(transaction);
        switch (position) {
            case 0:
                if (repairingFragment == null) {
                    repairingFragment = new RepairTaskListFragment();
                    repairingFragment.setType("1");
                    transaction.add(R.id.main_container_fl, repairingFragment);
                } else {
                    transaction.show(repairingFragment);
                }
                break;
            case 1:
                if (finishFragment == null) {
                    finishFragment = new RepairTaskListFragment();
                    finishFragment.setType("200");
                    transaction.add(R.id.main_container_fl, finishFragment);
                } else {
                    transaction.show(finishFragment);
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

}
