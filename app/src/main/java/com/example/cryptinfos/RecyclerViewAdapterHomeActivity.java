package com.example.cryptinfos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * RecyclerViewAdapterHomeActivity est un RecyclerView.Adapter contenant des ElementViewHolderHomeActivity. Il sert à l'affichage des cryptos dans l'activité HomeActivity
 */
public class RecyclerViewAdapterHomeActivity extends RecyclerView.Adapter<ElementViewHolderHomeActivity> {

    private final Context context;
    private final List<CoinObject> coinList;

    public RecyclerViewAdapterHomeActivity(Context context, List<CoinObject> coinList) {
        this.context = context;
        this.coinList = coinList;
    }

    public ElementViewHolderHomeActivity onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_home_layout, viewGroup, false);
        return new ElementViewHolderHomeActivity(view);
    }

    @Override
    public void onBindViewHolder(ElementViewHolderHomeActivity holder, int position) {
        CoinObject coin = coinList.get(position);

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
