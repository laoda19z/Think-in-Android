package com.example.uidemo.record.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.record.entitys.ClockRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class SportImpressionsFrament extends Fragment {
    private View root;
    private TextView two;
    private TextView date;
    private int child;

    private String text;
    private List<ClockRecord> list;
    private Gson gson;

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    showdata(list);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.frag_sportimpressions, container, false);
        two=root.findViewById(R.id.two);
        two.setMovementMethod(ScrollingMovementMethod.getInstance());
        date=root.findViewById(R.id.date);

        Bundle bundle=getActivity().getIntent().getExtras();
        child=bundle.getInt("id");

        list=new ArrayList<>();
        gson=new Gson();
        text="";
        GetText();

        return root;
    }

    private void GetText() {
        new Thread(){
            @Override
            public void run() {
                URL urlpath= null;
                try {
                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"/SportImpressionsServlet");
                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
                    //传过userid，根据userid查询
                    conn.setRequestMethod("POST");
                    OutputStream out1 = conn.getOutputStream();
                    out1.write((""+child).getBytes());
                    out1.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String textjson= URLDecoder.decode(buffer.readLine(),"utf8");
                    Log.i("impress",textjson);
                    Type type=new TypeToken<List<ClockRecord>>(){}.getType();
                    list=gson.fromJson(textjson,type);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //把从数据库中查询得到的数据显示
    private void showdata(List<ClockRecord> list) {
        ClockRecord record=list.get(0);
        text=record.getSportImpressions();
        two.setText("        "+text);
        date.setText(record.getMarkDate());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
