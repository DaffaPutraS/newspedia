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

import com.example.newspedia.DetailActivity;
import com.example.newspedia.R;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

public class suggestionListAdapter extends RecyclerView.Adapter<suggestionListAdapter.ViewHolder>  {
    ArrayList<modelNews> items;
    Context context;

    public suggestionListAdapter(ArrayList<modelNews> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public suggestionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion_list,parent,false);
        context = parent.getContext();
        return new suggestionListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull suggestionListAdapter.ViewHolder holder, int position) {

        String posterResourceName = items.get(position).getPoster();
        int drawableResourceId = context.getResources().getIdentifier(posterResourceName, "drawable", context.getPackageName());
        holder.suggestionImg.setImageResource(drawableResourceId);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("judul", items.get(position).getName());
            intent.putExtra("kategori", items.get(position).getCategory());
            intent.putExtra("Detail", items.get(position).getDetail());
            intent.putExtra("Date", items.get(position).getDate());
            intent.putExtra("poster", items.get(position).getPoster());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView suggestionImg ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            suggestionImg = itemView.findViewById(R.id.suggestionImg);
        }
    }
}
