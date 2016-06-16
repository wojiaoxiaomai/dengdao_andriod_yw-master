package com.choucheng.dengdao2.view.personcenter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.choucheng.dengdao2.R;
import com.choucheng.dengdao2.tools.Validator;
import com.choucheng.dengdao2.view.BaseActivity;


/**
 * 提现
 */
public class MyAccount_TX2Activity extends BaseActivity {
    private EditText edit_account,edit_truename,edit_value;
    private float mymoney=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initHeaderBar() {
        super.initHeaderBar();
        setTitle("提现");
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setContentView(R.layout.activity_my_accounts_tx2);
        findViewById(R.id.btn_sure).setOnClickListener(this);
        edit_account=(EditText)findViewById(R.id.edit_account);
        edit_truename=(EditText)findViewById(R.id.edit_truename);
        edit_value=(EditText)findViewById(R.id.edit_value);
        edit_value.addTextChangedListener(mytextWatcher);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_sure:
                //确定按钮
                if(!Validator.checkIsNull(this,edit_account,getString(R.string.alipay_account))&&
                        !Validator.checkIsNull(this,edit_truename,getString(R.string.true_name))&&
                        !Validator.checkIsNull(this,edit_value,getString(R.string.tixian_money))){
                    method_tixian();
                }
                break;
        }
    }

    /**
     * 提现
     */
    private void method_tixian(){
       /* final Dialog dialog= DialogUtil.loadingDialog(this, null, false);
        RequestParams params=new RequestParams();
        params.put("ucode", getCode());
        params.put("money", edit_value.getText().toString().trim());
        params.put("pay_num",edit_account.getText().toString().trim());
        params.put("pay_account","支付宝");
        new MHandler(this, APIConfig.account_tixian, params, false,
                null, null, true, false, new MHandler.DataCallBack() {
            @Override
            public void returnMessage(Message message) {
                dialog.dismiss();
                switch (message.what){
                    case 0:
                        Bundle bundle=message.getData();
                        String datastring=bundle.getString("data");
                        if(datastring!=null){
                            new DialogUtil(MyAccount_TX2Activity.this).commonDialog2(2, null,
                                    null, getString(R.string.sure), null,
                                    getString(R.string.account_tixian_suc), new DialogUtil.DialogCallBack() {
                                        @Override
                                        public void resulthandlerI(int id) {
                                            if(id==2){
                                                HttpMethodUtil.method_getUserInfo(MyAccount_TX2Activity.this);
                                                destroyGroup("tixian");
                                            }
                                        }
                                    });
                        }
                        break;
                    default:
                        break;
                }
            }
        });*/
    }



    TextWatcher mytextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
             if(!s.toString().equals("")){
                 float money=Float.parseFloat(s.toString());
                 if(money>mymoney){
                     showShortToast(getString(R.string.tixian_money_wrong_prompt));
                     edit_value.setText(0+"");
                 }
             }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
