package com.uneyung.boj_android;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class ViewHolder_class extends RecyclerView.ViewHolder {
    public TextView textView;

    ViewHolder_class(Context context, View view){
        super(view);
        textView = view.findViewById(R.id.class_num);
    }
}
