package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.instagram.fragments.FeedFragment;
import com.example.instagram.fragments.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    BottomNavigationItemView feed;
    BottomNavigationItemView post;
    BottomNavigationItemView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // define your fragments here
        final Fragment feedFragment = new FeedFragment();
        final Fragment postFragment = new PostFragment();
//        final Fragment fragment3 = new ThirdFragment();

        feed = findViewById(R.id.action_feed);
        post = findViewById(R.id.action_post);
        profile = findViewById(R.id.action_profile);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "Nav Clicked!");
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        Log.i(TAG, "Feed Fragment");
                        fragment = feedFragment;
                        break;
                    case R.id.action_post:
                        Log.i(TAG, "Post Fragment");
                        fragment = postFragment;
                        break;
//                    case R.id.action_profile:
//                        fragment = profileFragment;
//                        break;
                    default:
                        fragment = postFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }
}