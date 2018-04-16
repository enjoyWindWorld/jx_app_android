/*package com.kxw.smarthome.imagecycleview; 

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kxw.smarthome.R;
import com.kxw.smarthome.imagecycleview.ImageCycleView.ImageCycleViewListener;
import com.kxw.smarthome.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


@SuppressLint("NewApi")
public class ImageCycleFragment extends Fragment{
	
	private ImageCycleView mAdView;
	
	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();

	private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
			"http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
			"http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
			"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
			"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

	protected Context context;
		
    @Override  
    public void onAttach(Activity activity) {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onAttach");  
        super.onAttach(activity);  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onCreate");  
        super.onCreate(savedInstanceState);  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onCreateView");  
      View view = inflater.inflate(R.layout.activity_ad_cycle, container,false); 
        for(int i=0;i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("top-->" + i);
			infos.add(info);
		}
					
		mAdView = (ImageCycleView)view.findViewById(R.id.ad_view);
		mAdView.setImageResources(infos, mAdCycleViewListener);
		return view;
    }  

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {	
			ToastUtil.showShortToast("Toast from frament");
			Intent intent=new Intent(getActivity(),tiaozhuan.class);
			startActivity(intent);
			Log.i("log", "position="+position +"content="+info.getContent()); 
		}


		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
		}
	};

    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onActivityCreated");  
        super.onActivityCreated(savedInstanceState);  
    }  
  
    @Override  
    public void onStart() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onStart");  
        super.onStart();  
    }  
  
    @Override  
    public void onResume() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onResume");  
        super.onResume();  
    }  
  
    @Override  
    public void onPause() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onPause");  
        super.onPause();  
    }  
  
    @Override  
    public void onStop() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onStop");  
        super.onStop();  
    }  
  
    @Override  
    public void onDestroyView() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onDestroyView");  
        super.onDestroyView();  
    }  
  
    @Override  
    public void onDestroy() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onDestroy");  
        super.onDestroy();  
    }  
  
    @Override  
    public void onDetach() {  
        // TODO Auto-generated method stub  
        Log.d(this.getTag(), "onDetach");  
        super.onDetach();  
    }  
      
} */