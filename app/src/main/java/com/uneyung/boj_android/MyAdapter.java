package com.uneyung.boj_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<String> arrayList;

    public MyAdapter(){
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = arrayList.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {return arrayList.size();}

    public void setArrayData(ArrayList<String> strData){arrayList.addAll(strData);}

}
