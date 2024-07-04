package com.example.newspedia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspedia.Fragment.HomeFragment;

import com.example.newspedia.R;
import com.example.newspedia.modelItem.modelCategory;

import java.util.ArrayList;

public class categoryListAdapter extends RecyclerView.Adapter<categoryListAdapter.ViewHolder>{
    ArrayList<modelCategory> items;
    Context context;


    public categoryListAdapter(Context context, ArrayList<modelCategory> items, HomeFragment homeFragment) {
        this.context = context;
        this.items = items;

    }

    public void setItems(ArrayList<modelCategory> items) {
        this.items = items;
        notifyDataSetChanged(); // Ensure the data is updated in the adapter
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryListAdapter.ViewHolder holder, int position) {
        modelCategory category = items.get(position);
        holder.categoryName.setText(category.getNameCategory());
        Log.d("CategoryAdapter", "Binding category: " + category.getNameCategory()); // Logging the category being bound
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textCategory);

        }
    }
}
