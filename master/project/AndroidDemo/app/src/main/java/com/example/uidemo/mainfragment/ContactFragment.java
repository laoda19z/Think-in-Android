package com.example.uidemo.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.LoginActivity;
import com.example.uidemo.R;
import com.example.uidemo.adapter.ContactAdapter;
import com.example.uidemo.beans.Dynamic;
import com.example.uidemo.beans.User;
import com.example.uidemo.chat.ChatActivity;
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

public class ContactFragment extends FragmentActivity {
    private View view;
    private ListView listView;
    private List<User> userList = new ArrayList<>();
    private Handler handler;
    private ContactAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.chat_fragment,null);
        listView = view.findViewById(R.id.contact_listview);

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        adapter.setUserList(userList);
                        listView.setAdapter(adapter);
                        break;
                }
            }
        };

        initContactInfo(ConfigUtil.SERVER_ADDR+"SearchMyContactServlet?userid="+ LoginActivity.currentUserId);
        adapter = new ContactAdapter(view.getContext(),R.layout.chat_contact_item,userList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int chatId = userList.get(i).getUserId();
                Log.e("mll","chatid为："+chatId);
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("ec_chat_id", chatId+"");
                String contactName ="";
                String contactHead ="";
                for(int m = 0;m<userList.size();++m){
                    if(userList.get(m).getUserId()==chatId){
                        contactName = userList.get(m).getUsername();
                        contactHead = userList.get(m).getHeadImg();
                    }
                }
                intent.putExtra("ec_chat_name",contactName);
                intent.putExtra("receiverhead",contactHead);
                context.startActivity(intent);
            }
        });
        return view;
    }
    /**
     * 加载联系人信息
     */
    private void initContactInfo(final String s) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(s);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = reader.readLine().toString();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<User>>(){}.getType();
                    userList = gson.fromJson(str,type);
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
