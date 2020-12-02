package com.example.uidemo.mark;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.mark.Entity.JudgeMarkStatus;
import com.example.uidemo.mark.Entity.Mark;
import com.example.uidemo.mark.Entity.Status;
import com.example.uidemo.mark.backgroun.BackgroundChoice;
import com.google.gson.Gson;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MarkInformation extends AppCompatActivity {
    private CalendarView calendarView;
    private LinearLayout picker;
    private int year;
    private int month;
    private int day;
    private Map<String, Calendar> map = new HashMap<>();
    private Button btnBackGround;
    //三个TextView
    private TextView tvDate;
    private TextView tvMarkStatus;
    private TextView tvYearAndMonth;
    //三个Spinner属性,对应项目，运动事件h和min
    private Spinner spinner1;
    private String sport;
    private Spinner spinner2;
    private String spHours;
    private Spinner spinner3;
    private String spMinutes;
    //感想
    private EditText impressions;
    //获得感想的字符串
    private String impression;
    //获取传递过来的用户名时间
    private String username;
    private String date;
    private int child;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    //判断今日打卡了没
                    if(msg.obj.equals(true)){
                        //更新状态
                        tvMarkStatus.setText("今日已经打卡！");
                        drawAlready();

                    }
                    else{
                        Log.e("今日打卡","没打");
                        //没有打卡记录的，日历上显示没有打卡
                        initData();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_information_layout);

        calendarView=findViewById(R.id.calendarView);
        btnBackGround=findViewById(R.id.btn_background);
        tvDate=findViewById(R.id.tv_date);
        tvYearAndMonth=findViewById(R.id.tv_yearandmonth);
        tvMarkStatus=findViewById(R.id.tv_mark_status);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.sp_hours);
        spinner3=findViewById(R.id.sp_minutes);
        impressions=findViewById(R.id.impressions);

        //显示今天的日期
        year=calendarView.getCurYear();
        month=calendarView.getCurMonth();
        day=calendarView.getCurDay();
        //显示三个textView
        tvDate.setText(year+"-"+month+"-"+day);
        tvMarkStatus.setText("今天未打卡");
        tvYearAndMonth.setText(year+"年"+month+"月");

        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        date=intent.getStringExtra("date");
        child= Integer.parseInt(intent.getStringExtra("child"));

        //获取打卡的所有日期，若有打卡记录，则日历最上显示的为已经打卡
        JudegMarkStatus(year,month,day,username,child);
        //获得spinner选中的属性
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources resources=getResources();
                String[] sportType=resources.getStringArray(R.array.spingarr);
                sport=sportType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //获得输入的时间
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获得输入的小时
                Resources resources=getResources();
                String[] sportType=resources.getStringArray(R.array.hours);
                spHours=sportType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //获得输入的分钟
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获得输入的分钟
                Resources resources=getResources();
                String[] sportType=resources.getStringArray(R.array.minutes);
                spMinutes=sportType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //点击选择打卡图片进行跳转
        btnBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到的运动项目和事件信息
                Log.e("testtestetst",sport+spHours+spMinutes);
                if(impressions.getText().toString().length()==0){
                    impression="nulls";
                }
                else{
                    impression=impressions.getText().toString();
                }
                //这个方法是将输入的时间转化为分钟
                int minutes=formatMinutes(spMinutes,spHours);
                //获取到感想
                Intent intent=new Intent();
                intent.setClass(MarkInformation.this, BackgroundChoice.class);
                //传递数据,传递的数据有  用户名，时间，运动项目，运动时间，感想
                //转化为json串
                Gson gson=new Gson();
                Mark mark=new Mark(username,date,minutes,sport,impression,child);
                String toJson=gson.toJson(mark);
                intent.putExtra("json",toJson);
                startActivity(intent);
            }
        });

    }

    //这个方法是判断今日有没有打卡
    private void JudegMarkStatus(final int year, final int month, final int day, final String username, final int child) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL statusurl = new URL(ConfigUtil.SERVER_ADDR+"judge");
                    HttpURLConnection conn = (HttpURLConnection)statusurl.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    OutputStream out=conn.getOutputStream();
                    //将需要传输的数据变为Json串
                    //创建对象
                    JudgeMarkStatus judge=new JudgeMarkStatus(username,year+"",month+"",day+"",child);
                    Gson gson=new Gson();
                    String sql=gson.toJson(judge);
                    out.write(sql.getBytes());
                    InputStream in = conn.getInputStream();
                    InputStreamReader instr=new InputStreamReader(in,"UTF-8");
                    StringBuffer buffer=new StringBuffer();
                    //如果当月打卡无记录则传输过来的为null
                    int len=0;
                    char[] chars=new char[1024];
                    while((len=instr.read(chars))!=-1){
                        buffer.append(new String(chars,0,len));
                    }
                    String statusjson=buffer.toString();
                    Log.e("判断今日是否打卡的字符串",statusjson);
                    //通过传输过来的JSON串转化为对象
                    Status sta=gson.fromJson(statusjson, Status.class);
                    boolean booleans=sta.getStatus();
                    //Handler更新
                    Message msg=handler.obtainMessage();
                    msg.what=1;
                    msg.obj=booleans;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //全部转化为分钟
    private int formatMinutes(String spMinutes, String spHours) {
        int h= Integer.parseInt(spHours);
        int min= Integer.parseInt(spMinutes);
        int mins=h*60+min;
        return mins;
    }

    private void initData() {
        //这里修改时间,没有打卡的时间
        Calendar calendar1 = getSchemeCalendar(year, month, day, "1");
        map.put(calendar1.toString(), calendar1);
        calendarView.setSchemeDate(map);
    }
    private void drawAlready(){
        Calendar calendar=getSchemeCalendar(year,month,day,"2");
        map.put(calendar.toString(), calendar);
        calendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setScheme(text);
        return calendar;
    }
}
