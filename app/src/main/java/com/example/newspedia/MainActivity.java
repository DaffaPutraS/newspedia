package com.example.newspedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.newspedia.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frame_layout);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    loadFragment(new HomeFragment(), false);

                } else if (itemId == R.id.search) {
                    loadFragment(new SearchFragment(), false);

                } else if (itemId == R.id.bookmark) {
                    loadFragment(new BookmarkFragment(), false);

                } else {
                    loadFragment(new ProfileFragment(), false);

                }

                return true;
            }
        });

        loadFragment(new HomeFragment(), true);

    }


    private void loadFragment(Fragment fragment, boolean isAppInitialized) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized){
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }


        fragmentTransaction.commit();
    }

}