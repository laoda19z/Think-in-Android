package com.example.uidemo.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uidemo.R;
public class UpdatePwdActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etPwd;
    private EditText etNewPwd;
    private EditText etNewPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwd_layout);

        findViews();

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePwdActivity.this.finish();
            }
        });

        //修改密码完成，也可以直接返回登录页面，让用户用新密码重新登录
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //要写成返回登录页面


                //获取新旧密码参数
                String pwd = etPwd.getText().toString().trim();
                String newPwd = etNewPwd.getText().toString().trim();
                String newPwd2 = etNewPwd2.getText().toString().trim();

                //写一个接口，把新旧密码传到服务端，在用户表内根据用户id修改密码
                /**
                 * 修改密码
                 * */
                UpdatePwdActivity.this.finish();//返回前一个页面
            }
        });

    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_upPwdRet);
        btnFinish = findViewById(R.id.btn_finishPwd);
        etPwd = findViewById(R.id.et_pwd);
        etNewPwd = findViewById(R.id.et_newPwd);
        etNewPwd2 = findViewById(R.id.et_newPwd2);
    }
}
