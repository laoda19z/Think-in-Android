package com.example.uidemo.mainfragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.uidemo.dynamic.CommunicityFragment1;
import com.example.uidemo.dynamic.CommunicityFragment2;
import com.example.uidemo.R;
import com.example.uidemo.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommunicityFragment extends Fragment {
    public View view;
    private List<View> lists = new ArrayList<>();
    private View mViewPagerLine;//选项卡下面的线条控件
    private FrameLayout.LayoutParams params;
    private RadioGroup radioGroup;
    private RadioButton cardTab;
    private RadioButton commTab;
    private CommunicityFragment1 fragment1;
    private CommunicityFragment2 fragment2;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private List<View> lsViews;
    private Context mContext;

    public CommunicityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_trends,null);
        cardTab = view.findViewById(R.id.trends_tab1);
        commTab = view.findViewById(R.id.trends_tab2);
        mViewPagerLine = view.findViewById(R.id.imformation_viewpager_line);
        params = (FrameLayout.LayoutParams) mViewPagerLine.getLayoutParams();
        radioGroup = view.findViewById(R.id.tab_rg);
        viewPager = view.findViewById(R.id.trends_vp);
        Log.i("mll","1");
        initTab();
        cardTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                cardTab.setBackgroundColor(Color.rgb(138,138,138));
                commTab.setBackgroundColor(Color.rgb(247,247,247));
            }
        });
        commTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                commTab.setBackgroundColor(Color.rgb(138,138,138));
                cardTab.setBackgroundColor(Color.rgb(247,247,247));

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("mll","2");
        initTab();
        cardTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("mll","aaaaaaaaa");
            }
        });
    }

    /**
     * 初始化Tab
     */
    private void initTab() {

        commTab.setChecked(true);
        cardTab.setBackgroundColor(Color.rgb(138,138,138));
        commTab.setBackgroundColor(Color.rgb(247,247,247));
        lsViews = new ArrayList<>();
        lsViews.add(new CommunicityFragment1().onCreateView(null,viewPager.getContext(),null));
        lsViews.add(new CommunicityFragment2().onCreateView(null,viewPager.getContext(),null));
//        lsViews.add(new CommunicityFragment2().onCreateView(LayoutInflater.from(getLayoutInflater().getContext()),null,null));
        pagerAdapter = new MyPagerAdapter(lsViews);
        viewPager.setAdapter(pagerAdapter);
        Log.i("mll",lsViews.size()+"");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0://打卡
                        cardTab.setChecked(true);
                        Log.i("mll","打卡");
                        cardTab.setBackgroundColor(Color.rgb(138,138,138));
                        commTab.setBackgroundColor(Color.rgb(247,247,247));

                        break;
                    case 1://动态
                        commTab.setChecked(true);
                        Log.i("mll","dongtai");
                        commTab.setBackgroundColor(Color.rgb(138,138,138));
                        cardTab.setBackgroundColor(Color.rgb(247,247,247));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        /**
//         * 切换
//         */
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (i){
//                    case 0://打卡
//                        viewPager.setCurrentItem(0);
//                        Log.i("mll","0");
//                        commTab.setBackgroundColor(Color.rgb(138,138,138));
//                        cardTab.setBackgroundColor(Color.rgb(247,247,247));
//                        break;
//                    case 1://动态
//                        viewPager.setCurrentItem(1);
//                        Log.i("mll","1");
//                        cardTab.setBackgroundColor(Color.rgb(138,138,138));
//                        commTab.setBackgroundColor(Color.rgb(247,247,247));
//                        break;
//
//                }
//            }
//        });
    }
}
