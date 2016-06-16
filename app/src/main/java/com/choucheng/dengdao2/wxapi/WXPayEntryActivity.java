package com.choucheng.dengdao2.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.view.BaseActivity;
import com.choucheng.dengdao2.view.personcenter.pay.W_pay;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;
	private TextView tv_result;
	private int result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, W_pay.APP_ID);
		setTitle("支付结果");
		tv_result=(TextView)findViewById(R.id.tv_result);
		findViewById(R.id.btn_sure).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleresult();

			}
		});
		api.handleIntent(getIntent(), this);

	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.ib_back:
				handleresult();
				break;
		}
	}

	private void handleresult(){
		if(result==0){
    		if(W_pay.isrecharge){
				 EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_FRESH_RECHARGE));
				 Logger.i("FIAP", "recharge_suc");
    		}else{
				//EventBus.getDefault().post("suc", FinalVarible.TAG_REFRESH_RESOURCE);
			}
		}else{
			//支付失败或者取消
			EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_PAY_CANCEL));
		}

		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			result=resp.errCode;
			switch (resp.errCode) {
				case 0:
					//支付成功
					tv_result.setText("支付成功");
					break;
				case -1:
					tv_result.setText("支付失败");
					break;
				case -2:
					tv_result.setText("取消支付");
					break;
				default:
					tv_result.setText("支付失败");
					break;
			}
		}
	}
}