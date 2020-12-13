package com.example.uidemo.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.uidemo.R;
import com.example.uidemo.adapter.BannerAdapter;
import com.example.uidemo.adapter.HomePageHorizontalListViewAdapter;
import com.example.uidemo.beans.Data;
import com.example.uidemo.course.CourseActivity;
import com.example.uidemo.familyactivity.activities.FamilyActivity;
import com.example.uidemo.record.HorizontalListView;
import com.example.uidemo.record.RecordActivity;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends FragmentActivity {
    private View view;
    private BannerLayout bannerLayout;//上方滚动图
    private BannerAdapter mAdapter;//滚动图的适配器

    private LinearLayout btnRecord;
    private LinearLayout btnActivity;
    private LinearLayout btnCourse;

    private HorizontalListView horizontalListView1;//横向滑动listView
    private HorizontalListView horizontalListView2;
    private HorizontalListView horizontalListView3;

    public int[] urls = new int[]{//轮播图片地址
            R.drawable.record_new1,
            R.drawable.record_new2,
            R.drawable.record_new3,
            R.drawable.record_new4,
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.homepage_fragment,null);

        //加载视图控件
        findViews();
        //设置滚动图
        mAdapter = new BannerAdapter(context,urls);
        bannerLayout.setAdapter(mAdapter);
        //设置横向滑动listView的数据
        List<Data> list1 = new ArrayList<>();
        Data page1_data1=new Data(R.drawable.page1_img1,"和爸爸妈妈参加运动会");
        Data page1_data2=new Data(R.drawable.page1_img2,"教孩子学习跳绳");
        Data page1_data3=new Data(R.drawable.page1_img3,"带领孩子一起做早操");
        Data page1_data4=new Data(R.drawable.page1_img4,"去户外极限运动");
        list1.add(page1_data1);
        list1.add(page1_data2);
        list1.add(page1_data3);
        list1.add(page1_data4);

        List<Data> list2 = new ArrayList<>();
        Data page2_data1=new Data(R.drawable.page2_img1,"周末亲子跳绳比赛");
        Data page2_data2=new Data(R.drawable.page2_img2,"社区亲子会操表演");
        Data page2_data3=new Data(R.drawable.page2_img3,"少儿消防演练活动");
        Data page2_data4=new Data(R.drawable.page2_img4,"少儿运动体验馆");
        list2.add(page2_data1);
        list2.add(page2_data2);
        list2.add(page2_data3);
        list2.add(page2_data4);

        List<Data> list3 = new ArrayList<>();
        Data page3_data1=new Data(R.drawable.page3_img1,"和孩子一起植树的感受");
        Data page3_data2=new Data(R.drawable.page3_img2,"少儿攀爬馆体验");
        Data page3_data3=new Data(R.drawable.page3_img3,"快乐周末");
        Data page3_data4=new Data(R.drawable.page3_img4,"带孩子去骑行");
        list3.add(page3_data1);
        list3.add(page3_data2);
        list3.add(page3_data3);
        list3.add(page3_data4);


        //设置横向滑动listView1
        HomePageHorizontalListViewAdapter homePageHorizontalListViewAdapter1 =new HomePageHorizontalListViewAdapter(context,list1,R.layout.banner2);
        horizontalListView1.setAdapter(homePageHorizontalListViewAdapter1);
        //设置横向滑动listView2
        HomePageHorizontalListViewAdapter homePageHorizontalListViewAdapter2 =new HomePageHorizontalListViewAdapter(context,list2,R.layout.banner2);
        horizontalListView2.setAdapter(homePageHorizontalListViewAdapter2);
        //设置横向滑动listView3
        HomePageHorizontalListViewAdapter homePageHorizontalListViewAdapter3 =new HomePageHorizontalListViewAdapter(context,list3,R.layout.banner2);
        horizontalListView3.setAdapter(homePageHorizontalListViewAdapter3);
        //返回按钮
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordActivity.class);
                context.startActivity(intent);
            }
        });
        //活动按钮
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, FamilyActivity.class);
                context.startActivity(intent);
            }
        });
        //课程按钮
        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), CourseActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

    private void findViews() {
        bannerLayout=view.findViewById(R.id.recybanner);
        btnRecord = view.findViewById(R.id.homepage_record);
        btnActivity = view.findViewById(R.id.homepage_activity);
        btnCourse = view.findViewById(R.id.homepage_course);
        horizontalListView1=view.findViewById(R.id.hor1);
        horizontalListView2=view.findViewById(R.id.hor2);
        horizontalListView3=view.findViewById(R.id.hor3);
    }
}
