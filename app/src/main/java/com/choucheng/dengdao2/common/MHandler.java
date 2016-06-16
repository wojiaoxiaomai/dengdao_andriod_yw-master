package com.choucheng.dengdao2.common;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.HelperUtil;
import com.choucheng.dengdao2.tools.HttpUtil2;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.SharedUtil;
import com.choucheng.dengdao2.tools.SystemUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MHandler{
	
     private static final String TAG="MHandler";
	private DataCallBack dataCallBack;
	private Context context;
	private Gson gson;
	private boolean istoast;
	private String cachename;
	private SharedPreferences sp;
	private boolean isreturnalldata;
  
	/***
	 * 与服务器连接操作
	 * @param context
	 * @param methodname 方法名
	 * @param params 参数
	 * @param isreadcache 是否读取缓存
	 * @param sp 缓存目的对象
	 * @param cacheName 缓存名
	 * @param istoast 是否与操作失败的进行toast提示
	 * @param isreturnalldata 是否返回所有的得到的数据
	 * @param dataCallBack 回调函数
	 */
    public MHandler(final Context context,String methodname,final RequestParams params,Boolean isreadcache,
    		final SharedPreferences sp, final String cacheName, Boolean istoast,Boolean isreturnalldata,DataCallBack dataCallBack){
    	Logger.i(TAG, "method:"+methodname);
    	this.dataCallBack = dataCallBack;
		this.context=context;
		this.gson=new Gson();
		this.cachename=cacheName;
		this.istoast=istoast;
		this.sp=sp;
		this.isreturnalldata=isreturnalldata;
        if(!SystemUtil.isNetworkConnected(context)){
            // Toast.makeText(context,R.string.pleasecheckyounetIsok,Toast.LENGTH_SHORT).show();
            Message msg =new Message();
            msg.what=1;
            Bundle bundle=new Bundle();
            bundle.putString("data", null);
            bundle.putString("msg",context.getString(R.string.pleasecheckyounetIsok));
            msg.setData(bundle);
            localHandleMessage(msg);
            return;
        }
    	if(isreadcache&&cacheName != null){
    		//获得缓存资源，如果存在缓存信息，返回缓存信息
    		String cache ;
    		if(cacheName.startsWith("user")){
    			cache=SharedUtil.getAESInfo(sp, cacheName);
    		}else{
    			cache = sp.getString(cacheName, "");
    		}
			if(!cache.equals("")&&!cache.equals("null")){
				Message msg =new Message();
				msg.what=0;
				Bundle bundle=new Bundle();
				bundle.putString("data", cache);
				msg.setData(bundle);
				localHandleMessage(msg);
			}
		}
    	String url=FinalVarible.URL+methodname;
    	if(params==null){
    		HttpUtil2.post(url, responseHandler);
    	}else{
    		HttpUtil2.post(url, params, responseHandler);
    	}  	
    }
    
    
    AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){
    	
    	 @Override
    	 public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
    		 Message msg = handlResult("");
			 localHandleMessage(msg);
    	 }
    	

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			 Message msg = handlResult(new String(arg2));
		     localHandleMessage(msg);
			
		};
    };
    
    
 

   /**
    * 获得的结果处理
    * @param result 服务器返回的结果
    */
    public Message handlResult(String result){
        Logger.i(TAG, "result:"+result);
        Message msg =new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data", "");
        bundle.putString("msg", "");
        msg.what=10006;
        if(!result.equals("")){
            try {
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.has("status")){
                    JSONObject statusobj=jsonObject.getJSONObject("status");
                    msg.what=statusobj.getInt("code");
                    bundle.putString("msg", statusobj.getString("msg"));
                }
                //data
                String contentString="";
                if(isreturnalldata){
                    contentString=result;
                }else{
                    JSONObject temp = new JSONObject(result);
                    if(temp.has("data")){
                        Object jObj =temp.get("data");
                        contentString=jObj.toString();
                    }
                    if(temp.has("paging")){
                        bundle.putString("page", temp.getString("paging"));
                    }
                }
                bundle.putString("data", contentString);
                if(cachename != null){
                    if(msg.what==0){
                        if(contentString!=null&&!contentString.equals("null")&&!contentString.equals("")){
                            if(cachename.startsWith("user")){
                                SharedUtil.commitAESInfo(sp, cachename, contentString);
                            }else{
                                SharedUtil.commitInfo(sp, cachename, contentString);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            msg.what= R.string.handlefail;
            bundle.putString("msg", context.getString(R.string.handlefail));
        }
        msg.setData(bundle);
        return msg;
    }
 
    /**
	 * 本地消息处理
	 * @param msg
	 */
    public void localHandleMessage(Message msg){
        Bundle bundle=msg.getData();
        if(bundle!=null){
            switch (msg.what) {
                case 0:
                    break;
                case 10001:
                    String infos2=bundle.getString("msg");
                    if(infos2!=null&&!infos2.equals("")&&!infos2.equals("null")){
                        Toast.makeText(context,infos2,Toast.LENGTH_SHORT).show();
                    }
                    HelperUtil.logOut(context, false);
                    break;
                default:
                    if(istoast){
                        String infos=bundle.getString("msg");
                        if(infos==null||infos.equals("")||infos.equals("null")){
                            infos=context.getString(R.string.handlefail);
                        }
                        Toast.makeText(context,infos,Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
        dataCallBack.returnMessage(msg);
    }
    

	public interface DataCallBack{
		public void returnMessage(Message message);
	}
	
}
