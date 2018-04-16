package com.jx.intelligent.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.helper.ProgressHelper;
import com.jx.intelligent.view.ProgressWheel;


/**
 *加载进度条
 */

public class ProgressWheelDialog extends Dialog {

    private Context mContext;
    private TextView mDialogMsgTV;
    private ProgressWheel mProgressWheel;
    private ProgressHelper mProgressHelper;

    public ProgressWheelDialog(Context context) {
        this(context, R.style.DialogStyle);
    }

    public ProgressWheelDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mProgressHelper = new ProgressHelper(context);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
       /* ImageView dialogIV = (ImageView)findViewById(R.id.loading_dialog_iv);
        // 加载动画
        Animation loadingAnimation = AnimationUtils.loadAnimation(
                mContext, R.anim.load_animation);
        dialogIV.startAnimation(loadingAnimation);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        // 提示文字
        mDialogMsgTV = (TextView)findViewById(R.id.loading_dialog_tv);
        mProgressHelper.setProgressWheel((ProgressWheel)findViewById(R.id.progressWheel));
        mProgressWheel = mProgressHelper.getProgressWheel();
        setCancelable(true);// 可以用“返回键”取消
        setOnCancelListener(new OnCancelListener() {//取消监听
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mProgressWheel.stopSpinning();
            }
        });
    }

    public void setLoadMessage(String msg){
        mDialogMsgTV.setText(msg);// 设置加载信息
    }

    @Override
    public void show() {
        try {
            super.show();
            if(!mProgressWheel.isSpinning()){
                mProgressWheel.spin();
            }
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(mProgressWheel.isSpinning()){
            mProgressWheel.stopSpinning();
        }
    }
}
