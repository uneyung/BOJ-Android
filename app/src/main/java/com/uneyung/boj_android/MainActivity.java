package com.uneyung.boj_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<String[]> list = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //파일 접근 관련 변수
        String FileName = "Crawling_data";

        //크롤링 관련 변수
        String url = "https://www.acmicpc.net/user/";
        String[] arr = new String[6];

        //요일 관련 변수
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String text_day = "";

        //spinner 관련 변수
        String[] members = getResources().getStringArray(R.array.members);
        Spinner spinner = findViewById(R.id.spinner);

        //Adapter 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, members);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), members[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (day == 2 || day == 4 || day == 6) {
            if (day == 2)
                text_day = "월";
            else if (day == 4)
                text_day = "수";
            else
                text_day = "금";

            Toast.makeText(this, "오늘은 " + text_day + "요일로 백준 푸는 날입니다.", Toast.LENGTH_LONG).show();
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = openFileOutput(FileName, MODE_PRIVATE);

                    for (int i = 0; i < members.length; i++) {
                        Document doc = Jsoup.connect(url + members[i]).get();
                        Elements parsingDivs = doc.getElementsByClass("problem-list");
                        Element parsingDiv = parsingDivs.get(0);
                        String contents = parsingDiv.text();
                        contents.getBytes();
                        arr[i] = contents + "\n";

                        try {
                            fos.write(arr[i].getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        //RecyclerView 관련 변수
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.Adapter re_adapter;
        RecyclerView.LayoutManager layoutManager;

        try {
            FileInputStream fis = openFileInput(FileName);
            StringBuffer sb = new StringBuffer();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}