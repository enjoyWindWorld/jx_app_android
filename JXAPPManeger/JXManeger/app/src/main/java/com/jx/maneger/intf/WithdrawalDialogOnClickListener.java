package com.jx.maneger.intf;

import android.view.View;

public interface WithdrawalDialogOnClickListener {

    void clickOkButton(View view, String reason);

    void clickNoButton(View view, String reason);

    void clickCancelButton(View view);
}
