package com.example.newspedia.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspedia.R;
import com.example.newspedia.adapter.BookmarkAdapter;
import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    private static final String TAG = "BookmarkFragment";

    private RecyclerView recyclerView;
    private BookmarkAdapter adapter;
    private ArrayList<modelNews> newsList;
    private FirebaseUser currentUser;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.recycleViewFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsList = new ArrayList<>();
        adapter = new BookmarkAdapter(newsList, getContext());
        recyclerView.setAdapter(adapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            fetchUserBookmarks(currentUser.getUid());
        } else {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchUserBookmarks(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bookmarks");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> bookmarkNewsIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String newsId = snapshot.getValue(String.class);
                    bookmarkNewsIds.add(newsId);
                    Log.d(TAG, "Bookmark newsId fetched: " + newsId);
                }
                Log.d(TAG, "User bookmarks fetched successfully");
                fetchNewsDetails(bookmarkNewsIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch bookmarks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to fetch bookmarks: " + databaseError.getMessage());
            }
        });
    }


    private void fetchNewsDetails(ArrayList<String> bookmarkNewsIds) {
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("NewsList");
        ArrayList<modelNews> fetchedNewsList = new ArrayList<>();

        for (String newsId : bookmarkNewsIds) {
            newsRef.child(newsId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        modelNews news = dataSnapshot.getValue(modelNews.class);
                        if (news != null) {
                            fetchedNewsList.add(news);
                        } else {
                            Log.w(TAG, "DataSnapshot exists but failed to parse newsId: " + newsId);
                        }
                    } else {
                        Log.w(TAG, "No data found for newsId: " + newsId);
                    }

                    // Check if all news details have been fetched
                    if (fetchedNewsList.size() == bookmarkNewsIds.size()) {
                        // All news details fetched
                        updateRecyclerView(fetchedNewsList);
                        Log.d(TAG, "All news details fetched successfully");
                    } else {
                        Log.d(TAG, "Fetching news details for newsId: " + newsId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to fetch news details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to fetch news details for newsId: " + newsId, databaseError.toException());
                }
            });
        }
    }




    private void updateRecyclerView(ArrayList<modelNews> newsList) {
        this.newsList.clear();
        this.newsList.addAll(newsList);
        adapter.notifyDataSetChanged();
    }
}
