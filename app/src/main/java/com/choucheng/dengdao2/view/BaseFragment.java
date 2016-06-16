/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.choucheng.dengdao2.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.choucheng.dengdao2.common.CommParam;
import com.choucheng.dengdao2.common.FinalVarible;
import com.choucheng.dengdao2.pojo.AnyEventType;
import com.choucheng.dengdao2.pojo.User;
import com.choucheng.dengdao2.tools.Logger;
import com.choucheng.dengdao2.tools.SharedUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.greenrobot.event.EventBus;


/**
 * @author mrsimple
 */
public class BaseFragment extends Fragment implements View.OnClickListener{
    private static final String TAG="HomeHelper";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onCreateView");
        //注册事件总线
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onDestroy");
        //注销事件总线
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Logger.i(TAG, this.getClass().getSimpleName() + "-------------onClick");
    }

    /**
     * 获取ucode
     * @return
     */
    public String getCode() {
        String code= CommParam.getInstance().getUid();
        if(code==null){
            SharedPreferences use_share=getActivity().getSharedPreferences(FinalVarible.USER_SHARE,0);
            String detail= SharedUtil.getAESInfo(use_share, FinalVarible.OPENID);
            if(!detail.equals("")){
                code=detail;
                CommParam.getInstance().setUid(code);
            }
        }
        return code;
    }
    /**
     * 获取用户信息
     * @return
     */
    public User getUserInfo() {
        User userInfo= CommParam.getInstance().getUser();
        if(userInfo==null){
            SharedPreferences use_share=getActivity().getSharedPreferences(FinalVarible.USER_SHARE,0);
            String detail= SharedUtil.getAESInfo(use_share, FinalVarible.USERDETAIL);
            if(!detail.equals("")){
                try {
                    userInfo = new Gson().fromJson(detail,User.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
               /* if(userInfo!=null){
                    String code=userInfo.getId();
                    CommParam.getInstance().setUid(code);
                    CommParam.getInstance().setUser(userInfo);
                }*/

            }
        }
        return userInfo;
    }

    /**
     * Toast 信息提醒
     * @param showinfo
     */
    public void showToast(String showinfo){
        Toast.makeText(getActivity(), showinfo, Toast.LENGTH_SHORT).show();
    }

    /**
     * 如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，onEventMainThread都会在UI线程中执行，
     * 接收事件就会在UI线程中运行，这个在Android中是非常有用的，因为在Android中只能在UI线程中跟新UI，
     * 所以在onEvnetMainThread方法中是不能执行耗时操作的。
     * @param event
     */
    public void onEventMainThread(AnyEventType event) {
        switch (event.getmMsg()){

        }

    }

}
