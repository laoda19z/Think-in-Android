package com.example.uidemo.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.example.uidemo.ConfigUtil.isDataSuitable;


public class SitReachActivity extends AppCompatActivity {

    private JCVideoPlayerStandard playerStandard;
    private EditText edtData;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_reach);
        findViews();
        initVideo();
        String str = getIntent().getStringExtra("json");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                if (edtData.getText().toString().equals("") || edtData.getText().toString() == null){
                    Toast.makeText(SitReachActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                }else if (!isDataSuitable(edtData.getText().toString())){
                    Toast.makeText(SitReachActivity.this, "成绩格式有误", Toast.LENGTH_SHORT).show();
                }
                else {
                    jsonObject.addProperty("zwtqq",edtData.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("json",jsonObject.toString());
                    intent.setClass(SitReachActivity.this,SkipTimingActivity.class);
                    startActivity(intent);
                    finish();
                    playerStandard.release();
                }
            }
        });
    }

    private void initVideo() {
        playerStandard = findViewById(R.id.media_video);
        playerStandard.setUp(ConfigUtil.SERVER_ADDR +"video/zuoweitiqianqu.mp4",0,"坐位体前屈");
        playerStandard.startVideo();
    }
    private void findViews() {
        edtData = findViewById(R.id.edt_data);
        btnNext = findViewById(R.id.btn_next);
    }
}