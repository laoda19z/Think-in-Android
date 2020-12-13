package com.example.uidemo.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.adapter.ReportAdapter;
import com.example.uidemo.record.entitys.AssessmentReport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportListActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private List<AssessmentReport> reports = new ArrayList<>();
    private GridView gridView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    reports = (List<AssessmentReport>)msg.obj;
                    ReportAdapter reportAdapter = new ReportAdapter(
                            reports,
                            ReportListActivity.this,
                            R.layout.report_list_item
                    );
                    gridView.setAdapter(reportAdapter);
                    mDialog.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        mDialog = new ProgressDialog(ReportListActivity.this);
        mDialog.setMessage("获取数据中，请稍后...");
        mDialog.show();
        gridView = findViewById(R.id.gv_report);
        String str = getIntent().getStringExtra("id");
        int childId = Integer.parseInt(str);
        getReportListFromServer(ConfigUtil.SERVER_ADDR+"GetReportsByChildIdServlet",childId);
    }
    public void getReportListFromServer(final String s,int childId){
        new Thread(){
            @Override
            //使用URL和HttpURLConnection
            public void run() {
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    //获取网络输出流
                    OutputStream out = conn.getOutputStream();
                    out.write((childId+"").getBytes());
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String str = reader.readLine();
                    //转换信息
                    Gson gson = new Gson();
                    List<AssessmentReport> reports = gson.fromJson(str,new TypeToken<List<AssessmentReport>>() {}.getType());
                    //发送信息
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = reports;
                    handler.sendMessage(msg);
                    out.close();
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}