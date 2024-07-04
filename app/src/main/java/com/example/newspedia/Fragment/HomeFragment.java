package com.example.newspedia.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newspedia.R;
import com.example.newspedia.adapter.newsListAdapter;

import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView.Adapter adapterNewsList;

    private RecyclerView recycleViewNews;
    private ArrayList<modelNews> newsList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recycleCategory;




    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private TextView textName;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "User is not logged in");
            return view;
        }
        textName = view.findViewById(R.id.tvUsernameP);
        recycleViewNews = view.findViewById(R.id.recycleViewNews1);


        displayUser();
        fetchNews();


        // Set up the news RecyclerView
        recycleViewNews.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        newsList = new ArrayList<>();
        adapterNewsList = new newsListAdapter(newsList);
        recycleViewNews.setAdapter(adapterNewsList);

        // Set up the category RecyclerView



        return view;
    }



    private void fetchNews() {
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference().child("NewsList");
        newsList = new ArrayList<>();
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelNews news = snapshot.getValue(modelNews.class);
                    newsList.add(news);
                }
                adapterNewsList.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void displayUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        String displayName = firebaseUser.getDisplayName();
        if (displayName != null) {
            textName.setText(displayName);
        } else {
            textName.setText("User");
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null && !username.isEmpty()) {
                        textName.setText("Welcome, " + username);
                    } else {
                        textName.setText("No username found");
                    }
                } else {
                    textName.setText("Snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }



    private void filterByCategory(String category) {
        ArrayList<modelNews> filteredList = new ArrayList<>();
        for (modelNews news : newsList) {
            if (category.equals("All") || news.getCategory().equals(category)) {
                filteredList.add(news);
            }
        }

        // Update the adapter with filtered data
        adapterNewsList = new newsListAdapter(filteredList);
        recycleViewNews.setAdapter(adapterNewsList);
    }
}
