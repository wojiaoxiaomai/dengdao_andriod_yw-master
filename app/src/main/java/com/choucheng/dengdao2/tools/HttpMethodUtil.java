package com.choucheng.dengdao2.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.CommParam;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.MHandler;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.pojo.AppVersion;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;


public class HttpMethodUtil {
	private static final String TAG="HttpMethodUtil";


	/**
	 * 获取验证码
	 */
	public static void method_getverifyCode(Context context,String tel,Object obj){
		final Dialog dialog=DialogUtil.loadingDialog(context,null,false);
		RequestParams params=new RequestParams();
        params.add("phoneNum", tel);
		String method= FinalVarible.GETURL_GETVERIFYCODE;
		new MHandler(context, method, params, false,
				null, null, true, false, new MHandler.DataCallBack() {
			@Override
			public void returnMessage(Message message) {
				dialog.dismiss();
				switch (message.what){
					case 0:
						EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_TIMELIMIT));
						break;
				}
			}
		});
	}


	/**
	 * 获取用户基本信息
	 * @param context
	 */
	public static void method_getUserInfo(final Context context){
		RequestParams params=new RequestParams();
		SharedPreferences user_share = context.getSharedPreferences(FinalVarible.USER_SHARE, 0);
		params.add("open_id", CommParam.getInstance().getUid());
		params.setHttpEntityIsRepeatable(true);
		new MHandler(context, FinalVarible.GETURL_GETUSERINFO, params, false,
				user_share, FinalVarible.USERDETAIL, false, false, new MHandler.DataCallBack() {
			@Override
			public void returnMessage(Message message) {
				switch (message.what){
					case 0:
						Bundle bundle=message.getData();
						if(bundle!=null){
							String data=bundle.getString("data");
							if(!data.equals("")){
								SharedPreferences user_share=context.getSharedPreferences(FinalVarible.USER_SHARE, 0);
								HelperUtil.saveUserInfo(user_share, "", data);
							}
						}
						break;
				}
			}
		});
	}


	/**
	 * 检测新版本
	 */
	public static void checkNewApp(final Context context, final Handler handler, RequestParams params){
		new MHandler(context, FinalVarible.GETURL_ADORCHECKUPDATE, params,
				false, null, null, false, false, new MHandler.DataCallBack() {

			@Override
			public void returnMessage(Message message) {
				switch (message.what) {
					case 1:
						Bundle bundle=message.getData();
						if(bundle!=null){
							String dataString=bundle.getString("data");
							if(dataString!=null&&!dataString.equals("")
									&&!dataString.equals("null")){
								AppVersion appAndAd=new Gson().fromJson(dataString, AppVersion.class);
								if(appAndAd!=null){
									switch (appAndAd.getHasNewVersion()) {
										case 1:
											showUpdateDialog(context,appAndAd.getUrl(),appAndAd.getUpdateType());
											break;

										default:
											if(handler!=null)
												handler.sendEmptyMessage(FinalVarible.FRESH);
											break;
									}
								}

							}
						}
						break;

					default:
						if(handler!=null)
							handler.sendEmptyMessage(FinalVarible.FRESH);
						break;
				}
			}
		});
	}



	/**
	 * 信息比较
	 */
	private static void compareInfo(Context context,AppVersion version,boolean isshowToast){
		//比较版本信息
		boolean isupdate=false;
		String local_appversion=SystemUtil.getVersionName(context).trim();
		String receive_appversion=TextUtils.setStringArgument(version.getCurrVersion()).trim();
		if(!local_appversion.equals(receive_appversion)){
           /* if(isshowToast){
                new ToastView(context).initToast(R.string.this_is_lastversion, Toast.LENGTH_SHORT);
            }
        }else{*/
			String[] localNum=local_appversion.split("[^a-zA-Z0-9]+");
			String[] recevieNum=receive_appversion.split("[^a-zA-Z0-9]+");
			int local_num,rece_num;
			for(int i=0;i<recevieNum.length;i++){
				local_num=Integer.parseInt(localNum[i].trim());
				rece_num=Integer.parseInt(recevieNum[i].trim());
				if(rece_num>local_num){
					isupdate=true;
					break;
				}else{
					if(rece_num<local_num){
						isupdate=false;
						break;
					}
				}
			}
		}
		Bundle bundle=new Bundle();
		if(isupdate){
			bundle.putBoolean("isnew",true);
			if(isshowToast){
				showUpdateDialog(context,version.getUrl(),1);
			}else{
				bundle.putSerializable("app",version);
				EventBus.getDefault().post(new AnyEventType("appversion",bundle));
			}

		}else{
			bundle.putBoolean("isnew",false);
			EventBus.getDefault().post(new AnyEventType("appversion",bundle));
		}
	}


	/**
	 * 提示升级对话框
	 * @param url
	 */
	private  static void showUpdateDialog(Context context,final String url,final int updateType) {
		new DialogUtil(context).commonDialog2(2, "",
				context.getString(R.string.cancel), context.getString(R.string.sure), "",
				context.getString(R.string.get_newVersion), new DialogUtil.DialogCallBack() {

					@Override
					public void resulthandlerI(int dialogid) {
						switch (dialogid) {
							case 2:

								break;

							default:


								break;
						}
					}
				});

	}




}
