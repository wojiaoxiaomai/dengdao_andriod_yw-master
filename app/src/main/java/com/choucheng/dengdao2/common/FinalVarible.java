package com.choucheng.dengdao2.common;



public interface FinalVarible {
	String PROJECT_PATH="/Dengdao2";
	String Log_PATH=PROJECT_PATH+"/crashlog/";
	String PIC_PATH=PROJECT_PATH+"/pic/";
	String TAKE_PIC_PATH=PROJECT_PATH+"/photos";
	String WEBCACHE=PROJECT_PATH+"/web";
	 String APK_NAME="";
	
	 String IMAGE_FILE_NAME = "face.png";
	 String RESULT="Result";
	 String SEED = "0123456789ABCDEF";
	
	//translate data
	 String LOADURL="loading_url";//webview loading url
	 String BAR_LEFT="bar_left_text";
	 String BAR_TITLE="bar_title";
	 String ISLOGIN="islogin";
	 String PAGE_TAG="tag";//用来标示我的订单和历史订单的
	 String PARAMS_NAME="param_name";
	 String DATA="data";

	//Subscriber tag
	String TAG_TIMELIMIT="time_limit";
	String TAG_LOGINFRESH="login_suc";//登陆成功
	String TAG_FRESH_RECHARGE="recharege_refresh";
	String TAG_PAY_CANCEL="pay_cancel";
	String TAG_MSG="tag_recMsg";
	String TAG_FRESHORDER="tag_freshorder";
	String TAG_FRESHORDER_DETAIL="tag_fresh_orderdetail";
	String TAG_STOPWARN="tag_stop_warn";//停止响铃的指令
	String TAG_CATEGORY="tag_category";//分类通知
	String TAG_ADD_ADDRESS="tag_fresh_address";//添加地址
	String TAG_SELECT="tag_select_address";//地址选择

	//Handler msg what
	 int FRESH=0x10;
	 int FRESH_AD=0x12;
	 int GETDATA=0x011;
	 int FRESH_TIME=0x13;

	//Subscriber tag
	String TAG_EXIT="exit";


	//interface
	 String URL="";
	 String GETURL_LOGIN="";//登陆
	 String GETURL_REGISTER="";//注册
	 String GETURL_RESETPWD="";//重置密码
	 String GETURL_FINDPWD="";//找回密码
	 String GETURL_GETUSERINFO="";//
	
	 String GETURL_ADORCHECKUPDATE="";//版本更新
	 String GETURL_GETVERIFYCODE="";//获取验证码
	 String GETURL_CHECKVERIFYCODE="";//验证验证码
	
	 String GETURL_GETAD="";//获得广告
	 String GETURL_BANNER="";//binner图

	 String GETURL_ADDRESS_LIST="";//地址列表
	 String GETURL_ADD_ADDRESS="";//地址管理
	 String GETURL_SET_INSTALL="";//设置默认
	 String GETURL_DELET_ADDRESS="";//删除地址

	 String GETURL_SYTEMMSG="";//系统消息

	//拍照
	int IMAGE_REQUEST_CODE = 0;
	int CAMERA_REQUEST_CODE = 1;
	int RESULT_REQUEST_CODE=2;
	
	//缓存信息
	//基础缓存
	String BASE_SHARE="base_share";
		 String INDEX_AD="Index_ad";//首页广告图
         String ISFIRS="is_first";//是否是第一次使用

	 String DISH_ALLLIS="Dish_allList";//菜品信息类别
	 String USER_SHARE="user_share";
	     String MY_ORDERLIST="my_orderlist";
	     String USERDETAIL="user_detail";
	     String CURR_NAME="current_username";
	     String CURR_PWD="current_pwd";
	     String OPENID="current_code";
	     String MY_ADDLIST="my_addlist";//

	
}
