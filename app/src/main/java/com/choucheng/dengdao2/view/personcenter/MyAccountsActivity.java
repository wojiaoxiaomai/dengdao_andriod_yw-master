package com.choucheng.dengdao2.view.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.CommParam;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.definewidget.RefreshListView;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.pojo.User;
import com.choucheng.dengdao2.tools.HttpMethodUtil;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.view.BaseActivity;
import com.simple.commonadapter.ListViewAdapter;
import com.simple.commonadapter.viewholders.GodViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 *我的账户
 */
public class MyAccountsActivity extends BaseActivity implements RefreshListView.IOnRefreshListener,RefreshListView.IOnLoadMoreListener{
    private TextView tv_money,tv_month_money,tv_total_money;
    private RefreshListView list;


    private List<AccountRecord> datas=new ArrayList<>();
    private ListViewAdapter<AccountRecord> adapter;

    private User userInfo;
    private int  currentpage=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initHeaderBar() {
        super.initHeaderBar();
        setTitle("我的账户");
    }



    /**
     * 初始化控件
     */
    @Override
    public void initWidget(){
        setContentView(R.layout.activity_my_accounts);
        tv_money=(TextView)findViewById(R.id.tv_money);
        tv_month_money=(TextView)findViewById(R.id.tv_moth_money);
        tv_total_money=(TextView)findViewById(R.id.tv_total_money);

        list=(RefreshListView)findViewById(R.id.listview);
        adapter=new ListViewAdapter<AccountRecord>(R.layout.item_my_income) {


            @Override
            protected void onBindData(GodViewHolder viewHolder, int position, AccountRecord item) {

            }
        };
        list.setAdapter(adapter);
        list.setOnLoadMoreListener(this);
        list.setOnRefreshListener(this);
        list.setDividerHeight(1);
        method_getList();
        HttpMethodUtil.method_getUserInfo(this);

        findViewById(R.id.btn_recharge).setOnClickListener(this);
        findViewById(R.id.btn_tixian).setOnClickListener(this);
        showBaseInfo();
    }

    /**
     * 获取消费流水
     */
    private void method_getList(){
       /* final Dialog dialog= DialogUtil.loadingDialog(this, null, false);
        RequestParams params=new RequestParams();
        params.put("ucode", getCode());
        params.put("page", currentpage + "");
        new MHandler(this, APIConfig.account_turnoverlist, params, false,
                null, null, true, false, new MHandler.DataCallBack() {
            @Override
            public void returnMessage(Message message) {
                dialog.dismiss();
                list.onRefreshComplete();
                switch (message.what){
                    case 0:
                        Bundle bundle=message.getData();
                        String datastring=bundle.getString("data");
                        if(datastring!=null){
                            if(!datastring.equals("")){
                                ArrayList<AccountRecord> listData= new Gson().fromJson(datastring, new TypeToken<List<AccountRecord>>() {
                                }.getType());
                                if(listData!=null){
                                    installData(listData);
                                }
                            }
                        }
                        if(bundle.containsKey("page")){
                            PageInfo pageInfo=new Gson().fromJson(bundle.getString("page"),PageInfo.class);
                            if(pageInfo.getPage()*pageInfo.getNumberofpage()>=pageInfo.getTotalcount()){
                                list.onLoadMoreComplete(true);
                                list.removeFootView();
                            }else{
                                list.onLoadMoreComplete(false);
                            }
                        }
                        break;
                    default:
                        if(currentpage>1)
                            currentpage--;
                        list.onLoadMoreComplete(false);
                        break;
                }
            }
        });*/

    }

    /**
    * 组装数据
    */
    private void installData(final List<AccountRecord> olddata){
        if (olddata != null && olddata.size() > 0) {
            if (currentpage!=1) {
                datas.addAll(olddata);
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        int positon =datas.size()-olddata.size();
                        list.setSelection(positon);  //selection方法必须在post中执行，否则在很多时候该方法将执行无效
                    }
                });
            } else {
                datas.clear();
                datas.addAll(olddata);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 展示信息
     */
    private void showBaseInfo(){
      userInfo= CommParam.getInstance().getUser();
      if(userInfo!=null){

      }
    }





    @Override
    public void OnLoadMore() {
        //加载更多
        currentpage++;
        method_getList();
    }


    @Override
    public void onEvent(AnyEventType eventType) {
        super.onEvent(eventType);
        switch (eventType.getmMsg()){
            case FinalVarible.TAG_FRESH_RECHARGE:
                currentpage=1;
                //刷新列表
                method_getList();
                break;
            case FinalVarible.TAG_LOGINFRESH:
                Logger.i("TAG", "msg_fresh");
                showBaseInfo();
                method_getList();
                break;
        }
    }


    @Override
    public void OnRefresh() {
        method_getList();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent=null;
        switch (v.getId()){
            case R.id.btn_tixian:
                intent=new Intent(this, MyAccount_TX2Activity.class);
                break;
            case R.id.btn_recharge:
                intent=new Intent(this,MyAccount_RechargeActivity.class);
                break;

        }
        if(intent!=null){
            startActivity(intent);
        }
    }
}
