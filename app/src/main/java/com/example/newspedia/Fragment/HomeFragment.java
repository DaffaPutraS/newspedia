package com.example.newspedia.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import com.example.newspedia.modelItem.itemNews;
import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView.Adapter adapterNewsList;
    private ConstraintLayout clWorld,clScience, clSport,clPolitics,clCriminal,clAll;
    private CardView bgWorld,bgScience,bgSport,bgPolitics,bgCriminal,bgAll;
    private TextView textWorld,textScience,textSport,textPolitics,textCriminal,textAll;
    private RecyclerView recycleViewNews;
    private ArrayList<modelNews> newsList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private TextView textName;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        recycleViewNews = view.findViewById(R.id.recycleViewNews1);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "User is not logged in");
            return view;
        }
        textName = view.findViewById(R.id.tvUsernameP);
        displayUser();
        initView();
        clWorld = view.findViewById(R.id.clWorld);
        clScience = view.findViewById(R.id.clScience);
        clSport = view.findViewById(R.id.clSport);
        clCriminal = view.findViewById(R.id.clCriminal);
        clPolitics = view.findViewById(R.id.clPolitic);
        clAll = view.findViewById(R.id.clAll);
        textAll = view.findViewById(R.id.textAll);
        textWorld = view.findViewById(R.id.textWorld);
        textCriminal = view.findViewById(R.id.textCriminal);
        textPolitics= view.findViewById(R.id.tvName);
        textSport = view.findViewById(R.id.textSport);
        textScience=view.findViewById(R.id.textScience);
        bgWorld = view.findViewById(R.id.bgWorld);
        bgAll = view.findViewById(R.id.bgAll);
        bgCriminal = view.findViewById(R.id.bgCriminal);
        bgPolitics= view.findViewById(R.id.bgPolitics);
        bgScience= view.findViewById(R.id.bgScience);
        bgSport=view.findViewById(R.id.bgSport);



        bgAll.setOnClickListener(v -> onCategoryClicked(bgAll ,"All"));
        bgWorld.setOnClickListener(v -> onCategoryClicked(bgWorld,"#World"));
        bgScience.setOnClickListener(v -> onCategoryClicked(bgScience, "#Science"));
        bgCriminal.setOnClickListener(v -> onCategoryClicked(bgCriminal ,"#Criminal"));
        bgPolitics.setOnClickListener(v -> onCategoryClicked(bgPolitics,"#Politics"));
        bgSport.setOnClickListener(v -> onCategoryClicked(bgSport, "#Sports"));

        return view;

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
                        textName.setText("Welcome, "+username);
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


    private void onCategoryClicked(CardView clickedCategory,String category){
        resetCategoryBackground();
        clickedCategory.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.hijau_tua));
        textSport.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
        textCriminal.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
        textPolitics.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
        textWorld.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
        textAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
        textScience.setTextColor(ContextCompat.getColor(requireContext(),R.color.black));
        filterByCategory(category);
    }

    private void resetCategoryBackground() {

        bgAll.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_card));
        bgSport.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_card));
        bgWorld.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_card));
        bgPolitics.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_card));
        bgCriminal.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_card));
        bgScience.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_card));
    }

    private void initView(){
        if (recycleViewNews == null) {
            return;
        }

        recycleViewNews.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        newsList = new ArrayList<>();
        for (int i = 0; i < itemNews.posterItem.length; i++) {
            modelNews news = new modelNews(
                    itemNews.judulItem[i],
                    itemNews.KategoriItem[i],
                    itemNews.detailItem[i],
                    itemNews.tanggalItem[i],
                    itemNews.posterItem[i] // Assuming that posterItem contains resource IDs
            );

            newsList.add(news);
        }

        ArrayList<modelNews> simplifiedNewsList = new ArrayList<>();
        for (modelNews news : newsList) {
            simplifiedNewsList.add(new modelNews(news.getName(), news.getCategory(), news.getDetail(), news.getDate(), news.getPoster()));
        }

        // Set up the adapter and pass the original newsList
        adapterNewsList = new newsListAdapter(newsList);
        recycleViewNews.setAdapter(adapterNewsList);
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
