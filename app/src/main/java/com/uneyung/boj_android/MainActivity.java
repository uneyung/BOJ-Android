package com.uneyung.boj_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String text_day = "";

        if(day == 2 || day == 4 || day == 6){
            if(day == 2)
                text_day = "월";
            else if(day == 4)
                text_day = "수";
            else
                text_day = "금";

            Toast.makeText(this, "오늘은 " +
                    text_day + "요일로 백준 푸는 날입니다.", Toast.LENGTH_LONG).show();
        }
    }
}