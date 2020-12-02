package com.example.uidemo.record.fragment;

import android.graphics.Color;
import android.os.Bundle;
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
import com.example.uidemo.record.entitys.AssessmentReport;
import com.example.uidemo.record.entitys.Relation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

public class ScoreRecordFragment extends Fragment {
    private View root;
    private TextView see;
    private List<Relation> listid;
    private int userid;
    private LineChart line;
    private List<AssessmentReport> list0;
    List<Entry> list1=new ArrayList<>();
    List<Entry> list2=new ArrayList<>();
    List<Entry> list3=new ArrayList<>();
    List<Entry> list4=new ArrayList<>();
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.frag_scorerecord, container, false);
        gson=new Gson();
        see=root.findViewById(R.id.see);
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        list3=new ArrayList<>();
        list4=new ArrayList<>();
        listid=new ArrayList<>();
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
                    Log.i("scoreRecord",textjson);
                    Type type=new TypeToken<List<Relation>>(){}.getType();
                    listid=gson.fromJson(textjson,type);
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
                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"/ScoreRecordServlet");
                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
                    //传过userid，根据userid查询
                    conn.setRequestMethod("POST");
                    OutputStream out1 = conn.getOutputStream();
                    out1.write((""+listid.get(0).getChildid()).getBytes());
                    out1.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String textjson=buffer.readLine();
                    Log.i("scoreRecord",textjson);
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
        int i=0;
        for(AssessmentReport rs:list0){
            list1.add(new Entry(i,rs.getBodyScore()));
            list2.add(new Entry(i,rs.getUpScore()));
            list3.add(new Entry(i,rs.getDownScore()));
            list4.add(new Entry(i,rs.getOverallScore()));
            i++;
        }
        if(list0.get(0).getOverallScore()<list0.get(i-1).getOverallScore()){
            see.setText("        几次测评显示，您的孩子的成绩稳步上升，继续加油吧！");
        }else {
            see.setText("        好像并没有明显的提升，继续努力吧！");
        }
        line=root.findViewById(R.id.linechart);


        LineDataSet lineDataSet=new LineDataSet(list1,"身形得分");   //list是你这条线的数据  "语文" 是你对这条线的描述
        LineDataSet lineDataSet1=new LineDataSet(list2,"上肢得分");
        LineDataSet lineDataSet2=new LineDataSet(list3,"下肢得分");
        LineDataSet lineDataSet3=new LineDataSet(list4,"综合得分");
        LineData lineData=new LineData(lineDataSet);
        lineData.addDataSet(lineDataSet1);
        lineData.addDataSet(lineDataSet2);
        lineData.addDataSet(lineDataSet3);
        line.setData(lineData);

        line.getXAxis().setDrawGridLines(true);  //是否绘制X轴上的网格线（背景里面的竖线）
        line.getAxisLeft().setDrawGridLines(true);  //是否绘制Y轴上的网格线（背景里面的横线）

        //对于右下角一串字母的操作
        line.getDescription().setEnabled(false);                  //是否显示右下角描述
        line.getDescription().setText("这是折线图");    //修改右下角字母的显示
        line.getDescription().setTextSize(20);                    //字体大小
        line.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        Legend legend=line.getLegend();
        legend.setEnabled(true);    //是否显示图例
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置

        //X轴
        XAxis xAxis=line.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisLineWidth(2);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        final int finalI = i;
        xAxis.setValueFormatter(new IAxisValueFormatter() {   //X轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                for (int a = 0; a<(finalI +1); a++){
                    if (a==v){
                        return a+"";
                    }
                }
                return "";
            }
        });
        xAxis.setAxisMaximum(finalI);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(6,false);


        //Y轴
        YAxis AxisLeft=line.getAxisLeft();
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(Color.BLACK);
        AxisLeft.setAxisLineWidth(2);
        AxisLeft.setValueFormatter(new IAxisValueFormatter() {  //Y轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                for (int a=0;a<110;a=a+10){
                    if (a==v){
                        return a+"";
                    }
                }
                return "";
            }
        });
        AxisLeft.setAxisMaximum(110);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
        AxisLeft.setLabelCount(11,false);
        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        line.getAxisRight().setEnabled(false);


        //折线
        //设置折线的式样   这个是圆滑的曲线（有好几种自己试试）     默认是直线
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setColor(Color.BLUE);  //折线的颜色
        lineDataSet.setLineWidth(2);        //折线的粗细
        //是否画折线点上的空心圆  false表示直接画成实心圆
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCircleHoleRadius(3);  //空心圆的圆心半径
        //圆点的颜色     可以实现超过某个值定义成某个颜色的功能   这里先不讲 后面单独写一篇
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setCircleRadius(3);      //圆点的半径
        //定义折线上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v+"";
                }
                return "";
            }
        });

        lineDataSet1.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet1.setColor(Color.rgb(	139 ,0, 0));
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setCircleHoleRadius(3);
        lineDataSet1.setCircleColor(Color.RED);
        lineDataSet1.setValueTextSize(10);
        lineDataSet1.setCircleRadius(3);
        lineDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v+"";
                }
                return "";
            }
        });
        lineDataSet2.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet2.setColor(Color.rgb(	255, 0, 0));
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setCircleHoleRadius(3);
        lineDataSet2.setCircleColor(Color.RED);
        lineDataSet2.setValueTextSize(10);
        lineDataSet2.setCircleRadius(3);
        lineDataSet2.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v+"";
                }
                return "";
            }
        });
        lineDataSet3.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet3.setColor(Color.rgb(		238, 238,0));
        lineDataSet3.setLineWidth(2);
        lineDataSet3.setDrawCircleHole(false);
        lineDataSet3.setCircleHoleRadius(3);
        lineDataSet3.setCircleColor(Color.RED);
        lineDataSet3.setValueTextSize(10);
        lineDataSet3.setCircleRadius(3);
        lineDataSet3.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v+"";
                }
                return "";
            }
        });
        //数据更新
        line.notifyDataSetChanged();
        line.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
        //line.animateY(3000); //折线在Y轴的动画  参数是动画执行时间 毫秒为单位
        //line.animateX(2000); //X轴动画
        //line.animateXY(2000,2000);//XY两轴混合动画
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
