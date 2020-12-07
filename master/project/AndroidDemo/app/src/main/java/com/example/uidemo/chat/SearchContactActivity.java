package com.example.uidemo.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.beans.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SearchContactActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText etName;
    private User user;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        Intent intent = new Intent(SearchContactActivity.this,ChatActivity.class);
                        intent.putExtra("ec_chat_id",user.getUserId()+"");
                        intent.putExtra("ec_chat_name",etName.getText().toString().trim());
                        intent.putExtra("ec_chat_head",user.getHeadImg());
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(SearchContactActivity.this,"没有当前用户",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        findViews();
        user = new User();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = etName.getText().toString().trim();
                searchContactServer(ConfigUtil.SERVER_ADDR+"SearchContactServlet?contactName="+contact);
            }
        });
    }

    /**
     * 查找联系人
     * @param s
     */
    private void searchContactServer(String s) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(s);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String str = reader.readLine();
                    Message msg = new Message();
                    if("false".equals(str)){
                        msg.what = 2;
                    }else{
                        msg.what = 1;
                        Gson gson = new Gson();
                        user = gson.fromJson(str,User.class);
                    }
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
        btnSearch = findViewById(R.id.search_contact_btn);
        etName = findViewById(R.id.search_contact_et);
    }
}