package com.choucheng.dengdao2.view;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.MHandler;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.tools.HttpMethodUtil;
import com.loopj.android.http.RequestParams;

/**
 * 找回密码
 */
public class FindPasswordTwoActivity extends BaseActivity {
    private Button bt_get_check_code;
    private TextView tv_tip;
    private EditText et_check_code;
    private EditText login_et_password;
    private EditText login_et_password_again;

    String mobile = "";
    TimeCount time = new TimeCount(60000, 1000);// 定时器(验证码)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = getIntent().getStringExtra("mobile");
        addActToGroup("FindPassword", this);
        time.start();
    }

    @Override
    public void initHeaderBar() {
        super.initHeaderBar();
        setTitle("设置密码");
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setContentView(R.layout.activity_find_password_two);
        tv_tip=(TextView)findViewById(R.id.tv_tip);
        bt_get_check_code=(Button)findViewById(R.id.bt_get_check_code);
        et_check_code=(EditText)findViewById(R.id.et_check_code);
        login_et_password=(EditText)findViewById(R.id.login_et_password);
        login_et_password_again=(EditText)findViewById(R.id.login_et_password_again);
        tv_tip.setText("验证码已经发送至   " + mobile);
        findViewById(R.id.bt_get_check_code).setOnClickListener(this);
        findViewById(R.id.bt_submit).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_get_check_code:
                HttpMethodUtil.method_getverifyCode(this, mobile, true);
                break;
            case R.id.bt_submit:
                uplaod();
                break;
        }
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
                time.start();
                break;
        }
    }

    private void uplaod() {
        String check_code = et_check_code.getText().toString().trim();
        String pwd = login_et_password.getText().toString().trim();
        String pwd2 = login_et_password_again.getText().toString().trim();
        if (mobile.length() == 0) {
            showLongToast("请输入手机号");
            return;
        }

        if (check_code.length() == 0) {
            showLongToast("请输入验证码");
            return;
        }

        if (pwd.length() == 0) {
            showLongToast("请输入密码");
            return;
        }

        if (pwd2.length() == 0) {
            showLongToast("请输入密码");
            return;
        }
        if (!pwd.equals(pwd2)) {
            showLongToast( "两次输入密码不一样");
            return;
        }

        RequestParams SubmitParams = new RequestParams();
        SubmitParams.put("phone", mobile);
        SubmitParams.put("code", check_code);
        SubmitParams.put("password", pwd);

        final Dialog dialog= DialogUtil.loadingDialog(this, null, false);
        new MHandler(this, FinalVarible.GETURL_FINDPWD, SubmitParams, false,
                null, null, true, false, new MHandler.DataCallBack() {
            @Override
            public void returnMessage(Message message) {
                dialog.dismiss();
                switch (message.what){
                    case 0:
                        setResult(RESULT_OK);
                        destroyGroup("FindPassword");
                        break;
                }


            }
        });
    }



    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            bt_get_check_code.setText("重新获取");
            bt_get_check_code.setEnabled(true);

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            bt_get_check_code.setEnabled(false);
            bt_get_check_code.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
