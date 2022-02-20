package com.uneyung.boj_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onButton1Clicked);
    }

    public void onButton1Clicked(View V) {
        DateFormat day = new SimpleDateFormat("E");
        DateFormat hour = new SimpleDateFormat("K");
        DateFormat minute = new SimpleDateFormat("m");
        Date date = new Date();
        int problems_count = 0;
        int solve_way = 1;

        if (day.format(date).equals("Mon") || day.format(date).equals("Wen") || day.format(date).equals("Fri")) {
            if (hour.format(date).equals("9") && minute.format(date).equals("0")) {
                Toast.makeText(this, "오늘은 " +
                        day.format(date) + "요일로 백준 풀 시간입니다.", Toast.LENGTH_LONG).show();
            } else if (hour.format(date).equals("18") && minute.format(date).equals("0")) {
                if (problems_count >= 2)
                    Toast.makeText(this, "백준 푸는 시간이 끝났습니다. 축하합니다.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "백준 푸는 시간이 끝났습니다. 선정했던 2문제 중 " + problems_count + "문제를 풀었습니다.", Toast.LENGTH_LONG).show();
            }

        }
    }
}