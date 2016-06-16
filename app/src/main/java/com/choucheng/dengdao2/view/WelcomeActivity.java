package com.choucheng.dengdao2.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.CommParam;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.tools.AnimationUtil;
import com.choucheng.dengdao2.tools.HelperUtil;
import com.choucheng.dengdao2.tools.HttpMethodUtil;
import com.choucheng.dengdao2.tools.SharedUtil;
import com.choucheng.dengdao2.tools.SystemUtil;
import com.choucheng.dengdao2.view.adapter.AllViewPagerAdapter;
import com.choucheng.dengdao2.view.adapter.Main_ViewPagerAdapter;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;


/**
 * 欢迎页面
 */

public class WelcomeActivity extends Activity{
    private static final String TAG="WelcomeActivity";
    private static final int TO_THE_END =0x1110;// 到达最后一张
    private static final int LEAVE_FROM_END =0x1111;// 离开最后一张
    private ViewPager viewPager;
    private LinearLayout circlelayout;//小圆点指示图
    private Main_ViewPagerAdapter viewpager_adapter;
    private int[] ids = {R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
    private int[] ids2 = {R.drawable.ic_launcher};
    private List<View> guides = new ArrayList<>();
    private ImageView[] circleimageViews;//填装小圆点图标
    private SharedPreferences base_share;//程序缓存基本信息
    private boolean ismorepic=false;//是否有多张图
    private boolean is_yindao=false;//是否有引导页
    private boolean is_autologin=false;//是否需要自动登陆
    private Button btn_open;
    private int curPos = 0;// 记录当前的位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //启动定位
       // startService(new Intent(WelcomeActivity.this, LocationService.class));
        initWidget();
    }


    /**
     * 初始化控件
     */
    private void initWidget(){
        btn_open=(Button)findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedUtil.commitInfo(base_share, FinalVarible.ISFIRS, "1");
                HelperUtil.startLoginActivity(WelcomeActivity.this);
            }
        });
        viewPager=(ViewPager)findViewById(R.id.contentPager);
        circlelayout=(LinearLayout)findViewById(R.id.circle_images);
        initADData();
        //页面的切换
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {    }

            @Override
            public void onPageSelected(int positiion) {
                if(circleimageViews!=null&&circleimageViews.length>positiion){
                    for (int i = 0; i < circleimageViews.length; i++) {
                        // 不是当前选中的page，其小圆点设置为未选中的状态
                        if (positiion != i) {
                            circleimageViews[i].setBackgroundResource(R.drawable.circle_white);
                        }else{
                            circleimageViews[positiion].setBackgroundResource(R.drawable.circle_grey);
                        }
                        if (positiion == ids.length - 1) {// 到最后一张了
                            handler.sendEmptyMessage(TO_THE_END);
                        }else if (curPos == ids.length - 1) {
                            handler.sendEmptyMessageDelayed(LEAVE_FROM_END, 100);
                            // handler.sendEmptyMessage(LEAVE_FROM_END);
                        }
                        curPos = positiion;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {   }
        });
    }


    /**
     * 初始化广告信息
     */
    private void initADData(){
        if(base_share==null)
            base_share=getSharedPreferences(FinalVarible.BASE_SHARE, 1);
        String is=base_share.getString(FinalVarible.ISFIRS,"");
        if(is.equals("")){
            for (int id:ids) {
                ImageView iv = new ImageView(WelcomeActivity.this);
                iv.setBackgroundResource(id);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.CENTER);
                guides.add(iv);
            }
        }else{
            for (int id:ids2) {
                ImageView iv = new ImageView(WelcomeActivity.this);
                iv.setBackgroundResource(id);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.CENTER);
                guides.add(iv);
            }
        }
        if(guides.size()>1){
            ismorepic=true;
            AllViewPagerAdapter adapter = new AllViewPagerAdapter(this,guides);
            viewPager.setAdapter(adapter);
            initImage();
        }else{
            handler.sendEmptyMessage(FinalVarible.GETDATA);
        }
    }



    /**
     * 根据得到广告的数据，刷新小圆点
     */
    public void initImage() {
        circleimageViews = new ImageView[guides.size()];
        circlelayout.removeAllViews();
        ImageView imageView;
        for (int i = 0; i < guides.size(); i++) {
            imageView = new ImageView(this);
            LinearLayout.LayoutParams  layoutParams  = new LinearLayout.LayoutParams(12,12);
            layoutParams.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(layoutParams);
            // 将小圆点layout添加到数组中
            circleimageViews[i] = imageView;
            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                circleimageViews[i].setBackgroundResource(R.drawable.circle_white);
            } else {
                circleimageViews[i].setBackgroundResource(R.drawable.circle_grey);
            }
            // 将imageviews添加到小圆点视图组
            circlelayout.addView(circleimageViews[i]);
        }

    }



    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FinalVarible.GETDATA:
                    checkUserInfo();
                    break;
                case 0:
                    //获取用户信息成功
                    Bundle bundle=msg.getData();
                    if(bundle!=null){
                        String data=bundle.getString("data");
                       /* if(!data.equals(""))
                            HelperUtil.saveUserInfo(WelcomeActivity.this, "", data);
                        else
                            HelperUtil.startLoginActivity(WelcomeActivity.this);*/
                    }
                    break;
                case TO_THE_END:
                    btn_open.setVisibility(View.VISIBLE);
                    //
                    break;
                case LEAVE_FROM_END:
                    btn_open.setVisibility(View.GONE);
                    break;
                default:
                    HelperUtil.startLoginActivity(WelcomeActivity.this);
                    break;
            }

        }
    };


    /***
     * 判断本地缓存的栏目信息
     */
    private void checkLocalData(){
        //版本升级
        RequestParams requestParams=new RequestParams();
        requestParams.add("os", "android");
        requestParams.add("version", SystemUtil.getVersionName(this));
        if(ismorepic){
            HttpMethodUtil.checkNewApp(WelcomeActivity.this,null,requestParams);
        }else{
            HttpMethodUtil.checkNewApp(WelcomeActivity.this,handler,requestParams);
        }
    }

    /***
     * 用户信息检查--如果无用户信息-则跳转到登录界面，注册，进入主页
     *         --有用户信息，则获得用户信息---获取失败，进入登录界面--登录成功，进入主页
     *                             --成功，进入主页
     */
    private void checkUserInfo(){
        SharedPreferences sp=getSharedPreferences(FinalVarible.USER_SHARE,0);
        String code=SharedUtil.getAESInfo(sp,FinalVarible.OPENID);
        if(code.equals("")){
            HelperUtil.startLoginActivity(WelcomeActivity.this);
        }else{
            CommParam.getInstance().setUid(code);
          //  HttpMethodUtil.method_getUserDetail(this, true, "0", handler);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void finish() {
        super.finish();
        AnimationUtil.finishAnimation(this, R.anim.transalte_bottom_in, R.anim.transalte_top_out);
    }

}
