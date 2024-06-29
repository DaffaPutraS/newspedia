package com.example.newspedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newspedia.adapter.suggestionListAdapter;
import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterNewsSuggestionList;
    private RecyclerView recycleViewSuggestion;
    private ConstraintLayout backDetailBtn;
    private modelNews object;
    private DatabaseReference databaseReference;
    private TextView judulTextView, kategoriTextView, detailTextView, tanggalTextView;
    private ImageView posterImageView;

    private ArrayList<modelNews> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        judulTextView = findViewById(R.id.JudulTxt);
        kategoriTextView = findViewById(R.id.CategoryTxt);
        detailTextView = findViewById(R.id.detailTxt);
        tanggalTextView = findViewById(R.id.tanggalTxt);
        posterImageView = findViewById(R.id.posterDetail);
        backDetailBtn = findViewById(R.id.BackDetailBtn);
        recycleViewSuggestion = findViewById(R.id.recycleViewSuggestion);

        // Initialize the list before setting it to adapter
        newsList = new ArrayList<>();
        adapterNewsSuggestionList = new suggestionListAdapter(newsList);
        recycleViewSuggestion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycleViewSuggestion.setAdapter(adapterNewsSuggestionList);

        backDetailBtn.setOnClickListener(view -> onBackPressed());

        getBundle();
        populateNewsList();
    }

    private void populateNewsList() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("newsList");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelNews news = snapshot.getValue(modelNews.class);
                    if (news != null) {
                        newsList.add(news);
                        Log.d("DetailActivity", "News added: " + news.getNameNews()); // Log to check news items
                    } else {
                        Log.d("DetailActivity", "News item is null");
                    }
                }
                adapterNewsSuggestionList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void getBundle() {
        object = (modelNews) getIntent().getSerializableExtra("object");
        if (object != null) {
            Glide.with(this)
                    .load(object.getImageNews())
                    .centerCrop()
                    .into(posterImageView);

            judulTextView.setText(object.getNameNews());
            kategoriTextView.setText(object.getCategory());
            detailTextView.setText(object.getDescription());
            tanggalTextView.setText(object.getDatePublish());
        } else {
            Log.e("DetailActivity", "Object is null");
        }
    }
}
