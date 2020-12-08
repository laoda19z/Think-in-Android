package com.example.uidemo.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import static com.example.uidemo.LoginActivity.currentUserHead;
import static com.example.uidemo.LoginActivity.currentUserId;
import static com.example.uidemo.LoginActivity.currentUserPhoneNum;
import static com.example.uidemo.LoginActivity.currentUserRealName;
import static com.example.uidemo.LoginActivity.currentUserSex;

public class UpdateInfoActivity extends AppCompatActivity {
    private ImageView ivUpInfoHeader;
    private Button btnReturn;
    private Button btnUpdateInfo;
    private EditText etUpdateInfoName;
    private EditText etUpdateInfoSex;
    private EditText etUpdateInfoPhone;
    private EditText etUpdateInfoEmail;

    private Handler myHandler;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info_activity);
        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String)msg.obj;
                        if("OK".equals(sign)) {
                            Toast.makeText(UpdateInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        if("false".equals(sign)){
                            Toast.makeText(UpdateInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(UpdateInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViews();
        //设置头像
        if(currentUserHead.isEmpty()){
            Glide.with(this)
                    .load(R.mipmap.tx)
                    .circleCrop()
                    .into(ivUpInfoHeader);
        }else{
            Glide.with(this)
                    .load(ConfigUtil.SERVER_ADDR+currentUserHead+"")
                    .circleCrop()
                    .into(ivUpInfoHeader);
        }
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateInfoActivity.this.finish();
            }
        });

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户输入的新变量
                String newRealName = etUpdateInfoName.getText().toString().trim();
                String newSex = etUpdateInfoSex.getText().toString().trim();
                String newPhone = etUpdateInfoPhone.getText().toString().trim();
                String newEmail = etUpdateInfoEmail.getText().toString().trim();

                //更新全局用户属性
                currentUserSex = newSex;
                currentUserRealName = newRealName;
                currentUserPhoneNum = newPhone;
                currentUserEmail = newEmail;
                userToServer(currentUserId,newRealName,newSex,newPhone,newEmail);

                //跳转时保存数据
                //返回信息页时，将数据一同传输
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("newName",newRealName);
                bundle.putString("newSex",newSex);
                bundle.putString("newPhone",newPhone);
                bundle.putString("newEmail",newEmail);
                intent.putExtras(bundle);
                UpdateInfoActivity.this.setResult(2,intent);
                UpdateInfoActivity.this.finish();
            }
        });

    }

    private void findViews() {
        ivUpInfoHeader = findViewById(R.id.iv_upInfoHeader);
        btnReturn = findViewById(R.id.btn_updateInfoRet);
        btnUpdateInfo = findViewById(R.id.btn_updateInfoY);
        etUpdateInfoName = findViewById(R.id.et_updateInfoName);
        etUpdateInfoSex = findViewById(R.id.et_updateInfoSex);
        etUpdateInfoPhone = findViewById(R.id.et_updateInfoPhone);
        etUpdateInfoEmail = findViewById(R.id.et_updateInfoEmail);
    }

    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String userId, String newRealName,String newSex,String newPhone,String newEmail) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId)
                .add("newRealName", newRealName)
                .add("newSex",newSex)
                .add("newPhone",newPhone)
                .add("newEmail",newEmail);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "UserUpdateInfo")
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
