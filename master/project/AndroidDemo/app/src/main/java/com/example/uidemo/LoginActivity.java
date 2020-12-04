package com.example.uidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uidemo.beans.User;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    // 弹出框
    private ProgressDialog mDialog;
    private EditText etUserName;
    private EditText etUserPwd;
    private Button btnRegister;
    private Button btnLogin;
    private Handler myHandler;
    private OkHttpClient okHttpClient;
    public static String currentUserId;
    public static String currentUserName;
    public static String currentUserHead;
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override public void onSuccess() {
                Log.i("mll", "注销成功");
                // 调用退出成功，结束app
                finish();
            }

            @Override public void onError(int i, String s) {
                Log.i("mll", "注销失败 logout error " + i + " - " + s);
            }

            @Override public void onProgress(int i, String s) {
                Log.i("mll","运行中");
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
//        signOut();
        okHttpClient = new OkHttpClient();
        myHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //点击跳转注册页面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        etUserName = findViewById(R.id.et_userName);
        etUserPwd = findViewById(R.id.et_userPwd);

        btnRegister = findViewById(R.id.btn_regist);
        btnLogin = findViewById(R.id.btn_login);
        /**
         * 注册
         */
        /**
         * 登录
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /**
     * 登录
     */
    private void signIn() {
        //显示一个ProgressDialog进度
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();
//        try {
        String userPwd = etUserPwd.getText().toString().trim();
        String userName = etUserName.getText().toString().trim();
        currentUserName = userName;
        userToServer(userName, userPwd);
    }

    /**
     * 登录环信账户
     */
    private void loginInEMServer(String username) {
        String password = etUserPwd.getText().toString().trim();
        //TextUtils工具类
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //EMClient单例模式
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 使用回调方法需要实现三个方法
             * void onSuccess();
             *成功
             * void onError(int var1, String var2);
             *失败
             * void onProgress(int var1, String var2);
             *正在执行
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                new Thread() {
                    @Override
                    public void run() {
                        //ProgressDialog结束弹出框
                        mDialog.dismiss();
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();
                        // 登录成功跳转界面
                        currentUserId = username;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }.start();
                //登录成功后需要调用方法来保证进入主页面后本地会话和群组都load完毕
//                EMClient.getInstance().groupManager().loadAllGroups();
                //EMClient.getInstance().chatManager().loadAllConversations();
                //
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDialog.dismiss();
//                        // 加载所有会话到内存
//                        EMClient.getInstance().chatManager().loadAllConversations();
//                        // 加载所有群组到内存，如果使用了群组的话
//                        // EMClient.getInstance().groupManager().loadAllGroups();
//                        // 登录成功跳转界面
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "网络错误码: " + i + ", 消息:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LoginActivity.this,
                                        "无效的用户名码: " + i + ", 消息:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LoginActivity.this,
                                        "无效的密码码: " + i + ", 消息:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LoginActivity.this,
                                        "用户认证失败，用户名或密码错误码: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LoginActivity.this,
                                        "用户不存在码: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LoginActivity.this,
                                        "无法访问到服务器码: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LoginActivity.this,
                                        "等待服务器响应超时码: " + i + ", message:" + s, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LoginActivity.this,
                                        "服务器繁忙码: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LoginActivity.this,
                                        "未知的服务器异常码: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this,
                                        "特殊异常操作 ml_sign_in_failed code: " + i + ", message:" + s,
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
                Toast.makeText(LoginActivity.this, "正在验证中", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 向服务端传输用户名和密码的Json串
     */
    private void userToServer(String username, String password) {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("username", username)
                .add("password", password);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "LoginServlet")
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
                if ("false".equals(responsestr)) {
                    Message msg = myHandler.obtainMessage();
                    msg.what = 1;
                    msg.obj = responsestr;
                    myHandler.sendMessage(msg);
                } else {
                    Gson gson = new Gson();
                    User user = gson.fromJson(responsestr, User.class);
                    String userId = user.getUserId() + "";
                    currentUserHead = user.getHeadImg();
                    loginInEMServer(userId);
                }
            }
        });
    }
}