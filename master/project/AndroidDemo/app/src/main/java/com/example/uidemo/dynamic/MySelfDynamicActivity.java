package com.example.uidemo.dynamic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
import com.example.uidemo.R;
import com.example.uidemo.adapter.DynamicAdapter;
import com.example.uidemo.beans.Dynamic;
import com.example.uidemo.beans.User;
import com.example.uidemo.mainfragment.MyselfFragment;
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

public class MySelfDynamicActivity extends AppCompatActivity {
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
    private TextView contentView;
    private int position;
    private int currentpage = 1;//当前已经加载的页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_single_dynamic);


        userid = Integer.parseInt(LoginActivity.currentUserId);
        findViews();
        initData();

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
                        Glide.with(MySelfDynamicActivity.this).load(ConfigUtil.SERVER_ADDR + users.get(0).getHeadImg()).circleCrop().into(iv_head);
                        break;
                    case 3:
                        dynamics.remove(position);
                        adapter.setDynamic(dynamics);
                        myListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        String result = (String) msg.obj;
//                        Toast.makeText(MySelfDynamicActivity.this,result,Toast.LENGTH_SHORT).show();

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
            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                int id = dynamics.get(i).getDynamicId();
                String url = ConfigUtil.SERVER_ADDR+"DeleteDynamicServlet?dynamicid="+id;
                showPopupWindow(url);
                return false;
            }
        });
    }

    /**
     * 显示PopupWindow
     */
    private void showPopupWindow(String url){
        //设置它的视图
        View view = getLayoutInflater().inflate(R.layout.popupwindow,null);
        //创建PopupWindow对象
        final PopupWindow popupWindow = new PopupWindow(this);
        //设置弹出窗口的宽度
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置视图当中控件的属性和监听器
        Button btnCancel = view.findViewById(R.id.btn_popupwindow_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭弹出窗
                popupWindow.dismiss();
            }
        });
        Button btnDelete = view.findViewById(R.id.btn_popupwindow_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTrend(url);
                popupWindow.dismiss();
            }
        });
        //不同的方法，名字相同
        popupWindow.setContentView(view);
        //指定弹出窗显示在某个控件的下方
        popupWindow.showAsDropDown(contentView);
    }

    /**
     * 删除动态
     */
    private void deleteTrend(String s) {
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
                    msg.what = 3;
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
        iv_head = findViewById(R.id.trend_person_head);
        tv_name = findViewById(R.id.trend_person_user);
        myListView = findViewById(R.id.trend_person_listview);
        srl = findViewById(R.id.person_trend_srl);
        contentView = findViewById(R.id.hhh);
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

}