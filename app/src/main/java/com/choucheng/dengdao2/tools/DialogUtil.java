package com.choucheng.dengdao2.tools;
/**
 * @record
 * */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.definewidget.DefineWheelMain;
import com.choucheng.dengdao2.definewidget.datapicker_other.ScreenInfo;
import com.choucheng.dengdao2.definewidget.datapicker_other.WheelMain;
import com.choucheng.dengdao2.pojo.AnyEventType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


/*
 * 
 * 对话框管理
 */
public class DialogUtil {
	private  static final String TAG="DialogConfig";
	private Context context;
    private long touchTime = 0;
    private Drawable icon;
    private DialogCallBack dialogCallBack;
    private int width;


    public DialogUtil(Context context) {
        this.context = context;
        
        /*DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;*/
        
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        width=width/5*4;

    }

    /**
     * 加载进度对话框 msg表示加载中显示的信息
     *  if msg==null  表示对话框只显示图标，无字体显示
     *    else if msg.equals（"") 表示除图标外，字体信息要显示，显示默认字体
     *    else  表示除图标外，需要显示传递过来的msg信息
     * @param msg 加载进度显示的内容
     * @param isshowbgk 是否显示背景
     */
    public static Dialog loadingDialog(Context mContext,String msg,boolean isshowbgk) {
    	Logger.i(TAG, "loadingDialog");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.dialog_loading, null);
        ImageView img = (ImageView) layout.findViewById(R.id.img_loading);
        Animation ani = AnimationUtils.loadAnimation(mContext,
                R.anim.ani_loading);
        img.setAnimation(ani);

        TextView text = (TextView) layout.findViewById(R.id.msg);
        if(msg==null){
            text.setVisibility(View.GONE);
        }else{
            if(!msg.equals("")){
                text.setText(msg);
            }

        }
        Dialog dialog;
        if(isshowbgk){
        	dialog=new Dialog(mContext,R.style.progressDialog);
        }else{
        	dialog=new Dialog(mContext,R.style.CustomDialog);
        }
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }




	public void showInfoExit() {
		long currentTime = System.currentTimeMillis();
        long waitTime = 2000;
        if ((currentTime - touchTime) > waitTime) {
            Toast.makeText(context,R.string.exitpressedagain,Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			/*Intent intent = new Intent();
			intent.setClass(context, NetService.class);
			context.stopService(intent);*/
			//关闭相关的服务
            EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_EXIT));
		}
	}


	/**
	 * 是否打开gps
	 * @return
	 */
	public Dialog getLocationDialog() {
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(R.string.prompt)
				.setMessage(R.string.whetertoopengps)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(
								"android.settings.LOCATION_SOURCE_SETTINGS");
						context.startActivity(intent);
					}

				})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				}).create();
		return dialog;
	}


    /**
     * 显示通用dialog
     * @param num  表示按钮的个数
     * @param titleinfo  标题
     * @param button1msg 按钮1
     * @param button2msg 按钮2
     * @param button3msg 按钮3
     * @param msg   提交的内容
     * @param dialogCallBack  按钮操作事件的回调
     * @return
     */
    public void commonDialog2(int num,String titleinfo,String button1msg,String button2msg,
    		         String button3msg, String msg,DialogCallBack dialogCallBack){
    	Logger.i(TAG, "commonDialog2");
    	 this.dialogCallBack=dialogCallBack;
    	 final Dialog dlg=new Dialog(context,R.style.CustomDialog);
    	// final AlertDialog dlg = new AlertDialog.Builder(context).create();
    	 Window layout = dlg.getWindow();
         // *** 主要就是在这里实现这种效果的.
         // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
    	 layout.setContentView(R.layout.dialog);
         WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
         lp.width = width;//设置宽度
         dlg.getWindow().setAttributes(lp);
         dlg.setCancelable(false);
         dlg.setCanceledOnTouchOutside(false);
         TextView title=(TextView)layout.findViewById(R.id.dialog_showmessage);
         if(titleinfo!=null&&!titleinfo.equals("")){
        	 title.setText(titleinfo);
         }else{
        	 title.setText(R.string.prompt);
         }
         TextView content=(TextView)layout.findViewById(R.id.dialog_showmessage);
         content.setText(msg);
         TextView button1=(TextView) layout.findViewById(R.id.button1);
         button1.setText(button1msg);
         TextView button2=(TextView) layout.findViewById(R.id.button2);
         button2.setText(button2msg);
         TextView button3=(TextView) layout.findViewById(R.id.button3);
         button3.setText(button3msg);
         switch (num) {
			case 1:
				button1.setVisibility(View.GONE);
				button3.setVisibility(View.GONE);
				break;
			case 2:
				button3.setVisibility(View.GONE);
				break;
		}
         button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				handresultInt(1);
			}
		});
         button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 dlg.dismiss();
				handresultInt(2);
			}
		});
        button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   dlg.dismiss();
					handresultInt(3);
			}
		});
        dlg.show();
         //return dialog;
    }


    /***
     * 显示选择对话框
     * @param mActivity
     * @param datas 选择的数据list
     * @param handler
     * @param what 选择后返回的操作码
     * @param pos  默认选择的位置
     */
    public static void showPickerDialog(final Activity mActivity,List<List<String>> datas,final Handler handler, final int what,int pos){
        Logger.i(TAG,"showPickerDialog");
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View contentView = inflater.inflate(R.layout.picker, null);
        final Dialog dialog=new Dialog(mActivity,R.style.PickerDialog);
        Window layout = dialog.getWindow();
        layout.setGravity(Gravity.BOTTOM);
        LinearLayout timepicker= (LinearLayout) contentView.findViewById(R.id.timepicker2);
        final DefineWheelMain wheelMain=new DefineWheelMain(mActivity,timepicker,datas,pos);
        OnClickListener dialogviewClickListener=new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_sure:
                        Message message=new Message();
                        message.obj=wheelMain.getTime();
                        message.what= what;
                        if(handler!=null)
                            handler.sendMessage(message);
                        dialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        dialog.cancel();
                        break;
                    default:
                        break;
                }

            }
        };
        contentView.findViewById(R.id.btn_sure).setOnClickListener(dialogviewClickListener);
        contentView.findViewById(R.id.btn_cancle).setOnClickListener(dialogviewClickListener);
        dialog.setContentView(contentView);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = SystemUtil.getPhoneWidth(mActivity);//设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }



    /**
     * 显示切换头像的选择对话框
     */
    public static void getpicDialog(final Activity mActivity, final String imagename){
		/* 请求码*/
        final int IMAGE_REQUEST_CODE = 0;
        final int CAMERA_REQUEST_CODE = 1;
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View layout = inflater.inflate(R.layout.dialog_picselete, null);
        final Dialog dialog=new Dialog(mActivity,R.style.Dialog_Fullscreen);
        OnClickListener dialogviewClickListener=new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.getpic_fromcamer:
                        dialog.dismiss();
                        //拍照
                        SystemUtil.takePic(mActivity, CAMERA_REQUEST_CODE,FinalVarible.TAKE_PIC_PATH ,imagename);
                        break;
                    case R.id.getpic_fromsdcard:
                        dialog.dismiss();
                        //本地图片
                        SystemUtil.scanPic(mActivity, IMAGE_REQUEST_CODE);
                        break;
                    case R.id.cancelhandl:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        };
        layout.findViewById(R.id.getpic_fromsdcard).setOnClickListener(dialogviewClickListener);
        layout.findViewById(R.id.getpic_fromcamer).setOnClickListener(dialogviewClickListener);
        layout.findViewById(R.id.cancelhandl).setOnClickListener(dialogviewClickListener);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }





    /**
     * 显示时间选择对话框
     */
    public static void showTimePickerDialog(Date date,Activity mActivity, final Handler handler){
        LayoutInflater inflater=LayoutInflater.from(mActivity);
        final View contentView=inflater.inflate(R.layout.picker, null);
        LinearLayout timepicker=(LinearLayout)contentView.findViewById(R.id.timepicker2);
        inflater.inflate(R.layout.timepicker,timepicker);
        ScreenInfo screenInfo = new ScreenInfo(mActivity);
        final WheelMain wheelMain = new WheelMain(timepicker,false);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        if(date!=null){
            calendar.setTime(date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year,month,day);
        TextView  textviewyes=(TextView)contentView.findViewById(R.id.btn_sure);
        TextView  textviewcancle=(TextView)contentView.findViewById(R.id.btn_cancle);
        final Dialog dialog=new Dialog(mActivity,R.style.PickerDialog);
        dialog.setContentView(contentView);
        Window layout = dialog.getWindow();
        layout.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = SystemUtil.getPhoneWidth(mActivity);//设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        textviewcancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        textviewyes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Message message=new Message();
                message.obj=wheelMain.getTime();
                message.what= FinalVarible.FRESH_TIME;
                handler.sendMessage(message);
            }
        });
        dialog.show();

    }



    public void handresultInt(int dialogid){
		  if(dialogCallBack!=null){
	            dialogCallBack.resulthandlerI(dialogid);
	        }
	}
	

	
	public interface DialogCallBack{
		public void resulthandlerI(int id);
	}
}
