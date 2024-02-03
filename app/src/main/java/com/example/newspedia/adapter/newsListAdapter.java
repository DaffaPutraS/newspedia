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

public class newsListAdapter extends RecyclerView.Adapter<newsListAdapter.ViewHolder> {
    ArrayList<modelNews>items;
    Context context;

    public newsListAdapter(ArrayList<modelNews> items) {
        this.items = items;
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
        holder.judulTxt.setText(items.get(position).getName());
        String posterResourceName = items.get(position).getPoster();
        int drawableResourceId = context.getResources().getIdentifier(posterResourceName, "drawable", context.getPackageName());
        holder.posterImg.setImageResource(drawableResourceId);

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
        TextView judulTxt;
        ImageView posterImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulTxt = itemView.findViewById(R.id.judulTxt);
            posterImg = itemView.findViewById(R.id.posterImg);
        }
    }
}
