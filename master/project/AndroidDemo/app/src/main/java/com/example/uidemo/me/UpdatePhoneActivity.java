package com.example.uidemo.me;

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
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.uidemo.LoginActivity.currentUserId;
import static com.example.uidemo.LoginActivity.currentUserPhoneNum;

public class UpdatePhoneActivity extends AppCompatActivity {
    private Button btnReturn;
    private Button btnFinish;
    private EditText etNewPhone;
    private TextView tvPhone;

    private Handler myHandler;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_phone_layout);

        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String)msg.obj;
                        if("OK".equals(sign)) {
                            Toast.makeText(UpdatePhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        if("false".equals(sign)){
                            Toast.makeText(UpdatePhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(UpdatePhoneActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViews();
        tvPhone.setText(currentUserPhoneNum);

        //返回键
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePhoneActivity.this.finish();
            }
        });

        //修改成功，返回上一个页面，并向服务器提交数据
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交数据
                String newPhone = etNewPhone.getText().toString().trim();
                /**
                 * 根据用户id，修改电话号码
                 * */
                //更新全局用户属性
                currentUserPhoneNum = newPhone;
                userToServer(currentUserId,newPhone);
                UpdatePhoneActivity.this.finish();//返回上一个页面
            }
        });
    }

    private void findViews() {
        btnReturn = findViewById(R.id.btn_phRet);
        btnFinish = findViewById(R.id.btn_finishPhone);
        etNewPhone = findViewById(R.id.et_newPhone);
        tvPhone = findViewById(R.id.tv_phone);
    }

    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String userId, String newPhone) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId)
                .add("newPhone", newPhone);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "UserUpdatePhone")
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
