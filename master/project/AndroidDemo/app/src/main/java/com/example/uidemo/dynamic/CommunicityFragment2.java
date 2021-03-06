package com.example.uidemo.dynamic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.adapter.DynamicAdapter;
import com.example.uidemo.beans.Dynamic;
import com.example.uidemo.beans.User;
import com.example.uidemo.mainfragment.CommunicityFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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


public class CommunicityFragment2 extends FragmentActivity {
    private ProgressDialog dialog;
    private View view;
    private SmartRefreshLayout srl;
    private List<Dynamic> dynamics = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<User> commUsers = new ArrayList<>();
    private DynamicAdapter adapter;
    private EventBus eventBus;
    private Handler handler;
    private ListView listView;
    private Button btnPublishTrend;
    private Button btnMyselfTrend;
    private int page = 1;

    @Override
    public View onCreateView(@NonNull final String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.communicity_fragment2, null);
        listView = view.findViewById(R.id.trends_listview);
        btnPublishTrend = view.findViewById(R.id.btn_publishtrends);
        btnMyselfTrend = view.findViewById(R.id.btn_myselftrends);
        srl = view.findViewById(R.id.all_trend_srl);
        dialog = new ProgressDialog(context);
        initData(context);
        eventBus = EventBus.getDefault();
        if(!eventBus.isRegistered(CommunicityFragment2.this)){
            eventBus.register(CommunicityFragment2.this);
        }
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.setDynamic(dynamics);
                        adapter.setUsers(users);
                        adapter.setCommUsers(commUsers);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                        break;
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("userid",dynamics.get(i).getUserId());
                intent.setClass(context.getApplicationContext(), PersonSingleDynamicActivity.class);
                context.startActivity(intent);
            }
        });
        btnPublishTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), PublishTrendsActivity.class);
                context.startActivity(intent);
            }
        });
        btnMyselfTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),MySelfDynamicActivity.class);
                context.startActivity(intent);
            }
        });
        //给智能刷新控件注册下拉刷新事件监听器
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                dialog.setMessage("正在刷新，请稍后...");
                dialog.show();
                dynamics.clear();
                showDynamic(ConfigUtil.SERVER_ADDR + "ShowDynamicServlet?page=1");
                //通知刷新完毕
                srl.finishRefresh();
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                showDynamic(ConfigUtil.SERVER_ADDR + "ShowDynamicServlet?page="+page);
                srl.finishLoadMoreWithNoMoreData();
            }
        });

        return view;
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.ASYNC)
    public void onEvent(PublishTrendsActivity activity){
        dynamics.clear();
        showDynamic(ConfigUtil.SERVER_ADDR + "ShowDynamicServlet?page=1");
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.ASYNC)
    public void onDeleteEvent(MySelfDynamicActivity activity){
        dynamics.clear();
        showDynamic(ConfigUtil.SERVER_ADDR + "ShowDynamicServlet?page=1");
    }
    /**
     * 数据初始化
     */
    private void initData(Context context) {
        if(dynamics.size() == 0){
            showDynamic(ConfigUtil.SERVER_ADDR + "ShowDynamicServlet?page="+page);
        }
        adapter = new DynamicAdapter(view.getContext(), dynamics, R.layout.trends_item);
//        View header = View.inflate(context,R.layout.header,null);
//        Button btnPublish = header.findViewById(R.id.btn_publishtrends);
//        btnPublish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), PublishTrendsActivity.class);
//                context.startActivity(intent);
//            }
//        });
//        Button btnMyself = header.findViewById(R.id.btn_myselftrends);
//        btnMyself.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(),MySelfDynamicActivity.class);
//                context.startActivity(intent);
//            }
//        });
//        listView.addHeaderView(header);
        listView.setAdapter(adapter);
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
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type type = new TypeToken<Map<String ,String>>(){}.getType();
                    Map<String,String> maps = gson.fromJson(str,type);
                    //获取动态的信息
                    String dynamicInfo = maps.get("dynamic");
                    Type type1 = new TypeToken<List<Dynamic>>() {}.getType();
                    List<Dynamic> dynamicList = gson.fromJson(dynamicInfo,type1);
                    for(Dynamic dynamic:dynamicList){
                        dynamics.add(dynamic);
                    }
                    //获取发布动态的用户信息
                    String userinfo = maps.get("users");
                    Type type2 = new TypeToken<List<User>>(){}.getType();
                    List<User> userList = gson.fromJson(userinfo,type2);
                    for(User user:userList){
                        users.add(user);
                    }
                    //获取动态评论区的用户信息
                    String commUserInfo = maps.get("commusers");
                    Type type3 = new TypeToken<List<User>>(){}.getType();
                    List<User> commUserList = gson.fromJson(commUserInfo,type3);
                    for(User user:commUserList){
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
