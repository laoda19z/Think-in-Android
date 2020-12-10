package com.example.uidemo.dynamic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    private List<User> commUsers = new ArrayList<>();
    private Handler handler;
    private int userid;
    private SmartRefreshLayout srl;
    private int currentpage = 1;//当前已经加载的页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_single_dynamic);

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", 0);
        findViews();
        initData();
        Log.e("mll","userid:"+userid);
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.setDynamic(dynamics);
                        adapter.setUsers(users);
                        adapter.setCommUsers(commUsers);
                        myListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        tv_name.setText(users.get(0).getUsername());
                        Glide.with(PersonSingleDynamicActivity.this).load(ConfigUtil.SERVER_ADDR + users.get(0).getHeadImg()).circleCrop().into(iv_head);
                        break;
                }
            }
        };
    }

    private void initData() {
        srl.setReboundDuration(2000);
        String requestParam = "?userid=" + userid + "&page=" + currentpage;
        if (userid != 0) {
            showDynamic(ConfigUtil.SERVER_ADDR + "ShowOwnerDynamicServlet" + requestParam);
        } else {
            Toast.makeText(this, "该用户并未发布任何动态", Toast.LENGTH_SHORT).show();
        }
        adapter = new DynamicAdapter(this, dynamics, R.layout.trends_item);
//        View header = View.inflate(PersonSingleDynamicActivity.this,R.layout.header,null);
//        Button btn = header.findViewById(R.id.btn_publishtrends);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(PersonSingleDynamicActivity.this,"点击了",Toast.LENGTH_SHORT).show();
//            }
//        });
//        myListView.addHeaderView(header);
        myListView.setAdapter(adapter);

        //给智能刷新控件注册下拉刷新事件监听器
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dynamics.clear();
                String requestParam = "?userid=" + userid + "&page=1";
                showDynamic(ConfigUtil.SERVER_ADDR + "ShowOwnerDynamicServlet" + requestParam);
                //通知刷新完毕
                srl.finishRefresh();
            }
        });
        //给智能刷新控件注册上拉加载更多事件监听器
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentpage++;
                String requestParam = "?userid=" + userid + "&page=" + currentpage;
                showDynamic(ConfigUtil.SERVER_ADDR + "ShowOwnerDynamicServlet" + requestParam);
                srl.finishLoadMoreWithNoMoreData();
                //假设超过10条数据加载完毕（不可以一直加载数据库里的）
//                if(dynamics.size()<10){
//                    String requestParam = "?userid="+userid+"&page="+currentpage;
//                    currentpage++;
//                    showDynamic(ConfigUtil.SERVER_ADDR+"ShowOwnerDynamicServlet"+requestParam);
//                    //通知加载完毕，通知srl
//                    srl.finishLoadMore();
//                }else{
//                    //通知没有更多数据加载，数据库里没有更多文件了
//                    srl.finishLoadMoreWithNoMoreData();
//                }
            }
        });
    }

    private void findViews() {
        iv_head = findViewById(R.id.trend_person_head);
        tv_name = findViewById(R.id.trend_person_user);
        myListView = findViewById(R.id.trend_person_listview);
        srl = findViewById(R.id.person_trend_srl);
    }

    public void clickIntoChat(View view) {
        switch (view.getId()) {
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
                    Log.e("mll", "str的内容为：" + str);

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();
                    Map<String, String> maps = gson.fromJson(str, type);
                    //获取动态的信息
                    String dynamicInfo = maps.get("dynamic");
                    Type type1 = new TypeToken<List<Dynamic>>() {
                    }.getType();
                    List<Dynamic> dynamicList = gson.fromJson(dynamicInfo, type1);
                    for (Dynamic dynamic : dynamicList) {
                        dynamics.add(dynamic);
                    }
                    //获取发布动态的用户信息
                    String userinfo = maps.get("users");
                    Type type2 = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> userList = gson.fromJson(userinfo, type2);
                    for (User user : userList) {
                        users.add(user);
                    }
                    //获取动态评论区的用户信息
                    String commUserInfo = maps.get("commusers");
                    Type type3 = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> commUserList = gson.fromJson(commUserInfo, type3);
                    for (User user : commUserList) {
                        commUsers.add(user);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        currentpage = 1;
    }
}