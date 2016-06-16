package com.choucheng.dengdao2.tools;

/**
 * Created by zuoyan on 14-3-13.
 */

import android.os.Message;
import android.text.style.ClickableSpan;
import android.view.View;


/**
 * 继承于可点击的标签
 * @time  2014.3.13
 */
public class MyURLSpan extends ClickableSpan {
    private String mUrl;
    private OnFunctionListener l;
    /**
     * 构造器
     * @param url 可以点击的关键字，构造时传入的
     */
    MyURLSpan(String url,OnFunctionListener l) {
        mUrl = url;
        this.l=l;
    }

    @Override
    public void onClick(View widget) {
        Message message=new Message();
        message.obj=mUrl;
        l.processMessage(message);
    }

}