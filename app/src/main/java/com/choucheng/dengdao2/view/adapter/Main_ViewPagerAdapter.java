package com.choucheng.dengdao2.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.pojo.AD;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class Main_ViewPagerAdapter extends PagerAdapter {
	//private List<ImageView> images;
	private List<AD> images;
	private Context context;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	
	
	public Main_ViewPagerAdapter(Context context,List<AD> views){
		 this.context=context;
		 this.images=views;
		 imageLoader = ImageLoader.getInstance();
		 options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defaulet_dish)
				.showImageForEmptyUri(R.drawable.defaulet_dish)
		        .resetViewBeforeLoading(true) 
		        .displayer(new FadeInBitmapDisplayer(300))
				.cacheInMemory(true)
				.cacheOnDisk(true).build();
	}
	
	
	    @Override
	public int getCount() {
	    return images.size();
	}
	
	//是否是同一张图片
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
	    return arg0 == arg1;
	}
	
	    @Override
	public void destroyItem(ViewGroup view, int position, Object object) {
	   // view.removeView(images.get(position));
	    ((ViewPager) view).removeView((View) object);
	
	}
	
	   @Override
	public Object instantiateItem(ViewGroup view, int position) {
		   ImageView imageLayout = initImageview(); 
		   AD ad=images.get(position);
    	   String imgurl=ad.getPicture();
    	   imageLoader.displayImage(imgurl, imageLayout, options);
           ((ViewPager) view).addView(imageLayout, 0);     // 将图片增加到ViewPager
           return imageLayout;  
	
	}
	   
	private ImageView initImageview() {
		ImageView imageView=new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		imageView.setScaleType(ScaleType.FIT_XY);
		return imageView;
	}  
}
