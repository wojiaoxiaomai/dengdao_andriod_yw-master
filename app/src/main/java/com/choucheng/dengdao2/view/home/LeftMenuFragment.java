package com.choucheng.dengdao2.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.CommParam;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.definewidget.RoundImageView;
import com.choucheng.dengdao2.pojo.User;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.tools.HelperUtil;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.OnFunctionListener;
import com.choucheng.dengdao2.tools.image.BitmapUtils;
import com.choucheng.dengdao2.view.BaseFragment;
import com.choucheng.dengdao2.view.FindPasswordActivity;
import com.choucheng.dengdao2.view.WebViewActivity;
import com.choucheng.dengdao2.view.personcenter.MyAccountsActivity;
import com.choucheng.dengdao2.view.personcenter.MyOrderActivity;

@SuppressLint("ValidFragment")
public class LeftMenuFragment extends BaseFragment implements OnClickListener{
	private static final String TAG="LeftMenuFragment";
	private Activity mActivity;
	private RoundImageView rv_heardview;
	private TextView tv_sex_age,tv_jiguan,tv_tel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.i(TAG, "oncreate");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainviewView= inflater.inflate(R.layout.fragment_left_menu, null);
		initWidget(mainviewView);
		return mainviewView;
	}

	/**
	 * 初始化控件
	 */
	private void initWidget(View view){
		 rv_heardview=(RoundImageView)view.findViewById(R.id.rv_heardview);
		 tv_sex_age=(TextView)view.findViewById(R.id.tv_sex_age);
		 tv_jiguan=(TextView)view.findViewById(R.id.tv_guanji);
		 tv_tel=(TextView)view.findViewById(R.id.tv_tel);
		 showBasePerInfo();
		 view.findViewById(R.id.tv_myorder).setOnClickListener(this);
		 view.findViewById(R.id.tv_myorder).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
		 view.findViewById(R.id.tv_account).setOnClickListener(this);
		 view.findViewById(R.id.tv_account).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
		 view.findViewById(R.id.tv_mofidypwd).setOnClickListener(this);
		 view.findViewById(R.id.tv_mofidypwd).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
		 view.findViewById(R.id.tv_version).setOnClickListener(this);
		 view.findViewById(R.id.tv_version).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
		 view.findViewById(R.id.tv_question).setOnClickListener(this);
		 view.findViewById(R.id.tv_question).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
		 view.findViewById(R.id.tv_kef).setOnClickListener(this);
		 view.findViewById(R.id.tv_kef).setOnTouchListener(new BitmapUtils.OnTouchListener_view_transparency());
		 view.findViewById(R.id.btn_logout).setOnClickListener(this);
	}


    /**
	 * 展示基本信息
	 */
	private void showBasePerInfo(){
		String sex="",age="",tel="",guanji="";
		tv_sex_age.setText(String.format(getString(R.string.sexandage),sex,age));
		tv_jiguan.setText(String.format(getString(R.string.jiguan),guanji));
		tv_tel.setText(String.format(getString(R.string.per_tel),tel));

	}

	/**
	 * 控件点击事件
	 * @param v 点击的控件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
        switch (v.getId()) {
			case R.id.tv_myorder:
				//我的订单
                 intent=new Intent(getActivity(), MyOrderActivity.class);
				break;
			case R.id.tv_account:
				intent=new Intent(getActivity(), MyAccountsActivity.class);

				break;
			case R.id.tv_mofidypwd:
				intent=new Intent(getActivity(),FindPasswordActivity.class);
				break;
			case R.id.tv_question:
				intent=new Intent(getActivity(),WebViewActivity.class);
				intent.putExtra(FinalVarible.DATA,"常见问题");
				break;
			case R.id.tv_version:

				break;
			case R.id.tv_kef:

				break;
			case R.id.btn_logout:
				//注销
				if(CommParam.getInstance().getUser()!=null){
					new DialogUtil(getActivity()).commonDialog2(2, getString(R.string.prompt),
							getString(R.string.cancel), getString(R.string.sure), null,
							getString(R.string.are_you_sure_to_exit), new DialogUtil.DialogCallBack() {
								@Override
								public void resulthandlerI(int id) {
									if(id==2){
										HelperUtil.logOut(getActivity(), true);
										getActivity().finish();
									}
								}
							});
				}
				break;

		}
		if(intent!=null){
			startActivity(intent);
		}
	}



	
	
	
}
