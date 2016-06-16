package net.sourceforge.simcpux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.choucheng.dengdao2.view.personcenter.pay.W_pay;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		api.registerApp( W_pay.APP_ID);
	}
}
