package com.example.uidemo.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.uidemo.mark.MarkSuccess.BUserMark;
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
import java.io.File;
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
    private static final String TAG = "AddContactActivity";
    private static final String APP_ID = "1111276030";//官方获取的APPID
    private Tencent mTencent;
    private ShareUiListener mIUiListener;
    private Bundle params;
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
//                mTencent = Tencent.createInstance(APP_ID, AddContactActivity.this.getApplicationContext());
//                mIUiListener = new ShareUiListener();
//                shareToQQ();
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
    class ShareUiListener implements IUiListener{

        @Override
        public void onComplete(Object o) {
            //分享成功
            Log.e("mll","分享成功");
        }

        @Override
        public void onError(UiError uiError) {
            //分享失败
            Log.e("mll","分享失败");
        }

        @Override
        public void onCancel() {
            //分享取消
            Log.e("mll","分享取消");
        }

        @Override
        public void onWarning(int i) {
            Log.e("mll","授权警告");
        }
    }

    /**
     * 默认分享——图文并存
     */
    private void shareToQQ() {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "QQ分享");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "欢迎大家加入ThinkInAndroid");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://www.baidu.com");// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,ConfigUtil.SERVER_ADDR+"file/lovelive1.jpg");// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        mTencent.shareToQQ(AddContactActivity.this, params, mIUiListener);
    }
    /**
     * 分享图片
     *
     */
    private void shareToQQImage(){
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);// 设置分享类型为纯图片分享
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "QQ分享");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "欢迎大家加入ThinkInAndroid");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://www.baidu.com");// 内容地址
        String path = getFilesDir().getAbsolutePath();
        String file = path + "/imgs/"+"pkemeng3.jpg";
        Log.e("mll",file);
        File file1 = new File(file);
        if(file1.exists()){
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file);// 需要分享的本地图片URL
            mTencent.shareToQQ(AddContactActivity.this, params, mIUiListener);
        }
    }
}