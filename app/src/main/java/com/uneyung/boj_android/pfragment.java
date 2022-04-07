package com.uneyung.boj_android;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class pfragment extends Fragment {

    ArrayList<String> solved_list = new ArrayList<>();
    ArrayList<String> member_list = new ArrayList<>();
    ArrayList<String> check_class = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_p, container, false);

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                member_list = bundle.getStringArrayList("bundleKey");
            }
        });

        RecyclerView class_recycler = rootView.findViewById(R.id.recycler_class);
        LinearLayoutManager linearLayoutManager_counter = new LinearLayoutManager(getActivity());
        class_recycler.setLayoutManager(linearLayoutManager_counter);

        CheckBox class2 = rootView.findViewById(R.id.checkBox2);
        CheckBox class3 = rootView.findViewById(R.id.checkBox3);
        CheckBox class4 = rootView.findViewById(R.id.checkBox4);
        CheckBox class5 = rootView.findViewById(R.id.checkBox5);
        CheckBox class6 = rootView.findViewById(R.id.checkBox6);
        CheckBox class7 = rootView.findViewById(R.id.checkBox7);

        class class_list extends Thread{
            @Override
            public void run() {
                try {
                    for(int i = 0; i<check_class.size(); i++){

                        String class_num = check_class.get(i);
                        String solved_URL = "https://solved.ac/search?query=in_class:"; //클래스 전까지 solved.ac 주소
                        Document solved_doc = Jsoup.connect(solved_URL + class_num).get();
                        Elements problem_list = solved_doc.getElementsByClass("ProblemInline__ProblemStyle-sc-yu6g1r-0 jRAQI"); //solved.ac에서 class_num에 해당하는 클래스 문제 리스트
                        String[] solved_contents = problem_list.text().split(" "); //문제 리스트 " "을 기준으로 String 배열에 넣음

                        solved_list.addAll(Arrays.asList(solved_contents));
                    }

                    solved_list.removeAll(member_list);
                    Collections.sort(solved_list);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Button random = rootView.findViewById(R.id.button2);
        TextView numbers = rootView.findViewById(R.id.textView6);

        Random rnd = new Random();
        int val[] = new int[2];

        random.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(solved_list.size() != 0){
                    for(int i = 0; i < 2; i++){
                        val[i] = rnd.nextInt(solved_list.size());
                    }
                    numbers.setText(solved_list.get(val[0]) + ", " + solved_list.get(val[1]));
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "데이터 리스트가 업습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        Button button = rootView.findViewById(R.id.button3);

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
                check_class.clear();
                solved_list.clear();

                if(class2.isChecked())
                    check_class.add("2");
                else
                    check_class.remove(String.valueOf(2));

                if(class3.isChecked())
                    check_class.add("3");
                else
                    check_class.remove(String.valueOf(3));

                if(class4.isChecked())
                    check_class.add("4");
                else
                    check_class.remove(String.valueOf(4));

                if(class5.isChecked())
                    check_class.add("5");
                else
                    check_class.remove(String.valueOf(5));

                if(class6.isChecked())
                    check_class.add("6");
                else
                    check_class.remove(String.valueOf(6));

                if(class7.isChecked())
                    check_class.add("7");
                else
                    check_class.remove(String.valueOf(7));

                class_list class_start = new class_list();
                class_start.start();
                try {
                    class_start.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                MyAdapter_class class_adapter = new MyAdapter_class();
                class_adapter.setArrayData(solved_list);
                class_recycler.setAdapter(class_adapter);
            }

        });

        return rootView;
    }
}