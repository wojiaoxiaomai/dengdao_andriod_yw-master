package com.choucheng.dengdao2.definewidget.dialog.wheeldialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.definewidget.datapicker_other.WheelView;
import com.choucheng.dengdao2.tools.SystemUtil;
import com.choucheng.dengdao2.tools.viewtools.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/10/27 0027.
 */
public class Dialog_Wheel_one extends Dialog {
    public int show_lstData1_index = 0;
    private String tag = "Dialog_Wheel_one";
    private OnClick_Callback onClick_Callback;
    public WheelView wheelView1;
    public ArrayList<HashMap<String, String>> lstData1;
    private Context context;
    private TextView textView;
    public TextView textView_sure;
    public String str_id;
    public String str_title;
    private Boolean isInitValue;

    public Dialog_Wheel_one(Context context, int theme, TextView textView,  ArrayList<HashMap<String, String>> lstData1,Boolean isInitValue,int show_lstData1_index) {
        super(context, theme);
        this.context = context;
        this.textView = textView;
        this.lstData1 = lstData1;
        this.show_lstData1_index=show_lstData1_index;
        this.isInitValue=isInitValue;
        init();
    }


    public Dialog_Wheel_one(Context context, TextView textView,  ArrayList<HashMap<String, String>> lstData1,int show_lstData1_index) {
        this(context, R.style.NoTitleDialog, textView, lstData1,true,show_lstData1_index);
    }
    public Dialog_Wheel_one(Context context, TextView textView,  ArrayList<HashMap<String, String>> lstData1,Boolean isInitValue) {
        this(context, R.style.NoTitleDialog, textView, lstData1,isInitValue,0);
    }
    public Dialog_Wheel_one(Context context, TextView textView,  ArrayList<HashMap<String, String>> lstData1) {
        this(context, textView, lstData1,false);
    }
    private void init() {
        LayoutInflater inflater = getWindow().getLayoutInflater();
        View view = inflater.inflate(R.layout.wheel_view_layout_one, null);

        wheelView1 = (WheelView) view.findViewById(R.id.wheelView1);
        wheelView1.setAdapter(new WheelSpinnerAdapter(lstData1));
        //  wheelView1.setCyclic(true);// 可循环滚动
        // wheelView.setLabel("选项");// 添加文字

        if(isInitValue){
            wheelView1.setCurrentItem(show_lstData1_index);// 初始化时显示的数据
             str_id=lstData1.get(show_lstData1_index).get(WheelSpinnerAdapter.LstDataKeys.ID);
             str_title=lstData1.get(show_lstData1_index).get(WheelSpinnerAdapter.LstDataKeys.TITLE);
            textView.setText(str_title);
        }else{
//            textView.setText(context.getString(R.string.app_please_select));
            textView.setHint(context.getString(R.string.app_please_select));
        }

        if(lstData1!=null){
            if(lstData1.size()>5){
                wheelView1.setCyclic(true);// 可循环滚动
            }else{
                wheelView1.setCyclic(false);// 不可循环滚动
            }
        }
        // 根据屏幕密度来指定选择器字体的大小
        wheelView1.TEXT_SIZE = SystemUtil.sp2px(context,16);
        textView_sure = (TextView) view.findViewById(R.id.textView_sure);
        TextView textView_cancel = (TextView) view
                .findViewById(R.id.textView_cancel);
        // 确定
        textView_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                textView.setText(lstData1.get(
                        wheelView1.getCurrentItem()).get(
                        WheelSpinnerAdapter.LstDataKeys.TITLE));

                str_id=lstData1.get(
                            wheelView1.getCurrentItem()).get(
                            WheelSpinnerAdapter.LstDataKeys.ID);
                str_title=lstData1.get(
                        wheelView1.getCurrentItem()).get(
                        WheelSpinnerAdapter.LstDataKeys.TITLE);
                if(onClick_Callback!=null){
                    if(onClick_Callback.onClick_sure()){
                        return;
                    }


                }
                dismiss();
            }
        });
        textView_sure.setOnTouchListener(new ViewUtil.OnTouchListener_view_transparency());
        // 取消
        textView_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(onClick_Callback!=null) {
                    if( onClick_Callback.onClick_cancel()){
                        return;
                    }

                }
                dismiss();
            }
        });
        textView_cancel.setOnTouchListener(new ViewUtil.OnTouchListener_view_transparency());
        // 设置dialog的布局,并显示
        setContentView(view);
        Window window = getWindow();
        window.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setOnClick_Callback(OnClick_Callback onClick_Callback) {
        this.onClick_Callback = onClick_Callback;
    }

    public interface  OnClick_Callback{
        public boolean onClick_sure();
        public boolean onClick_cancel();
    }
}
