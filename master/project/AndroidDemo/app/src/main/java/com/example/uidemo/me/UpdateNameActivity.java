package com.example.uidemo.me;

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
import static com.example.uidemo.LoginActivity.currentUserName;
import static com.example.uidemo.LoginActivity.currentUserPhoneNum;

public class UpdateNameActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etNewName;

    private Handler myHandler;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name_layout);

        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String)msg.obj;
                        if("OK".equals(sign)) {
                            Toast.makeText(UpdateNameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        if("false".equals(sign)){
                            Toast.makeText(UpdateNameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(UpdateNameActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViews();

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateNameActivity.this.finish();
            }
        });

        //修改完成按钮，并将数据传递给服务器
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 将新昵称传递给服务器
                 * 根据用户id修改
                 * */
                String newName = etNewName.getText().toString();
                //更新全局用户属性
                currentUserName = newName;
                userToServer(currentUserId,newName);
                UpdateNameActivity.this.finish();//返回上一个页面
            }
        });
    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_updateNameRet);
        btnFinish = findViewById(R.id.btn_finishName);
        etNewName = findViewById(R.id.et_newName);
    }

    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String userId, String newName) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId)
                .add("newName", newName);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "UserUpdateName")
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
