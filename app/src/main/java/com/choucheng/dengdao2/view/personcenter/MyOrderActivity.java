package com.choucheng.dengdao2.view.personcenter;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.MHandler;
import com.choucheng.dengdao2.tools.AnimationUtil;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.view.BaseActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.RequestParams;
import com.simple.commonadapter.RecyclerAdapter;
import com.simple.commonadapter.viewholders.RecyclerViewHolder;

import java.util.ArrayList;

/**我的订单
 * Created by Administrator on 2016/3/12 0012.
 */
public class MyOrderActivity extends BaseActivity implements XRecyclerView.LoadingListener{

    private XRecyclerView listView;
    private int page = 1;
    private ArrayList<Object> datas=new ArrayList<>();
    private RecyclerAdapter<Object> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initHeaderBar() {
        super.initHeaderBar();
        setTitle(getString(R.string.my_order));
    }


    @Override
    public void initWidget() {
        super.initWidget();
        setContentView(R.layout.xrecyclerview_list);
        listView=(XRecyclerView)findViewById(R.id.xrecyclerview);
        adapter=new RecyclerAdapter<Object>(R.layout.item_systemmsg,datas) {
            @Override
            protected void onBindData(RecyclerViewHolder viewHolder, int position, Object item) {

            }
        };
        listView.setAdapter(adapter);
        listView.setLoadingMoreEnabled(true);
        listView.setPullRefreshEnabled(true);
    }

    /**
     * 可以解冻的单身资源列表
     */
    private void method_getList(){
        final Dialog dialog= DialogUtil.loadingDialog(this, null, false);
        final RequestParams params=new RequestParams();
        params.put("ucode", getCode());
        params.put("page", page + "");
        new MHandler(this, FinalVarible.GETURL_SYTEMMSG, params, false,
                null, null, true, false, new MHandler.DataCallBack() {
            @Override
            public void returnMessage(Message message) {
                dialog.dismiss();
                listView.loadMoreComplete();

            }
        });

    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
      page++;
      method_getList();
    }


    @Override
    public void finish() {
        super.finish();
        AnimationUtil.finishAnimation(this,R.anim.keep,R.anim.transalte_out_right);
    }
}
