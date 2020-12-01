package com.example.uidemo;

import android.app.Application;

import com.xuexiang.xui.XUI;

public class MyApplication extends Application {
    private int userid;
    public int getUserid(){
        return  userid;
    }
    public void setUserid(int id){
        userid=id;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this);

    }
}
