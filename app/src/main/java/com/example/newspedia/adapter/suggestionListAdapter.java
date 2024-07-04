package com.example.newspedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspedia.DetailActivity;
import com.example.newspedia.R;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

public class suggestionListAdapter extends RecyclerView.Adapter<suggestionListAdapter.ViewHolder> {
    private ArrayList<modelNews> items;
    private Context context;

    public suggestionListAdapter(ArrayList<modelNews> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public suggestionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion_list, parent, false);
        context = parent.getContext();
        return new suggestionListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull suggestionListAdapter.ViewHolder holder, int position) {
        modelNews news = items.get(position);
        String posterResourceUrl = items.get(position).getImageNews();
        Log.d("suggestionListAdapter", "Loading image URL: " + posterResourceUrl); // Log to check image URLs
        Glide.with(context)
                .load(posterResourceUrl)
                .into(holder.suggestionImg);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", news);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView suggestionImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestionImg = itemView.findViewById(R.id.suggestionImg);
        }
    }
}
