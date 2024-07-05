package com.example.newspedia;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newspedia.Fragment.BookmarkFragment;
import com.example.newspedia.Fragment.HomeFragment;
import com.example.newspedia.Fragment.ProfileFragment;
//import com.example.newspedia.Fragment.SearchFragment;
import com.example.newspedia.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private ProgressBar progressBar; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frame_layout);
        progressBar = findViewById(R.id.progressBar); // Initialize the ProgressBar


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                progressBar.setVisibility(View.VISIBLE); // Show the ProgressBar when a navigation item is selected

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
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);

        // Check if the fragment to load is already the current fragment
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return; // Do nothing if the fragment to load is the same as the current one
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized){
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }

        fragmentTransaction.commit();

        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar during fragment transition

        // Delay hiding ProgressBar for 5 seconds
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                2000 // delay in milliseconds (5 seconds)
        );

        // Register fragment lifecycle callback to hide ProgressBar when view is destroyed
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar when the fragment view is destroyed
            }
        }, false);
    }

}