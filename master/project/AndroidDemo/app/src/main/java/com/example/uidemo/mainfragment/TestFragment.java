package com.example.uidemo.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.uidemo.R;
import com.example.uidemo.test.BasicInfoActivity;
import com.example.uidemo.test.Child;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.uidemo.ConfigUtil.SERVER_ADDR;
import static com.example.uidemo.LoginActivity.currentChildId;

public class TestFragment extends FragmentActivity {
    private View view;
    private TextView tvTest;
    private String childData;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    childData = (String) msg.obj;
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.page_test,null);
        Log.e("mll",currentChildId+"");
        if (currentChildId == 0){
            Toast.makeText(context, "请输入", Toast.LENGTH_SHORT).show();
        }else {
            getChildInfoFromServer(SERVER_ADDR+"GetChildServlet",currentChildId);
            tvTest = view.findViewById(R.id.tv_test);
            tvTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JsonObject jsonObject = new JsonObject();
                    //创建孩子类
                    Child child = new Gson().fromJson(childData,Child.class);
                    //导入数据
                    jsonObject.addProperty("id",child.getChildId()+"");
                    jsonObject.addProperty("xb",child.getChildSex());
                    jsonObject.addProperty("nl",child.getChildAge()+"");
                    jsonObject.addProperty("nj",child.getChildGrade()+"");
                    Intent intent = new Intent();
                    intent.putExtra("json",jsonObject.toString());
                    intent.setClass(context, BasicInfoActivity.class);
                    context.startActivity(intent);
                    finish();
                }
            });
        }
        return view;
    }
    public void getChildInfoFromServer(final String s,int childId){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //发送用户名称至服务端
                    conn.setRequestMethod("POST");
                    OutputStream out = conn.getOutputStream();
                    out.write((childId+"").getBytes());
                    //接收信息
                    InputStream in = conn.getInputStream();
                    //使用字符流读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    //读取字符信息
                    String str = reader.readLine();
                    Log.i("zsd",str);
                    //关闭流
                    reader.close();
                    in.close();
                    //创建Message
                    Message msg = new Message();
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
}
