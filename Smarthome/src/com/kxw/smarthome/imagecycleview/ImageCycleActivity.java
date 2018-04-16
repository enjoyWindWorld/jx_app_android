/*package com.kxw.smarthome.imagecycleview;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kxw.smarthome.R;
import com.kxw.smarthome.imagecycleview.ImageCycleView.ImageCycleViewListener;
import com.kxw.smarthome.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageCycleActivity extends Activity {

	private ImageCycleView mAdView;


	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();


	private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
			"http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
			"http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
			"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
	"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_cycle);
		mImageUrl = new ArrayList<String>();
		mImageUrl2 = new ArrayList<String>();
		for(int i=0;i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("top-->" + i);
			infos.add(info);
		}


		mAdView = (ImageCycleView) findViewById(R.id.ad_view);
		mAdView.setImageResources(infos, mAdCycleViewListener);

	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			ToastUtil.showShortToast("Toast from Activity");

			//ToastUtil.showShortToast("11");
			//Toast.makeText(ImageCycleActivity.this, "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.startImageCycle();
	};

	@Override
	protected void onPause() {
		super.onPause();
		mAdView.pushImageCycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdView.pushImageCycle();
	}

}
*/