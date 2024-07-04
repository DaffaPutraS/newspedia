package com.example.newspedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspedia.DetailActivity;
import com.example.newspedia.R;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    private ArrayList<modelNews> newsList;
    private Context context;

    public BookmarkAdapter(ArrayList<modelNews> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        modelNews news = newsList.get(position);

        holder.judulTextView.setText(news.getNameNews());
        String posterResourceUrl = news.getImageNews();
        Glide.with(context)
                .load(posterResourceUrl)
                .centerCrop()
                .into(holder.posterImageView);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", news);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView judulTextView;
        public ImageView posterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulTextView = itemView.findViewById(R.id.judulTitleFav);
            posterImageView = itemView.findViewById(R.id.posterImgFav);
        }
    }
}
