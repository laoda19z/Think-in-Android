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


public class SitUpInfoActivity extends AppCompatActivity {

    private EditText edtData;
    private TextView tvNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_up_info);
        findViews();
        String str = getIntent().getStringExtra("json");
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
        String grade = jsonObject.get("nj").getAsString();
        if (grade.equals(("3")) || grade.equals("4")) {
            tvNext.setText("生成报告");
        }
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvNext.getText().toString().equals("下一步")) {
                    if (edtData.getText().toString().equals("") || edtData.getText().toString() == null){
                        Toast.makeText(SitUpInfoActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                    }else if (!isDataSuitable(edtData.getText().toString())){
                        Toast.makeText(SitUpInfoActivity.this, "成绩格式有误", Toast.LENGTH_SHORT).show();
                    } else {
                        jsonObject.addProperty("yfzywqz", edtData.getText().toString());
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =  new Intent();
                                intent.putExtra("json",jsonObject.toString());
                                intent.setClass(SitUpInfoActivity.this,FiftymeterRunBackAcitivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }else {
                    if (edtData.getText().toString().equals("") || edtData.getText().toString() == null){
                        Toast.makeText(SitUpInfoActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                    }else if (!isDataSuitable(edtData.getText().toString())){
                        Toast.makeText(SitUpInfoActivity.this, "成绩格式有误", Toast.LENGTH_SHORT).show();
                    } else {
                        jsonObject.addProperty("yfzywqz", edtData.getText().toString());
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =  new Intent();
                                intent.putExtra("json",jsonObject.toString());
                                intent.setClass(SitUpInfoActivity.this,ResultActivity.class);
                                startActivity(intent);
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