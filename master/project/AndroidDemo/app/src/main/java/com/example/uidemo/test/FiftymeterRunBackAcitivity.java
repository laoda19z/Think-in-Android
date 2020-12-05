package com.example.uidemo.test;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.example.uidemo.ConfigUtil.isDataSuitable;

public class FiftymeterRunBackAcitivity extends AppCompatActivity {

    private JCVideoPlayerStandard playerStandard;
    private EditText edtDataMin;
    private EditText edtDataSec;
    private TextView tvNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiftymeter_run_back_acitivity);
        findViews();
        initVideo();
        String str = getIntent().getStringExtra("json");
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                if (edtDataMin.getText().toString().equals("") || edtDataMin.getText().toString() == null){
                    Toast.makeText(FiftymeterRunBackAcitivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                }else if (edtDataSec.getText().toString().equals("") || edtDataSec.getText().toString() == null){
                    Toast.makeText(FiftymeterRunBackAcitivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                }else if (!isDataSuitable(edtDataMin.getText().toString())){
                    Toast.makeText(FiftymeterRunBackAcitivity.this, "分钟格式有误", Toast.LENGTH_SHORT).show();
                } else if (!isDataSuitable(edtDataSec.getText().toString())){
                    Toast.makeText(FiftymeterRunBackAcitivity.this, "秒钟格式有误", Toast.LENGTH_SHORT).show();
                }else {
                    int min = Integer.parseInt(edtDataMin.getText().toString());
                    int second = Integer.parseInt(edtDataSec.getText().toString());
                    String data = (min*60+second)+"";
                    jsonObject.addProperty("wsmcbwfp",data);
                    Intent intent = new Intent();
                    intent.putExtra("json",jsonObject.toString());
                    intent.setClass(FiftymeterRunBackAcitivity.this,ResultActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
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

    private void findViews() {
        edtDataMin = findViewById(R.id.edt_data_m);
        edtDataSec = findViewById(R.id.edt_data_s);
        tvNext = findViewById(R.id.tv_next);
    }
}