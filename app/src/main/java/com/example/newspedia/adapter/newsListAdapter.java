package com.example.newspedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class newsListAdapter extends RecyclerView.Adapter<newsListAdapter.ViewHolder> {
    ArrayList<modelNews>items;
    Context context;

    public newsListAdapter(ArrayList<modelNews> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_news,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        modelNews news = items.get(position);

        holder.judulTxt.setText(items.get(position).getNameNews());
        String posterResourceUrl = items.get(position).getImageNews();
        Glide.with(context)
                .load(posterResourceUrl)
                .into(holder.posterImg);
        Log.d("news", "Binding news: " + news.getNameNews());
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView judulTxt;
        ImageView posterImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulTxt = itemView.findViewById(R.id.judulTxt);
            posterImg = itemView.findViewById(R.id.posterImg);
        }
    }
}
