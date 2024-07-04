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
import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterNewsSuggestionList;
    private RecyclerView recycleViewSuggestion;
    private ConstraintLayout backDetailBtn;
    private modelNews object;
    private DatabaseReference bookmarksRef;
    private TextView judulTextView, kategoriTextView, detailTextView, tanggalTextView;
    private ImageView posterImageView, bookmarkFav;
    private ArrayList<modelNews> newsList;
    private FirebaseUser currentUser;
    private String newsCategory;
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
        getBundle();

        bookmarkFav.setOnClickListener(view -> {
            if (currentUser != null && newsKey != null) {
                toggleBookmark(newsKey, currentUser.getUid());
            } else {
                Toast.makeText(DetailActivity.this, "Unable to bookmark. User or NewsKey is null.", Toast.LENGTH_SHORT).show();
                if (currentUser != null) {
                    Log.e("DetailActivity", "Current User ID: " + currentUser.getUid());
                }
                Log.e("DetailActivity", "NewsKey: " + newsKey);
            }
        });
    }

    private void fetchSuggestedNews(String currentNewsId, String currentCategory) {
        if (currentNewsId == null || currentCategory == null) {
            Log.e("DetailActivity", "Current news ID or category is null");
            return;
        }

        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("NewsList");
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<modelNews> filteredNewsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelNews news = snapshot.getValue(modelNews.class);
                    if (news != null) {
                        String key = snapshot.getKey();
                        if (key != null && !key.equals(currentNewsId) && currentCategory.equals(news.getCategory())) {
                            news.setNewsId(key); // Set the key as newsId for convenience
                            filteredNewsList.add(news);
                        }
                    }
                }

                // Shuffle the list and get up to 3 items
                Collections.shuffle(filteredNewsList);
                List<modelNews> suggestedNewsList = filteredNewsList.size() > 3 ? filteredNewsList.subList(0, 3) : filteredNewsList;

                // Update the adapter with the suggested news
                newsList.clear();
                newsList.addAll(suggestedNewsList);
                adapterNewsSuggestionList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Failed to fetch news suggestions: " + databaseError.getMessage());
            }
        });
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

            newsCategory = object.getCategory(); // Ensure newsCategory is set
            fetchNewsKey();
        } else {
            Log.e("DetailActivity", "Object is null");
        }

        // Log to verify the newsKey
        Log.d("DetailActivity", "Received newsKey: " + newsKey);
    }

    private void fetchNewsKey() {
        // Assuming you're fetching the newsKey from the database using object.getNewsId()
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("NewsList").child(object.getNewsId());
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsKey = snapshot.getKey();
                Log.d("DetailActivity", "Fetched newsKey: " + newsKey);

                // Call fetchSuggestedNews after newsKey and newsCategory are initialized
                fetchSuggestedNews(newsKey, newsCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DetailActivity", "Failed to fetch newsKey: " + error.getMessage());
            }
        });
    }

    private void checkBookmarkStatus(String newsId, String userId) {
        DatabaseReference bookmarkRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bookmarks").child(newsId);
        bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isBookmarked = snapshot.getValue(Boolean.class);
                if (isBookmarked != null && isBookmarked) {
                    bookmarkFav.setImageResource(R.drawable.ic_bokmarked2); // Change to your bookmarked icon
                } else {
                    bookmarkFav.setImageResource(R.drawable.bookmark2); // Change to your unbookmarked icon
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DetailActivity", "Failed to check bookmark status: " + error.getMessage());
            }
        });
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
                            bookmarkFav.setImageResource(R.drawable.bookmark2);
                            Toast.makeText(DetailActivity.this, "Bookmark removed successfully!", Toast.LENGTH_SHORT).show();
                            checkBookmarkStatus(newsKey, userId); // Refresh bookmark status
                        } else {
                            Toast.makeText(DetailActivity.this, "Failed to remove bookmark.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Bookmark doesn't exist, add it
                    userBookmarksRef.setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            bookmarkFav.setImageResource(R.drawable.ic_bokmarked2);
                            Toast.makeText(DetailActivity.this, "Bookmark added successfully!", Toast.LENGTH_SHORT).show();
                            checkBookmarkStatus(newsKey, userId); // Refresh bookmark status
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
