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


public class SkipInfoActivity extends AppCompatActivity {

    private EditText edtData;
    private TextView tvNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_info);
        findViews();
        String str = getIntent().getStringExtra("json");
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
        String grade = jsonObject.get("nj").getAsString();
        if (grade.equals(("1")) || grade.equals("2")) {
            tvNext.setText("生成报告");
        }
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvNext.getText().toString().equals("下一步")) {
                    if (edtData.getText().toString().equals("") || edtData.getText().toString() == null){
                        Toast.makeText(SkipInfoActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                    }else if (!isDataSuitable(edtData.getText().toString())){
                        Toast.makeText(SkipInfoActivity.this, "成绩格式有误", Toast.LENGTH_SHORT).show();
                    } else {
                        jsonObject.addProperty("yfzts", edtData.getText().toString());
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =  new Intent();
                                intent.putExtra("json",jsonObject.toString());
                                intent.setClass(SkipInfoActivity.this,SitUpTimingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }else {
                    if (edtData.getText().toString().equals("") || edtData.getText().toString() == null){
                        Toast.makeText(SkipInfoActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                    }else if (!isDataSuitable(edtData.getText().toString())){
                        Toast.makeText(SkipInfoActivity.this, "成绩格式有误", Toast.LENGTH_SHORT).show();
                    } else {
                        jsonObject.addProperty("yfzts", edtData.getText().toString());
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =  new Intent();
                                intent.putExtra("json",jsonObject.toString());
                                intent.setClass(SkipInfoActivity.this,ResultActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }
    private void findViews() {
        edtData = findViewById(R.id.edt_data);
        tvNext = findViewById(R.id.tv_next);
    }
}