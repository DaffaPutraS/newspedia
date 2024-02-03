package com.example.newspedia;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newspedia.adapter.newsListAdapter;
import com.example.newspedia.modelItem.itemNews;
import com.example.newspedia.modelItem.modelNews;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView.Adapter adapterNewsList;
    private ConstraintLayout clWorld,clScience, clSport,clPolitics,clCriminal,clAll;
    private RecyclerView recycleViewNews;
    private ArrayList<modelNews> newsList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycleViewNews = view.findViewById(R.id.recycleViewNews1);
        clWorld = view.findViewById(R.id.clWorld);
        clScience = view.findViewById(R.id.clScience);
        clSport = view.findViewById(R.id.clSport);
        clCriminal = view.findViewById(R.id.clCriminal);
        clPolitics = view.findViewById(R.id.clPolitic);
        clAll = view.findViewById(R.id.clAll);

        initView();

        clWorld.setOnClickListener(v -> filterByCategory("#World"));
        clScience.setOnClickListener(v -> filterByCategory("#Science"));
        clSport.setOnClickListener(v -> filterByCategory("#Sports"));
        clCriminal.setOnClickListener(v -> filterByCategory("#Criminal"));
        clPolitics.setOnClickListener(v -> filterByCategory("#Politics"));
        clAll.setOnClickListener(v -> filterByCategory("All"));

        return view;

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
