package com.example.uidemo.familyactivity.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidemo.ConfigUtil;
import com.example.uidemo.R;
import com.example.uidemo.familyactivity.adpter.ActivitiesAdapter;
import com.example.uidemo.familyactivity.adpter.MyLovesAdapter;
import com.example.uidemo.familyactivity.beans.ParentChildActivities;
import com.example.uidemo.familyactivity.beans.ParentChildActivitiesInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.BitmapFactory.decodeStream;

public class MyLovesActivity extends AppCompatActivity {

    private List<ParentChildActivities> parentChildActivities;
    private String url = ConfigUtil.SERVER_ADDR + "MyLovesServlet";
    private Handler myHandler;
    private ParentChildActivitiesInfo activitiesInfo;
    private int flag = 0;
    private ImageView btnBack;
    private SmartRefreshLayout srl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_love);

        btnBack = findViewById(R.id.btn_back_loves);
        srl = findViewById(R.id.refreshLayout);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (flag == 0) {
            downLoadActivities(url);
            flag = 1;
        }
        myHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        activitiesInfo = (ParentChildActivitiesInfo)msg.obj;
                        //设置展示适配器
                        MyLovesAdapter myLovesAdapter = new MyLovesAdapter(MyLovesActivity.this, activitiesInfo.getActivities());
                        ListView parentChildList = findViewById(R.id.myLove_list);
                        parentChildList.setAdapter(myLovesAdapter);
                        //设置全局的Header构建器
                        SmartRefreshLayout.setDefaultRefreshHeaderCreator(
                                new DefaultRefreshHeaderCreator() {
                                    public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                                        //全局设置主题颜色
                                        layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                                        return new ClassicsHeader(context);
                                    }
                                });
                        //设置全局的Footer构建器
                        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
                            @NonNull
                            @Override
                            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                                return new ClassicsFooter(context).setDrawableSize(20);
                            }
                        });
                        break;
                }
            }
        };
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                downLoadActivities(url);
                srl.finishRefresh();
            }
        });
    }
    public void downLoadActivities(final String url) {
        new Thread() {
            @Override
            public void run() {
                try {
//                    Log.i("查看结果", "进入downLoadCakes");
                    //1. 通过网络请求下载数据(图片要下载到本地，修改图片地址为本地地址)
                    //创建URL对象
                    URL urlPath = new URL(url);
                    //通过URL对象获取网络输入流
                    InputStream in = urlPath.openStream();
                    //读数据（Json串),循环读写方式
                    byte[] bytes = new byte[512];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while ((len = in.read(bytes, 0, bytes.length)) != -1) {
                        buffer.append(new String(bytes, 0, len));
                    }
                    String result = buffer.toString();
                    Log.i("查看结果JSON串", result);
                    in.close();
                    //先将Json串解析成外部ParentChildActivitiesInfo对象
                    ParentChildActivitiesInfo activitiesInfo = new ParentChildActivitiesInfo();
                    List<ParentChildActivities> activities = new ArrayList<>();
                    //创建外层JSONObject对象
                    String result1 = URLDecoder.decode(result,"UTF-8");
                    JSONObject jCakes = new JSONObject(result1);
                    JSONArray jArray = jCakes.getJSONArray("myLoves");
                    Log.i("jArray",String.valueOf(jArray));
                    //遍历JSONArray对象,解析其中的每个元素
                    for (int i = 0; i < jArray.length(); i++) {
                        ParentChildActivities activity = new ParentChildActivities();
                        //获取当前的JSONObject对象
                        JSONObject jActivity = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        String name = jActivity.getString("name");
                        Integer id = jActivity.getInt("id");
                        String time = jActivity.getString("time");
                        String img = jActivity.getString("img");
                        Integer peoNum = jActivity.getInt("peoNum");
                        String content = jActivity.getString("content");
                        String district = jActivity.getString("district");
                        String pay = jActivity.getString("pay");

                        //!!!!下载图片
                        String urlImg = ConfigUtil.SERVER_ADDR + img;
                        URL url = new URL(urlImg);
                        InputStream in1 = url.openStream();
                        Bitmap bt = decodeStream(in1);

                        //给User对象赋值
                        activity.setActivityName(name);
                        activity.setActivityId(id);
                        activity.setActivityTime(time);
                        activity.setActivityImg(bt);
                        activity.setActivityPeoNum(peoNum);
                        activity.setActivityContent(content);
                        activity.setActivityDistrict(district);
                        activity.setActivityPay(pay);

                        //把当前的对象添加到集合中
                        activities.add(activity);
                    }
                    //给cakeInfo对象赋值
                    activitiesInfo.setActivities(activities);
                    //2. 通过发送Message对象，将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 1;
                    msg.obj = activitiesInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
