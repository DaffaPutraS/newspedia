package com.example.newspedia.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newspedia.R;
//import com.example.newspedia.adapter.searchListAdapter;
import com.example.newspedia.adapter.searchListAdapter;
import com.example.newspedia.modelItem.itemNews;
import com.example.newspedia.modelItem.modelNews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView.Adapter adapterSearchList;
    private RecyclerView recycleSearch;
    private EditText inputSearch;
    private ArrayList<modelNews> newsList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the RecyclerView
        recycleSearch = view.findViewById(R.id.recycleSearch);
        recycleSearch.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Initialize the search input field
        inputSearch = view.findViewById(R.id.inputSearch);

        // Initialize the news list and adapter
        newsList = new ArrayList<>();
        adapterSearchList = new searchListAdapter(newsList);
        recycleSearch.setAdapter(adapterSearchList);

        // Fetch the search data
        fetchSearch();
        initView();
        return view;
    }
    private void fetchSearch() {
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference().child("NewsList");
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelNews news = snapshot.getValue(modelNews.class);
                    if (news != null) {
                        newsList.add(news);
                    } else {
                        Log.e("SearchFragment", "modelNews is null");
                    }
                }
                adapterSearchList.notifyDataSetChanged(); // Notify the adapter that the data has changed
                Log.d("SearchFragment", "Data loaded: " + newsList.size() + " items");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SearchFragment", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void initView() {
        if (recycleSearch == null) {
            return;
        }



        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // kosongkan metode ini
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // panggil metode untuk melakukan pencarian
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // kosongkan metode ini
            }
        });
    }
    private void performSearch(String query) {
        ArrayList<modelNews> filteredList = new ArrayList<>();
        for (modelNews news : newsList) {
            // Jika judul berisi teks pencarian, tambahkan ke daftar hasil pencarian
            if (news.getNameNews().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(news);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(requireContext(), "Berita tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
        // Buat adapter baru dengan daftar hasil pencarian yang difilter
        adapterSearchList = new searchListAdapter(filteredList);
        // Atur adapter ke RecyclerView
        recycleSearch.setAdapter(adapterSearchList);
    }
}