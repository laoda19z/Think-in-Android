package com.example.uidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterActivity extends AppCompatActivity {
    private EditText etReUserName;
    private EditText etReUserPwd;
    private EditText etReUserPwdAgain;
    private Button btnRegister;
    private Handler myHandler;
    private ProgressDialog mDialog;
    private OkHttpClient okHttpClient;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        myHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        String sign = (String) msg.obj;
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    case 2:
                        Toast.makeText(RegisterActivity.this, "注册失败", LENGTH_SHORT).show();
                        //删除用户数据
                        deleteUserInfo();
                        break;
                    case 3:
                        Toast.makeText(RegisterActivity.this, "注册失败", LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViews();
        okHttpClient = new OkHttpClient();
        final String userName = etReUserName.getText().toString().trim();
        final String userPwd = etReUserPwd.getText().toString().trim();
        String userPwdAgain = etReUserPwdAgain.getText().toString().trim();

        if (!userPwd.equals(userPwdAgain)) {
            Toast.makeText(getApplicationContext(), "两次输入密码不一致", LENGTH_SHORT).show();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myServerSignUp();
            }
        });
    }



    private void findViews() {
        etReUserName = findViewById(R.id.et_reUserName);
        etReUserPwd = findViewById(R.id.et_reUserPwd);
        etReUserPwdAgain = findViewById(R.id.et_reUserPwdAgain);
        btnRegister = findViewById(R.id.btn_register);
    }

    /**
     * 注册
     */
    private void myServerSignUp() {
        String username = etReUserName.getText().toString().trim();
        String password = etReUserPwd.getText().toString().trim();
        // 注册是耗时过程，所以要显示一个dialog来提示下用户
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("username", username)
                .add("password", password);
        FormBody formBody = formBuilder.build();
        //创建Request对象
        Request request = new Request.Builder().url(ConfigUtil.SERVER_ADDR + "SignUpServlet")
                .post(formBody)
                .build();
        //创建Call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mDialog.dismiss();
                Message msg = new Message();
                msg.what = 2;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String emUserName = response.body().string();
                Log.e("mll", emUserName);
                emServerSignUp(emUserName, password);
            }
        });

    }

    /**
     * 自己的服务器注册成功但是环信的后台未注册成功。
     * 这里需要到自己的服务器数据库中删除用户数据。
     */
    private void deleteUserInfo() {
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("userid", userId);
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
                String emUserName = response.body().string();
                userId = emUserName;
                Log.e("mll", emUserName);
            }
        });
    }

    /**
     * 注册环信后台的账户。
     * @param username
     * @param password
     */
    private void emServerSignUp(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //注册失败会抛出HyphenateException
                    //该方法是注册的方法，需要在后台开放注册权限，并且用户名要小写
                    EMClient.getInstance().createAccount(username, password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            Message msg = new Message();
                            msg.what = 1;
                            myHandler.sendMessage(msg);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("lzan13",
                                    String.format("sign up - errorCode:%d, errorMsg:%s", errorCode,
                                            e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(RegisterActivity.this,
                                            "网络错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(RegisterActivity.this,
                                            "用户已存在 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(RegisterActivity.this,
                                            "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: "
                                                    + errorCode
                                                    + ", message:"
                                                    + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(RegisterActivity.this,
                                            "服务器未知错误 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(RegisterActivity.this,
                                            "账户注册失败 code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this,
                                            "ml_sign_up_failed code: " + errorCode + ", message:" + message,
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}