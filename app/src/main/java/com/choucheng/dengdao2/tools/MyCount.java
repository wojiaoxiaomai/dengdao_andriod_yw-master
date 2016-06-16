package com.choucheng.dengdao2.tools;

/**
 *  定义一个倒计时的内部类
 * Created by Administrator on 2015/3/31.
 */

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.choucheng.dengdao2.R;


/**
 * 定义一个倒计时的内部类*/
public class MyCount extends CountDownTimer {
    TextView text_timer;
    Context context;
    public MyCount(Context context, long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.text_timer=textView;
        this.context=context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(text_timer!=null)
        text_timer.setText(String.format(context.getString(R.string.repeat_getverifycode2), millisUntilFinished / 1000L));
    }


    @Override

    public void onFinish() {
        if(text_timer!=null){
            text_timer.setText(R.string.repeat_getverifycode);
            text_timer.setClickable(true);
        }
    }
}
