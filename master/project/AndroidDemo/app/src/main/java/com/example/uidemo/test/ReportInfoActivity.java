package com.example.uidemo.test;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.record.entitys.AssessmentReport;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class ReportInfoActivity extends AppCompatActivity {

    private RadarChart radarChart;
    private ProgressDialog mDialog;
    private TextView tvOverallScore;
    private TextView tvBodyScore;
    private TextView tvUpScore;
    private TextView tvDownScore;
    private ImageView imgBack;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    AssessmentReport report = (AssessmentReport) msg.obj;
                    tvBodyScore.setText(report.getBodyScore()+"分");
                    tvDownScore.setText(report.getDownScore()+"分");
                    tvUpScore.setText(report.getUpScore()+"分");
                    tvOverallScore.setText(report.getOverallScore()+"");
                    initChart(report);
                    mDialog.dismiss();

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info);
        mDialog = new ProgressDialog(ReportInfoActivity.this);
        mDialog.setMessage("正在获取信息，请稍后...");
        mDialog.show();
        String str = getIntent().getStringExtra("id");
        findViews();
        int reportId = Integer.parseInt(str);
        getReportInfoFromServer(ConfigUtil.SERVER_ADDR+"GetReportByReportId",reportId);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void findViews() {
        tvBodyScore = findViewById(R.id.tv_body_score);
        tvDownScore = findViewById(R.id.tv_down_score);
        tvUpScore = findViewById(R.id.tv_up_score);
        tvOverallScore = findViewById(R.id.tv_overall_score);
        imgBack = findViewById(R.id.img_back);
        radarChart = findViewById(R.id.chart_rc);
    }
    public void getReportInfoFromServer(final String s,int reportId){
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
                    out.write((reportId+"").getBytes());
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String str = reader.readLine();
                    //转换信息
                    Gson gson = new Gson();
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(str).getAsJsonObject();
                    AssessmentReport report = gson.fromJson(jsonObject,AssessmentReport.class);
                    //发送数据
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = report;
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
    public void initChart(AssessmentReport report){
        radarChart = (RadarChart) findViewById(R.id.chart_rc);
        List<RadarEntry> radarEntries = new ArrayList<>();
        radarEntries.add(new RadarEntry(report.getUpScore()));
        radarEntries.add(new RadarEntry(report.getBodyScore()));
        radarEntries.add(new RadarEntry(report.getDownScore()));
        RadarDataSet iRadarDataSet = new RadarDataSet(radarEntries, "个人成绩");
        iRadarDataSet.setColor(Color.BLACK);
        RadarData radarData=new RadarData(iRadarDataSet);
        radarChart.setData(radarData);
        radarChart.getYAxis().setAxisMinimum(0);
        radarChart.setBackgroundColor(Color.parseColor("#e5e5e5"));
        XAxis xAxis=radarChart.getXAxis();
        xAxis.setTextColor(Color.BLACK);//X轴字体颜色
        xAxis.setTextSize(16);     //X轴字体大小
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==0){
                    return "上肢得分";
                }
                if (v==1){
                    return "身体得分";
                }
                if (v==2){
                    return "下肢得分";
                }
                return "";
            }
        });
    }
}