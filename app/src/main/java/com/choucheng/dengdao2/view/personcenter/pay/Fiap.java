package com.choucheng.dengdao2.view.personcenter.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.tools.Logger;


import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/5/21.
 */
public class Fiap {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    Activity mActivity = null;
    private MyHandler myHandler;


    // ===================================
    // JAVA 的接口
    // ===================================

    public Fiap(Activity activity,boolean isrecharge){
        mActivity = activity;
        myHandler=new MyHandler(mActivity,isrecharge);
    }


    static class MyHandler extends Handler {
        WeakReference<Activity> mActivity;
        boolean isrecharge=false;

        MyHandler(Activity activity,boolean isrecharge) {
            mActivity = new WeakReference<>(activity);
            this.isrecharge=isrecharge;
        }

        @Override
        public void handleMessage(Message msg) {
            Activity theActivity = mActivity.get();
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    A_PayResult APayResult = new A_PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = APayResult.getResult();
                    String resultStatus = APayResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(theActivity, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        if(isrecharge){
                            EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_FRESH_RECHARGE));
                            Logger.i("FIAP", "recharge_suc");
                        }

                        theActivity.finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(theActivity, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(theActivity, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                        EventBus.getDefault().post(new AnyEventType(FinalVarible.TAG_PAY_CANCEL));
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(theActivity, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(final String data) {
      /*  // 订单
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();*/

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
             //   AliPay alipay = new AliPay(mActivity, myHandler);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(data);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
