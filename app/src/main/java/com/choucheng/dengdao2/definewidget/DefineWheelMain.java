package com.choucheng.dengdao2.definewidget;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.choucheng.dengdao2.definewidget.datapicker_other.ArrayWheelAdapter;
import com.choucheng.dengdao2.definewidget.datapicker_other.ListWheelAdapter;
import com.choucheng.dengdao2.definewidget.datapicker_other.OnWheelChangedListener;
import com.choucheng.dengdao2.definewidget.datapicker_other.ScreenInfo;
import com.choucheng.dengdao2.definewidget.datapicker_other.WheelView;
import com.choucheng.dengdao2.pojo.City;

import java.util.ArrayList;
import java.util.List;


public class DefineWheelMain {

	private LinearLayout view;
    private List<WheelView> wvs=new ArrayList<WheelView>();
	public int screenheight;
    private Activity context;
    private List<List<String>> showdata=new ArrayList<List<String>>();
    private List<List<City>> showdata2=new ArrayList<List<City>>();
	public View getView() {
		return view;
	}

	public void setView(LinearLayout view) {
		this.view = view;
	}

	public DefineWheelMain(Activity activity, LinearLayout view, List<List<String>> data) {
		super();
		this.view = view;
        this.showdata=data;
        this.context=activity;
		setView(view);
        ScreenInfo screenInfo = new ScreenInfo(activity);
        this.screenheight=screenInfo.getHeight();
        initPicker(0);
	}
    public DefineWheelMain(Activity activity, LinearLayout view, List<List<String>> data, int pos) {
        super();
        this.view = view;
        this.showdata=data;
        this.context=activity;
        setView(view);
        ScreenInfo screenInfo = new ScreenInfo(activity);
        this.screenheight=screenInfo.getHeight();
        initPicker(pos);
    }
	/**
	 * @Description:  弹出日期时间选择器
	 */
	public void initPicker(int pos) {
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = (screenheight / 100) * 4;

        //添加布局内容
        WheelView wView;
        LinearLayout.LayoutParams  params;
        ArrayWheelAdapter<String> adapter;
        String[] arrays;
        int i=0;
        for (List<String> aShowdata : showdata) {
            wView = new WheelView(context);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            params.gravity= Gravity.CENTER;
            if(i>0){
                params.leftMargin=1;
            }
            wView.setLayoutParams(params);
            arrays = aShowdata.toArray(new String[aShowdata.size()]);
            adapter = new ArrayWheelAdapter<String>(arrays,8);
            wView.setAdapter(adapter);
            wView.TEXT_SIZE = textSize;
           // wView.setCurrentItem(aShowdata.size()/2);
            wView.setCurrentItem(pos);//默认选择第一个
            view.addView(wView);
            wvs.add(wView);
            i++;
        }
	}


    //==========================>
    ListWheelAdapter<City> adapter1,adapter2,adapter3;
    /**
     * @Description:  地方选择
     */
    public void initPicker2(int pos) {
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = (screenheight / 100) * 4;
        //添加布局内容
        WheelView wView;
        LinearLayout.LayoutParams  params;
        int i=0;
        for (List<City> aShowdata : showdata2) {
            wView = new WheelView(context);
            wView.addChangingListener(changedListener);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            params.gravity= Gravity.CENTER;
            if(i>0){
                params.leftMargin=1;
            }
            wView.setLayoutParams(params);
            switch (i){
                case 0:
                    adapter1 = new ListWheelAdapter<City>(aShowdata,8);
                    wView.setAdapter(adapter1);
                    break;
                case 1:
                    adapter2 = new ListWheelAdapter<City>(aShowdata,8);
                    wView.setAdapter(adapter2);
                    break;
                case 2:
                    adapter3 = new ListWheelAdapter<City>(aShowdata,8);
                    wView.setAdapter(adapter3);
                    break;
            }
            wView.TEXT_SIZE = textSize;
            // wView.setCurrentItem(aShowdata.size()/2);
            wView.setCurrentItem(pos);//默认选择第一个
            view.addView(wView);
            wvs.add(wView);
            i++;
        }
    }


    OnWheelChangedListener changedListener=new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if ((int)wheel.getTag() != 0) {
                int tag = (int) wheel.getTag();
                switch (tag) {
                    case 0:
                        //    adapter1.setList();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

        }


    };



	public String getTime() {
		StringBuilder sb = new StringBuilder();
        WheelView view1;
        for(int i=0;i<wvs.size();i++){
            view1=wvs.get(i);
            if(i==wvs.size()-1){
                sb.append(view1.getCurrentItem());
            }else{
                sb.append(view1.getCurrentItem()).append(",");
            }
        }
		return sb.toString();
	}
}
