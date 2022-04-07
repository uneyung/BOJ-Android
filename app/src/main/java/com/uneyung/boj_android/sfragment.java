package com.uneyung.boj_android;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class  sfragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_s, container, false);


        //크롤링 관련 변수
        String url_list = "https://www.acmicpc.net/user/";
        String[] members = getResources().getStringArray(R.array.members);

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String now = dtf.format(cal.getTime());

        Date date = null;
        try {
            date = dtf.parse(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Spinner spinner = rootView.findViewById(R.id.spinner);


        //RecyclerView 관련 변수
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView count_recycler = rootView.findViewById(R.id.re_count);
        LinearLayoutManager linearLayoutManager_counter = new LinearLayoutManager(getActivity());
        count_recycler.setLayoutManager(linearLayoutManager_counter);

        Date finalDate = date;

        class th_count extends Thread{
            @Override
            public void run() {
                try {
                    for (int i = 0; i < members.length; i++) {
                        boolean check = false;
                        ArrayList<String> num_list = new ArrayList<>();
                        String url_count = "https://www.acmicpc.net/status?user_id=" + members[i] + "&result_id=4";
                        String next_page = "";


                        while(true){
                            Document doc_count = Jsoup.connect(url_count + next_page).get();
                            Elements count_list = doc_count.select("tr");
                            String next_line = doc_count.getElementsByClass("text-center").get(1).getElementsByAttributeValue("id", "next_page").toString();

                            for(int j = 0; j < count_list.size(); j++){
                                Elements line = count_list.get(j).getElementsByAttribute("title");

                                if(line.size() == 0)
                                    continue;
                                else{
                                    Date date_check = new Date(dtf.parse(line.get(1).attr("title").toString().substring(0, 10)).getTime());

                                    if(date_check.equals(finalDate)){
                                        if(num_list.contains(line.get(0).attr("href").toString().substring(9)))
                                            continue;

                                        num_list.add(line.get(0).attr("href").toString().substring(9));
                                    }
                                    else{
                                        if(date_check.before(finalDate)){
                                            check = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            if(check == true)
                                break;

                            next_page = "&" + next_line.substring(url_count.length()-5, next_line.lastIndexOf("\" "));
                        }


                        String num_list_slice = num_list.toString();
                        num_list_slice = num_list_slice.replace("[", "").replace("]", "");

                        FileOutputStream fos_count = getActivity().openFileOutput(members[i] + "_count.txt", Context.MODE_PRIVATE);
                        fos_count.write(num_list_slice.getBytes());
                        fos_count.close();
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        class th_num extends Thread{
            @Override
            public void run() {
                try {

                    for (int i = 0; i < members.length; i++) {
                        Document doc_list = Jsoup.connect(url_list + members[i]).get();
                        Elements parsingDivs = doc_list.getElementsByClass("problem-list");
                        Element parsingDiv = parsingDivs.get(0);
                        String contents_list = parsingDiv.text();


                        //크롤링한 데이터 저장하기
                        try {
                            FileOutputStream fos = getActivity().openFileOutput(members[i] + ".txt", Context.MODE_PRIVATE);
                            fos.write(contents_list.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Button button = rootView.findViewById(R.id.button);

        abstract class OnSingleClickListener implements View.OnClickListener{

            //중복 클릭 방지 시간 설정 ( 해당 시간 이후에 다시 클릭 가능 )
            private static final long MIN_CLICK_INTERVAL = 5000;
            // 이전에 클릭한 시간 변수
            private long mLastClickTime = 0;

            public abstract void onSingleClick(View v);

            @Override
            public final void onClick(View v) {
                // 현재 클릭한 시간 변수
                long currentClickTime = SystemClock.uptimeMillis();
                long elapsedTime = currentClickTime - mLastClickTime;
                mLastClickTime = currentClickTime;

                // 중복클릭 아닌 경우 (이전클리과 현재클릭의 시간차가 MIN_CLICK_INTERVAL 보다 큰 경우)
                if (elapsedTime > MIN_CLICK_INTERVAL) {
                    onSingleClick(v);
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

        // 버튼 클릭 중복방지
        button.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                th_count count = new th_count();
                count.start();

                th_num num = new th_num();
                num.start();


                Toast.makeText(getActivity().getApplicationContext(), "갱신되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        TextView textView = rootView.findViewById(R.id.textView);
        TextView textView_todaycount = rootView.findViewById(R.id.textView5);
        TextView textView_count = rootView.findViewById(R.id.textView3);


        //spinner Adapter 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, members);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //spinner 목록 선택했을 때
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //파일을 저장할 임시 배열
                String[] problemData = {};
                String[] countData = {};
                int today_count = 0;
                int count = 0;

                //저장되어 있는 데이터 가져오기
                try {
                    FileInputStream fis = getActivity().openFileInput(members[i] + ".txt");
                    FileInputStream fis_count = getActivity().openFileInput(members[i] + "_count.txt");
                    String read_data = new BufferedReader(new InputStreamReader(fis)).readLine();
                    String read_count = new BufferedReader(new InputStreamReader(fis_count)).readLine();
                    fis.close();
                    fis_count.close();

                    if(read_count != null){
                        countData = read_count.split(", ");
                        today_count = countData.length;
                    }

                    if(read_data != null){
                        problemData = read_data.split(" ");
                        count = problemData.length;
                    }

                    Bundle result = new Bundle();
                    result.putStringArrayList("bundleKey", new ArrayList(Arrays.asList(problemData)));
                    getParentFragmentManager().setFragmentResult("requestKey", result);

                    MyAdapter re_adapter = new MyAdapter();
                    MyAdapter_counter count_adapter = new MyAdapter_counter();
                    re_adapter.setArrayData(new ArrayList(Arrays.asList(problemData)));
                    count_adapter.setArrayData(new ArrayList(Arrays.asList(countData)));
                    recyclerView.setAdapter(re_adapter);
                    count_recycler.setAdapter(count_adapter);

                } catch (FileNotFoundException e) {
                    th_count t_count = new th_count();
                    t_count.start();
                    try {
                        t_count.join();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }

                    th_num num = new th_num();
                    num.start();
                    try {
                        num.join();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (i){
                    case 0:
                        textView.setText("권현지");
                        textView_todaycount.setText(Integer.toString(today_count));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 1:
                        textView.setText("박은영");
                        textView_todaycount.setText(Integer.toString(today_count));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 2:
                        textView.setText("변연희");
                        textView_todaycount.setText(Integer.toString(today_count));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 3:
                        textView.setText("한태희");
                        textView_todaycount.setText(Integer.toString(today_count));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 4:
                        textView.setText("조경욱");
                        textView_todaycount.setText(Integer.toString(today_count));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 5:
                        textView.setText("최승혁");
                        textView_todaycount.setText(Integer.toString(today_count));
                        textView_count.setText(Integer.toString(count));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return rootView;
    }
}