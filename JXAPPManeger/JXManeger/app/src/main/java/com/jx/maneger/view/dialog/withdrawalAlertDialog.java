package com.jx.maneger.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.intf.WithdrawalDialogOnClickListener;
import com.jx.maneger.util.ScreenSizeUtil;


/**
 * Created by Weavey on 2016/9/3.
 */
public class withdrawalAlertDialog implements View.OnClickListener {

    private static Context mContext;
    private TextView mTitle;
    private TextView mContent;
    private EditText edit_reason;
    private Button btn_ok;
    private Button btn_no;
    private Button btn_cancel;
    private Dialog mDialog;
    private View mDialogView;
    private Builder mBuilder;

    public withdrawalAlertDialog(Builder builder) {

        this.mBuilder = builder;
        mDialog = new Dialog(mContext, R.style.NormalDialogStyle);
        mDialogView = View.inflate(mContext, R.layout.widget_dialog_withdrawl, null);
        mTitle = (TextView) mDialogView.findViewById(R.id.dialog_normal_title);
        mContent = (TextView) mDialogView.findViewById(R.id.dialog_normal_content);
        btn_ok = (Button) mDialogView.findViewById(R.id.btn_ok);
        btn_no = (Button) mDialogView.findViewById(R.id.btn_no);
        btn_cancel = (Button) mDialogView.findViewById(R.id.btn_cancel);
        edit_reason = (EditText) mDialogView.findViewById(R.id.edit_reason);
        mDialogView.setMinimumHeight((int) (ScreenSizeUtil.getInstance(mContext).getScreenHeight
                () * builder.getHeight()));
        mDialog.setContentView(mDialogView);

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if(lp != null)
        {
            lp.width = (int) (ScreenSizeUtil.getInstance(mContext).getScreenWidth() * builder.getWidth());
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialogWindow.setAttributes(lp);

            initDialog(builder);
        }
    }

    private void initDialog(Builder builder) {

        mDialog.setCanceledOnTouchOutside(builder.isTouchOutside());
        mDialog.setCancelable(builder.isCancelable);
        if (builder.getTitleVisible()) {
            mTitle.setVisibility(View.VISIBLE);
        } else {

            mTitle.setVisibility(View.GONE);
        }

        mTitle.setText(builder.getTitleText());
        mTitle.setTextColor(builder.getTitleTextColor());
        mTitle.setTextSize(builder.getTitleTextSize());
        mContent.setText(builder.getContentText());
        mContent.setTextColor(builder.getContentTextColor());
        mContent.setTextSize(builder.getContentTextSize());
        btn_ok.setText(builder.getOkButtonText());
        btn_ok.setTextColor(builder.getOkButtonTextColor());
        btn_ok.setTextSize(builder.getButtonTextSize());
        btn_no.setText(builder.getNoButtonText());
        btn_no.setTextColor(builder.getNoButtonTextColor());
        btn_no.setTextSize(builder.getButtonTextSize());
        btn_cancel.setText(builder.getCancelButtonText());
        btn_cancel.setTextColor(builder.getCancelButtonTextColor());
        btn_cancel.setTextSize(builder.getButtonTextSize());

        btn_ok.setOnClickListener(this);
        btn_no.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.btn_ok && mBuilder.getOnclickListener() != null) {

            mBuilder.getOnclickListener().clickOkButton(btn_ok, edit_reason.getText().toString());
            return;
        }

        if (i == R.id.btn_no && mBuilder.getOnclickListener() != null) {

            mBuilder.getOnclickListener().clickNoButton(btn_no, edit_reason.getText().toString());
            return;
        }

        if (i == R.id.btn_cancel && mBuilder.getOnclickListener() != null) {

            mBuilder.getOnclickListener().clickCancelButton(btn_cancel);
            return;
        }

    }

    public void show() {

        mDialog.show();
    }

    public void dismiss() {

        mDialog.dismiss();
    }

    public static class Builder {

        private String titleText;
        private int                   titleTextColor;
        private int                   titleTextSize;
        private String contentText;
        private int                   contentTextColor;
        private int                   contentTextSize;
        private String okButtonText;
        private int                   okButtonTextColor;
        private String noButtonText;
        private int                   noButtonTextColor;
        private String cancelButtonText;
        private int                   cancelButtonTextColor;
        private int                   buttonTextSize;
        private WithdrawalDialogOnClickListener onclickListener;
        private boolean               isTitleVisible;
        private boolean               isTouchOutside;
        private boolean               isCancelable;
        private float                 height;
        private float                 width;

        public Builder(Context context) {

            mContext = context;
            titleText = "温馨提示";
            titleTextColor = ContextCompat.getColor(mContext, R.color.color_363636);

            contentText = "";
            contentTextColor = ContextCompat.getColor(mContext, R.color.color_363636);
            okButtonText = "确定";
            okButtonTextColor = ContextCompat.getColor(mContext, R.color.color_363636);
            noButtonText = "取消";
            noButtonTextColor = ContextCompat.getColor(mContext, R.color.color_363636);
            cancelButtonText = "确定";
            cancelButtonTextColor = ContextCompat.getColor(mContext, R.color.color_363636);
            onclickListener = null;
            isTitleVisible = false;
            isTouchOutside = true;
            height = 0.23f;
            width = 0.65f;
            titleTextSize = 16;
            contentTextSize = 14;
            buttonTextSize = 14;

        }

        public String getTitleText() {
            return titleText;
        }

        public Builder setTitleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public int getTitleTextColor() {
            return titleTextColor;
        }

        public Builder setTitleTextColor(@ColorRes int titleTextColor) {
            this.titleTextColor = ContextCompat.getColor(mContext, titleTextColor);
            return this;
        }

        public String getContentText() {
            return contentText;
        }

        public Builder setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public int getContentTextColor() {
            return contentTextColor;
        }

        public Builder setContentTextColor(@ColorRes int contentTextColor) {
            this.contentTextColor = ContextCompat.getColor(mContext, contentTextColor);
            return this;
        }



        public String getOkButtonText() {
            return okButtonText;
        }

        public Builder setOkButtonText(String okButtonText) {
            this.okButtonText = okButtonText;
            return this;
        }

        public int getOkButtonTextColor() {
            return okButtonTextColor;
        }

        public Builder setOkButtonTextColor(@ColorRes int okButtonTextColor) {
            this.okButtonTextColor = ContextCompat.getColor(mContext, okButtonTextColor);
            return this;
        }

        public String getNoButtonText() {
            return noButtonText;
        }

        public Builder setNoButtonText(String noButtonText) {
            this.noButtonText = noButtonText;
            return this;
        }

        public int getNoButtonTextColor() {
            return noButtonTextColor;
        }

        public Builder setNoButtonTextColor(@ColorRes int noButtonTextColor) {
            this.noButtonTextColor = ContextCompat.getColor(mContext, noButtonTextColor);
            return this;
        }

        public String getCancelButtonText() {
            return cancelButtonText;
        }

        public Builder setCancelButtonText(String cancelButtonText) {
            this.cancelButtonText = cancelButtonText;
            return this;
        }

        public int getCancelButtonTextColor() {
            return cancelButtonTextColor;
        }

        public Builder setCancelButtonTextColor(@ColorRes int cancelButtonTextColor) {
            this.cancelButtonTextColor = ContextCompat.getColor(mContext, cancelButtonTextColor);
            return this;
        }

        public WithdrawalDialogOnClickListener getOnclickListener() {
            return onclickListener;
        }

        public Builder setOnclickListener(WithdrawalDialogOnClickListener onclickListener) {
            this.onclickListener = onclickListener;
            return this;
        }

        public boolean getTitleVisible() {
            return isTitleVisible;
        }

        public Builder setTitleVisible(boolean isVisible) {
            isTitleVisible = isVisible;
            return this;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setCanceledOnTouchOutside(boolean isTouchOutside) {

            this.isTouchOutside = isTouchOutside;
            return this;
        }

        public Builder setCancelable(boolean isCancelable){
            this.isCancelable = isCancelable;
            return this;
        }

        public int getContentTextSize() {
            return contentTextSize;
        }

        public Builder setContentTextSize(int contentTextSize) {
            this.contentTextSize = contentTextSize;
            return this;
        }

        public int getTitleTextSize() {
            return titleTextSize;
        }

        public Builder setTitleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public int getButtonTextSize() {
            return buttonTextSize;
        }

        public Builder setButtonTextSize(int buttonTextSize) {
            this.buttonTextSize = buttonTextSize;
            return this;
        }

        public float getHeight() {
            return height;
        }

        public Builder setHeight(float height) {
            this.height = height;
            return this;
        }

        public float getWidth() {
            return width;
        }

        public Builder setWidth(float width) {
            this.width = width;
            return this;
        }

        public withdrawalAlertDialog build() {

            return new withdrawalAlertDialog(this);
        }
    }


}
