package com.choucheng.dengdao2.definewidget.dialog.wheeldialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.definewidget.datapicker_other.OnWheelChangedListener;
import com.choucheng.dengdao2.definewidget.datapicker_other.WheelView;
import com.choucheng.dengdao2.tools.viewtools.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/10/27 0027.
 */
public class Dialog_Wheel_two extends Dialog {
    public int show_lstData1_index;
    private String tag = "Dialog_Wheel_two";
    public WheelView wheelView1;
    public WheelView wheelView2;
    public ArrayList<HashMap<String, String>> lstData1;
    public ArrayList<HashMap<String, String>> lstData2;
    public ArrayList<DataGrouping> lstData_all;
    private Context context;
    private TextView textView;
    public String str_id;
    public String str_title;
    private Boolean isInitValue;


    public Dialog_Wheel_two(Context context, int theme, TextView textView, ArrayList<DataGrouping> lstData_all, int show_lstData1_index, Boolean isInitValue) {
        super(context, theme);
        this.context = context;
        this.textView = textView;
        this.lstData_all = lstData_all;
        this.show_lstData1_index = show_lstData1_index;
        this.isInitValue = isInitValue;
        lstData1 = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        for (int i = 0; i < lstData_all.size(); i++) {
            map = new HashMap<String, String>();
            map.put(WheelSpinnerAdapter.LstDataKeys.ID, lstData_all.get(i).data_parent.get(WheelSpinnerAdapter.LstDataKeys.ID));
            map.put(WheelSpinnerAdapter.LstDataKeys.TITLE, lstData_all.get(i).data_parent.get(WheelSpinnerAdapter.LstDataKeys.TITLE));
            lstData1.add(map);
//            Log.d(tag,"parent.ID:"+lstData_all.get(i).data_parent.get(WheelSpinnerAdapter.LstDataKeys.ID));
//            Log.d(tag,"parent.TITLE:"+lstData_all.get(i).data_parent.get(WheelSpinnerAdapter.LstDataKeys.TITLE));
//            for (int j=0;j<lstData_all.get(i).data_child.size();j++){
//                Log.d(tag,"data_child.ID:"+lstData_all.get(i).data_child.get(j).get(WheelSpinnerAdapter.LstDataKeys.ID));
//                Log.d(tag,"data_child.TITLE:"+lstData_all.get(i).data_child.get(j).get(WheelSpinnerAdapter.LstDataKeys.TITLE));
//            }
        }

        init();
    }

    public Dialog_Wheel_two(Context context, TextView textView, ArrayList<DataGrouping> lstData_all, int show_lstData1_index, Boolean isInitValue) {
        this(context, R.style.NoTitleDialog, textView, lstData_all, show_lstData1_index, isInitValue);
    }

    public Dialog_Wheel_two(Context context, TextView textView, ArrayList<DataGrouping> lstData_all, Boolean isInitValue) {
        this(context, textView, lstData_all, 0, isInitValue);
    }

    public Dialog_Wheel_two(Context context, TextView textView, ArrayList<DataGrouping> lstData_all, int show_lstData1_index) {
        this(context, textView, lstData_all, show_lstData1_index, false);
    }

    public Dialog_Wheel_two(Context context, TextView textView, ArrayList<DataGrouping> lstData_all) {
        this(context, textView, lstData_all, 0, false);
    }

    private void init() {
        LayoutInflater inflater = getWindow().getLayoutInflater();
        View view = inflater.inflate(R.layout.wheel_view_layout, null);
        init_wheelView1(view);
        init_wheelView2(view);
        TextView textView_sure = (TextView) view.findViewById(R.id.textView_sure);
        TextView textView_cancel = (TextView) view
                .findViewById(R.id.textView_cancel);
        // 确定
        textView_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                textView.setText(lstData2.get(
                        wheelView2.getCurrentItem()).get(
                        WheelSpinnerAdapter.LstDataKeys.TITLE));

                str_id = lstData2.get(
                        wheelView2.getCurrentItem()).get(
                        WheelSpinnerAdapter.LstDataKeys.ID);
                str_title = lstData2.get(
                        wheelView2.getCurrentItem()).get(
                        WheelSpinnerAdapter.LstDataKeys.TITLE);

                dismiss();
            }
        });
        textView_sure.setOnTouchListener(new ViewUtil.OnTouchListener_view_transparency());
        // 取消
        textView_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
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
    private void init_wheelView1(View contentView){
        wheelView1 = (WheelView) contentView.findViewById(R.id.wheelView1);
        wheelView1.setAdapter(new WheelSpinnerAdapter(lstData1));
        wheelView1.setCurrentItem(show_lstData1_index);// 初始化时显示的数据
        if(lstData1!=null){
            if(lstData1.size()>5){
                wheelView1.setCyclic(true);// 可循环滚动
            }else{
                wheelView1.setCyclic(false);// 不可循环滚动
            }
        }
        // 字体的大小
        wheelView1.TEXT_SIZE = context.getResources().getDimensionPixelSize(R.dimen.textsize_wheelView);
        wheelView1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // Log.d(tag, "onChanged() oldValue:" + oldValue + ",newValue:" + newValue);
                lstData2 = lstData_all.get(newValue).data_child;
                //  Log.d(tag,"lstData2.size():"+lstData2.size());
//                for (int i=0;i<lstData2.size();i++){
//                    Log.d(tag,"ID:"+lstData2.get(i).get(WheelSpinnerAdapter.LstDataKeys.ID));
//                    Log.d(tag,"TITLE:"+lstData2.get(i).get(WheelSpinnerAdapter.LstDataKeys.TITLE));
//                }
                wheelView2.setAdapter(new WheelSpinnerAdapter(lstData2));
                if(lstData2.size()>5){
                    wheelView2.setCyclic(true);// 可循环滚动
                }else{
                    wheelView2.setCyclic(false);// 可循环滚动
                }
                wheelView2.setCurrentItem(lstData2.size()/2);// 初始化时显示的数据
            }
        });
    }
    private void init_wheelView2(View contentView){
            if(lstData_all.size()>show_lstData1_index){
                lstData2 = lstData_all.get(show_lstData1_index).data_child;

            }else{
                lstData2=new ArrayList<HashMap<String, String>>();
            }
            if (isInitValue) {
                if(lstData2!=null&&lstData2.size()>0){

                    str_id = lstData2.get(0).get(WheelSpinnerAdapter.LstDataKeys.ID);
                    str_title = lstData2.get(0).get(WheelSpinnerAdapter.LstDataKeys.TITLE);
                    textView.setText(str_title);
                }
            } else {
                textView.setText(context.getString(R.string.app_please_select));
            }

        wheelView2 = (WheelView) contentView.findViewById(R.id.wheelView2);
        wheelView2.setAdapter(new WheelSpinnerAdapter(lstData2));
        wheelView2.setCyclic(true);// 可循环滚动
        if(lstData2!=null){
            if(lstData2.size()>5){
                wheelView2.setCyclic(true);// 可循环滚动
            }else{
                wheelView2.setCyclic(false);// 可循环滚动
            }
        }


        // 根据屏幕密度来指定选择器字体的大小
        wheelView2.TEXT_SIZE = context.getResources().getDimensionPixelSize(R.dimen.textsize_wheelView);
    }
    public static class DataGrouping {
        public HashMap<String, String> data_parent;
        public ArrayList<HashMap<String, String>> data_child;
    }
}
