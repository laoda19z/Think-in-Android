package com.example.uidemo.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.MainActivity;
import com.example.uidemo.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;



public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvOverallScore;
    private TextView tvBodyScore;
    private TextView tvUpScore;
    private TextView tvDownScore;
    private TextView tvSuggestion;
    private ImageView imgBack;
    private Report report;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Report report = (Report) msg.obj;
                    tvBodyScore.setText(report.getBodyScore()+"分");
                    tvDownScore.setText(report.getDownScore()+"分");
                    tvUpScore.setText(report.getUpScore()+"分");
                    tvOverallScore.setText(report.getOverallScore()+"");

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String str = getIntent().getStringExtra("json");
        Log.i("张绍达",str);
        findViews();
        setListener();
        getReportFromServer(ConfigUtil.SERVER_ADDR +"/GetReportServlet",str);
    }

    private void setListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        tvBodyScore = findViewById(R.id.tv_body_score);
        tvDownScore = findViewById(R.id.tv_down_score);
        tvUpScore = findViewById(R.id.tv_up_score);
        tvSuggestion = findViewById(R.id.tv_suggestion);
        tvOverallScore = findViewById(R.id.tv_overall_score);
        imgBack = findViewById(R.id.img_back);
    }

    public void getReportFromServer(final String s,String data){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //发送用户名称至服务端
                    conn.setRequestMethod("POST");
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    //接收信息
                    InputStream in = conn.getInputStream();
                    //使用字符流读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    //读取字符信息
                    String str = reader.readLine();
                    //将字符串转化为Json格式
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                    Gson gson = new Gson();
                    Report report = gson.fromJson(jsonObject,Report.class);
                    //发送修改数据
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = report;
                    handler.sendMessage(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}