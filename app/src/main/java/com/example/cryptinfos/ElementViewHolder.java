package com.example.cryptinfos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElementViewHolder extends RecyclerView.ViewHolder {
    TextView nameTextView, symbolTextView, rankTextView, priceTextView;
    ImageView iconImageView;

    public ElementViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.coinName);
        symbolTextView = itemView.findViewById(R.id.coinSymbol);
        rankTextView = itemView.findViewById(R.id.coinRank);
        priceTextView = itemView.findViewById(R.id.coinPrice);
        iconImageView = itemView.findViewById(R.id.coinIcon);
    }
}
