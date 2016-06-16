package com.choucheng.dengdao2.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/5/20.
 */
public class MyBaseAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private HashMap<?,?> data;
    private Activity context;

    public MyBaseAdapter(Activity context, HashMap<?, ?> data) {
        this.mInflater=context.getLayoutInflater();
        this.data=data;
        this.context = context;

    }

    @Override
    public int getCount() {
        return data!=null&&data.size()>0?data.size():0;
    }

    @Override
    public Object getItem(int i) {
        return data!=null&&data.size()>0?data.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return data!=null&&data.size()>0?i:0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

}
