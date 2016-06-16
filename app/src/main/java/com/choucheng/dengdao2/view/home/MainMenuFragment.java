package com.choucheng.dengdao2.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.view.BaseFragment;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;


public class MainMenuFragment extends BaseFragment implements OnClickListener,XRecyclerView.LoadingListener{
    private TextView tv_date,tv_today_num,tv_today_money;
	private XRecyclerView listView;
	private int page = 1;
	private ArrayList<Object> datas=new ArrayList<>();
	private RecyclerAdapter<Object> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainviewView= inflater.inflate(R.layout.fragment_main_content, null);
		initWidget(mainviewView);
		return mainviewView;
	}



	private void initWidget(View view){
		view.findViewById(R.id.btn_grab).setOnClickListener(this);
		view.findViewById(R.id.tv_refresh_order).setOnClickListener(this);
		tv_date=(TextView)view.findViewById(R.id.tv_date);
		tv_today_num=(TextView)view.findViewById(R.id.tv_today_num);
		tv_today_money=(TextView)view.findViewById(R.id.tv_today_money);

		listView=(XRecyclerView)view.findViewById(R.id.xrecyclerview);
		adapter=new RecyclerAdapter<Object>(R.layout.item_systemmsg,datas) {
			@Override
			protected void onBindData(RecyclerViewHolder viewHolder, int position, Object item) {

			}
		};
		listView.setAdapter(adapter);
		listView.setLoadingMoreEnabled(true);
	}

	
	/**
	 * 控件点击事件
	 * @param v 点击的控件
	 */
	@Override
	public void onClick(View v) {
        switch (v.getId()) {
			case R.id.btn_grab:
                Intent grabintent=new Intent(getActivity(),OrderWaitActivity.class);
				startActivity(grabintent);
				break;
			case R.id.tv_refresh_order:

				break;
        }
	}


	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
}
