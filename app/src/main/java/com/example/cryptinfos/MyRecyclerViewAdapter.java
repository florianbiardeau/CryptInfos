package com.example.cryptinfos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ElementViewHolder> {

    private final List<Coin> coinList;

    public MyRecyclerViewAdapter(List<Coin> coinList) {
        this.coinList = coinList;
    }

    public ElementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout, viewGroup, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElementViewHolder holder, int position) {
        Coin coin = coinList.get(position);

        // Assumer que tu as un TextView pour afficher le nom et le prix
        holder.nameTextView.setText(coin.getName());
        holder.symbolTextView.setText(coin.getSymbol());
        holder.rankTextView.setText(String.valueOf(coin.getRank()));
        holder.priceTextView.setText(String.valueOf(coin.getPrice()));

        // Utiliser Picasso ou Glide pour charger l'icône
        //Picasso.get().load(coin.getIcon()).into(holder.iconImageView);
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

}
