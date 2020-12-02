package com.example.uidemo.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.example.uidemo.ConfigUtil.isDataSuitable;


public class SitReachActivity extends AppCompatActivity {

    private EditText edtData;
    private TextView tvNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_reach);
        findViews();
        String str = getIntent().getStringExtra("json");
        tvNext.setOnClickListener(new View.OnClickListener() {
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
                }
            }
        });
    }

    private void findViews() {
        edtData = findViewById(R.id.edt_data);
        tvNext = findViewById(R.id.tv_next);
    }
}