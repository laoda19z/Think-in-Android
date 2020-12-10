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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
    private BarChart bar;
    private List<AssessmentReport> list0;
    private List<BarEntry> list;
    private int child;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.frag_maxscore, container, false);
        bar=root.findViewById(R.id.redar);
        list=new ArrayList<>();
        gson=new Gson();

        Bundle bundle=getActivity().getIntent().getExtras();
        child=bundle.getInt("id");

        GetList();
        return root;
    }

    //获取孩子id现在没用。
//    private void GetChildid() {
//        new Thread(){
//            @Override
//            public void run() {
//                URL urlpath= null;
//                try {
//                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"/GetChildidServlet");
//                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
//                    //传过userid，根据userid查询
//                    conn.setRequestMethod("POST");
//                    OutputStream out1 = conn.getOutputStream();
//                    out1.write((""+child).getBytes());
//                    out1.flush();
//                    InputStream in = conn.getInputStream();
//                    BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
//                    String textjson=buffer.readLine();
//                    Log.i("max",textjson);
//                    Type type=new TypeToken<List<Relation>>(){}.getType();
//                    list1=gson.fromJson(textjson,type);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

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
                    out1.write((""+child).getBytes());
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
        list.add(new BarEntry(1,assessmentReport.getBodyScore()/10));
        list.add(new BarEntry(2,assessmentReport.getDownScore()/10));
        list.add(new BarEntry(3,assessmentReport.getUpScore()/10));

        BarDataSet barDataSet=new BarDataSet(list,"当前孩子");   //list是你这条线的数据  "语文" 是你对这条线的描述
        BarData barData=new BarData(barDataSet);
        bar.setData(barData);


        //折线图背景
        bar.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        bar.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）

        //对于右下角一串字母的操作
        bar.getDescription().setEnabled(false);                  //是否显示右下角描述
        bar.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        bar.getDescription().setTextSize(20);                    //字体大小
        bar.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        Legend legend=bar.getLegend();
        legend.setEnabled(true);    //是否显示图例
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置

        //X轴
        XAxis xAxis=bar.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.setAxisLineColor(Color.RED);   //X轴颜色
        xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        xAxis.setValueFormatter(new IAxisValueFormatter() {   //X轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==1){
                    return "身形得分";
                }
                if (v==2){
                    return "下肢得分";
                }
                if (v==3){
                    return "上肢得分";
                }
                return "";//注意这里需要改成 ""
            }
        });
        xAxis.setAxisMaximum(4);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(4,false);


        //Y轴
        YAxis AxisLeft=bar.getAxisLeft();
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(Color.RED);  //Y轴颜色
        AxisLeft.setAxisLineWidth(2);           //Y轴粗细
        AxisLeft.setValueFormatter(new IAxisValueFormatter() {  //Y轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {

                for (int a=0;a<11;a++){     //用个for循环方便
                    if (a==v){
                        return a*10+"";
                    }
                }

                return "";
            }
        });
        AxisLeft.setAxisMaximum(11);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        AxisLeft.setLabelCount(11,false);

        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        bar.getAxisRight().setEnabled(false);


        //柱子
//        barDataSet.setColor(Color.BLACK);  //柱子的颜色
        barDataSet.setColors(Color.BLUE);//设置柱子多种颜色  循环使用
        barDataSet.setBarBorderWidth(2);       //柱子边框厚度
        barDataSet.setBarShadowColor(Color.RED);
        barDataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
        barDataSet.setStackLabels(new String[]{"aaa","bbb","ccc"});
        //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
        barDataSet.setValueTextSize(20);
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v*10+"分";
                }
                return "";
            }
        });

        //数据更新
        bar.notifyDataSetChanged();
        bar.invalidate();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
