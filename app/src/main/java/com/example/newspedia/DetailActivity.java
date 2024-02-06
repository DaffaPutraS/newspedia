package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.newspedia.adapter.suggestionListAdapter;
import com.example.newspedia.modelItem.itemNews;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterNewsSuggestionList;
    private RecyclerView recycleViewSuggestion;
    ConstraintLayout backDetailBtn ;

    private ArrayList<modelNews> newsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        String judul = getIntent().getStringExtra("judul");
        String kategori = getIntent().getStringExtra("kategori");
        String detail = getIntent().getStringExtra("Detail");
        String tanggal = getIntent().getStringExtra("Date");
        String poster = getIntent().getStringExtra("poster");
        TextView judulTextView = findViewById(R.id.JudulTxt);
        judulTextView.setText(judul);


        TextView kategoriTextView = findViewById(R.id.CategoryTxt);
        kategoriTextView.setText(kategori);

        TextView detailTextView = findViewById(R.id.detailTxt);
        detailTextView.setText(detail);

        TextView tanggalTextView = findViewById(R.id.tanggalTxt);
        tanggalTextView.setText(tanggal);

        ImageView posterImageView = findViewById(R.id.posterDetail);
        int drawableResourceId = getResources().getIdentifier(poster, "drawable", getPackageName());
        posterImageView.setImageResource(drawableResourceId);


        RecyclerView recycleViewSuggestion = findViewById(R.id.recycleViewSuggestion);
        if (recycleViewSuggestion == null) {
            return;
        }
        recycleViewSuggestion.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < itemNews.posterItem.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        newsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int index = indices.get(i);
            modelNews news = new modelNews(
                    itemNews.judulItem[index],
                    itemNews.KategoriItem[index],
                    itemNews.detailItem[index],
                    itemNews.tanggalItem[index],
                    itemNews.posterItem[index] // Assuming that posterItem contains resource IDs
            );

            newsList.add(news);
        }
        ArrayList<modelNews> simplifiedNewsList = new ArrayList<>();
        for (modelNews news : newsList) {
            simplifiedNewsList.add(new modelNews(news.getName(), news.getCategory(), news.getDetail(), news.getDate(), news.getPoster()));
        }
        adapterNewsSuggestionList = new suggestionListAdapter(newsList);
        recycleViewSuggestion.setAdapter(adapterNewsSuggestionList);

        backDetailBtn = findViewById(R.id.BackDetailBtn);

        backDetailBtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }

}