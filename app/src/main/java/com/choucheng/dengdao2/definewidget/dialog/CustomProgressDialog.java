package com.choucheng.dengdao2.definewidget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.choucheng.dengdao2.R;


/**
 * *****************************************************************
 * 加载页面
 * *****************************************************************
 */

public class CustomProgressDialog extends Dialog {
    private Activity mParentActivity;
    public ProgressBar progressBar_loading;
    public TextView textView_loadingmsg;
    public int iLoadingTypeCount = 0;
    private Context context;
    private CustomProgressDialogHandler handler;
//    private MainApplication mainApplication;

    public CustomProgressDialog(Activity activity) {
        //super(context);
        this(activity, R.style.CustomProgressDialog);
        mParentActivity = activity;
//        mainApplication=MainApplication.getMainApplication();
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_custom_progress);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        this.context = context;
        init();
    }

    private void init() {
        progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
        textView_loadingmsg = (TextView) findViewById(R.id.textView_loadingmsg);
        handler = new CustomProgressDialogHandler();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (iLoadingTypeCount > 0) {
                    iLoadingTypeCount_subtract();
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void show() {
//        mainApplication.logUtil.d("show");
//        mainApplication.logUtil.d("mParentActivity:"+mParentActivity);
//        mainApplication.logUtil.d("(mParentActivity!=null):"+(mParentActivity!=null));
//        mainApplication.logUtil.d("mParentActivity.isFinishing():"+mParentActivity.isFinishing());
        if (mParentActivity != null && !mParentActivity.isFinishing()) {
            try {
                super.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dismiss() {
//        mainApplication.logUtil.d("dismiss");
//        mainApplication.logUtil.d("mParentActivity:"+mParentActivity);
//        mainApplication.logUtil.d("(mParentActivity!=null):"+(mParentActivity!=null));
//        mainApplication.logUtil.d("mParentActivity.isFinishing():"+mParentActivity.isFinishing());
//        if (mParentActivity != null && !mParentActivity.isFinishing()) {
//            try {
//                super.dismiss();    //调用超类对应方法
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        try {
            super.dismiss();    //调用超类对应方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void iLoadingTypeCount_Add(int type) {
//        mainApplication.logUtil.d("iLoadingTypeCount_Add");
        textView_loadingmsg.setText(context.getString(type));
        iLoadingTypeCount++;
//        handler.post(new update_Dialog_Runnable());
        Message msg=new Message();
        msg.what= CustomProgressDialogHandler.UPD_iLoadingTypeCount_Add;
        handler.sendMessage(msg);
    }

    public synchronized void iLoadingTypeCount_subtract() {
//        mainApplication.logUtil.d("iLoadingTypeCount_subtract");
        iLoadingTypeCount--;
//        handler.post(new update_Dialog_Runnable());
        Message msg=new Message();
        msg.what= CustomProgressDialogHandler.UPD_iLoadingTypeCount_subtract;
        handler.sendMessage(msg);
    }

    public void show_by_iLoadingTypeCount() {
//        mainApplication.logUtil.d( "iLoadingTypeCount:" + iLoadingTypeCount);
        if (iLoadingTypeCount == 0) {
            textView_loadingmsg.setText(context.getString(R.string.app_dialog_progress_default));
            if (isShowing()) {
                dismiss();
            }

        } else if (iLoadingTypeCount < 0) {
            iLoadingTypeCount = 0;
            textView_loadingmsg.setText(context.getString(R.string.app_dialog_progress_default));
            if (isShowing()) {
                dismiss();
            }
        } else if (iLoadingTypeCount > 0) {
            if (!isShowing()) {
                try {
                       show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }


    public class update_Dialog_Runnable implements Runnable {

        @Override
        public void run() {
            show_by_iLoadingTypeCount();

        }

    }

    private class CustomProgressDialogHandler extends Handler {
        public static final int UPD_iLoadingTypeCount_Add = 1;
        public static final int UPD_iLoadingTypeCount_subtract = 2;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPD_iLoadingTypeCount_Add:

//                    mainApplication.logUtil.d("mParentActivity:"+mParentActivity);
//                    mainApplication.logUtil.d("(mParentActivity!=null):"+(mParentActivity!=null));
//                    mainApplication.logUtil.d("mParentActivity.isFinishing():"+mParentActivity.isFinishing());
                    show_by_iLoadingTypeCount();
//                    this.removeMessages(UPD_iLoadingTypeCount_Add);
                    break;
                case UPD_iLoadingTypeCount_subtract:
//                    mainApplication.logUtil.d("mParentActivity:"+mParentActivity);
//                    mainApplication.logUtil.d("(mParentActivity!=null):"+(mParentActivity!=null));
//                    mainApplication.logUtil.d("mParentActivity.isFinishing():"+mParentActivity.isFinishing());
//                    this.removeMessages(UPD_iLoadingTypeCount_subtract);
                    show_by_iLoadingTypeCount();
                    break;
            }
        }
    }


}
