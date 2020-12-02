package com.example.uidemo.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.uidemo.R;

public class SitUpTimingActivity extends AppCompatActivity {
    private TextView tvTime;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String time = (String) msg.obj;
                    if (time.equals("-1")){
                        Intent intent = new Intent();
                        String str = getIntent().getStringExtra("json");
                        intent.putExtra("json",str);
                        intent.setClass(SitUpTimingActivity.this,SitUpInfoActivity.class);
                        startActivity(intent);
                    }else {
                        tvTime.setText((String) msg.obj);
                        beginTiming(Integer.parseInt(time));
                    }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_up_timing);
        findViews();
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginTiming(60);
            }
        });
    }
    private void findViews() {
        tvTime = findViewById(R.id.tv_time);
    }

    public void beginTiming(int time){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = time-1;
                Message msg = new Message();
                msg.what = 1;
                msg.obj = i+"";
                handler.sendMessage(msg);
            }
        }.start();
    }
}