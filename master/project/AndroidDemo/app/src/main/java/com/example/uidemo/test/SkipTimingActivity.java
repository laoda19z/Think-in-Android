package com.example.uidemo.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class SkipTimingActivity extends AppCompatActivity {
    private JCVideoPlayerStandard playerStandard;
    private TextView tvData;
    private Button btnAdd;
    private Button btnMinus;
    private Button btnNext;
    private TextView tvTime;
    private JsonObject jsonObject;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    tvTime.setClickable(false);
                    String time = (String) msg.obj;
                    if (time.equals("-1")){
                        tvTime.setClickable(true);
                        tvTime.setText("60");
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
        setContentView(R.layout.activity_skip_timing);
        String str = getIntent().getStringExtra("json");
        jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
        findViews();
        setListener();
        initVideo();
    }
    private void setListener() {
        MyListener listener = new MyListener();
        btnNext.setOnClickListener(listener);
        btnMinus.setOnClickListener(listener);
        btnAdd.setOnClickListener(listener);
        tvTime.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_add:
                    addNum();
                    break;
                case R.id.btn_minus:
                    minusNum();
                    break;
                case R.id.btn_next:
                    String grade = jsonObject.get("nj").getAsString();
                    if (grade.equals(("1")) || grade.equals("2")) {
                        btnNext.setText("生成报告");
                        nextItem(jsonObject);
                    }else {
                        btnNext.setText("下一步");
                        nextItem(jsonObject);
                    }
                    break;
                case R.id.tv_time:
                    beginTiming(60);
                    break;
            }
        }
    }
    private void initVideo() {
        playerStandard = findViewById(R.id.media_video);
        playerStandard.setUp(ConfigUtil.SERVER_ADDR +"/video/yifenzhongtiaosheng.mp4",JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"一分钟跳绳");
        playerStandard.startVideo();
    }
    private void findViews() {
        tvData = findViewById(R.id.tv_data);
        tvTime = findViewById(R.id.tv_time);
        btnAdd = findViewById(R.id.btn_add);
        btnMinus = findViewById(R.id.btn_minus);
        btnNext = findViewById(R.id.btn_next);
    }

    public void beginTiming(int time){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(1000);
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
    private void addNum() {
        int i = Integer.parseInt(tvData.getText().toString());
        i++;
        tvData.setText(i+"");
    }

    private void minusNum() {
        int i = Integer.parseInt(tvData.getText().toString());
        i--;
        tvData.setText(i+"");
    }

    private void nextItem(JsonObject jo) {
        Intent intent = new Intent();
        jsonObject.addProperty("yfzts", tvData.getText().toString());
        if (jo.get("nj").getAsString().equals("1") || jo.get("nj").getAsString().equals("2")){
            intent.putExtra("json",jo.toString());
            intent.setClass(SkipTimingActivity.this,ResultActivity.class);
        }else {
            intent.putExtra("json",jo.toString());
            intent.setClass(SkipTimingActivity.this,SitUpTimingActivity.class);
        }
        startActivity(intent);
        finish();
        playerStandard.release();
    }
}