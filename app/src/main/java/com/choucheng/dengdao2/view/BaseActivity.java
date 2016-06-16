package com.choucheng.dengdao2.view;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.CommParam;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.pojo.User;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.SharedUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="BaseActivity";
    //toolbar
    public ActionBar actionBar;
    public TextView tv_title;
    public ImageButton leftbutton,ib_rightbutton;
    public Button text_leftbutton;
    public TextView rightbutton;
    // 用来保存
    private static Map<String, Set<Activity>> activityGroup = new HashMap<String, Set<Activity>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------oncreate");
        initHeaderBar();
        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);
        initWidget();
    }

    /**
     * 初始化标题栏
     */
    public void initHeaderBar(){
        ////////////////////////////////////////////初始化标题栏
        actionBar  = getSupportActionBar();
        // 左侧图标点击事件使能
        actionBar.setHomeButtonEnabled(true);
        // 使左上角图标(系统)是否显示
        actionBar.setDisplayShowHomeEnabled(true);
        //显示自定义视图
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.common_titlebar);
        leftbutton = (ImageButton) actionBar.getCustomView().findViewById(R.id.ib_back);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);

        leftbutton.setOnClickListener(this);
        ///////////////////////////////////////初始化标题栏
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tv_title=  (TextView) actionBar.getCustomView().findViewById(R.id.bar_title);
        tv_title.setText(title);
    }


    /**
     * 设置左边文本操作按钮
     * @param s
     */
    public void setText_Leftbutton(String s){
        text_leftbutton=(Button)actionBar.getCustomView().findViewById(R.id.bar_left_button2);
        text_leftbutton.setVisibility(View.VISIBLE);
        text_leftbutton.setText(s);
        text_leftbutton.setOnClickListener(this);
    }


    /**
     * 隐藏左边带图标按钮
     */
    public void setIv_leftButton_dismiss(){
        leftbutton.setVisibility(View.GONE);
    }

    /**
     * 修改左边带图标按钮
     */
    public void setIv_leftButton(int resid){
        leftbutton.setImageResource(resid);
    }



    /**
     * 设置右侧文本操作按钮
     * @param s
     */
    public void setText_rightButton(String s){
        rightbutton=(TextView)actionBar.getCustomView().findViewById(R.id.bar_right_button);
        rightbutton.setVisibility(View.VISIBLE);
        rightbutton.setText(s);
        rightbutton.setOnClickListener(this);
    }


    /**
     * 设置右侧带图标操作按钮
     * @param resid
     */
    public void setIv_rightButton(int resid){
        ib_rightbutton=(ImageButton)actionBar.getCustomView().findViewById(R.id.bar_right_button2);
        ib_rightbutton.setVisibility(View.VISIBLE);
        ib_rightbutton.setImageResource(resid);
        ib_rightbutton.setOnClickListener(this);
    }

    /**
     * 设置右侧带图标操作按钮,隐藏
     */
    public void setIv_rightButton_dismiss(){
        ib_rightbutton=(ImageButton)actionBar.getCustomView().findViewById(R.id.bar_right_button2);
        ib_rightbutton.setVisibility(View.GONE);
    }


    /**
     * 初始化控件
     */
    public void initWidget(){}


    public synchronized static void addActToGroup(String Groupname, Activity cur) {
        Set<Activity> Group = activityGroup.get(Groupname);
        if (Group == null) {
            Group = new HashSet<Activity>();
            activityGroup.put(Groupname, Group);
        }
        Group.add(cur);
    }

    public synchronized static void destroyGroup(String Groupname) {
        Set<Activity> group = activityGroup.get(Groupname);
        if (group != null) {
            for (Activity one : group) {
                if (one != null) {
                    one.finish();
                }
            }
            activityGroup.get(Groupname).clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onDestroy");
        // ****** 不要忘了进行注销 ****
        EventBus.getDefault().unregister(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onClick");
        switch (v.getId()){
            case R.id.ib_back:
                Logger.i(TAG, "click___actionbar_back");
                finish();
                break;
        }
    }

    /**
     * toast 消息提示，短时间
     * @param msg
     */
    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast 消息提示，长时间
     * @param msg
     */
    public void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 返回按钮的事件监听
     */
    @Override
    public void onBackPressed() {
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onBackPressed");
        super.onBackPressed();

    }

    /**
     * 如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，onEventMainThread都会在UI线程中执行，
     * 接收事件就会在UI线程中运行，这个在Android中是非常有用的，因为在Android中只能在UI线程中跟新UI，
     * 所以在onEvnetMainThread方法中是不能执行耗时操作的。
     * @param event
     */
    public void onEventMainThread(AnyEventType event) {
        switch (event.getmMsg()){
            case FinalVarible.TAG_EXIT:
                finish();
                break;
        }

    }

    /**
     * :如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，
     * 也就是说发布事件和接收事件线程在同一个线程。使用这个方法时，在onEvent方法中不能执行耗时操作，
     * 如果执行耗时操作容易导致事件分发延迟。
     * @param eventType
     */
    public void onEvent(AnyEventType eventType){

    }

    /**
     * 使用这个函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行onEventAsync.
     * @param eventType
     */
    public void onEventAsync(AnyEventType eventType){

    }

    /**
     * 获取ucode
     * @return
     */
    public String getCode() {
        String code= CommParam.getInstance().getUid();
        if(code==null){
            SharedPreferences use_share=getSharedPreferences(FinalVarible.USER_SHARE,0);
            String detail= SharedUtil.getAESInfo(use_share, FinalVarible.OPENID);
            if(!detail.equals("")){
                code=detail;
                CommParam.getInstance().setUid(code);
            }
        }
        return code;
    }
    /**
     * 获取用户信息
     * @return
     */
    public User getUserInfo() {
        User userInfo= CommParam.getInstance().getUser();
        if(userInfo==null){
            SharedPreferences use_share=getSharedPreferences(FinalVarible.USER_SHARE,0);
            String detail= SharedUtil.getAESInfo(use_share, FinalVarible.USERDETAIL);
            if(!detail.equals("")){
                try {
                    userInfo = new Gson().fromJson(detail,User.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                /*if(userInfo!=null){
                    String code=userInfo.getId();
                    CommParam.getInstance().setUid(code);
                    CommParam.getInstance().setUser(userInfo);
                }*/

            }
        }
        return userInfo;
    }
}
