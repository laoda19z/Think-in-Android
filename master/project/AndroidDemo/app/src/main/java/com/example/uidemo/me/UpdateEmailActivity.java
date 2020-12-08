package com.example.uidemo.me;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import static com.example.uidemo.LoginActivity.currentUserEmail;
import static com.example.uidemo.LoginActivity.currentUserId;

public class UpdateEmailActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etNewEmail;
    private TextView etEmail;

    private Handler myHandler;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email_layout);
        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String)msg.obj;
                        if("OK".equals(sign)) {
                            Toast.makeText(UpdateEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        if("false".equals(sign)){
                            Toast.makeText(UpdateEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(UpdateEmailActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViews();
        etEmail.setText(currentUserEmail);

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateEmailActivity.this.finish();
            }
        });

        //修改成功，返回上一个页面，并提交数据给服务器
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取新邮箱
                String newEmail = etNewEmail.getText().toString().trim();
                /**
                 * 传递到服务端
                 * 并根据用户id进行修改
                 * */
                currentUserEmail = newEmail;
                userToServer(currentUserId,newEmail);
                UpdateEmailActivity.this.finish();//返回上一个页面
            }
        });
    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_upEmailRet);
        btnFinish = findViewById(R.id.btn_finishEmail);
        etNewEmail = findViewById(R.id.et_newEmail);
        etEmail = findViewById(R.id.tv_email);
    }

    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String userId, String newEmail) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId)
                .add("newEmail", newEmail);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "UserUpdateNameEmail")
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
