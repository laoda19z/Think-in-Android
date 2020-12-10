package com.example.uidemo.dynamic;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bigkoo.pickerview.TimePickerView;
import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.mainfragment.CommunicityFragment;
import com.example.uidemo.mainfragment.MyselfFragment;
import com.example.uidemo.mark.Entity.MarkDate;
import com.example.uidemo.mark.Entity.NeedSearchDate;
import com.example.uidemo.mark.MarkInformation;
import com.example.uidemo.mark.calendar.PictureInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommunicityFragment1 extends FragmentActivity {
    private View view;
    private Map<String, Calendar> map = new HashMap<>();
    private CalendarView calendarView;
    private LinearLayout picker;
    private TextView tvMonth;
    private ImageView img1;
    private ImageView img2;
    private ArrayList<String> lists=new ArrayList<>();
    private Button btnMark;
    private int nowyear;
    private int nowmonth;
    private int nowday;
    private EventBus eventBus;
    //本月所有打卡数
    private int total;
    //本月最大连续打卡数
    private int success;
    //用于画图的三个数组
    //年数组
    private List<Integer> yearList;
    //月数组
    private List<Integer> monthList;
    //日数组
    private List<Integer> dayList;
    //更新
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    //首先更新打卡数
                    //这个是累计打卡
                    DrawFirstCircle(success+"");
                    //这个是连续打卡
                    DrawSecondCircle(total+"");

                    String array=(String)msg.obj;
                    //这个方法是绘制当前打卡日期的绿色标注
                    DrawGreenSign(array);
                    break;
                case 2:
                    DrawFirstCircle(success+"");
                    //这个是连续打卡
                    DrawSecondCircle(total+"");
                    break;
            }
        }
    };

    //这个方法是画当前日历绿色图标
    private void DrawGreenSign(String array) {
        yearList=new ArrayList<>();
        monthList=new ArrayList<>();
        dayList=new ArrayList<>();
        //首先将传进来的json变为数组
        Gson gson=new Gson();
        List<MarkDate> marklist=gson.fromJson(array,new TypeToken<List<MarkDate>>(){}.getType());
        for(int i=0;i<marklist.size();++i){
            //arrays[0]为2020-11-22
            String[] array1=marklist.get(i).getMarkdate().split("-");
            Log.e("array1",marklist.get(i).getMarkdate());
            yearList.add(Integer.parseInt(array1[0]));
            monthList.add(Integer.parseInt(array1[1]));
            dayList.add(Integer.parseInt(array1[2]));
        }

        //清空map数组，防止11月的表中绘画出12月初的情况
        map.clear();
        //进行循环添加Calendar数组
        for(int i=0;i<yearList.size();++i){
            Calendar calendar=getSchemeCalendar(yearList.get(i),monthList.get(i),dayList.get(i),"2");
            map.put(calendar.toString(),calendar);
        }
        calendarView.setSchemeDate(map);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        Log.e("mll","执行了一次");
        view = LayoutInflater.from(context).inflate(R.layout.communicity_fragment1,null);
        calendarView = view.findViewById(R.id.calendarView);
        picker = view.findViewById(R.id.picker);
        tvMonth = view.findViewById(R.id.tv_month);
        img1=view.findViewById(R.id.img1);
        img2=view.findViewById(R.id.img2);
        btnMark=view.findViewById(R.id.btn_mark);

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(CommunicityFragment1.this)){
            eventBus.register(CommunicityFragment1.this);
        }
        //初始化当前年月
        tvMonth.setText(calendarView.getCurYear() + "年" + calendarView.getCurMonth() + "月");
        nowyear=calendarView.getCurYear();
        nowmonth=calendarView.getCurMonth();
        nowday=calendarView.getCurDay();
        //查询年月并查询出打卡的连续数和本月总数
//        searchNowMonth(nowyear,nowmonth,nowday,123,1);
        //月份切换改变事件
        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                //清空数组,使上个月绘画数组清空
                calendarView.clearSchemeDate();
                //这里获取的是当前月份
                tvMonth.setText(year + "年" + month + "月");
                //查询年月并查询出打卡的连续数和本月总数
                int days=searchDaysByYearAndMonth(year,month);
                searchNowMonth(year,month,days,123,1);
            }
        });
        //设置点击事件
        final boolean[] type = {true, true, false, false, false, false};
        //时间选择器选择年月，对应的日历切换到指定日期
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerView pvTime=new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        java.util.Calendar c = java.util.Calendar.getInstance();
                        c.setTime(date);
                        int year = c.get(java.util.Calendar.YEAR);
                        int month = c.get(java.util.Calendar.MONTH);
                        //通过选择的年月份来判断当月的天数
                        int days=searchDaysByYearAndMonth(year,month+1);
                        //通过年月日来绘绿色圈圈
                        //查询年月并查询出打卡的连续数和本月总数(123账号，孩子为1)
                        searchNowMonth(year,month+1,days,123,1);
                        //滚动到指定日期
                        calendarView.scrollToCalendar(year,month + 1, 1);

                    }
                }).setType(type).build();
                pvTime.show();
            }
        });

        //这个是进入日历某一天看图片的
        calendarView.setOnDateLongClickListener(new CalendarView.OnDateLongClickListener() {
            @Override
            public void onDateLongClick(Calendar calendar) {
                Log.e("长点击了",calendar.getYear()+calendar.getMonth()+calendar.getDay()+"");
                Intent intent=new Intent();
                intent.setClass(context, PictureInfo.class);
                //2020-11-16
                intent.putExtra("date",calendar.getYear()+"-"+calendar.getMonth()+"-"+calendar.getDay()+"");
                intent.putExtra("username","123");
                intent.putExtra("child","1");
                context.startActivity(intent);
            }
        });


        //打卡按钮的点击事件,跳转到详细的打卡页面,传递打卡状态值参数
        btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传递状态值，用来显示今天是否已经打卡过
                String status=null;
                Intent intent=new Intent();
                intent.setClass(context, MarkInformation.class);
                //传递用户名和今日的时间
                intent.putExtra("username",123+"");
                intent.putExtra("date",nowyear+"-"+nowmonth+"-"+nowday);
                intent.putExtra("child","1");
                context.startActivity(intent);
            }
        });

        return view;
    }
    @Subscribe(sticky = true)
    public void onEvent(MyselfFragment fragment){
        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                //清空数组,使上个月绘画数组清空
                calendarView.clearSchemeDate();
                //这里获取的是当前月份
                tvMonth.setText(year + "年" + month + "月");
                //查询年月并查询出打卡的连续数和本月总数
                int days=searchDaysByYearAndMonth(year,month);
                searchNowMonth(year,month,days,12,1);
            }
        });
    }
    //通过年月来获得当月的天数
    private int searchDaysByYearAndMonth(int year, int month) {
        int day = 0;
        boolean runnian;
        if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
            runnian = true;
        } else {
            runnian = false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                if (runnian) {
                    day = 29;
                } else {
                    day = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = 30;
                break;
        }
        return day;
    }

    //查询当前月份的数据
    private void searchNowMonth(final int year, final int month, final int day, int username, int child) {
        new Thread(){
            @Override
            public void run() {
                URL urlpath = null;
                try {
                    urlpath = new URL(ConfigUtil.SERVER_ADDR+"date");
                    HttpURLConnection conn = (HttpURLConnection)urlpath.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    OutputStream out=conn.getOutputStream();
                    //将需要传输的数据变为Json串
                    //创建对象
                    NeedSearchDate need=new NeedSearchDate(username,year+"",month+"",1);
                    Gson gson=new Gson();
                    String sql=gson.toJson(need);
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
                    String json=buffer.toString();
                    //集合的反序列化
                    List<MarkDate> marklist=gson.fromJson(json,new TypeToken<List<MarkDate>>(){}.getType());
                    if(marklist.get(0).getMarkdate().equals("null")==true){
                        //当月无打卡记录
                        total=0;
                        success=0;
                        Message msg=handler.obtainMessage();
                        msg.what=2;
                        msg.obj=buffer.toString();
                        handler.sendMessage(msg);
                    }
                    else{
                        //有打卡记录
                        //buffer.toString()---》2020-11-22&2020-11-23&2020-11-24&2020-11-26&2020-11-11&
                        //这个是计算累计打卡的日子
                        total=countDays(marklist);
                        //这个是计算连续打卡的日子
                        success=countSuccessionDays(year,month,day,marklist);
                        //使用Handler类更新
                        Message msg=handler.obtainMessage();
                        msg.what=1;
                        msg.obj=buffer.toString();
                        handler.sendMessage(msg);
                    }
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

    //计算连续打卡的日子
    private int countSuccessionDays(int year,int month,int day,List<MarkDate> markDateList) {
        //首先先把当月的年月日全部转化为数组(2020-11-3)格式
        List<String> listDate = new ArrayList();
        for (int i = 1; i <= day; i++) {
            listDate.add(year+"-"+month+"-"+i);
        }
        //获得获取的时间数组(2020-11-22 2020-11-23 2020-11-24 2020-11-26 2020-11-11)
        List<String> signDate=new ArrayList<>();
        for(int a=0;a<markDateList.size();++a){
            signDate.add(markDateList.get(a).getMarkdate());
        }
        //算法
        int max = 0;
        int continuity = 0;
        for (int i = 0; i < listDate.size(); i++) {
            for (int j = 0; j < markDateList.size(); j++) {
                if(listDate.get(i).equals(signDate.get(j))){
                    continuity ++;
                    //判断第n次最大数和第n-1次最大数比较
                    if(max > continuity){
                        break;
                    }
                    max = continuity;
                }else {
                    continue;
                }
            }
            if(!signDate.contains(i == listDate.size()-1 ? listDate.get(listDate.size()-1) : listDate.get(i + 1))){
                continuity = 0;
            }
        }
        return max;
    }

    //计算累计打卡的日期
    private int countDays(List<MarkDate> markDateList) {
        int num=markDateList.size();
        return num;
    }


    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setScheme(text);
        return calendar;
    }

    //绘画连续打卡框
    private void DrawFirstCircle(String count){
        Bitmap bitmap= Bitmap.createBitmap(400,350, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint = new Paint();
        Path path = new Path();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(false); //设置为无锯齿
        paint.setStyle(Paint.Style.STROKE);
        Paint paint1=new Paint();

        paint1.setStrokeWidth(5);
        paint1.setTextSize(80);
        paint1.setColor(Color.BLUE);
        canvas.drawCircle(200, 200, 120, paint);
        canvas.drawPath(path,paint);
        //通过获取的天数不同来实现位置不一样
//        String count="8";
        int counts=Integer.parseInt(count);
        if(counts<10){
            canvas.drawText(count+"天", 135, 225, paint1);
        }
        else{
            canvas.drawText(count+"天",115,225,paint1);
        }

        img1.setImageBitmap(bitmap);
    }

    //绘画累计打卡框
    private void DrawSecondCircle(String count){
        Bitmap bitmap= Bitmap.createBitmap(400,350, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint = new Paint();
        Path path = new Path();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(false); //设置为无锯齿
        paint.setStyle(Paint.Style.STROKE);//设置为空心

        Paint paint1=new Paint();
        paint1.setStrokeWidth(5);
        paint1.setTextSize(80);
        paint1.setColor(Color.GREEN);
        canvas.drawCircle(200, 200, 120, paint);
        canvas.drawPath(path,paint);

        //通过获取的天数不同来实现位置不一样
//        String count="10";
        int counts=Integer.parseInt(count);
        if(counts<10){
            canvas.drawText(count+"天", 135, 225, paint1);
        }
        else{
            canvas.drawText(count+"天",115,225,paint1);
        }
        img2.setImageBitmap(bitmap);
    }
}
