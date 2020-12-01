package com.example.uidemo.record.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.record.entitys.AssessmentReport;
import com.example.uidemo.record.entitys.Relation;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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
import java.util.ArrayList;
import java.util.List;


public class MaxScoreFragment extends Fragment {
    private View root;
    private RadarChart redarChart;
    private List<AssessmentReport> list0;
    private List<RadarEntry> list;
    private List<Relation> list1;
    private int userid;
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.frag_maxscore, container, false);
        redarChart=root.findViewById(R.id.redar);
        list=new ArrayList<>();
        list1=new ArrayList<>();
        gson=new Gson();

        Bundle bundle=getActivity().getIntent().getExtras();
        userid=bundle.getInt("id");

        GetChildid();
        return root;
    }
    private void GetChildid() {
        new Thread(){
            @Override
            public void run() {
                URL urlpath= null;
                try {
                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"/GetChildidServlet");
                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
                    //传过userid，根据userid查询
                    conn.setRequestMethod("POST");
                    OutputStream out1 = conn.getOutputStream();
                    out1.write((""+userid).getBytes());
                    out1.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String textjson=buffer.readLine();
                    Log.i("max",textjson);
                    Type type=new TypeToken<List<Relation>>(){}.getType();
                    list1=gson.fromJson(textjson,type);
                    GetList();
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

    private void GetList() {
        new Thread(){
            @Override
            public void run() {
                URL urlpath= null;
                try {
                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"/MaxScoreServlet");
                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
                    conn.setRequestMethod("POST");
                    //传过userid，根据userid查询
                    OutputStream out1 = conn.getOutputStream();
                    out1.write((""+list1.get(0).getChildid()).getBytes());
                    out1.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String textjson=buffer.readLine();
                    Log.i("max",textjson);
                    Type type=new TypeToken<List<AssessmentReport>>(){}.getType();
                    list0=gson.fromJson(textjson,type);
                    showdata(list0);
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

    private void showdata(List<AssessmentReport> list0) {
        AssessmentReport assessmentReport=list0.get(0);
        list=new ArrayList<>();
        list.add(new RadarEntry(assessmentReport.getBodyScore()));
        list.add(new RadarEntry(assessmentReport.getDownScore()));
        list.add(new RadarEntry(assessmentReport.getUpScore()));

        RadarDataSet radarDataSet=new RadarDataSet(list,"小孩一");
        radarDataSet.setColor(Color.rgb(	0 ,191 ,255));
        radarDataSet.setDrawFilled(true);
        radarDataSet.setLineWidth(2f);


        RadarData radarData=new RadarData(radarDataSet);
        redarChart.setData(radarData);
        redarChart.setExtraBottomOffset(5f);
        redarChart.setScaleX(1.1f);
        redarChart.setScaleY(1.1f);
        redarChart.setWebLineWidth(2f);
        // 内部线条宽度，外面的环状线条
        redarChart.setWebLineWidthInner(2f);
        // 所有线条WebLine透明度
        redarChart.setWebAlpha(100);
        //Y轴最小值不设置会导致数据中最小值默认成为Y轴最小值
        redarChart.getYAxis().setAxisMinimum(0);
        //大字的颜色（中心点和各顶点的连线）
        redarChart.setWebColor(Color.rgb(0,0,0));
        //所有三角形的颜色
        redarChart.setWebColorInner(Color.rgb(0,0,0));
        //整个控件的背景颜色

        XAxis xAxis=redarChart.getXAxis();
        xAxis.setTextColor(Color.rgb(0,0,0));//X轴字体颜色
        xAxis.setTextSize(20);     //X轴字体大小
        //自定义X轴坐标描述（也就是五个顶点上的文字,默认是0、1、2、3、4）
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==0){
                    return "身形得分";
                }
                if (v==1){
                    return "下肢得分";
                }
                if (v==2){
                    return "上肢得分";
                }
                return "";
            }
        });


        //是否绘制雷达框上对每个点的数据的标注    和Y轴坐标点一般不同时存在 否则显得很挤  默认为true
        radarDataSet.setDrawValues(false);
        radarDataSet.setValueTextSize(20);  //数据值得字体大小（这里只是写在这）
        radarDataSet.setValueTextColor(Color.CYAN);//数据值得字体颜色（这里只是写在这）

        YAxis yAxis=redarChart.getYAxis();
        //是否绘制Y轴坐标点  和雷达框数据一般不同时存在 否则显得很挤 默认为true
        yAxis.setDrawLabels(true);
        yAxis.setTextColor(Color.rgb(238 ,121, 66));//Y轴坐标数据的颜色
        yAxis.setAxisMaximum(80);   //Y轴最大数值
        yAxis.setAxisMinimum(0);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        yAxis.setLabelCount(5,false);


        //对于右下角一串字母的操作
        redarChart.getDescription().setEnabled(false);                  //是否显示右下角描述
        redarChart.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        redarChart.getDescription().setTextSize(20);                    //字体大小
        redarChart.getDescription().setTextColor(Color.CYAN);             //字体颜色

        //图例
        Legend legend=redarChart.getLegend();
        legend.setEnabled(true);    //是否显示图例
        legend.setDrawInside(true);
        legend.setFormSize(15);
        legend.setTextSize(15);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
