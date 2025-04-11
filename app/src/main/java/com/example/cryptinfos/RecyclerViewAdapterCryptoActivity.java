package com.example.cryptinfos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


/**
 * RecyclerViewAdapterCryptoActivity est un RecyclerView.Adapter contenant des ElementViewHolderCryptoActivity. Il sert à l'affichage des liens liés à la crypto courante dans l'activité CryptoActivity
 */
public class RecyclerViewAdapterCryptoActivity extends RecyclerView.Adapter<ElementViewHolderCryptoActivity> {

    private final Context context;
    private final List<String> liens;

    public RecyclerViewAdapterCryptoActivity(Context context, List<String> liens) {
        this.context = context;
        this.liens = liens;
    }

    public ElementViewHolderCryptoActivity onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_crypto_layout, viewGroup, false);
        return new ElementViewHolderCryptoActivity(view);
    }

    @Override
    public void onBindViewHolder(ElementViewHolderCryptoActivity holder, int position) {
        // Récupération de l'url courant
        String url = liens.get(position);

        // Placement du lien dans le TextView
        TextView lien = holder.lienTextView;
        lien.setText(url);

        // Rendre chaque item cliquable pour ouvrir le lien (Intent Implicite)
        lien.setPaintFlags(lien.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        lien.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return liens.size();
    }
}
