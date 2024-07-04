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
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newspedia.adapter.suggestionListAdapter;
import com.example.newspedia.modelItem.modelBookmark;
import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private modelBookmark objectBookmark;
    private DatabaseReference bookmarksRef;
    private TextView judulTextView, kategoriTextView, detailTextView, tanggalTextView;
    private ImageView posterImageView, bookmarkFav;
    private ArrayList<modelNews> newsList;
    private FirebaseUser currentUser;

    private String newsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bookmarksRef = database.getReference("users");

        judulTextView = findViewById(R.id.JudulTxt);
        kategoriTextView = findViewById(R.id.CategoryTxt);
        detailTextView = findViewById(R.id.detailTxt);
        tanggalTextView = findViewById(R.id.tanggalTxt);
        posterImageView = findViewById(R.id.posterDetail);
        bookmarkFav = findViewById(R.id.bookmarkFav);
        backDetailBtn = findViewById(R.id.BackDetailBtn);
        recycleViewSuggestion = findViewById(R.id.recycleViewSuggestion);

        newsList = new ArrayList<>();
        adapterNewsSuggestionList = new suggestionListAdapter(newsList);
        recycleViewSuggestion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycleViewSuggestion.setAdapter(adapterNewsSuggestionList);

        backDetailBtn.setOnClickListener(view -> onBackPressed());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        bookmarkFav.setOnClickListener(view -> {
            if (currentUser != null && object != null) {
                toggleBookmark(object.getNewsId(), currentUser.getUid());
            } else if (currentUser != null) {
                Toast.makeText(DetailActivity.this,"User ada bang" + currentUser,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetailActivity.this, "Unable to bookmark. User or News is null.", Toast.LENGTH_SHORT).show();
            }
        });

        getBundle();
    }

    private void getBundle() {
        object = (modelNews) getIntent().getSerializableExtra("object");
        newsKey = getIntent().getStringExtra("newsKey");
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

    private void toggleBookmark(String newsKey, String userId) {
        DatabaseReference userBookmarksRef = bookmarksRef.child(userId).child("bookmarks").child(newsKey);
        userBookmarksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Bookmark exists, remove it
                    userBookmarksRef.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailActivity.this, "Bookmark removed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Failed to remove bookmark.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Bookmark doesn't exist, add it
                    userBookmarksRef.setValue(object).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailActivity.this, "Bookmark added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Failed to add bookmark.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
