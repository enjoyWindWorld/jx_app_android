package com.jx.intelligent.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.ui.activitise.payment.SuccessPaymentActivity;
import com.jx.intelligent.ui.activitise.personalcenter.MyOrderActivity;
import com.jx.intelligent.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
    	api = WXAPIFactory.createWXAPI(this, "wx2e4c1225544f04b1");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.errCode == 0)//成功
		{
			successPay();
		}
		else if(resp.errCode == -1)//错误
		{
			failPay();
		}
		else if(resp.errCode == -2)//用户取消
		{
			failPay();
		}
	}

	void successPay()
	{
		if("3".equals(RHBaseApplication.getInstance().getTag()))
		{
			RHBaseApplication.getInstance().setwXPay(2);
			ToastUtil.showToast("支付成功！");
			finish();
		}
		else
		{
			Intent intent = new Intent(WXPayEntryActivity.this, SuccessPaymentActivity.class);
			intent.putExtra("orderId", RHBaseApplication.getInstance().getOrd_no());
			startActivity(intent);
		}

	}


	void failPay()
	{
		if("3".equals(RHBaseApplication.getInstance().getTag()))
		{
			RHBaseApplication.getInstance().setwXPay(1);
			ToastUtil.showToast("支付失败！");
			finish();
		}
		else
		{
			Intent intent = new Intent(WXPayEntryActivity.this, MyOrderActivity.class);
			intent.putExtra("state","");
			intent.putExtra("postion", 1);
			startActivity(intent);
		}
	}
}