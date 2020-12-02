package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uidemo.R;
public class LogoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_layout);

        Button btnReturn = findViewById(R.id.btn_logRet);
        Button btnLogout = findViewById(R.id.btn_logoutY);

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutActivity.this.finish();
            }
        });

        //确认注销账号，并返回登录页面
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 将当前用户id，传送到服务端，并在数据库中删除该用户的所有数据
                 * */

                LogoutActivity.this.finish();//返回登录页面
            }
        });
    }
}
