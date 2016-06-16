package com.choucheng.dengdao2.view;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.User;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.tools.OnFunctionListener;
import com.choucheng.dengdao2.tools.image.BitmapUtils;
import com.choucheng.dengdao2.view.home.LeftMenuFragment;
import com.choucheng.dengdao2.view.home.MainMenuFragment;
import com.choucheng.dengdao2.view.home.SystemMessageActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener{
    private static final String TAG="MainActivity";
    private CanvasTransformer mTransformer;
    private DialogUtil dc;
    private MainMenuFragment fragment_main;
    private LeftMenuFragment fragment_left;
    private User uparams;
    private TextView tv_location_addr;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置主界面视图
        setContentView(R.layout.activity_main);
        initWdiget();
        initAnimation();
        initSlidingMenu();
        dc=new DialogUtil(this);

    }


    private void initWdiget(){
        //toolbar
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
        findViewById(R.id.bar_right_button2).setOnClickListener(this);
        //location
        findViewById(R.id.ib_refresh).setOnClickListener(this);
        tv_location_addr=(TextView)findViewById(R.id.tv_location_address);
    }


    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu(){
        fragment_left=new LeftMenuFragment();
        fragment_main=new MainMenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame2, fragment_main).commit();

        // 设置滑动菜单视图
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, fragment_left).commit();

        // 设置滑动菜单的属性值
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindScrollScale(0.0f);
        sm.setBehindCanvasTransformer(mTransformer);

        setSlidingActionBarEnabled(true);
    }

    private static Interpolator interp = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t + 1.0f;
        }
    };

    /**
     * 初始化动画效果
     */
    private void initAnimation(){
        mTransformer = new CanvasTransformer(){
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                canvas.translate(0, canvas.getHeight() * (1 - interp.getInterpolation(percentOpen)));
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                toggle();
                break;
            case R.id.bar_right_button2:
                Intent intent=new Intent(this, SystemMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_refresh:
                //刷新定位

                break;
        }

    }
}
