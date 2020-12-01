package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.R;
import com.example.uidemo.beans.Child;
import com.google.gson.Gson;

public class AddKidActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info_layout);

        Button btnRet = findViewById(R.id.btn_addRet);
        EditText etName = findViewById(R.id.et_addKidName);
        EditText etSex = findViewById(R.id.et_addKidSex);
        EditText etAge = findViewById(R.id.et_addKidAge);
        EditText etGrade = findViewById(R.id.et_addKidGrade);
        Button btnFinish = findViewById(R.id.btn_addKidFinish);

        final String addName = etName.getText().toString().trim();
        final String addSex = etSex.getText().toString().trim();
        final String addAge = etAge.getText().toString().trim();
        final String addGrade = etGrade.getText().toString().trim();

        btnRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddKidActivity.this.finish();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int age = Integer.parseInt(addAge);
                int grade = Integer.parseInt(addGrade);
                Child child = new Child(age,grade,addName,addSex);
                Gson gson = new Gson();
                String childJson = gson.toJson(child);
                //发送构造成的JSON串到服务端，添加到数据库，然后在mian中显示出来

            }
        });




    }
}
