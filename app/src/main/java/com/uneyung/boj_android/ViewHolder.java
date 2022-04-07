package com.uneyung.boj_android;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    ViewHolder(Context context, View view){
        super(view);
        textView = view.findViewById(R.id.solve_num);
    }
}
