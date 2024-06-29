package com.example.newspedia.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newspedia.R;
import com.example.newspedia.adapter.searchListAdapter;
import com.example.newspedia.modelItem.itemNews;
import com.example.newspedia.modelItem.modelNews;

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
        View view = inflater.inflate(R.layout.fragment_search,container,false);
       recycleSearch = view.findViewById(R.id.recycleSearch);

       inputSearch = view.findViewById(R.id.inputSearch);
        initView();
        return view;
    }

    private void initView() {
        if (recycleSearch == null) {
            return;
        }
        recycleSearch.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        newsList = new ArrayList<>();
        for (int i = 0; i< itemNews.posterItem.length;i++){
            modelNews news = new modelNews(
                    itemNews.judulItem[i],
                    itemNews.KategoriItem[i],
                    itemNews.detailItem[i],
                    itemNews.tanggalItem[i],
                    itemNews.posterItem[i]
            );
            newsList.add(news);
        }
        ArrayList<modelNews> simplifiedNewsList = new ArrayList<>();
        for (modelNews news : newsList) {
            simplifiedNewsList.add(new modelNews(news.getName(), news.getCategory(), news.getDetail(), news.getDate(), news.getPoster()));
        }
        adapterSearchList = new searchListAdapter(newsList);


        recycleSearch.setAdapter(adapterSearchList);

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
            if (news.getName().toLowerCase().contains(query.toLowerCase())) {
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