package com.example.uidemo.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
import com.example.uidemo.R;
import com.example.uidemo.dynamic.GPS;
import com.example.uidemo.mainfragment.ContactFragment;
import com.example.uidemo.zxing.android.CaptureActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.OkHttpClient;

public class AddContactActivity extends AppCompatActivity {
    private Button btnAddContact;
    private EditText etContactName;
    private Button btnErweima;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private ProgressDialog mDialog;
    private EventBus eventBus;
    //QQ分享
    private static final String TAG = "LoginActivity";
    private static final String APP_ID = "1111276030";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        findViews();
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        String result = (String) msg.obj;
                        if(!"FALSE".equals(result)){
                            Log.e("mll",result);
                            String[] strs = result.split("|");
                            Toast.makeText(AddContactActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddContactActivity.this, ChatActivity.class);
                            intent.putExtra("ec_chat_id", strs[1]);
                            intent.putExtra("ec_chat_name",etContactName.getText().toString().trim());
                            intent.putExtra("ec_chat_head",strs[2]);
                            startActivity(intent);
                            setResult(200);
                            finish();
                        }else {
                            Toast.makeText(AddContactActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                            setResult(200);
                            finish();
                        }
                        eventBus.getDefault().postSticky(AddContactActivity.this);
                        mDialog.dismiss();
                        break;
                }
            }
        };
        okHttpClient = new OkHttpClient();
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ProgressDialog(AddContactActivity.this);
                mDialog.setMessage("添加中，请稍后...");
                mDialog.show();
                String contactName = etContactName.getText().toString().trim();
                if(contactName!=null&&!"".equals(contactName)){
                    Log.e("mll",contactName);
                    addContactServer(contactName);
                }else {
                    Toast.makeText(AddContactActivity.this,"输入用户名不合法",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });
        btnErweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContactActivity.this, CaptureActivity.class);
                startActivityForResult(intent,300);
//                IUiListener shareListener = new IUiListener() {
//                    @Override
//                    public void onComplete(Object o) {
//                        Toast.makeText(AddContactActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(UiError uiError) {
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//
//                    @Override
//                    public void onWarning(int i) {
//
//                    }
//                };
            }
        });
    }

    /**
     * 添加联系人
     */
    private void addContactServer(final String contactname) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ConfigUtil.SERVER_ADDR+"AddContactServlet?contactusername="+contactname+"&userid="+LoginActivity.currentUserId);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = reader.readLine();
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = str;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void findViews() {
        btnAddContact = findViewById(R.id.add_contact_search);
        btnErweima = findViewById(R.id.add_contact_find);
        etContactName = findViewById(R.id.add_contact_et);
    }
    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            //登录成功
            Toast.makeText(AddContactActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        JSONObject obj = (JSONObject) response;
                        Log.e("mll",obj.toString());
                        //是一个json串response.tostring，直接使用gson解析就好
                        //登录成功后进行Gson解析即可获得你需要的QQ头像和昵称
                        // Nickname  昵称
                        //Figureurl_qq_1 //头像
                        try {
                            String name = obj.getString("nickname");
                            String head = obj.getString("figureurl");
                            String figureurl_1 = obj.getString("figureurl_1");
                            String figureurl_2 = obj.getString("figureurl_2");
                            Log.e("name",name);
                            Log.e("head",head);//30
                            Log.e("figureurl_1",figureurl_1);//50
                            Log.e("figureurl_2",figureurl_2);//100
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }

                    @Override
                    public void onWarning(int i) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(AddContactActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(AddContactActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onWarning(int i) {
            Toast.makeText(AddContactActivity.this, "授权警告", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        if(requestCode == 300 && resultCode == RESULT_OK){
            if(data!=null){
                String content = data.getStringExtra("codedContent");
                etContactName.setText(content);
                mDialog = new ProgressDialog(AddContactActivity.this);
                mDialog.setMessage("添加中，请稍后...");
                mDialog.show();
                Log.e("mll","联系人"+content);
                addContactServer(content);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void qqShare(View view){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,"摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://blog.csdn.net/u013451048");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://avatar.csdn.net/C/3/D/1_u013451048.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "CSDN");
        mTencent.shareToQQ(this, params, mIUiListener);
    }
}