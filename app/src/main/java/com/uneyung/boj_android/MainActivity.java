package com.uneyung.boj_android;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    sfragment Sfragment;
    pfragment Pfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //요일 관련 변수@
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String text_day = "";



        //fragment 관련
        Sfragment = new sfragment();
        Pfragment = new pfragment();
        tabs = findViewById(R.id.tabs);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, Sfragment).commit();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, Sfragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, Pfragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //월, 수, 금 마다 알림
        if (day == 2 || day == 4 || day == 6) {
            if (day == 2)
                text_day = "월";
            else if (day == 4)
                text_day = "수";
            else
                text_day = "금";

            Toast.makeText(this, "오늘은 " + text_day + "요일로 백준 푸는 날입니다.", Toast.LENGTH_LONG).show();
        }
    }
}