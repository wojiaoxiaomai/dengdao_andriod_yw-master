package com.choucheng.dengdao2.view.personcenter;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.common.MHandler;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.tools.DialogUtil;
import com.choucheng.dengdao2.tools.HttpMethodUtil;
import com.choucheng.dengdao2.view.BaseActivity;
import com.choucheng.dengdao2.view.personcenter.pay.Fiap;
import com.choucheng.dengdao2.view.personcenter.pay.W_pay;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 充值
 */
public class MyAccount_RechargeActivity extends BaseActivity {
    private RadioGroup rg_payment;
    private EditText edit_money;

    private int type=2;//2微信,1支付
    private String ordernum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initHeaderBar() {
        super.initHeaderBar();
        setTitle("充值");
    }

    /**
     * 控件初始化
     */
    @Override
    public void initWidget(){
        setContentView(R.layout.activity_my_accounts_recharge);
        rg_payment=(RadioGroup)findViewById(R.id.rg_payment);
        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_wechat:
                        type = 2;
                        break;
                    case R.id.rb_alipay:
                        type = 1;
                        break;
                }
            }
        });
        edit_money=(EditText)findViewById(R.id.edit_money);
        ( (RadioButton)findViewById(R.id.rb_wechat)).setChecked(true);
        findViewById(R.id.btn_nextstep).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ib_back:
                if (!edit_money.getText().toString().equals("")) {
                    new DialogUtil(MyAccount_RechargeActivity.this).commonDialog2(2, null,
                            getString(R.string.cancel), getString(R.string.sure), null,
                            getString(R.string.are_you_sure_to_giveup_to_rechange), new DialogUtil.DialogCallBack() {
                                @Override
                                public void resulthandlerI(int id) {
                                    if (id == 2) {
                                        finish();
                                    }
                                }
                            });
                } else {
                    finish();
                }
                break;
            case R.id.btn_nextstep:
                //下一步
                if(edit_money.getText().toString().trim().equals("")){
                    showShortToast(getString(R.string.recharge_money_hint));
                }else{
                    double money=0;
                    try {
                        money=Double.parseDouble(edit_money.getText().toString().trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if(money<0.01){
                        showShortToast(getString(R.string.recharge_min_money));
                    }else{
                        if(ordernum==null){
                            method_recharge();
                        }else{
                            method_recharge2();
                        }

                    }

                }
                break;
        }
    }

    /**
     * 充值接口
     */
    private void method_recharge(){
       /* final Dialog dialog=DialogUtil.loadingDialog(this,null,false);
        RequestParams params=new RequestParams();
        params.add("ucode", getCode());
        params.add("money", edit_money.getText().toString().trim());
        new MHandler(this, APIConfig.account_recharege1,params,false,
                null,null,true,false,new MHandler.DataCallBack(){

            @Override
            public void returnMessage(Message message) {
                dialog.dismiss();
                switch (message.what){
                    case 0:
                        Bundle bundle=message.getData();
                        if(bundle!=null){
                            String data=bundle.getString("data");
                            if(data!=null&&!data.equals("")){
                                try {
                                    JSONObject jsonObject=new JSONObject(data);
                                    if(jsonObject.has("order_num"))
                                        ordernum=jsonObject.getString("order_num");
                                    if(ordernum!=null){
                                        method_recharge2();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                }
            }
        });*/
    }

    /**
     * 支付订单
     */
    private void method_recharge2(){
       /* final Dialog dialog=DialogUtil.loadingDialog(this,null,false);
        RequestParams params=new RequestParams();
        params.add("ucode", getCode());
        params.add("order_num", ordernum);
        params.add("pay_type",type+"");
        new MHandler(this, APIConfig.account_pay_order,params,false,
                null,null,true,false,new MHandler.DataCallBack(){

            @Override
            public void returnMessage(Message message) {
                dialog.dismiss();
                switch (message.what){
                    case 0:
                        Bundle bundle=message.getData();
                        if(bundle!=null){
                            String data=bundle.getString("data");
                            if(data!=null&&!data.equals("")){
                                switch (type){
                                    case 1:
                                        //支付宝
                                        try {
                                            JSONObject jsonObject=new JSONObject(data);
                                            if(jsonObject.has("sign")){
                                                String signinfo=jsonObject.getString("sign");
                                                new Fiap(MyAccount_RechargeActivity.this,true).pay(signinfo);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                    case 2:
                                        //微信支付
                                        W_pay w_pay=new W_pay(MyAccount_RechargeActivity.this,true);
                                        w_pay.genPayReq(data);
                                        break;
                                }
                            }
                        }
                        break;
                }
            }
        });*/
    }






    /***
     * 返回按钮的监听事件
     */
    @Override
    public void onBackPressed() {
        if(!edit_money.getText().toString().equals("")){
            new DialogUtil(this).commonDialog2(2, null,
                    getString(R.string.cancel), getString(R.string.sure), null,
                    getString(R.string.are_you_sure_to_giveup_to_rechange), new DialogUtil.DialogCallBack() {
                        @Override
                        public void resulthandlerI(int id) {
                            if(id==2){
                                finish();
                            }
                        }
                    });
        }else{
            finish();
        }
    }

    /****
     * 充值刷新
     */
    @Override
    public void onEvent(AnyEventType eventType) {
        super.onEvent(eventType);
        switch (eventType.getmMsg()){
            case  FinalVarible.TAG_FRESH_RECHARGE:
                HttpMethodUtil.method_getUserInfo(this);
                //刷新列表
                finish();
                break;
        }
    }
}
