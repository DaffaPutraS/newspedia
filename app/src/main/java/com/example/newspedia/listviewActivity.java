package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.newspedia.adapter.newsListAdapter;
import com.example.newspedia.modelItem.itemNews;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

public class listviewActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterNewsList;
    private RecyclerView recycleViewNews;
    private ArrayList<modelNews> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        initView();
    }

    private void initView() {
        recycleViewNews = findViewById(R.id.recycleViewNews);
        recycleViewNews.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        newsList = new ArrayList<>();
        for (int i = 0; i < itemNews.posterItem.length; i++) {
            modelNews news = new modelNews(
                    itemNews.judulItem[i],
                    "",
                    "",
                    "",
                    itemNews.posterItem[i] // Assuming that posterItem contains resource IDs
            );

            newsList.add(news);

        }
        adapterNewsList = new newsListAdapter(newsList);
        recycleViewNews.setAdapter(adapterNewsList);
    }
}