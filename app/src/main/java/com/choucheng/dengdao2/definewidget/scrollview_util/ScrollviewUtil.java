package com.choucheng.dengdao2.definewidget.scrollview_util;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2014/11/28 0028.
 */
public class ScrollviewUtil extends ScrollView {
    public ScrollviewUtil(Context context) {
        super(context);
    }

    public ScrollviewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollviewUtil(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return false;
    }
}
