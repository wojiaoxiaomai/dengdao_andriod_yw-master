package com.choucheng.dengdao2.view.personcenter.pay;

import android.app.Activity;

import com.choucheng.dengdao2.tools.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class W_pay {
	   public  static String APP_ID = "wxe7edcc49d5bd01a1";
	   public IWXAPI msgApi;
	   public static boolean isrecharge=false;
	   
	  public W_pay(Activity activity,boolean isrecharge){
		 	 msgApi = WXAPIFactory.createWXAPI(activity, null);
		 	 this.isrecharge=isrecharge;
	    }
	  
	  public void genPayReq(String data) {
		  Logger.i("w_pay", "genPayReq");
	    	PayReq req=new PayReq(); 
	    	JSONObject jsonObject;
	    	try {
				jsonObject = new JSONObject(data);
				if(jsonObject.has("appid"))
					req.appId = jsonObject.getString("appid");
				else
					req.appId = APP_ID;
				if(jsonObject.has("partnerid"))
				req.partnerId = jsonObject.getString("partnerid");
				if(jsonObject.has("prepayid"))
				req.prepayId = jsonObject.getString("prepayid");
				req.packageValue = "Sign=WXPay";
				if(jsonObject.has("noncestr"))
				req.nonceStr = jsonObject.getString("noncestr");
				if(jsonObject.has("timestamp"))
				req.timeStamp =jsonObject.getString("timestamp");
			
			/*	List<NameValuePair> signParams = new LinkedList<NameValuePair>();
				signParams.add(new BasicNameValuePair("appid", req.appId));
				signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
				signParams.add(new BasicNameValuePair("package", req.packageValue));
				signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
				signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
				signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
				Log.e("orion", signParams.toString());
				req.sign = genAppSign(signParams);*/
				if(jsonObject.has("sign"))
				req.sign =jsonObject.getString("sign");
				
				msgApi.registerApp(APP_ID);
			    msgApi.sendReq(req);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	  
	   /* private String genAppSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append("8578305c08834e9891a55b808ef03f60");
			String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion", appSign);
			return appSign;
		}
	    */


}
