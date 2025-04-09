package com.example.cryptinfos;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ElementViewHolderCryptoActivity extends RecyclerView.ViewHolder {
    TextView lienTextView;

    public ElementViewHolderCryptoActivity(View itemView) {
        super(itemView);
        lienTextView = itemView.findViewById(R.id.lien);
    }
}
