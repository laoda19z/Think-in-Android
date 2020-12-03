package com.example.uidemo.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.adapter.DynamicAdapter;
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


public class CommunicityFragment2 extends FragmentActivity {
    private View view;
    private List<Dynamic> dynamics = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private DynamicAdapter adapter;
    private Handler handler;
    private ListView listView;
    private Button btnPublishTrend;

    @Override
    public View onCreateView(@NonNull final String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.communicity_fragment2, null);
        listView = view.findViewById(R.id.trends_listview);
        btnPublishTrend = view.findViewById(R.id.btn_publishtrends);
        initData();
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.setDynamic(dynamics);
                        adapter.setUsers(users);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
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

        return view;
    }


    /**
     * 数据初始化
     */
    private void initData() {
        if(dynamics.size() == 0){
            showDynamic(ConfigUtil.SERVER_ADDR + "ShowDynamicServlet");
        }
        adapter = new DynamicAdapter(view.getContext(), dynamics, R.layout.trends_item);
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
