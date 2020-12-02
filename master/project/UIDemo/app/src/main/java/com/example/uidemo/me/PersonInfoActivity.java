package com.example.uidemo.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uidemo.R;
public class PersonInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_layout);

        ImageView ivHeader = findViewById(R.id.iv_perInfoHeader);
        Button btnReturn = findViewById(R.id.btn_perInfoRet);
        Button btnUpdateName = findViewById(R.id.btn_upName);
        Button btnUpdateInfo = findViewById(R.id.btn_updateInfo);

        Glide.with(this)
                .load(R.drawable.girl)
                .circleCrop()
                .into(ivHeader);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonInfoActivity.this.finish();
            }
        });

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PersonInfoActivity.this,UpdateNameActivity.class);
                startActivity(intent);
            }
        });

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PersonInfoActivity.this,UpdateInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
