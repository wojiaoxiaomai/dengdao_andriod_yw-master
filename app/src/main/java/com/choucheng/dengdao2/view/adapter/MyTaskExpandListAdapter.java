package com.choucheng.dengdao2.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 14-3-5.
 */
public class MyTaskExpandListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private static final String TAG="DishExpandListAdapter";
    private int[] fathetitle;
    private List<List<Object>> childlistList=new ArrayList<>(); //二级菜单类
    private LayoutInflater mInflater;

   public MyTaskExpandListAdapter(Activity context,List<List<Object>> list){
        Logger.i(TAG, " init alrodsetmealListAdapter");
        this.context=context;
        this.mInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.childlistList=list;
    }

     class Dish_father{
    	 public int id;
    	 public String name;
     }
    
    
    
    @Override
    public void notifyDataSetChanged() {
    	super.notifyDataSetChanged();
    }
    

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childlistList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView != null) {
            holder = (ChildHolder) convertView.getTag();
        } else {
            //convertView = mInflater.inflate(R.layout.fragment_task_item, parent, false);
           
           // convertView.setTag(holder);
        }
        
       
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childlistList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
       return fathetitle[groupPosition];
    }

    @Override
    public int getGroupCount() {
    	return fathetitle.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.textview_s, parent, false);
            holder = new ViewHolder();
            holder.name=(TextView)convertView.findViewById(R.id.text_title);
            holder.name.setTextColor(context.getResources().getColor(R.color.themecolor_text));
            convertView.setTag(holder);
        }
    
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

   /**
    * 子布局
    */
    class ChildHolder{
    	TextView  taskaddress;
    	TextView  taskstatus;
    	TextView  timer;
    }
    
    class ViewHolder {
        TextView name;
    }
}
