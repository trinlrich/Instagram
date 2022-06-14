package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.load.model.UriLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView rvPosts;
    Button btnNewPost;
    Button btnLogout;

    Long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        rvPosts = findViewById(R.id.rvPosts);
        btnNewPost = findViewById(R.id.btnNewPost);
        btnLogout = findViewById(R.id.btnLogout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromParse(maxId);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts();
    }

    private void loadNextDataFromParse(Long offset) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setSkip(Math.toIntExact(offset));
        query.setLimit(5);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getCaption() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                maxId += offset;
            }
        });
    }

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(5);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getCaption() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                maxId = Long.valueOf(posts.size());
            }
        });
    }

    public void onNewPostClick(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    public void onLogoutClick(View view) {
        ParseUser.logOutInBackground();
        ParseUser current = ParseUser.getCurrentUser();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}