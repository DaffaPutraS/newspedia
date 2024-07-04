package com.example.newspedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspedia.DetailActivity;
import com.example.newspedia.R;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

public class searchListAdapter extends RecyclerView.Adapter<searchListAdapter.ViewHolder> {
    ArrayList<modelNews> items;
    Context context;

    public searchListAdapter(ArrayList<modelNews> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public searchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_list,parent,false);
        context= parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull searchListAdapter.ViewHolder holder, int position) {
        modelNews news = items.get(position);
        holder.judulSearch.setText(items.get(position).getNameNews());
        String posterResourceUrl = items.get(position).getImageNews();
        Glide.with(context)
                .load(posterResourceUrl)
                .into(holder.imageSearch);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", news);
            context.startActivity(intent);
        });
        holder.categorySearch.setText(items.get(position).getCategory());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView judulSearch, categorySearch;
        ImageView imageSearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulSearch = itemView.findViewById(R.id.judulSearch);
            categorySearch = itemView.findViewById(R.id.categorySearch);
            imageSearch = itemView.findViewById(R.id.imageSearch);
        }
    }
    public void setLayoutManager(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
    }
}
