package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.newspedia.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    // Konstanta untuk nilai-nilai ID menu
    private static final int HOME_MENU_ID = R.id.home;
    private static final int SEARCH_MENU_ID = R.id.search;
    private static final int BOOKMARK_MENU_ID = R.id.bookmark;
    private static final int PROFILE_MENU_ID = R.id.profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        // mengganti fragment yang ditampilkan dengan HomeFragment
        replaceFragment(new HomeFragment());

        // mengatur orientasi layar menjadi portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int selectedMenuId = item.getItemId();

            if (selectedMenuId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (selectedMenuId == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (selectedMenuId == R.id.bookmark) {
                replaceFragment(new BookmarkFragment());
            } else if (selectedMenuId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    // method untuk mengganti fragment yang ditampilkan
    private void replaceFragment(Fragment fragment) {
        Log.d("FragmentSwitch", "Replacing fragment with: " + fragment.getClass().getSimpleName());

        // mengambil FragmentManager dari activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // memulai transaksi fragment
        fragmentTransaction.replace(R.id.frame_layout, fragment); // mengganti fragment yang ditampilkan pada frame_layout dengan fragment yang di-passing
        fragmentTransaction.commit(); // menyelesaikan transaksi fragment
    }

}