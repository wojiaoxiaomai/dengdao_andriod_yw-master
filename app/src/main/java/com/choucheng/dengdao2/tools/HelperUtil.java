package com.choucheng.dengdao2.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.view.LoginActivity;
import com.choucheng.dengdao2.view.MainActivity;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import de.greenrobot.event.EventBus;

public class HelperUtil {
   private Context context;
   private SharedPreferences menu_share;
//   private AppAndAd appAndAd;
   private Handler handler;
   private boolean isshowToast;//是否显示toast信息
   
   
  public HelperUtil(Context context,Handler handler,boolean isshowToast){
	  this.context=context;
	  this.handler=handler;
	  this.isshowToast=isshowToast;
  }	
   

	/*****************************MD5加密*****************************/

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };
    public static String toHexString(byte[] b) {
        //String to  byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }


    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }



    public static String md5_B(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String md5_S(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();

    }


    /*****************************MD5加密*****************************/


    /** textview 中文字响应方法s
	     */
	    public static void addMark(TextView textView, String s, OnFunctionListener l) {
	        Spannable sp1 = (Spannable) Html.fromHtml(s);
	        textView.setText(sp1);
	        textView.setMovementMethod(LinkMovementMethod.getInstance());
	        CharSequence text = textView.getText();
	        if (text instanceof Spannable) {
	            int end = text.length();
	            //其实就是得到sp1
	            Spannable sp = (Spannable) textView.getText();
	            //得到其中所有的关键字的数组
	            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
	            //其中关键字的样式
	            SpannableStringBuilder style = new SpannableStringBuilder(text);
	            //清楚掉所有的关键字标志
	            style.clearSpans();
	            for (URLSpan url : urls) {
	                //    将关键字数组中的文字添加到新生成的style中去
	                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), l);
	                style.setSpan(myURLSpan, sp.getSpanStart(url),
	                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	            //将新生成的放置到TextView上
	            textView.setText(style);
	        }
	    }

	   /**
	    * 将字符串格式的颜色值为textview设置颜色 
	    * @param textView
	    * @param colorString
	    */
	  public static void setTextColor(TextView textView,String colorString){
		  String StrColor = colorString;  
		  if(StrColor.contains("#")) {
			  StrColor.replace("#", "");
		  }
		  int length = StrColor.length();  
		  if (length == 6) {  
			  textView.setTextColor(Color.rgb(  
		              Integer.valueOf(StrColor.substring(0, 2), 16),  
		              Integer.valueOf(StrColor.substring(2, 4), 16),  
		              Integer.valueOf(StrColor.substring(4, 6), 16)));  
		  } else if (length == 8) {  
			  textView.setTextColor(Color.argb(  
		              Integer.valueOf(StrColor.substring(0, 2), 16),  
		              Integer.valueOf(StrColor.substring(2, 4), 16),  
		              Integer.valueOf(StrColor.substring(4, 6), 16),  
		              Integer.valueOf(StrColor.substring(6, 8), 16)));  
		  }  
	  }   
	  
	  /**
	   * 将服务返回的用户信息进行缓存处理
	   * @param dataString 得到的用户信息
	   */
	  public static void saveUserInfo(SharedPreferences user_share,String pwd,String dataString){
		 /* if(dataString!=null&&!dataString.equals("")){
          	 UserInfo userDetailInfo=new Gson().fromJson(dataString, UserInfo.class);
          	  CommParam.getInstance().setUid(userDetailInfo.getUid());
              String cachename=SharedUtil.getAESInfo(user_share, FinalVarible.CURR_NAME);
  			  if(!cachename.equals(userDetailInfo.getPhone())){
  				//以前缓存的用户和新登录的用户不同
  				SharedUtil.clearInfo(user_share);//清除所有的用户信息
  				SharedUtil.commitAESInfo(user_share, FinalVarible.CURR_NAME, userDetailInfo.getPhone());
  				SharedUtil.commitAESInfo(user_share, FinalVarible.CURR_PWD,pwd);
  			 }
  			  Intent serviceIntent=new Intent(mActivity,LocationService.class);
  			  mActivity.startService(serviceIntent);
  			  startMainActivity(mActivity);
          }*/


     /*     String cacheUserName= SharedUtil.getAESInfo(user_share, FinalVariables.CURR_NAME);
          if(!cacheUserName.equals(userName)){
              //以前缓存的用户和新登录的用户不同
              SharedUtil.clearInfo(user_share);//清除所有的用户信息
              SharedUtil.commitAESInfo(user_share, FinalVariables.CURR_NAME, userName);
              SharedUtil.commitAESInfo(user_share, FinalVariables.CURR_PWD,pwd);
              logUtil.d("已缓存用户信息");
          }else{
              String cacheUserPwd= SharedUtil.getAESInfo(user_share, FinalVariables.CURR_PWD);
              if(!cacheUserPwd.equals(pwd)){
                  //以前缓存的用户相同，但是密码改变
                  SharedUtil.clearInfo(user_share);//清除所有的用户信息
                  SharedUtil.commitAESInfo(user_share, FinalVariables.CURR_NAME, userName);
                  SharedUtil.commitAESInfo(user_share, FinalVariables.CURR_PWD,pwd);
                  logUtil.d("已缓存用户信息");
              }
          }*/
	  }


    /**
     * 退出登录
     * @param  isclear  是否清楚缓存数据
     */
    public static void logOut(Context mActivity,boolean isclear){
        //服务的暂停
       /* Intent serviceintent=new Intent(mActivity, MessageService.class);
        mActivity.stopService(serviceintent);
        serviceintent=new Intent(mActivity, LocationService.class);
        mActivity.stopService(serviceintent);*/
        //数据缓存的清除
        if(isclear){
            SharedPreferences user_share=mActivity.getSharedPreferences(FinalVarible.USER_SHARE,0);
            SharedUtil.clearInfo(user_share,FinalVarible.USERDETAIL);
        }
        //页面的跳转
        Intent intent=new Intent(mActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mActivity.startActivity(intent);
        if(mActivity instanceof Activity){
            ((Activity)mActivity).finish();
        }
    }

	  /**
	     * 统一spinner的风格
	     */
	    public static ArrayAdapter<String> initSpinner(Context context, int arrayId) {
	        String[] array_type = context.getResources().getStringArray(arrayId);
	        return new ArrayAdapter<String>(context, R.layout.spinner_item, R.id.item_tv, array_type);
	    }
	  
	  /**
	   * 启动到主页
	   */
	  public static void startMainActivity(Activity mActivity){
		  Intent intent=new Intent(mActivity,MainActivity.class);
		  AnimationUtil.startAnimation(mActivity, intent,
				  R.anim.alpha_enter, R.anim.alpha_exit);
		  mActivity.finish();
	  }
	  
	   /**
	     * 启动登陆页面
	     */
	    public static  void startLoginActivity(Activity mActivity){
	    	Intent intent=new Intent(mActivity,LoginActivity.class);
			AnimationUtil.startAnimation(mActivity, intent,
					R.anim.alpha_enter, R.anim.alpha_enter);
			mActivity.finish();
	    }
	    
	    /**
	     * 退出登录
	     */
	    public static void logOut(Activity mActivity,boolean isclear){
			//数据缓存的清除
			if(isclear){
				SharedPreferences user_share=mActivity.getSharedPreferences(FinalVarible.USER_SHARE,0);
				SharedUtil.clearInfo(user_share,FinalVarible.USERDETAIL);
				SharedUtil.clearInfo(user_share,FinalVarible.CURR_PWD);
				SharedUtil.clearInfo(user_share,FinalVarible.OPENID);
			}
			//页面的跳转
			Intent intent=new Intent(mActivity, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			mActivity.startActivity(intent);
			if(mActivity instanceof Activity){
				((Activity)mActivity).finish();
			}
			EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_EXIT));
	    }
	    


    /**
     * 修改部分字体颜色
     * @param textView 修改字体显示的对象
     * @param color 需要显示的颜色字
     * @param start 需要改变颜色值的字符串起始点
     * @param end 需要改变颜色值的字符结束点
     */
    public static  void changeTextpartColor(TextView textView,int color,int start,int end){
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }
	public static  void changeTextpartColor(TextView textView,int color,int start,int end,int start2,int end2){
		SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
		//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
		builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan, start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(builder);
	}

    /**
     * 创建imageview
     * @return
     */
    public static ImageView createImageview(Context context){
        ImageView imageView=new ImageView(context);
       // imageView.setBackgroundResource(R.drawable.button_image);
        int paddig=SystemUtil.dip2px(context,2);
        imageView.setPadding(paddig,paddig,paddig,paddig);
        int width=SystemUtil.dip2px(context,75);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(width,width);
        layoutParams.setMargins(0, SystemUtil.dip2px(context, 2), SystemUtil.dip2px(context, 10), SystemUtil.dip2px(context, 2));
        imageView.setLayoutParams(layoutParams);
        imageView.setClickable(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return  imageView;
    }


	/***
	 * 滤镜模式，只需要一张图，添加点击效果
	 */
	public static final View.OnTouchListener TouchDark = new View.OnTouchListener() {

		public final float[] BT_SELECTED = new float[]{1, 0, 0, 0, -50, 0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0};
		public final float[] BT_NOT_SELECTED = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}
			return false;
		}
	};



	/**
	 * 获取小数点后一位
	 * @param data 需要处理的数据
	 * @return
	 */
	public static String getOneDecimal(Double data){
		DecimalFormat format = new DecimalFormat("#0.0"); //java.text包里面的DecimalFormat 类
		String buf = format.format(data).toString();
		return buf;
	}

	/***
	 * java科学计数法转换成普通计数法
	 * @param datas "12345E-10";
	 */
	public static String ChangeBigDec(String datas){
		BigDecimal db = new BigDecimal(datas);
		String ii = db.toPlainString();
		return ii;
	}

	/**
	 * 判断是否是中文字符
	 * @param str
	 * @return
	 */
	public static boolean isCN(String str){
		try {
			byte [] bytes = str.getBytes("UTF-8");
			if(bytes.length == str.length()){
				return false;
			}else{
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
}
