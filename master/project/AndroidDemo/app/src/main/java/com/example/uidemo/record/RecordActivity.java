package com.example.uidemo.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
import com.example.uidemo.MyApplication;
import com.example.uidemo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecordActivity extends AppCompatActivity {
    private int flag;
    private int child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        MyApplication myapplication=(MyApplication)this.getApplication();
        flag=0;
        child= LoginActivity.currentChildId;
        Log.e("mll","当前孩子的id为"+child);
        //判断用户是否有打卡和测评的记录
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(ConfigUtil.SERVER_ADDR+"/FirstServlet");
                    HttpURLConnection conn= null;
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    OutputStream out=conn.getOutputStream();
                    out.write((""+child).getBytes());
                    out.flush();
                    InputStream in=conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str1 = reader.readLine();
                    if(str1.equals("OK")){
                        flag=1;
                    }else{
                        flag=0;
                    }
                    if(flag==0){
                        Intent intent=new Intent();
                        intent.setClass(getApplicationContext(),NoRecordActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent=new Intent();
                        intent.putExtra("id",child);
                        intent.setClass(getApplicationContext(),GrowthRecordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    reader.close();
                    in.close();
                    out.close();
                } catch (IOException ioException) {

                }
            }
        }.start();
        finish();
    }

}