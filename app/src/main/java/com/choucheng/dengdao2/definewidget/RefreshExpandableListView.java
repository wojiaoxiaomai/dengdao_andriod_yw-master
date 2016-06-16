package com.choucheng.dengdao2.definewidget;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.Logger;

import java.util.Date;


public class RefreshExpandableListView extends ExpandableListView implements 
        OnScrollListener { 

    @Override 
    public void setAdapter(ListAdapter adapter) { 
        super.setAdapter(adapter); 
    } 
    
    public interface IOnRefreshListener{
		void OnRefresh();
	}
	

    //松开刷新 
    private final static int RELEASE_TO_REFRESH=0;// 
    private final static int PULL_TO_REFRESH=1;//下拉刷新 
    
    private final static int REFRESHING =2;//正在刷新 
    
    private final static int DONE = 3;    
    private final static int RATIO=3;//实际的padding的距离与界面 上偏移距离 的比例 
    private LayoutInflater inflater; 
    private LinearLayout headLayout;//头linearlayout 
    private TextView tipsTextview; 
    private TextView lastUpdatedTextView; 
    private ImageView arrowImageView;//箭头的图标 
    
    private ProgressBar progressBar; 

    private RotateAnimation animation; 
    // 反转动画 
    private RotateAnimation reverseAnimation; 

    private int headContentWidth;//头部的宽度 
    
    private LinearLayout headView; 
    private int headContentHeight; 
     
    /** 手势按下的起点位置 */ 
    private int startY; 
    private int firstItemIndex; 

    private int state; 

    private boolean isBack; 

    private IOnRefreshListener mOnRefreshListener;			// 头部刷新监听器

    private boolean isRefreshable; 

    public RefreshExpandableListView(Context context) { 
        super(context); 
        init(context); 
    } 
    public RefreshExpandableListView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(context); 
    } 

    public void onClick(DialogInterface dialog, int which) { 

    } 
    public void init(Context context) 
    { 
        inflater =LayoutInflater.from(context); 
        headView =(LinearLayout) inflater.inflate(R.layout.head, null);
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);//箭头 
        arrowImageView.setMinimumWidth(70); 
        arrowImageView.setMinimumHeight(50); 
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar); 
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView); 
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView); 
        
        headView.measure(0, 0); 
        headContentHeight=headView.getMeasuredHeight(); 
        headContentWidth=headView.getMeasuredWidth(); 
        
        headView.setPadding(0, -headContentHeight, 0, 0);//把headview隐藏到顶部 
        headView.invalidate();//刷新界面 
        
        addHeaderView(headView,null,false); 
        setOnScrollListener(this);//滚动监听 
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f); 
        animation.setInterpolator(new LinearInterpolator()); 
        animation.setDuration(250); 
        animation.setFillAfter(true); 
        
        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f); 
        reverseAnimation.setInterpolator(new LinearInterpolator()); 
        reverseAnimation.setDuration(200); 
        reverseAnimation.setFillAfter(true); 
        
        state = DONE; 
        isRefreshable = false; 
    } 
    public void onScroll(AbsListView view, int firstVisibleItem, 
            int visibleItemCount, int totalItemCount) { 
        firstVisibleItem=firstVisibleItem; 
        
    } 
    public void onScrollStateChanged(AbsListView view, int scrollState) { 
        
    } 
    /** 
     * 设置触摸事件 总的思路就是 
     * 
     * 1 ACTION_DOWN：记录起始位置 
     * 
     * 2 ACTION_MOVE：计算当前位置与起始位置的距离，来设置state的状态 
     * 
     * 3 ACTION_UP：根据state的状态来判断是否下载 
     */ 
    int tempy=0;
    public boolean onTouchEvent(MotionEvent event) 
    { 
        isRefreshable = true; 
        if (isRefreshable) { 
            switch (event.getAction()) { 
            case MotionEvent.ACTION_DOWN://按下屏幕 
                System.out.println("按下屏"); 
                if (firstItemIndex==0) { 
                    startY=(int)event.getY(); 
                    tempy=startY;
                    System.out.println("记录down下当前的位置"); 
                } 
                break; 
                
            case MotionEvent.ACTION_MOVE: //移动屏幕 
                System.out.println("移动下屏"); 
                int tempY1=(int)event.getY(); 
                int offset=tempY1-tempy;
                if(tempy==0) tempy=tempY1;
              //  tempy=tempY1;
                Logger.i("listview", "offset:"+offset);
                Logger.i("listview", "startY:"+tempy);
                
                switch (state) {
					case DONE:
						if (offset > 0){		
							state = PULL_TO_REFRESH; 
	                        changeHeaderViewByState();
						}
						break;
					case PULL_TO_REFRESH:
						setSelection(0);//很重要 
					    headView.setPadding(0, -headContentHeight + offset, 0, 0);
					    if (offset < 0){
					    	 state =DONE; 
		                     changeHeaderViewByState(); 
						}else if (offset > headContentHeight){
							 state=RELEASE_TO_REFRESH; 
		                     isBack=true; 
		                     changeHeaderViewByState(); 
						}
						break;
					case RELEASE_TO_REFRESH:
						setSelection(0);
						headView.setPadding(0, -headContentHeight + offset, 0, 0); 
						 if ((offset < headContentHeight) && offset> 0) { 
		                        state = PULL_TO_REFRESH; 
		                        changeHeaderViewByState(); 
//		                        Log.v(TAG, "由松开刷新状态转变到下拉刷新状态"); 
		                 }else if (offset < 0){
		                	 state =DONE; 
		                     changeHeaderViewByState(); 
		 				}
						break;
				}
                break; 
            case MotionEvent.ACTION_UP: 
                System.out.println("ACTION_UP"); 
                tempy=0;
                if (state != REFRESHING) { 
                    // 不在刷新状态 
                    if (state == PULL_TO_REFRESH) { 
                        state = DONE; 
                        changeHeaderViewByState(); 
                    } 
                    if (state == RELEASE_TO_REFRESH) { 
                        state = REFRESHING; 
                        changeHeaderViewByState(); 
                        isRefreshable = true; 
                        onRefresh();
                    } 
                } 
                isBack = false; 
                break; 

            } 
        } 
        return super.onTouchEvent(event); 
        
    } 
    //当状态改变时候，调用 该方法，以更新界面 
    private void changeHeaderViewByState() 
    { 
        switch (state) { 
        case RELEASE_TO_REFRESH: 
            arrowImageView.setVisibility(View.VISIBLE); 
            progressBar.setVisibility(View.GONE); 
            tipsTextview.setVisibility(View.VISIBLE); 

            arrowImageView.clearAnimation(); 
            arrowImageView.startAnimation(animation); 

            tipsTextview.setText("松开刷新"); 
            break; 
        case PULL_TO_REFRESH: 
            progressBar.setVisibility(View.GONE); 
            tipsTextview.setVisibility(View.VISIBLE); 

            arrowImageView.clearAnimation(); 
            arrowImageView.setVisibility(View.VISIBLE); 
            tipsTextview.setText("下拉刷新"); 
            if (isBack) { 
                isBack = false; 
                arrowImageView.startAnimation(reverseAnimation); 
            } 
            break; 

        case REFRESHING: 
            headView.setPadding(0, 0, 0, 0); 
            progressBar.setVisibility(View.VISIBLE); 
            arrowImageView.clearAnimation(); 
            arrowImageView.setVisibility(View.GONE); 
            tipsTextview.setText("正在刷新..."); 
            break; 
        case DONE: 
            headView.setPadding(0, -headContentHeight, 0, 0); 
            progressBar.setVisibility(View.GONE); 
            arrowImageView.clearAnimation(); 
            arrowImageView.setImageResource(R.drawable.arrow); 
            tipsTextview.setText("下拉刷新"); 
            break; 
        } 
    } 
    
	public void setOnRefreshListener(IOnRefreshListener listener)
	{
		mOnRefreshListener = listener;
	}
	
	private void onRefresh(){
		if (mOnRefreshListener != null)
		{
			mOnRefreshListener.OnRefresh();
		}
	}
    
	public void onRefreshComplete(){
		headView.setPadding(0,  -1 * headContentWidth, 0, 0);
		lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
	    state =DONE; 
        changeHeaderViewByState();
	}
}