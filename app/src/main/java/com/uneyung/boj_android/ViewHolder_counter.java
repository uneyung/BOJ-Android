package com.uneyung.boj_android;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class ViewHolder_counter extends RecyclerView.ViewHolder {
    public TextView textView;

    ViewHolder_counter(Context context, View view){
        super(view);
        textView = view.findViewById(R.id.count_num);
    }
}
