package com.choucheng.dengdao2.common;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

public class InitAppliction extends Application {
    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }
	private static InitAppliction mInstance = null;
    public LocalBroadcastManager lbm;



	public static InitAppliction getInstance() {
		return mInstance;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		mInstance=this;

        init();
	}


    private void init() {
        initImageLoader(getApplicationContext());
        lbm = LocalBroadcastManager.getInstance(this);

        initStrictMode();
        initImageLoader(getApplicationContext());//Imageloader初始化

    }
   public void initStrictMode() {
        if (Config.DEVELOPER_MODE&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }

    }

    public static void initImageLoader(Context context) {
        String path= Environment.getExternalStorageDirectory()+FinalVarible.PROJECT_PATH;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) 
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                  .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                  .memoryCacheSize(2 * 1024 * 1024)
                  .memoryCacheSizePercentage(13) // default
                  .diskCacheSize(50 * 1024 * 1024)
//                  .memoryCacheExtraOptions(1024, 1024)
                .memoryCacheExtraOptions(480, 800) // default=device screen dimensions
                  .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                  .diskCache(new UnlimitedDiskCache(new File(path+"/cache")))  //最多缓存图片张数，如果超出则删掉最早的图片
                  .diskCacheFileCount(80)
                  .writeDebugLogs()
                  .build();
        ImageLoader.getInstance().init(config);
    }

}
