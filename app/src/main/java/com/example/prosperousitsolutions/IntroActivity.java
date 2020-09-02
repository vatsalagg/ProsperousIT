package com.example.prosperousitsolutions;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    WormDotsIndicator wormDotsIndicator;
    ViewPager2 viewPager;
    ViewPagerAdapter adapter;
    List<IntroModel> introList = new ArrayList<>();
    ConstraintLayout viewpagerLayout;
    Button freelancerIntroBtn, jobIntroBtn;

    protected void init(){
        viewpagerLayout = findViewById(R.id.viewpager_layout);
        freelancerIntroBtn = findViewById(R.id.freelancer_intro_btn);
        jobIntroBtn = findViewById(R.id.job_intro_btn);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getSupportActionBar().hide(); // hide the title bar

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.setStatusBarColor(Color.TRANSPARENT);

        init();

        introList.add(new IntroModel("giu", "ugi","#03DAC5"));
        introList.add(new IntroModel("giu", "ugi","#3700B3"));
        introList.add(new IntroModel("giu", "ugi","#6200EE"));

        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator);
        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(IntroActivity.this, introList);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);
        wormDotsIndicator.setViewPager2(viewPager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(position==0){
                    freelancerIntroBtn.setTextColor(Color.parseColor("#03DAC5"));
                    jobIntroBtn.setTextColor(Color.parseColor("#03DAC5"));
                }else if (position==1){
                    freelancerIntroBtn.setTextColor(Color.parseColor("#3700B3"));
                    jobIntroBtn.setTextColor(Color.parseColor("#3700B3"));
                }else {
                    freelancerIntroBtn.setTextColor(Color.parseColor("#6200EE"));
                    jobIntroBtn.setTextColor(Color.parseColor("#6200EE"));
                }

             //   Log.e("Selected_Page", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        adapter.notifyDataSetChanged();
    }
}