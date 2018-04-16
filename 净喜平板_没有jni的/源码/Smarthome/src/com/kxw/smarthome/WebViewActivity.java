package com.kxw.smarthome;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kxw.smarthome.utils.LoadingDialog;
import com.kxw.smarthome.utils.ToolsUtils;

public class WebViewActivity extends BaseActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.webview_activity);
		webView = (WebView) findViewById(R.id.my_webview);
		if (!ToolsUtils.isEmpty(getIntent().getStringExtra("url"))) {
			if (webView != null) {
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						LoadingDialog.dismiss();
					}
				});

				loadUrl(getIntent().getStringExtra("url"));
			}
		}
	}

	public void loadUrl(String url) {
		if (webView != null) {
			webView.loadUrl(url);
			new LoadingDialog(WebViewActivity.this,
					getString(R.string.str_loading), true);
			webView.reload();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back_ll:
			finish();
			break;
		default:
			break;
		}
	}
}
