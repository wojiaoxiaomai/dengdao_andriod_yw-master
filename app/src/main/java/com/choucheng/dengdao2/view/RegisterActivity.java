package com.choucheng.dengdao2.view;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.MHandler;
import com.choucheng.dengdao2.common.Regs;
import com.choucheng.dengdao2.definewidget.RoundImageView;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.tools.AnimationUtil;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.tools.FileUtil;
import com.choucheng.dengdao2.tools.HttpMethodUtil;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.MyCount;
import com.choucheng.dengdao2.tools.SharedUtil;
import com.choucheng.dengdao2.tools.SystemUtil;
import com.choucheng.dengdao2.tools.Validator;
import com.choucheng.dengdao2.tools.image.BitmapUtils;
import com.choucheng.dengdao2.view.adapter.AllViewPagerAdapter;
import com.loopj.android.http.RequestParams;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private static final String TAG="RegisterActivity";
	private ViewPager viewPager;
    private List<View> views=new ArrayList<View>();
	private EditText edit_phonenum,edit_verifycode,edit_pwd,edit_pwd2,edit_IDCardNum;
	private TextView tv_getverifycode;
	private RoundImageView rv_heardview;
	private CheckBox check_xieyi;
	private int currentindex=0;
	private String tel;
	private MyCount myCount;
	private SharedPreferences user_share;
	private List<List<String>> selectdate;
	private String picname;
	private File tempFile;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	/**
	 * 初始化标题栏
	 */
	public void initHeaderBar(){
		super.initHeaderBar();
		setTitle(R.string.register_title);
	}

	/**
	 * 初始化控件
	 */
	public void initWidget(){
		setContentView(R.layout.activity_register);
		viewPager=(ViewPager)findViewById(R.id.viewpager_register);
		LayoutInflater inflater= LayoutInflater.from(this);
    	View step1_view= inflater.inflate(R.layout.activity_register_page1,null);
    	initpage1(step1_view);
    	views.add(step1_view);
    	AllViewPagerAdapter adapter=new AllViewPagerAdapter(this, views);
    	viewPager.setAdapter(adapter);
    	viewPager.setCurrentItem(currentindex);//显示第一页
	}
	
	/**
	 * 初始化第一页
	 * @param view
	 */
	private void initpage1(View view){
		edit_phonenum=(EditText)view.findViewById(R.id.edit_phoneNum);
		edit_verifycode=(EditText)view.findViewById(R.id.edit_verifycode);
		edit_IDCardNum=(EditText)view.findViewById(R.id.edit_IDCardNum);
		tv_getverifycode=(TextView)view.findViewById(R.id.btn_getverifycode);
		tv_getverifycode.setOnClickListener(this);
		user_share=getSharedPreferences(FinalVarible.USER_SHARE, 1);
		edit_pwd=(EditText)view.findViewById(R.id.edit_pwd);
		edit_pwd2=(EditText)view.findViewById(R.id.edit_pwd_repeat);
		rv_heardview=(RoundImageView)view.findViewById(R.id.rv_heardview);
		rv_heardview.setOnClickListener(this);

		check_xieyi=(CheckBox)view.findViewById(R.id.checkbox_agrrensug);
		view.findViewById(R.id.btn_register).setOnClickListener(this);
		view.findViewById(R.id.tv_rules).setOnClickListener(this);
	}

	
	/**
	 * 输入框的检测
	 * @return
	 */
	private boolean checkALL_step1(){
		if(Validator.checkIsNull(this, edit_phonenum, getString(R.string.phonenum))){
			return false;
		}else{
			if(Validator.checkIsCorrect(this, edit_phonenum, Regs.sjhmReg, getString(R.string.phonenum))){
				return true;
			}
			return false;
		}
	}
	
	/**
	 * 输入框的检测
	 * @return
	 */
	private boolean checkALL_step2(){
		if(Validator.checkIsNull(this, edit_phonenum, getString(R.string.phonenum))||
				Validator.checkIsNull(this,edit_pwd, getString(R.string.pwd))||
				Validator.checkIsNull(this,edit_pwd2,getString(R.string.pwdrepeat))||
				Validator.checkIsNull(this,edit_verifycode,getString(R.string.verifycode))
				//||Validator.checkIsNull(this,edit_nickname,getString(R.string.nickname))
				){
			return false;
		}else{
			if(Validator.checkIsCorrect(this,edit_phonenum,Regs.sjhmReg,getString(R.string.phonenum))
					&&Validator.checkIsCorrect(this, edit_pwd, Regs.passwordReg, getString(R.string.pwd))) {
				if(!edit_pwd.getText().toString().trim().equals(edit_pwd2.getText().toString().trim())){
					showShortToast(getString(R.string.pwddifferent));
				}else{
					if(check_xieyi.isChecked()){
						return true;
					}else{
						showShortToast(getString(R.string.xieyi_prompt));
					}
				}
			}
			return false;
		}
	}

	/**
	 * 注册接口
	 */
	private void method_step3(){
		final Dialog dialog= DialogUtil.loadingDialog(this, null, false);
		RequestParams params=new RequestParams();
		params.put("phoneNum", tel);
		params.put("phoneCode", edit_verifycode.getText().toString().trim());
		String pwd=edit_pwd.getText().toString().trim();
		params.put("password", pwd);
		params.put("file","");

		new MHandler(this, FinalVarible.GETURL_REGISTER, params, false,
				null, null, true, false, new MHandler.DataCallBack() {
			@Override
			public void returnMessage(Message message) {
			    dialog.dismiss();
				switch (message.what){
					case 0:
						Bundle bundle=message.getData();
						if(bundle!=null){
							String datainfo=bundle.getString("data");
							try {
								JSONObject jsonObject=new JSONObject(datainfo);
								if(jsonObject.has("openId")){
									String openId=jsonObject.getString("openId");
									//SharedUtil.commitAESInfo(user_share, FinalVarible.OPENID, openId);
								}
								if(jsonObject.has("user")){
									String userinfo=jsonObject.get("user").toString();
									if(userinfo!=null&&!userinfo.equals("")){
										//HelperUtil.saveUserInfo(user_share, edit_pwd.getText().toString().trim(), userinfo);
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						setResult(RESULT_OK);
						finish();
						break;
				}
			}
		});
	}
	

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
			case R.id.ib_back:
				//标题栏左侧按钮,登录
				if(currentindex==0){
					finish();
				}
				break;
			case R.id.btn_getverifycode:
				//重新获取验证码
				if(checkALL_step1()){
					tel=edit_phonenum.getText().toString().trim();
					HttpMethodUtil.method_getverifyCode(this, tel, v.getTag());
				}
				break;
			case R.id.btn_register:
				//注册，注册成功后默认为已登陆状态
				if(checkALL_step2()){
					tel=edit_phonenum.getText().toString().trim();
					if(tel.equals(edit_phonenum.getText().toString().trim())){
						SharedUtil.clearInfo(user_share);
					method_step3();
					}else{
						showShortToast(getString(R.string.phome_diff));
					}
				}
				break;
			 case R.id.tv_rules:
			// case R.id.checkbox_agrrensug:
				 Intent intent=new Intent(this,WebViewActivity.class);
				 intent.putExtra(FinalVarible.DATA,getString(R.string.register_xieyi));
				 intent.putExtra(FinalVarible.LOADURL,FinalVarible.URL);
				 AnimationUtil.startAnimation(this, intent, R.anim.transalte_right_in, R.anim.keep);
				 break;
			 case R.id.rv_heardview:
				 picname = FinalVarible.IMAGE_FILE_NAME;
				 new DialogUtil(this).getpicDialog(this, picname);
				 break;
			default:
				break;
		}
	}




	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		Logger.i(TAG, "onActivityResult");
		if(resultcode!=RESULT_CANCELED) {
			switch (requestcode) {
				case FinalVarible.IMAGE_REQUEST_CODE:
					if (SystemUtil.hasSdcard(this)) {
						tempFile = new File(FileUtil.getTakepicPath(),picname);
						Uri uri = data.getData();
						UCrop.of(uri, Uri.fromFile(tempFile))
								.withAspectRatio(1, 1)
								.withMaxResultSize(500, 500)
								.start(this);
					} else {
						showShortToast(getString(R.string.nosdcartosavePic));
					}
					break;
				case FinalVarible.CAMERA_REQUEST_CODE:
					if (SystemUtil.hasSdcard(this)) {
						tempFile = new File(FileUtil.getTakepicPath(),picname);
						UCrop.of(Uri.fromFile(tempFile),  Uri.fromFile(tempFile))
								.withAspectRatio(1, 1)
								.withMaxResultSize(500, 500)
								.start(this);
					} else {
						showShortToast( getString(R.string.nosdcartosavePic));
					}

					break;
				case UCrop.REQUEST_CROP:
					setImageToView(data);
					break;
			}
		}
	}

	/**
	 * 保存裁剪之后的图片数据 ,显示图片，
	 */
	private void setImageToView(Intent data) {
		if (tempFile != null) {
			rv_heardview.setImageURI(Uri.fromFile(tempFile));
		}
	}

	@Override
	public void onBackPressed() {
		if(currentindex==0){
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(myCount!=null){
			myCount.cancel();
		}
	}


	@Override
	public void onEventMainThread(AnyEventType event) {
		super.onEventMainThread(event);
		switch (event.getmMsg()){
			case FinalVarible.TAG_TIMELIMIT:
				//获取验证码成功之后，时间倒计时
				tv_getverifycode.setClickable(false);
				tv_getverifycode.setTextColor(Color.GRAY);
				tv_getverifycode.setBackgroundResource(android.R.color.transparent);
				myCount=new MyCount(RegisterActivity.this,60*1000,1000,tv_getverifycode){
					@Override
					public void onFinish() {
						super.onFinish();
						tv_getverifycode.setTextColor(getResources().getColor(R.color.themecolor_text));
						tv_getverifycode.setTag(1);
					}
				};
				myCount.start();
				break;
		}
	}





	@Override
	public void finish() {
		super.finish();
		AnimationUtil.finishAnimation(this, R.anim.transalte_left_in, R.anim.transalte_out_right);
	}
}
