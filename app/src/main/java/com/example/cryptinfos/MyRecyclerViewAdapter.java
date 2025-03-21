package com.example.cryptinfos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ElementViewHolder> {

    private final List<String> data;

    MyRecyclerViewAdapter(List<String> data) {
        this.data = data;
    }

    public ElementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout, viewGroup, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElementViewHolder holder, int position) {
        holder.setTextView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
