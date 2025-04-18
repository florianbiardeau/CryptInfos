package com.example.cryptinfos;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElementViewHolderHomeActivity extends RecyclerView.ViewHolder {
    TextView nameTextView, symbolTextView, rankTextView, priceTextView;
    ImageView iconImageView;

    public ElementViewHolderHomeActivity(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.coinName);
        symbolTextView = itemView.findViewById(R.id.coinSymbol);
        priceTextView = itemView.findViewById(R.id.coinPrice);
        iconImageView = itemView.findViewById(R.id.coinIcon);
    }
}
