package com.example.uidemo.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private BannerLayout bannerLayout;
    private BannerAdapter mAdapter;

    private LinearLayout btnRecord;
    private LinearLayout btnActivity;
    private LinearLayout btnCourse;

    private HorizontalListView horizontalListView;

    public int[] urls = new int[]{//轮播图片地址
            R.drawable.record1,
            R.drawable.record2,
            R.drawable.record3,
            R.drawable.record4,
            R.drawable.record5,
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull final Context context, @NonNull AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.homepage_fragment,null);
        bannerLayout=view.findViewById(R.id.recybanner);
        btnRecord = view.findViewById(R.id.homepage_record);
        btnActivity = view.findViewById(R.id.homepage_activity);
        btnCourse = view.findViewById(R.id.homepage_course);
        mAdapter = new BannerAdapter(context,urls);
        bannerLayout.setAdapter(mAdapter);

        horizontalListView=view.findViewById(R.id.hor);
        List<Data> list=new ArrayList<>();
        Data data1=new Data(R.drawable.record1,"是联合国是读后感凉山大火");
        Data data2=new Data(R.drawable.record2,"大圣归来函数的观看");
        Data data3=new Data(R.drawable.record3,"大圣归来函数的观看");
        Data data4=new Data(R.drawable.record3,"大圣归来函数的观看");
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        HomePageHorizontalListViewAdapter homePageHorizontalListViewAdapter =new HomePageHorizontalListViewAdapter(context,list,R.layout.banner2);
        horizontalListView.setAdapter(homePageHorizontalListViewAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击listview中

            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordActivity.class);
                context.startActivity(intent);
            }
        });
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, FamilyActivity.class);
                context.startActivity(intent);
            }
        });
        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), CourseActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
