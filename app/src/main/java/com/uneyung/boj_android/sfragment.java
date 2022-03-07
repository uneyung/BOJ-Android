package com.uneyung.boj_android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView count_recycler = rootView.findViewById(R.id.re_count);
        LinearLayoutManager linearLayoutManager_counter = new LinearLayoutManager(getActivity());
        count_recycler.setLayoutManager(linearLayoutManager_counter);


        Date finalDate = date;
        Thread th = new Thread() {
            @Override
            public void run() {
                try {

                    for (int i = 0; i < members.length; i++) {
                        boolean check = false;
                        Document doc_list = Jsoup.connect(url_list + members[i]).get();
                        Elements parsingDivs = doc_list.getElementsByClass("problem-list");
                        Element parsingDiv = parsingDivs.get(0);
                        String contents_list = parsingDiv.text();

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

                        //크롤링한 데이터 저장하기
                        try {
                            FileOutputStream fos = getActivity().openFileOutput(members[i]+".txt", Context.MODE_PRIVATE);
                            fos.write(contents_list.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ParseException e) {
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


        rootView.findViewById(R.id.button).setOnClickListener(mClickListener);
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
                int todaycount = 0;
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
                        todaycount = countData.length;
                    }

                    if(read_data != null){
                        problemData = read_data.split(" ");
                        count = problemData.length;
                    }
                    
                    MyAdapter re_adapter = new MyAdapter();
                    MyAdapter_counter count_adapter = new MyAdapter_counter();
                    re_adapter.setArrayData(new ArrayList(Arrays.asList(problemData)));
                    count_adapter.setArrayData(new ArrayList(Arrays.asList(countData)));
                    recyclerView.setAdapter(re_adapter);
                    count_recycler.setAdapter(count_adapter);

                } catch (FileNotFoundException e) {
                    th.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (i){
                    case 0:
                        textView.setText("권현지");
                        textView_todaycount.setText(Integer.toString(todaycount));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 1:
                        textView.setText("박은영");
                        textView_todaycount.setText(Integer.toString(todaycount));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 2:
                        textView.setText("변연희");
                        textView_todaycount.setText(Integer.toString(todaycount));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 3:
                        textView.setText("한태희");
                        textView_todaycount.setText(Integer.toString(todaycount));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 4:
                        textView.setText("조경욱");
                        textView_todaycount.setText(Integer.toString(todaycount));
                        textView_count.setText(Integer.toString(count));
                        break;
                    case 5:
                        textView.setText("최승혁");
                        textView_todaycount.setText(Integer.toString(todaycount));
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