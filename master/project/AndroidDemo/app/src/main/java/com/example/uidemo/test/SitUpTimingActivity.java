package com.example.uidemo.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class SitUpTimingActivity extends AppCompatActivity {

    private JCVideoPlayerStandard playerStandard;
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
                        finish();
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
        initVideo();
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

    private void initVideo() {
        new Thread(){
            @Override
            public void run() {
                playerStandard = findViewById(R.id.media_video);
                playerStandard.setUp(ConfigUtil.SERVER_ADDR +"/video/wushimichengbawangfanpao.mp4",JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"五十往返跑");
                playerStandard.startVideo();
            }
        }.start();

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