package com.example.cryptinfos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.ExplorerViewHolder> {

    private Context context;
    private List<String> explorers;

    public ExplorerAdapter(Context context, List<String> explorers) {
        this.context = context;
        this.explorers = explorers;
    }

    @NonNull
    @Override
    public ExplorerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ExplorerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorerViewHolder holder, int position) {
        String url = explorers.get(position);
        holder.textView.setText(url);

        // Rendre chaque item cliquable pour ouvrir le lien
        holder.textView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return explorers.size();
    }

    public static class ExplorerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ExplorerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
