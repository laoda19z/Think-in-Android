package com.example.uidemo.dynamic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.adapter.DynamicAdapter;
import com.example.uidemo.beans.Comment;
import com.example.uidemo.beans.Dynamic;
import com.example.uidemo.beans.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonSingleDynamicActivity extends AppCompatActivity {
    private ListView myListView;
    private ImageView iv_head;
    private TextView tv_name;
    private DynamicAdapter adapter;
    private List<User> users = new ArrayList<>();
    private List<Dynamic> dynamics = new ArrayList<>();
    private Handler handler;
    private int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_single_dynamic);

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",0);
        findViews();
        initData();
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        adapter.setDynamic(dynamics);
                        adapter.setUsers(users);
                        myListView.setAdapter(adapter);
                        for(int i = 0 ;i<users.size();++i){
                            if (users.get(i).getUserId()==userid){
                                tv_name.setText(users.get(i).getUsername());
                                Glide.with(PersonSingleDynamicActivity.this).load(users.get(i).getHeadImg()).circleCrop().into(iv_head);
                            }
                        }
                        break;
                }
            }
        };

    }

    private void initData() {
        String requestParam = "?userid="+userid+"&page="+1;
        if (userid != 0) {
            showDynamic(ConfigUtil.SERVER_ADDR+"ShowOwnerDynamicServlet"+requestParam);
        }else {
            Toast.makeText(this,"该用户并未发布任何动态",Toast.LENGTH_SHORT).show();
        }
        adapter = new DynamicAdapter(this, dynamics, R.layout.trends_item);
        myListView.setAdapter(adapter);
    }

    private void findViews() {
        iv_head = findViewById(R.id.trend_person_head);
        tv_name = findViewById(R.id.trend_person_user);
        myListView = findViewById(R.id.trend_person_listview);
    }

    public void clickIntoChat(View view) {
        switch (view.getId()){
            case R.id.trend_person_head:

                break;
        }
    }
    private void showDynamic(final String s) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(s);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = reader.readLine().toString();
                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String ,String>>(){}.getType();
                    Map<String,String> maps = gson.fromJson(str,type);
                    //获取动态的信息
                    String dynamicInfo = maps.get("dynamic");
                    Type type1 = new TypeToken<List<Dynamic>>() {}.getType();
                    List<Dynamic> dynamicList = gson.fromJson(dynamicInfo,type1);
                    dynamics = dynamicList;
                    //获取发布动态的用户信息
                    String userinfo = maps.get("users");
                    Type type2 = new TypeToken<List<User>>(){}.getType();
                    List<User> userList = gson.fromJson(userinfo,type2);
                    users = userList;
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    reader.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}