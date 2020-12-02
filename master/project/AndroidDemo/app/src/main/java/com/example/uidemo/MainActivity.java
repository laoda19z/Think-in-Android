package com.example.uidemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.uidemo.adapter.MyPagerAdapter;
import com.example.uidemo.beans.User;
import com.example.uidemo.mainfragment.CommunicityFragment;
import com.example.uidemo.mainfragment.ContactFragment;
import com.example.uidemo.mainfragment.HomePageFragment;
import com.example.uidemo.mainfragment.MyselfFragment;
import com.example.uidemo.mainfragment.TestFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static User currentUser = new User();
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private MyPagerAdapter pagerAdapter;
    private List<View> lsViews;
    private RadioButton rbMe;
    private RadioButton rbMsg;
    private RadioButton rbCenter;
    private RadioButton rbTends;
    private RadioButton rbTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser.setUsername("mll");
        currentUser.setPassword("123");
        currentUser.setUserId(12);
        findViews();

        rbCenter.setChecked(true);
        lsViews = new ArrayList<>();
        HomePageFragment homePageFragment = new HomePageFragment();
        lsViews.add(homePageFragment.onCreateView(null,this,null));
//        lsViews.add(getLayoutInflater().inflate(R.layout.page_center, null, false));
        CommunicityFragment communicityFragment = new CommunicityFragment();
        lsViews.add(communicityFragment.onCreateView(LayoutInflater.from(this),null,null));
        TestFragment testFragment = new TestFragment();
        lsViews.add(testFragment.onCreateView(null,this,null));
        ContactFragment contactFragment = new ContactFragment();
        lsViews.add(contactFragment.onCreateView(null,this,null));
        MyselfFragment myselfFragment = new MyselfFragment();
        lsViews.add(myselfFragment.onCreateView(null,this,null));
//        lsViews.add(getLayoutInflater().inflate(R.layout.page_me, null, false));
        pagerAdapter = new MyPagerAdapter(lsViews);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //选中ViewPage页的时候触发该事件
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0://主页
                        rbCenter.setChecked(true);
                        break;
                    case 1://动态
                        rbTends.setChecked(true);
                        break;
                    case 2://测评
                        rbTest.setChecked(true);
                        break;
                    case 3://消息
                        rbMsg.setChecked(true);
                        break;
                    case 4://我的
                        rbMe.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置Radio选中的时候切换ViewPager
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_centerpage:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_trends:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_testpage:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_message:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.rb_me:
                        viewPager.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    private void findViews() {
        viewPager = (ViewPager) findViewById(R.id.main_vp);
        radioGroup = (RadioGroup) findViewById(R.id.main_rg);
        rbTends = (RadioButton) findViewById(R.id.rb_trends);
        rbCenter = (RadioButton) findViewById(R.id.rb_centerpage);
        rbMsg = (RadioButton) findViewById(R.id.rb_message);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        rbTest = findViewById(R.id.rb_testpage);
    }

    /**
     * 销毁时注销登录
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        signOut();
    }
    /**
     * 退出登录
     */
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override public void onSuccess() {
                Log.i("mll", "注销成功");
                // 调用退出成功，结束app
                finish();
            }

            @Override public void onError(int i, String s) {
                Log.i("mll", "注销失败 logout error " + i + " - " + s);
            }

            @Override public void onProgress(int i, String s) {
                Log.i("mll","运行中");
            }
        });
    }
}