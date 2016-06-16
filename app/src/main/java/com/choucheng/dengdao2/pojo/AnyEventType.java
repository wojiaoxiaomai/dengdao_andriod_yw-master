package com.choucheng.dengdao2.pojo;

import android.os.Bundle;

/**
 * Created by zuoyan on 2016/5/3.
 */
public class AnyEventType {
    private String type;
    private Bundle obj;
    public AnyEventType() {
    }

    public AnyEventType(String type) {
        this.type = type;
    }
    public AnyEventType(String type,Bundle bundle) {
        this.type = type;
        this.obj=bundle;
    }
    public String getmMsg() {
        return type;
    }

    public void setmMsg(String mMsg) {
        this.type = mMsg;
    }


    public Bundle getObj() {
        return obj;
    }

    public void setObj(Bundle obj) {
        this.obj = obj;
    }
}
