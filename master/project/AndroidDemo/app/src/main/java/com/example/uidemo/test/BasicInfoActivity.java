package com.example.uidemo.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.example.uidemo.ConfigUtil.isDataSuitable;


public class BasicInfoActivity extends AppCompatActivity {

    private EditText edtHeight;
    private EditText edtWeight;
    private EditText editFeihuoliang;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        String str = getIntent().getStringExtra("json");
        findViews();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                int age = Integer.parseInt(jsonObject.get("nl").getAsString());
                String sex = jsonObject.get("xb").getAsString();
                String feihuoliang = editFeihuoliang.getText().toString();
                int fhl = 0;
                if (edtHeight.getText().toString().equals("")
                        || edtHeight.getText().toString() == null
                        || edtWeight.getText().toString().equals("")
                        || edtWeight.getText().toString() == null){
                    Toast.makeText(BasicInfoActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                } else if (!isDataSuitable(edtHeight.getText().toString())){
                    Toast.makeText(BasicInfoActivity.this, "身高格式有误", Toast.LENGTH_SHORT).show();
                } else if (!isDataSuitable(edtWeight.getText().toString())){
                    Toast.makeText(BasicInfoActivity.this, "体重格式有误", Toast.LENGTH_SHORT).show();
                } else if (!isDataSuitable(editFeihuoliang.getText().toString())){
                    if (editFeihuoliang.getText().toString().equals("") || editFeihuoliang.getText().toString() == null){
                        fhl = getFeihuoliangByAge(age,sex);
                        jsonObject.addProperty("h",edtHeight.getText().toString());
                        jsonObject.addProperty("w",edtWeight.getText().toString());
                        jsonObject.addProperty("fhl",fhl+"");
                        Intent intent = new Intent();
                        intent.putExtra("json",jsonObject.toString());
                        intent.setClass(BasicInfoActivity.this,FiftymeterRunActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(BasicInfoActivity.this, "肺活量格式有误", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    jsonObject.addProperty("h",edtHeight.getText().toString());
                    jsonObject.addProperty("w",edtWeight.getText().toString());
                    jsonObject.addProperty("fhl",fhl+"");
                    Intent intent = new Intent();
                    intent.putExtra("json",jsonObject.toString());
                    intent.setClass(BasicInfoActivity.this,FiftymeterRunActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void findViews() {
        edtHeight = findViewById(R.id.edt_height);
        edtWeight = findViewById(R.id.edt_weight);
        editFeihuoliang = findViewById(R.id.edt_feihuoliang);
        btnNext = findViewById(R.id.btn_next);
    }
    private int getFeihuoliangByAge(int age,String sex){
        int fhl = 0;
        int[] male = {1342,1496,1972,1843,2010,2200};
        int[] female = {1213,1354,1516,1685,1883,2077};
        if (sex.equals("男")){
            fhl = male[age-7];
        }else {
            fhl = female[age-7];
        }
        return fhl;
    }

}