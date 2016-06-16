package com.choucheng.dengdao2.tools.image;

import android.graphics.Bitmap;
import android.os.Environment;

import com.choucheng.dengdao2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

public class ImageLoaderUtils {

	public static final String PROJECT_NAME = "YiXing";
	public static final String ROOTPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ PROJECT_NAME;

	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)
				// default
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.imgloadingfail)
				.showImageOnFail(R.drawable.imgloadingfail)
				.showImageOnLoading(R.drawable.imgloading)
				.displayer(new FadeInBitmapDisplayer(50)).build();
		return options;
	}

	public static DisplayImageOptions getAvatarOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
//				.showImageForEmptyUri(R.drawable.test_bmw)
//				.showImageOnFail(R.drawable.test_bmw)
//				.showImageOnLoading(R.drawable.test_bmw)
				.displayer(new FadeInBitmapDisplayer(50)).build();
		return options;
	}

	public static DisplayImageOptions getNotMemoryOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false)
				// default
				// .delayBeforeLoading(1000)
				// .cacheInMemory(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .displayer(new SimpleBitmapDisplayer())
				.displayer(new FadeInBitmapDisplayer(50)).build();
		return options;
	}

	public static DisplayImageOptions getBannerOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// .resetViewBeforeLoading(false)
				// default
				// .delayBeforeLoading(1000)
				// .cacheInMemory(true)
				.cacheOnDisk(true)
				// .showImageForEmptyUri(R.drawable.banner_default_bg)
				// .showImageOnFail(R.drawable.banner_default_bg)
				// .showImageOnLoading(R.drawable.banner_default_bg)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(50)).build();
		return options;
	}

	public static DisplayImageOptions getSplashOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false)
				// default
				// .delayBeforeLoading(1000)
				// .cacheInMemory(true)
				.cacheOnDisk(true)
				// .showImageForEmptyUri(R.drawable.loading_bg)
				// .showImageOnFail(R.drawable.loading_bg)
				// .showImageOnLoading(R.drawable.loading_bg)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .displayer(new SimpleBitmapDisplayer())
				.displayer(new FadeInBitmapDisplayer(50)).build();
		return options;
	}

	public static DisplayImageOptions getCacnheMemory() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)
				// default
				// .delayBeforeLoading(1000)
				.cacheInMemory(true)
				// .cacheOnDisk(true)
				.showImageForEmptyUri(R.drawable.imgloadingfail)
				.showImageOnFail(R.drawable.imgloadingfail)
				.showImageOnLoading(R.drawable.imgloading)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(50)).build();
		return options;
	}
}
