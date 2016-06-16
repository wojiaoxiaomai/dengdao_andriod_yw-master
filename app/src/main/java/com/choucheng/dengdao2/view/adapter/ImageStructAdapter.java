package com.choucheng.dengdao2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choucheng.dengdao2.R;

import java.util.List;


public class ImageStructAdapter extends BaseAdapter {


	private LayoutInflater mInflater;
	private List<Object> data;
	private Context context;
    

	public ImageStructAdapter(Context context, List<Object> data) {
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
	}


    public void delete(int position){
        data.remove(position);
        notifyDataSetChanged();

    }

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder ;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.baseimagestruct_item, null);
			//holder.logo = (ImageView) convertView.findViewById(R.id.carimage);
			//holder.title = (TextView) convertView.findViewById(R.id.car_hphm);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}


		return convertView;
	}

	
	
	class Holder {
		TextView order_No; //订单编号 
		TextView order_state;//订单状态
		ImageView open_closemark;
		LinearLayout progressarea;//订单进度区域
		
		
	}

}
