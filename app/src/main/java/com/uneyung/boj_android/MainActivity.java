package com.uneyung.boj_android;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //크롤링 관련 변수
        String url = "https://www.acmicpc.net/user/";
        String[] members = getResources().getStringArray(R.array.members);
        String[] arr = new String[members.length];

        //요일 관련 변수
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String text_day = "";

        //spinner 관련 변수
        Spinner spinner = findViewById(R.id.spinner);

        //spinner Adapter 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, members);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < members.length; i++) {
                        Document doc = Jsoup.connect(url + members[i]).get();
                        Elements parsingDivs = doc.getElementsByClass("problem-list");
                        Element parsingDiv = parsingDivs.get(0);
                        String contents = parsingDiv.text();

                        //크롤링한 데이터 저장하기
                        try {
                            FileOutputStream fos = openFileOutput(members[i]+".txt", Context.MODE_PRIVATE);
                            fos.write(contents.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        View.OnClickListener mClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                th.start();
            }
        };

        findViewById(R.id.button).setOnClickListener(mClickListener);

        //spinner 목록 선택했을 때
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //불러온 파일을 저장할 변수
                String[] tempData = new String[0];

                //저장되어 있는 데이터 가져오기
                try {
                    FileInputStream fis = openFileInput(members[i] + ".txt");
                    String read_data = new BufferedReader(new InputStreamReader(fis)).readLine();
                    fis.close();
                    tempData = read_data.split(" ");
                } catch (FileNotFoundException e) {
                    th.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //RecyclerView 관련 변수
                MyAdapter re_adapter = new MyAdapter();
                re_adapter.setArrayData(new ArrayList(Arrays.asList(tempData)));
                recyclerView.setAdapter(re_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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