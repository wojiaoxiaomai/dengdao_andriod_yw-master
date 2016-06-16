package com.choucheng.dengdao2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.tools.HttpMethodUtil;


/**
 * 找回密码
 */
public class FindPasswordActivity extends BaseActivity {
    private EditText et_username;
    boolean isfrist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActToGroup("FindPassword", this);
    }


    @Override
    public void initHeaderBar() {
        super.initHeaderBar();
        setTitle(R.string.getverifycode);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setContentView(R.layout.activity_find_password_code);
        et_username=(EditText)findViewById(R.id.edit_username);
        findViewById(R.id.bt_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_submit:
                isfrist=false;
                getCaptcha();
                break;
        }
    }

    private void getCaptcha() {
        final String mobile = et_username.getText().toString().trim();
        if (mobile.length() == 0) {
            showLongToast("请输入手机号");
            return;
        }
        HttpMethodUtil.method_getverifyCode(this, mobile, true);
    }

    /**
     * 获取验证码成功之后，时间倒计时
     * 这里是进行页面跳转
     */
    @Override
    public void onEventMainThread(AnyEventType event) {
        super.onEventMainThread(event);
        switch (event.getmMsg()){
            case FinalVarible.TAG_TIMELIMIT:
                if(!isfrist){
                    isfrist=true;
                    startActivity(new Intent(this,FindPasswordTwoActivity.class));
                }
                break;
        }
    }
}
