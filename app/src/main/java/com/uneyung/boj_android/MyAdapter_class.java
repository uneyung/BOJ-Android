package com.uneyung.boj_android;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyAdapter_class extends RecyclerView.Adapter<ViewHolder_class> {
    private ArrayList<String> arrayList;

    public MyAdapter_class(){
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder_class onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.class_item, parent, false);
        ViewHolder_class viewHolder = new ViewHolder_class(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_class viewHolder_class, int position) {
        String text = arrayList.get(position);
        viewHolder_class.textView.setText(text);
    }

    @Override
    public int getItemCount() {return arrayList.size();}

    public void setArrayData(ArrayList<String> strData){arrayList.addAll(strData);}

}
