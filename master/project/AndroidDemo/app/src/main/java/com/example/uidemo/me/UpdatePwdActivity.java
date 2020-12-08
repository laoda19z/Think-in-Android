package com.example.uidemo.me;

import android.content.Intent;
import android.icu.lang.UProperty;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.uidemo.LoginActivity.currentUserId;

public class UpdatePwdActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etPwd;
    private EditText etNewPwd;
    private EditText etNewPwd2;

    private Handler myHandler;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwd_layout);

        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String)msg.obj;
                        if("OK".equals(sign)) {
                            Toast.makeText(UpdatePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        if("false".equals(sign)){
                            Toast.makeText(UpdatePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(UpdatePwdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

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
                //获取新密码参数
                String newPwd = etNewPwd.getText().toString().trim();
                String newPwd2 = etNewPwd2.getText().toString().trim();
                if(!newPwd2.equals(newPwd)){
                    Toast.makeText(UpdatePwdActivity.this,"两次输入密码不一致，请重新输入！",Toast.LENGTH_SHORT);
                }
                //写一个接口，把新旧密码传到服务端，在用户表内根据用户id修改密码
                /**
                 * 修改密码
                 * */
                userToServer(currentUserId,newPwd);
                //要写成返回登录页面
                Intent intent = new Intent();
                intent.setClass(UpdatePwdActivity.this,LogoutActivity.class);
                startActivity(intent);
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

    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String userId, String newPassword) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId)
                .add("newPassword", newPassword);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "UserUpdatePwd")
                .post(formBody)
                .build();
        //创建Call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message msg = new Message();
                msg.what = 3;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responsestr = response.body().string();
                Log.e("mll", responsestr);
                Message msg = myHandler.obtainMessage();
                msg.what = 1;
                msg.obj = responsestr;
                myHandler.sendMessage(msg);
            }
        });
    }

}
