package com.choucheng.dengdao2.definewidget.dialog.wheeldialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/11/21 0021.
 */
public class ToolDialogRadioListAdapter extends BaseAdapter {
    private String tag = "ToolDialogRadioListAdapter";
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, Object>> lstData;
    private Item_info item_info;

    public ToolDialogRadioListAdapter(Context context,
                                      ArrayList<HashMap<String, Object>> lstData) {
        this.lstData = lstData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // mainApplication.logUtil.d("lstData.size():"+lstData.size());
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {

        return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(tag, "getView() position:" + position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.tool_dialog_radio_item, null);
            item_info = new Item_info();
            item_info.tv = (TextView) convertView
                    .findViewById(R.id.tv);
            convertView.setTag(item_info);
        } else {
            item_info = (Item_info) convertView.getTag();
        }
        String title = MapUtil.getString(lstData.get(position), LstDataKeys.TITLE);
        item_info.tv.setText(title);
        return convertView;
    }


    private class Item_info {
        TextView tv;
    }

    public class LstDataKeys {
        public final static String ID = "id";
        public final static String TITLE = "title";
        public final static String STR = "str";
    }
}
