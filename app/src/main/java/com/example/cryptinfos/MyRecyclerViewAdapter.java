package com.example.cryptinfos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;


import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ElementViewHolder> {

    private final Context context;
    private final List<Coin> coinList;

    public MyRecyclerViewAdapter(Context context, List<Coin> coinList) {
        this.context = context;
        this.coinList = coinList;
    }

    public ElementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout, viewGroup, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElementViewHolder holder, int position) {
        Coin coin = coinList.get(position);

        holder.nameTextView.setText(coin.getName());
        holder.symbolTextView.setText(coin.getSymbol());
        holder.priceTextView.setText(String.valueOf(coin.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(coin.getIconUrl())
                .into(holder.iconImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CryptoActivity.class);
            intent.putExtra("id", coin.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

}
