package com.example.uidemo.record;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.uidemo.R;
import com.example.uidemo.record.fragment.MaxScoreFragment;
import com.example.uidemo.record.fragment.ScoreRecordFragment;
import com.example.uidemo.record.fragment.SportImpressionsFrament;
import com.example.uidemo.record.fragment.SportWriteTimeRecordFragment;
import com.example.uidemo.record.fragment.SummaryFragment;
import com.example.uidemo.record.view.VerticalViewPager;


public class GrowthRecordActivity extends AppCompatActivity {
    private static final String TAG = "GrowthRecordActivity";
    private VerticalViewPager verticalViewPager;
    private SportImpressionsFrament sportImpressionsFrament;
    private SummaryFragment summaryFragment;
    private SportWriteTimeRecordFragment sportWriteTimeRecordFragment;
    private MaxScoreFragment maxScoreFragment;
    private ScoreRecordFragment scoreRecordFragment;

    private boolean isFinish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_record);

        verticalViewPager=findViewById(R.id.pages);
        verticalViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if(position==0){
                    if(sportImpressionsFrament==null){
                        sportImpressionsFrament=new SportImpressionsFrament();
                    }
                    return sportImpressionsFrament;
                }else if(position==1){
                    if(sportWriteTimeRecordFragment==null){
                        sportWriteTimeRecordFragment=new SportWriteTimeRecordFragment();
                    }
                    return sportWriteTimeRecordFragment;
                }else if(position==2){
                    if(maxScoreFragment ==null){
                        maxScoreFragment =new MaxScoreFragment();
                    }
                    return maxScoreFragment;
                } else if(position==3){
                    if(scoreRecordFragment ==null){
                        scoreRecordFragment =new ScoreRecordFragment();
                    }
                    return scoreRecordFragment;
                } else {
                    if(summaryFragment==null){
                        summaryFragment=new SummaryFragment();
                    }
                    return summaryFragment;
                }
            }
            @Override
            public int getCount() {
                return 5;
            }
        });
        verticalViewPager.setOffscreenPageLimit(5);
        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当前页是第一页，并且是拖动状态，并且像素偏移量为0
                if (position == 0 && positionOffset == 0 && positionOffsetPixels == 0) {
                    if (isFinish) {
                        JumpToNext();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isFinish = state == 1; //当前为拖动状态
            }
        });
        verticalViewPager.setCurrentItem(0);
    }
    private void JumpToNext() {
        finish();
    }
}