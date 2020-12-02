package com.example.uidemo.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uidemo.R;
public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_center_layout);

        Button btnReturn = findViewById(R.id.btn_seCenterRet);
        Button btnUpdatePwd = findViewById(R.id.btn_updatePwd);
        Button btnUpdateEmail = findViewById(R.id.btn_updateEmail);
        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnUpdatePhone = findViewById(R.id.btn_updatePhone);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecurityActivity.this.finish();
            }
        });

        btnUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SecurityActivity.this,UpdatePwdActivity.class);
                startActivity(intent);
            }
        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SecurityActivity.this,UpdateEmailActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SecurityActivity.this,LogoutActivity.class);
                startActivity(intent);
            }
        });

        btnUpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SecurityActivity.this,UpdatePhoneActivity.class);
                startActivity(intent);
            }
        });
    }
}
