package com.uneyung.boj_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyAdapter_counter extends RecyclerView.Adapter<ViewHolder_counter> {
    private ArrayList<String> arrayList;

    public MyAdapter_counter(){
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder_counter onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.counter_item, parent, false);
        ViewHolder_counter viewHolder = new ViewHolder_counter(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_counter viewHolder_counter, int position) {
        String text = arrayList.get(position);
        viewHolder_counter.textView.setText(text);
    }

    @Override
    public int getItemCount() {return arrayList.size();}

    public void setArrayData(ArrayList<String> strData){arrayList.addAll(strData);}

}
