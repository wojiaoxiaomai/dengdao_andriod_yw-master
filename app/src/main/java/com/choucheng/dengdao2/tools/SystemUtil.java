package com.choucheng.dengdao2.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;

import java.io.File;
import java.util.List;


/**
 *
 *@description  取得一些系统信息的工具类。注意需要配置一些权限
 *
 */
public class SystemUtil {


    /**
     * @描述  获取手机IMSI号码
     * */

    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSubscriberId();
    }

    /**
     * @描述  获取手机IMEI号码
     * */

    public static String getIMEI(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * @描述  获取手机号码
     * */

    public static String getPHONENUM(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }



    /**
     * @描述  获取当前网络环境
     * */

    public static String getNet(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        String typeName = networkInfo.getTypeName();
        if (typeName.equals("WIFI")) {
            return "W";
        } else if (typeName.equals("mobile")) {
            return "G";
        }
        return "";
    }

    /**
     * @描述    获得软件当前的版本号，对应manifest     android:versionCode="1"
     * */
    public static int getVersionCode(Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo versionName = null;
        int version = 1;
        try {
            versionName = pm.getPackageInfo(context.getPackageName(), 0);
            version = versionName.versionCode;
        } catch (NameNotFoundException e) {
            return 1;
        }
        return version;

    }


    /**
     * @描述    获得软件当前的版本名称，对应manifest      android:versionName="1.0"
     * */

    public static String getVersionName(Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo versionName = null;
        String version = "";
        try {
            versionName = pm.getPackageInfo(context.getPackageName(), 0);
            version = versionName.versionName;
        } catch (NameNotFoundException e) {
            return version;
        }
        return version;

    }
    
    public static boolean hasSdcard(Context context){
      boolean is= Environment.getExternalStorageState().equals(
              Environment.MEDIA_MOUNTED);
      if(!is){
          Toast.makeText(context, R.string.no_found_sdcard,Toast.LENGTH_SHORT).show();
      }
      return is;
     }
    
	/**
     * 拍照
     * @param filename 拍照后保存图片的名称
     * @return
     */
    public static void takePic(Activity maActivity,int requestcode,String path,String filename){
        //        Intent intentFromCapture = new Intent(); //调用照相机
//        intentFromCapture.setAction("android.media.action.STILL_IMAGE_CAMERA");
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard(maActivity)) {
            FileUtil fileUtil=new FileUtil();
            File file= null;
            fileUtil.createSDDir(path);
            file = fileUtil.createSDFile(path + "/" + filename);
            if(file!=null){
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                maActivity.startActivityForResult(intentFromCapture,requestcode);
            }

        }
    }


    /**
     * 查看本地图片
     * @return Activity接收到的data中，data.getData()得到的是图片的uri
     */
    public static void scanPic(Activity maActivity,int requestcode){
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        maActivity.startActivityForResult(intentFromGallery, requestcode);
    }

    
    /**
     * 拨打电话
     * @param activity
     * @param tel
     */
    public static void callTel(Activity activity ,String tel){
    	  Uri telUri = Uri.parse("tel:"
                  + tel.split("，")[0]);
          Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
          activity.startActivity(intent);
    }
    
    
    /**
     * 获得手机的宽度
     * @param mContext
     * @return
     */
    public static int getPhoneWidth(Context mContext){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     *获得手机的高度
     * @param mContext
     * @return
     */
    public static int getPhoneHeight(Context mContext){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    
    
    /***
     * 将dp值转换成px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue){
	      final float scale = context.getResources().getDisplayMetrics().density;   
	      return (int)(dipValue * scale + 0.5f);   
	}   
 
    /**
     * 将px值转换成dp
     * @param context
     * @param pxValue
     * @return
     */
	public static int px2dip(Context context, float pxValue){
	        final float scale = context.getResources().getDisplayMetrics().density;   
	        return (int)(pxValue / scale + 0.5f);   
	}
    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context,float pxValue) {

        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue) {

        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
 	 * 键盘管理--隐藏
 	 */
     public static void hiddenKeyBord(Context context,View v) {
         InputMethodManager imm = (InputMethodManager) context
                 .getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(v.getWindowToken(),
                 InputMethodManager.HIDE_NOT_ALWAYS);
     }


    /**
     * 键盘管理--开启
     */
    public static void openKeyBord(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    public static void hideSoftInput(IBinder token,Context context) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

     /**
      * 启动系统默认浏览器
      * @param context
      * @param url
      */
    public static void loadbrowser(Context context,String url){
    	Intent intent=new Intent();
    	intent.setAction("android.intent.action.VIEW");
    	Uri contentUri= Uri.parse(url);
    	intent.setData(contentUri);
    	context.startActivity(intent);
    	
    }
    
    /**
	 * @描述  检查指定服务是否在运行中
	 * 
	 * */

	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager mActivityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
				.getRunningServices(200);
		for (int i = 0; i < mServiceList.size(); i++) {
			if (className.equals(mServiceList.get(i).service.getClassName())) {
				System.out
						.println("  service is still running, no need to  start...");
				isRunning = true;
			}
		}
		return isRunning;
	}
	
	/**
	 * 判断activity是否在运行在前台
	 * @param mContext
	 * @return
	 */
	public static boolean isActivityRunningForward(Context mContext){
		String cmdName=mContext.getClass().getName();
		ActivityManager activityManager = (ActivityManager) mContext
                                  .getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfos=activityManager.getRunningTasks(30);
		String cmpNameTemp = null;
		if (null != taskInfos) {
            cmpNameTemp = (taskInfos.get(0).topActivity).getClassName();
        }
		if (null == cmpNameTemp){
            return false;
        }
        
       return cmpNameTemp.equals(cmdName);
	}
	 /***
     * 获得当前网络连接的类型，判断当前连接是否是WIFI
     */
    public static boolean isWIFIConnecting(Context context){
        ConnectivityManager mConnectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if(info == null || !mConnectivity.getBackgroundDataSetting())   {
            return false;
        }
        int netType = info.getType();
        return netType == ConnectivityManager.TYPE_WIFI && info.isConnected();
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGPS(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

   /**
    * * 强制帮用户打开GPS
    * @param context
    */
    public static final void toggleGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static Boolean isNetworkConnected(Context context){
    	if(context!=null){
    		ConnectivityManager connectivityManager=(ConnectivityManager) context.
    				 getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo mNetworkInfo=connectivityManager.getActiveNetworkInfo();
    		if(mNetworkInfo!=null){
    			return mNetworkInfo.isAvailable();
    		}
    	}
    	return false;
    }

    /**
     * 调用打开设置网络界面
     * @param context
     */
    public static void open_wifi(Context context){
        Intent intent;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if(android.os.Build.VERSION.SDK_INT>10){
            intent = new Intent(Settings.ACTION_SETTINGS);
        }else{
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }
    
	/**
	*调用系统电话薄
	*/
	public static void getTelInfo(Context context){
		  ((Activity) context).startActivityForResult(
                  new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
	}
	
	/**
	 * 调用系统视频播放
	 * @param videoPath
	 */
	public static void playVideo(Context context,String videoPath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String strend="";
        if(videoPath.toLowerCase().endsWith(".mp4")){  
            strend="mp4";  
        }  
        else if(videoPath.toLowerCase().endsWith(".3gp")){  
            strend="3gp";  
        }  
        else if(videoPath.toLowerCase().endsWith(".mov")){  
            strend="mov";  
        }  
        else if(videoPath.toLowerCase().endsWith(".wmv")){  
            strend="wmv";  
        }  
          
        intent.setDataAndType(Uri.parse(videoPath), "video/" + strend);
        context.startActivity(intent);  
        
    }  

	
	 /**
	  * 短信发送 
	  * @param smsBody
	  */
	public static void sendSMS(Context context,String telnum,String smsBody){
		Uri smsToUri = Uri.parse("smsto:" + telnum);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);  
		context.startActivity(intent);  
	  
	}


    /**
     * 群发短信
     * @param context
     * @param mobiles
     * @param smsBody
     */
    public static void sendSMSTOMorePerson(Context context,List<String> mobiles,String smsBody){
        String mobile="";
        for(String s:mobiles){
            if(mobile.equals("")){
                mobile=s;
            }else{
                mobile=mobile+";"+s;
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", mobile);
        intent.putExtra("sms_body", smsBody);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }
    /**
     * 调用系统分享文本文档
     */
    public static void System_ShareText(Context context,String title,String sharetext){
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.setType("text/plain");
        intent1.putExtra(Intent.EXTRA_SUBJECT, title);          // 分享的主题
        intent1.putExtra(Intent.EXTRA_TEXT,sharetext);  // 分享的内容
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent1, context.getString(R.string.app_name)));
    }

    /**
     * 分享本地图片
     */
    public static void System_ShareImage(Context context,String imageurl){
         Intent shareIntent = new Intent(Intent.ACTION_SEND);
         File file = new File(imageurl);
         shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
         shareIntent.setType("image/jpeg");
         context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.app_name)));
    }

    /**
     * @return 系统版本号
     */
    public int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }

        return version;
    }

    /**
     * 获取程序外部的缓存目录
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }


    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
       // cmb.setText(content.trim()); 11版本之后被舍弃
        cmb.setPrimaryClip(ClipData.newPlainText(null, content));
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
       // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        String content="";
        if (cmb.hasPrimaryClip()){
            content=cmb.getPrimaryClip().getItemAt(0).getText().toString();
        }
        return content;
    }

    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache(Context context){
        //WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath()+ FinalVarible.WEBCACHE);
        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()+"/webviewCache");
        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            FileUtil.deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            FileUtil.deleteFile(appCacheDir);
        }
    }



}
