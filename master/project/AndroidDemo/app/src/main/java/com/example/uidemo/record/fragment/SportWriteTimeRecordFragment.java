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
import com.example.uidemo.record.entitys.ClockRecord;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

public class SportWriteTimeRecordFragment extends Fragment {
    private View root;
    private TextView time;
    private int userid;

    private PieChart pie;
    private List<ClockRecord> list0;
    private List<PieEntry> list;
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.frag_sportwritetimerecord, container, false);
        time=root.findViewById(R.id.time);

        Bundle bundle=getActivity().getIntent().getExtras();
        userid=bundle.getInt("id");

        gson=new Gson();
        list0=new ArrayList<>();
        GetList();
        return root;
    }

    private void GetList() {
        new Thread(){
            @Override
            public void run() {
                URL urlpath= null;
                try {
                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"/SportWriteTimeRecordServlet");
                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
                    conn.setRequestMethod("POST");
                    //传过userid，根据userid查询
                    OutputStream out1 = conn.getOutputStream();
                    out1.write((""+userid).getBytes());
                    out1.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String textjson= URLDecoder.decode(buffer.readLine(),"utf8");
                    Log.i("sporttime",textjson);
                    Type type=new TypeToken<List<ClockRecord>>(){}.getType();
                    list0=gson.fromJson(textjson,type);
                    float times=0;
                    for(ClockRecord cr:list0){
                        if(cr.getSportTime()>times){
                            times=cr.getSportTime();
                        }
                    }
                    time.setText(times+"分钟");
//                    time.setText(list0.get(0).getSportTime()+"分钟");
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

    private void showdata(List<ClockRecord> list0) {
        int data[] = new int[5];
        int a=0;
        int b=0;
        int c=0;
        int d=0;
        int e=0;
        for(ClockRecord cr:list0){
            if(cr.getSportTime()<60){
                a++;
            }else if(cr.getSportTime()<120&&cr.getSportTime()>60){
                b++;
            }else if(cr.getSportTime()<180&&cr.getSportTime()>120){
                c++;
            }else if(cr.getSportTime()<240&&cr.getSportTime()>180){
                d++;
            }else{
                e++;
            }
        }
        System.out.println(a+""+b+""+c+""+d+""+e+"");
        data[0]= Math.round(a*100/(a+b+c+d+e));
        data[1]= Math.round(b*100/(a+b+c+d+e));
        data[2]= Math.round(c*100/(a+b+c+d+e));
        data[3]= Math.round(d*100/(a+b+c+d+e));
        data[4]=100-(data[0]+data[1]+data[2]+data[3]);
        pie=(PieChart)root.findViewById(R.id.pie);
        list=new ArrayList<>();
        list.add(new PieEntry(data[0],"小于1h"));
        list.add(new PieEntry(data[1],"1h~2h"));
        list.add(new PieEntry(data[2],"2h~3h"));
        list.add(new PieEntry(data[3],"3h~4h"));
        list.add(new PieEntry(data[4],"4h"));
        PieDataSet pieDataSet=new PieDataSet(list,"");
        PieData pieData=new PieData(pieDataSet);
        pie.setData(pieData);
        pieDataSet.setColors(Color.rgb(255,218,185)
                , Color.rgb(	162 ,205, 90)
                , Color.rgb(	139, 101 ,8)
                , Color.rgb(	72, 209 ,204)
                , Color.rgb(151,255,255));//设置各个数据的颜色

        //实体扇形的空心圆的半径   设置成0时就是一个圆 而不是一个环
        pie.setHoleRadius(30);
        //中间半透明白色圆的半径    设置成0时就是隐藏
        pie.setTransparentCircleRadius(30);
        //设置中心圆的颜色
        pie.setHoleColor(Color.rgb(255,240,225));
        //设置中心部分的字  （一般中间白色圆不隐藏的情况下才设置）
        pie.setCenterText("时间占比");
        //设置中心字的字体颜色
        pie.setCenterTextColor(Color.RED);
        //设置中心字的字体大小
        pie.setCenterTextSize(10);
        //设置描述的字体大小（图中的  男性  女性）
        pie.setEntryLabelTextSize(16);
        //设置数据的字体大小  （图中的  44     56）
        pieDataSet.setValueTextSize(16);
        //设置描述的位置
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        //设置数据的位置
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pie.getDescription().setEnabled(false);
        //图例
        Legend legend=pie.getLegend();
        legend.setEnabled(true);    //是否显示图例
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置

        //数据更新
        pie.notifyDataSetChanged();
        pie.invalidate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
