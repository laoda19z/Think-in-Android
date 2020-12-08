package com.example.uidemo.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class FiftymeterRunActivity extends AppCompatActivity {
    private JCVideoPlayerStandard playerStandard;
    private TextView tvTime;
    private Button btnStart;
    private Button btnRestart;
    private Button btnNext;
    private Thread thread;
    private String str;
    private EditText edtData;
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    if (msg.arg1 == 0){
                        float f = (Float) msg.obj;
                        tvTime.setText(f+"");
                    }else {
                        Log.i("zsd","暂停");
                    }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiftymeter_run);
        str = getIntent().getStringExtra("json");
        findViews();
        initVideo();
        setListener();
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
                        Toast.makeText(FiftymeterRunActivity.this, "请先暂停", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        restartTiming();
                    }
                    break;
                case R.id.btn_next:
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                    jsonObject.addProperty("wsmp",tvTime.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("json",jsonObject.toString());
                    intent.setClass(FiftymeterRunActivity.this,SitReachActivity.class);
                    startActivity(intent);
                    finish();
                    playerStandard.release();
                    break;
            }
        }
    }
    private void initVideo() {
        playerStandard = findViewById(R.id.media_video);
        playerStandard.setUp(ConfigUtil.SERVER_ADDR +"video/wushimipao.mp4",0,"五十米跑");
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
                for (int i=0;i<i+1;i++){
                    float f = Float.parseFloat(tvTime.getText().toString());
                    f += 0.1;
                    BigDecimal b  = new BigDecimal(f);
                    float n = b.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = n;
                    msg.arg1 = 0;
                    handler.sendMessageDelayed(msg,88);
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
    }
    private void restartTiming(){
        tvTime.setText("0.0");
    }

}
