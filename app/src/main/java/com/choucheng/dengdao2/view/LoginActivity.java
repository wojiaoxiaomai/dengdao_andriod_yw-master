package com.choucheng.dengdao2.view;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.MHandler;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.tools.HelperUtil;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.SharedUtil;
import com.choucheng.dengdao2.tools.Validator;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends BaseActivity implements OnClickListener{
	private static final String TAG="LoginActivity";
	private EditText edit_username,edit_pwd;
	private SharedPreferences user_share;
	private SharedPreferences base_share;
	private String username,pwd;
    private Dialog dialog;
  
	private String type="phone",openid="",name="";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "oncreate");
	}


	/**
	 * 初始化标题栏
	 */
	@Override
	public void initHeaderBar(){
		super.initHeaderBar();
		setTitle(R.string.login);
	}

	/**
	 * 初始化控件
	 */
	@Override
	public void initWidget(){
		setContentView(R.layout.activity_login);
		edit_username=(EditText)findViewById(R.id.edit_username);
		edit_pwd=(EditText)findViewById(R.id.edit_pwd);
		findViewById(R.id.button_login).setOnClickListener(this);
		findViewById(R.id.tv_register).setOnClickListener(this);
		findViewById(R.id.tv_forget_pwd).setOnClickListener(this);
		initCache();
	}
	/**
	 * 初始化缓存信息
	 */
	private void initCache(){
		base_share=getSharedPreferences(FinalVarible.BASE_SHARE, MODE_PRIVATE);
		user_share=getSharedPreferences(FinalVarible.USER_SHARE, MODE_PRIVATE);
		username=SharedUtil.getAESInfo(user_share, FinalVarible.CURR_NAME);
		pwd=SharedUtil.getAESInfo(user_share, FinalVarible.CURR_PWD);
		edit_username.setText(username);
		edit_pwd.setText(pwd);
	}
	
    /**
     * 检查输入框
     */
	private boolean check_Input(){
		if(!Validator.checkIsNull(this, edit_username, getString(R.string.username))
				&&!Validator.checkIsNull(this, edit_pwd, getString(R.string.pwd))){
			return true;
		}
	   return false;		
	}
	
    /***
     * 控件点击事件
     * @param v
     */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.button_login:
				//登陆操作
				/*type="phone";
				openid="";
				name="";
				if(check_Input()){
					method_login();
				}*/
				startActivity(new Intent(this,MainActivity.class));
				finish();
				break;
			case R.id.tv_register:
                 startActivity(new Intent(this,RegisterActivity.class));
				break;
			case R.id.tv_forget_pwd:
                startActivity(new Intent(this,FindPasswordActivity.class));
				break;
		}
	}
	
	/**
	 * 登陆操作
	 */
	private void method_login(){
		if(type.equals("phone"))
		    dialog=DialogUtil.loadingDialog(this, null, false);
		RequestParams params=new RequestParams();
        final String pwd=edit_pwd.getText().toString().trim();
		params.add("phone", edit_username.getText().toString().trim());
		params.add("password", pwd);
		
		new MHandler(this, FinalVarible.GETURL_LOGIN, params,
				false,user_share, FinalVarible.USERDETAIL, true, false, new MHandler.DataCallBack() {
					
					@Override
					public void returnMessage(Message message) {
						if(dialog!=null)
							dialog.dismiss();
						switch (message.what) {
							case 0:
								Bundle bundle=message.getData();
								if(bundle!=null){
									String dataString=bundle.getString("data");
		                            HelperUtil.saveUserInfo(user_share, pwd,dataString);
						}
								setResult(RESULT_OK);
								finish();
								break;
	
							default:
								break;
						}
						
					}
				});
	}


	@Override
	public void finish() {
		super.finish();
		//AnimationUtil.finishAnimation(this, R.anim.transalte_left_in,R.anim.transalte_out_right);
	}

}
