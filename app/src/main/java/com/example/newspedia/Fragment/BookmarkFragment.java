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
                ArrayList<String> bookmarkKeys = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Boolean isBookmarked = snapshot.getValue(Boolean.class);
                    if (isBookmarked != null && isBookmarked) {
                        String key = snapshot.getKey();
                        bookmarkKeys.add(key);
                    }
                }
                fetchNewsDetails(bookmarkKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch bookmarks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to fetch bookmarks: " + databaseError.getMessage());
            }
        });
    }

    private void fetchNewsDetails(ArrayList<String> bookmarkKeys) {
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("NewsList");
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelNews news = snapshot.getValue(modelNews.class);
                    if (news != null && bookmarkKeys.contains(news.getNewsId())) {
                        newsList.add(news);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch news details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to fetch news details: " + databaseError.getMessage());
            }
        });
    }
}
