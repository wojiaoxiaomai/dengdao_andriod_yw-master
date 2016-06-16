package com.choucheng.dengdao2.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;


public class WebViewActivity extends BaseActivity implements OnClickListener {
	private WebView webView;
	private ProgressBar pb;
	private String loadurl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initHeaderBar() {
		super.initHeaderBar();
		setTitle(R.string.register);
		Intent recintent=getIntent();
		if(recintent.hasExtra(FinalVarible.DATA)){
			setTitle(recintent.getStringExtra(FinalVarible.DATA));
		}
	}

	/**
	 * 初始化控件
	 */
	public void initWidget(){
		setContentView(R.layout.activity_webview);
		webView = (WebView)findViewById(R.id.webview);
		pb=(ProgressBar)findViewById(R.id.pb);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
					//调用拨号程序
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
					return true;
				} else {
					//loadurl=url;
					//  view.loadUrl(url);
				}

				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				pb.setVisibility(View.VISIBLE);
			}


			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pb.setVisibility(View.GONE);
			}
		});
        webView.setWebChromeClient(new WebChromeViewClient());
        method_getContent("1");

	}



	/**
	 * 获取关于我们与服务协议内容
	 */
	private void method_getContent(String tag){
		/*final Dialog dialog=DialogUtil.loadingDialog(this,null,false);
		RequestParams params=new RequestParams();
		params.add("content_id", tag);
		SharedPreferences base_share=getSharedPreferences(FinalVarible.BASE_SHARE,0);
		new MHandler(this, FinalVarible.GETURL_CONTENT, params,
				true, base_share,  "list_"+tag,
				false, false, new MHandler.DataCallBack() {

			@Override
			public void returnMessage(Message message) {
				dialog.dismiss();
				switch (message.what) {
					case 0:
						Bundle bundle=message.getData();
						String datas=bundle.getString("data");
						if(datas!=null&&!datas.equals("")) {
							Content content=new Gson().fromJson(datas,Content.class);
							if(content!=null&&content.texts!=null){
								//webView.loadData(content.texts,"text/html","utf-8");
								webView.loadDataWithBaseURL("", content.texts, "text/html", "utf-8", "");
							}
						}
						break;

					default:
						break;
				}

			}
		});*/
	}

	class Content {
		public String id;
		public String title;
		public String texts;
	}



	
	private class WebChromeViewClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			return true;
		}
		@Override
		public boolean onJsBeforeUnload(WebView view, String url, String message,
		JsResult result) {
			return super.onJsBeforeUnload(view, url, message, result);
		}	
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
		JsResult result) {
			return super.onJsConfirm(view, url, message, result);
		}	
		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
		String defaultValue, JsPromptResult result) {
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}
		@Override
		public boolean onJsTimeout() {
			return super.onJsTimeout();	
		}
				
	    @Override
	    public void onProgressChanged(WebView view, int newProgress) {
	        pb.setProgress(newProgress);  
	        if(newProgress==100){  
	            pb.setVisibility(View.GONE);
	        }  
	        super.onProgressChanged(view, newProgress);  
	    }
	    @Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
		}
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}
		@Override
		public void onRequestFocus(WebView view) {
			super.onRequestFocus(view);	
		}
    
	} 
	
	/**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            // 返回键退回
        	webView.goBack(); //向后
        	//webView.goForward();向前
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
		case R.id.ib_back:
			if(webView.canGoBack()){
				webView.goBack(); //向后
			}else{
				finish();
			}
			break;
		default:
			break;
		}		
	}
	
	
    
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
    
	
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    
}
