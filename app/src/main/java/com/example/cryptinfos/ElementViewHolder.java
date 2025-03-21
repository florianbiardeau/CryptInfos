package com.example.cryptinfos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElementViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;

    public ElementViewHolder(View view) {
        super(view);
        textView = view.findViewById(R.id.textRecycler);
    }

    public void setTextView(String text) {
        textView.setText(text);
    }
}
