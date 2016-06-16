package com.choucheng.dengdao2.definewidget.dialog.wheeldialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/11/21 0021.
 */
public class RadioListDialogUtil extends Dialog {
     TextView tv_show;
    Context context;
    ToolDialogRadioListAdapter toolDialogRadioListAdapter;
    ArrayList<HashMap<String, Object>> lstData;
    public String id;
    public String title;
    public String str;

    public RadioListDialogUtil(Context context, TextView tv_show, ArrayList<HashMap<String, Object>> lstData, int initDataId) {
        super(context);
        this.context=context;
        this.tv_show=tv_show;
        this.lstData=lstData;
        if(initDataId!=-1){
            id= MapUtil.getString(lstData.get(initDataId), ToolDialogRadioListAdapter.LstDataKeys.ID);
            title=MapUtil.getString(lstData.get(initDataId),ToolDialogRadioListAdapter.LstDataKeys.TITLE);
            str=MapUtil.getString(lstData.get(initDataId),ToolDialogRadioListAdapter.LstDataKeys.STR);
            tv_show.setText(str);
        }
        init();
    }
    public RadioListDialogUtil(Context context, TextView tv_show, ArrayList<HashMap<String, Object>> lstData) {
       this( context,  tv_show, lstData, -1);
    }

    public RadioListDialogUtil(Context context, int theme) {
        super(context, theme);
    }

    protected RadioListDialogUtil(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init(){
        setTitle(context.getString(R.string.app_please_select));
        LayoutInflater inflater = getWindow().getLayoutInflater();
        View view = inflater.inflate(R.layout.tool_dialog_radio_list, null);
        ListView listView=(ListView)view.findViewById(R.id.listView);
        toolDialogRadioListAdapter=new ToolDialogRadioListAdapter(context,lstData);
        listView.setAdapter(toolDialogRadioListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RadioListDialogUtil.this.id=MapUtil.getString(lstData.get(position),ToolDialogRadioListAdapter.LstDataKeys.ID);
                str=MapUtil.getString(lstData.get(position),ToolDialogRadioListAdapter.LstDataKeys.STR);
                title=MapUtil.getString(lstData.get(position),ToolDialogRadioListAdapter.LstDataKeys.TITLE);
                tv_show.setText(title);
                dismiss();
            }
        });


        setContentView(view);

    }
}
