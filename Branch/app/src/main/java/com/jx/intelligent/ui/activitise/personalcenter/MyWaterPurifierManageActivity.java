package com.jx.intelligent.ui.activitise.personalcenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.ui.fragments.MyWaterPurifierFragment;

/**
 * 我的净水器
 * Created by Administrator on 2016/11/15 0015.
 */
public class MyWaterPurifierManageActivity extends RHBaseActivity{

    private MyWaterPurifierFragment myWaterPurifierFragment;
    private MyWaterPurifierFragment otherWaterPurifierFragment;
    private int fragmentPosition;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    private RelativeLayout layout_my, layout_other;
    private TextView txt_my, txt_other;
    private View view_my, view_other;

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_water_purifier;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("我的净水机")
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        layout_my = (RelativeLayout) contentView.findViewById(R.id.layout_my);
        layout_other = (RelativeLayout) contentView.findViewById(R.id.layout_other);
        txt_my = (TextView) contentView.findViewById(R.id.txt_my);
        txt_other = (TextView) contentView.findViewById(R.id.txt_other);
        view_my = contentView.findViewById(R.id.view_my);
        view_other = contentView.findViewById(R.id.view_other);
        setFragment(0);

        layout_my.setOnClickListener(this);
        layout_other.setOnClickListener(this);
    }

    /**
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
        if (myWaterPurifierFragment != null) {
            trans.hide(myWaterPurifierFragment);
        }
        if (otherWaterPurifierFragment != null) {
            trans.hide(otherWaterPurifierFragment);
        }
    }

    private void setFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        setAnimation(position, transaction);
        hideFragments(transaction);
        switch (position) {
            case 0:
                if (myWaterPurifierFragment == null) {
                    myWaterPurifierFragment = new MyWaterPurifierFragment();
                    myWaterPurifierFragment.setType("0");
                    transaction.add(R.id.main_container_fl, myWaterPurifierFragment);
                } else {
                    transaction.show(myWaterPurifierFragment);
                }
                break;
            case 1:
                if (otherWaterPurifierFragment == null) {
                    otherWaterPurifierFragment = new MyWaterPurifierFragment();
                    otherWaterPurifierFragment.setType("1");
                    transaction.add(R.id.main_container_fl, otherWaterPurifierFragment);
                } else {
                    transaction.show(otherWaterPurifierFragment);
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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_my:
                setFragment(0);
                txt_my.setTextColor(getResources().getColor(R.color.color_1bb6ef));
                view_my.setBackgroundColor(getResources().getColor(R.color.color_1bb6ef));

                txt_other.setTextColor(getResources().getColor(R.color.color_000000));
                view_other.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.layout_other:
                setFragment(1);
                txt_my.setTextColor(getResources().getColor(R.color.color_000000));
                view_my.setBackgroundColor(getResources().getColor(R.color.white));

                txt_other.setTextColor(getResources().getColor(R.color.color_1bb6ef));
                view_other.setBackgroundColor(getResources().getColor(R.color.color_1bb6ef));
                break;
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }
}