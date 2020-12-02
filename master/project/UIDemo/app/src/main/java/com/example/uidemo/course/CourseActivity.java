package com.example.uidemo.course;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.coursedemo.bean.Course;
import com.example.uidemo.R;
import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseActivity extends EuclidActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_course);
        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourseActivity.this, "Oh hi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected BaseAdapter getAdapter(List<Course> courseList) {
        List<Course> courses = courseList;

        //人物简介
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();

        //人物头像数组
        int[] avatars = {
                R.drawable.basketball,
                R.drawable.yumaoqiu,
                R.drawable.xiao,
                R.drawable.tennis};

        String[] names = getResources().getStringArray(R.array.array_names);
        if(courses!=null){
            //准备数据
            for (int i = 0; i < avatars.length; i++) {
                profileMap = new HashMap<>();
                profileMap.put(EuclidListAdapter.KEY_AVATAR, avatars[i]);
                profileMap.put(EuclidListAdapter.KEY_NAME,names[i]);
                profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, "点击查看课程详情");
                //profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, "sss");
                profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, courses.get(i).getCourseContent());
                profilesList.add(profileMap);
            }
        }else {
            Toast.makeText(CourseActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
        }


        //第二个参数没啥用
        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }
}