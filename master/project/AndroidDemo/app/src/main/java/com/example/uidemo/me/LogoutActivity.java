package com.example.uidemo.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
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

public class LogoutActivity extends AppCompatActivity {

    private Handler myHandler;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_layout);
        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String)msg.obj;
                        if("OK".equals(sign)) {
                            Toast.makeText(LogoutActivity.this, "账号注销成功", Toast.LENGTH_SHORT).show();
                        }
                        if("FALSE".equals(sign)){
                            Toast.makeText(LogoutActivity.this, "账号注销失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(LogoutActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

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
                userToServer(currentUserId);
                //返回登录页面
                Intent intent = new Intent();
                intent.setClass(LogoutActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String userId) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "DeleteUserServlet")
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
