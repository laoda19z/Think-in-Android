package com.example.uidemo.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;



public class FiftymeterRunBackAcitivity extends AppCompatActivity {

    private JCVideoPlayerStandard playerStandard;
    private TextView tvTime;
    private Button btnStart;
    private Button btnRestart;
    private Button btnNext;
    private Thread thread;
    private String str;
    private int a,b,i;
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    if (msg.arg1 == 0){
                        i = (int) msg.obj;
                        a = i/60;
                        b = i-(60*a);
                        tvTime.setText(a+"'"+b+"''");
                    }else {
                        Log.i("zsd","暂停");
                    }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiftymeter_run_back_acitivity);
        str = getIntent().getStringExtra("json");
        findViews();
        setListener();
        initVideo();

    }

    private void setListener() {
        MyListener listener = new MyListener();
        btnNext.setOnClickListener(listener);
        btnRestart.setOnClickListener(listener);
        btnStart.setOnClickListener(listener);
    }
    class MyListener implements  View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_start:
                    if (btnStart.getText().toString().equals("开始")){
                        startTiming();
                        btnStart.setText("暂停");
                    }else {
                        stopTiming();
                        btnStart.setText("开始");
                    }
                    break;
                case R.id.btn_restart:
                    if (btnStart.getText().toString().equals("暂停")){
                        Toast.makeText(FiftymeterRunBackAcitivity.this, "请先暂停", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        restartTiming();
                    }
                    break;
                case R.id.btn_next:
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                    String s = tvTime.getText().toString();
                    String[] info = s.split("'");
                    int time = Integer.parseInt(info[0])*60+Integer.parseInt(info[1]);
                    jsonObject.addProperty("wsmcbwfp",time+"");
                    Intent intent = new Intent();
                    intent.putExtra("json",jsonObject.toString());
                    intent.setClass(FiftymeterRunBackAcitivity.this,ResultActivity.class);
                    startActivity(intent);
                    finish();
                    playerStandard.release();
                    break;
            }
        }
    }
    private void initVideo() {
        playerStandard = findViewById(R.id.media_video);
        playerStandard.setUp(ConfigUtil.SERVER_ADDR +"/video/wushimichengbawangfanpao.mp4",JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"五十往返跑");
        playerStandard.startVideo();
    }

    private void findViews() {
        tvTime = findViewById(R.id.tv_time);
        btnStart = findViewById(R.id.btn_start);
        btnRestart = findViewById(R.id.btn_restart);
        btnNext = findViewById(R.id.btn_next);
    }
    private void startTiming(){
        thread = new Thread(){
            @Override
            public void run() {
                while (true){
                    String s = tvTime.getText().toString();
                    String[] info = s.split("'");
                    int time = Integer.parseInt(info[0])*60+Integer.parseInt(info[1]);
                    time += 1;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = time;
                    msg.arg1 = 0;
                    handler.sendMessageDelayed(msg,200);
                    if(Thread.currentThread().isInterrupted()){
                        break;
                    }
                }
            }
        };
        thread.start();

    }
    private void stopTiming(){
        thread.interrupt();
        Log.i("zsd","暂停");
    }
    private void restartTiming(){
        tvTime.setText("0'0''");
    }
}