package com.jx.intelligent.ui.activitise.personalcenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetMyWPInfoResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 滤芯状态
 * Created by Administrator on 2016/11/15 0015.
 */
public class FilterElementStatusActivity extends RHBaseActivity {

    private static final String TAG = "FilterElement";
    ImageView titlebar_left_vertical_iv;
    ProgressWheelDialog dialog;

    ProgressBar grxx_jsqzt_ppjdt, grxx_jsqzt_klhxtjdt,
            grxx_jsqzt_romjdt, grxx_jsqzt_hxbxjdt,
            grxx_jsqzt_rjlxjdt;

    TextView grxx_jsqzt_ppbfb, grxx_jsqzt_klhxtbfb,
            grxx_jsqzt_rombfb, grxx_jsqzt_hxbxbfb,
            grxx_jsqzt_rjlxbfb;

    UserCenter userCenter;
    GetMyWPInfoResult getMyWPInfoResult;
    String pro_id;
    private DBManager dbManager;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        userCenter = new UserCenter();
        if (getIntent() != null) {
            pro_id = getIntent().getStringExtra("pro_id");
            getFilterElementStat();
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_filter_element_status;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("滤芯状态")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {

        grxx_jsqzt_ppbfb = (TextView) contentView.findViewById(R.id.grxx_jsqzt_ppbfb);
        grxx_jsqzt_ppjdt = (ProgressBar) contentView.findViewById(R.id.grxx_jsqzt_ppjdt);
        grxx_jsqzt_klhxtbfb = (TextView) contentView.findViewById(R.id.grxx_jsqzt_klhxtbfb);
        grxx_jsqzt_klhxtjdt = (ProgressBar) contentView.findViewById(R.id.grxx_jsqzt_klhxtjdt);
        grxx_jsqzt_rombfb = (TextView) contentView.findViewById(R.id.grxx_jsqzt_rombfb);
        grxx_jsqzt_romjdt = (ProgressBar) contentView.findViewById(R.id.grxx_jsqzt_romjdt);
        grxx_jsqzt_hxbxbfb = (TextView) contentView.findViewById(R.id.grxx_jsqzt_hxbxbfb);
        grxx_jsqzt_hxbxjdt = (ProgressBar) contentView.findViewById(R.id.grxx_jsqzt_hxbxjdt);
        grxx_jsqzt_rjlxbfb = (TextView) contentView.findViewById(R.id.grxx_jsqzt_rjlxbfb);
        grxx_jsqzt_rjlxjdt = (ProgressBar) contentView.findViewById(R.id.grxx_jsqzt_rjlxjdt);

        dialog = new ProgressWheelDialog(FilterElementStatusActivity.this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.grxx_zfaq_sjh_jr:
                break;
            case R.id.grxx_zfaq_xgmm_jr:
                break;
        }
    }

    /**
     * 获取滤芯状态
     */
    void getFilterElementStat() {
        getPreData();
        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
            return;
        }

        dialog.show();
        userCenter.getFilterElementStatTask(SesSharedReferences.getPhoneNum(FilterElementStatusActivity.this), pro_id, new ResponseResult() {

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                getMyWPInfoResult = (GetMyWPInfoResult) object;
                showStatusData(getMyWPInfoResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的滤芯的数据
     */
    void getPreData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", SesSharedReferences.getPhoneNum(FilterElementStatusActivity.this));
        map.put("pro_id", pro_id);
        String js = dbManager.getUrlJsonData(Constant.USER_GET_FE_STAT_WP + StringUtil.obj2JsonStr(map));
        if (!StringUtil.isEmpty(js)) {
            GetMyWPInfoResult getMyWPInfoResult = new Gson().fromJson(js, GetMyWPInfoResult.class);
            showStatusData(getMyWPInfoResult);
        }
    }

    /**
     * 显示滤芯数据
     *
     * @param myWPInfoResult
     */
    void showStatusData(GetMyWPInfoResult myWPInfoResult) {
        if (myWPInfoResult != null && myWPInfoResult.getData().size() > 0) {
            getMyWPInfoResult = myWPInfoResult;
            setInfo();
        } else {
            ToastUtil.showToast("获取净水器状态失败");
        }
    }

    /**
     * 王云修改，
     * 如果服务器返回的滤芯值是null，那么就显示0%
     */
    public void setInfo() {
        GetMyWPInfoResult.Data data = getMyWPInfoResult.getData().get(0);
        String pp = data.getPp();
        String cto = data.getCto();
        String ro = data.getRo();
        String wfr = data.getWfr();
        //pp滤芯
        if (pp == null) {
            grxx_jsqzt_ppbfb.setText(0 + "%");
            grxx_jsqzt_ppjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getPp()));
        } else {
            grxx_jsqzt_ppbfb.setText(getMyWPInfoResult.getData().get(0).getPp() + "%");
            grxx_jsqzt_ppjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getPp()));
        }
        //CTO活性炭滤芯
        if (cto == null) {
            grxx_jsqzt_klhxtbfb.setText(0 + "%");
            grxx_jsqzt_klhxtjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getCto()));
        } else {
            grxx_jsqzt_klhxtbfb.setText(getMyWPInfoResult.getData().get(0).getCto() + "%");
            grxx_jsqzt_klhxtjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getCto()));
        }

        //RO膜滤芯
        if (ro == null) {
            grxx_jsqzt_rombfb.setText(0 + "%");
            grxx_jsqzt_romjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getRo()));
        } else {
            grxx_jsqzt_rombfb.setText(getMyWPInfoResult.getData().get(0).getRo() + "%");
            grxx_jsqzt_romjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getRo()));
        }

        //复合能量矿化滤芯
        if (wfr == null) {
            grxx_jsqzt_hxbxbfb.setText(0 + "%");
            grxx_jsqzt_hxbxjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getT33()));
        } else {
            grxx_jsqzt_hxbxbfb.setText(getMyWPInfoResult.getData().get(0).getT33() + "%");
            grxx_jsqzt_hxbxjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getT33()));
        }
//            grxx_jsqzt_ppbfb.setText(getMyWPInfoResult.getData().get(0).getPp()+"%");
//            grxx_jsqzt_ppjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getPp()));
//            grxx_jsqzt_klhxtbfb.setText(getMyWPInfoResult.getData().get(0).getCto()+"%");
//            grxx_jsqzt_klhxtjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getCto()));
//            grxx_jsqzt_rombfb.setText(getMyWPInfoResult.getData().get(0).getRo()+"%");
//            grxx_jsqzt_romjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getRo()));
//            grxx_jsqzt_rjlxbfb.setText(getMyWPInfoResult.getData().get(0).getWfr()+"%");
//            grxx_jsqzt_rjlxjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getWfr()));
//            grxx_jsqzt_hxbxbfb.setText(getMyWPInfoResult.getData().get(0).getT33()+"%");
//            grxx_jsqzt_hxbxjdt.setProgress(strToInt(getMyWPInfoResult.getData().get(0).getT33()));



    }

    public int strToInt(String str) {

        if (str != null) {
            String newStr = str.replace("%", "");
            int i = Integer.parseInt(newStr);
            return i;
        } else {
            return 0;
        }
    }
}
