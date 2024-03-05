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

import com.example.newspedia.DetailActivity;
import com.example.newspedia.R;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

public class searchListAdapter extends RecyclerView.Adapter<searchListAdapter.ViewHolder> {
    ArrayList<modelNews> items;
    Context context;

    public searchListAdapter(ArrayList<modelNews> items) {
        this.items = items;
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
        holder.judulSearch.setText(items.get(position).getName());
        String posterResourceName = items.get(position).getPoster();
        int drawableResourceId =context.getResources().getIdentifier(posterResourceName,"drawable",context.getPackageName());
        holder.imageSearch.setImageResource(drawableResourceId);
        holder.categorySearch.setText(items.get(position).getCategory());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("judul",items.get(position).getName());
            intent.putExtra("kategori",items.get(position).getCategory());
            intent.putExtra("Detail",items.get(position).getDetail());
            intent.putExtra("Date",items.get(position).getDate());
            intent.putExtra("poster",items.get(position).getPoster());
            context.startActivity(intent);
        });
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
