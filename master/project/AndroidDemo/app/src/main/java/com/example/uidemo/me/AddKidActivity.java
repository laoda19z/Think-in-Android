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
import com.example.uidemo.beans.Child;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.uidemo.LoginActivity.currentUserId;
import static com.example.uidemo.LoginActivity.currentUserKids;

public class AddKidActivity extends AppCompatActivity {
    private Button btnRet;
    private EditText etName;
    private EditText etSex;
    private EditText etAge;
    private EditText etGrade;
    private Button btnFinish;
    private Handler myHandler;
    private OkHttpClient okHttpClient;
    private String childId;
    private String addName;
    private String addSex;
    private String age;
    private String grade;
    private Child child = new Child();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info_layout);
        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        childId = (String)msg.obj;
                        child.setChildId(Integer.parseInt(childId));
                        child.setChildSex(addSex);
                        child.setChildName(addName);
                        child.setChildGrade(Integer.parseInt(grade));
                        child.setChildAge(Integer.parseInt(age));
                        currentUserKids.add(child);
                        Toast.makeText(AddKidActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(AddKidActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //设置控件对象
        findViews();

        //返回键
        btnRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddKidActivity.this.finish();
            }
        });

        //添加孩子功能
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的参数
                addName = etName.getText().toString();
                addSex = etSex.getText().toString();
                age = etAge.getText().toString();
                grade = etGrade.getText().toString();

                //发送构造成的JSON串到服务端，添加到数据库，然后在main中显示出来
                userToServer(currentUserId,addName,grade,addSex,age);
                AddKidActivity.this.finish();
            }
        });
    }
    /**
     * 获取视图控件方法
     * */
    private void findViews() {
        btnRet = findViewById(R.id.btn_addRet);
        etName = findViewById(R.id.et_addKidName);
        etSex = findViewById(R.id.et_addKidSex);
        etAge = findViewById(R.id.et_addKidAge);
        etGrade = findViewById(R.id.et_addKidGrade);
        btnFinish = findViewById(R.id.btn_addKidFinish);
    }

    /**
     * 向服务端传输用户名id和新添加的孩子信息
     */
    private void userToServer(String userId, String addName,String addGrade,String addSex,String addAge) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userId", userId)
                .add("addName", addName)
                .add("addGrade",addGrade)
                .add("addSex",addSex)
                .add("addAge",addAge);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "AddChild")
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
                Log.e("添加孩子是id", responsestr);
                Message msg = myHandler.obtainMessage();
                msg.what = 1;
                msg.obj = responsestr;
                myHandler.sendMessage(msg);
            }
        });
    }
}
