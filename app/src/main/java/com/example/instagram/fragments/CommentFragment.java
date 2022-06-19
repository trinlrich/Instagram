package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.Comment;
import com.example.instagram.CommentsAdapter;
import com.example.instagram.EndlessRecyclerViewScrollListener;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    public static final String TAG = "CommentFragment";

    protected CommentsAdapter adapter;
    protected List<Comment> allComments;

    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvComments;

    Long maxId;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(getContext(), allComments);

        rvComments = view.findViewById(R.id.rvComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(linearLayoutManager);

//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                loadNextDataFromParse(maxId);
//            }
//        };
//        rvComments.addOnScrollListener(scrollListener);
        queryComments();

        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        Log.i(TAG, "Swiped!");

        // specify what type of data we want to query - Comment.class
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        // include data referred by user key
        query.include(Comment.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(5);
        // order comments by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for comments
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                // for debugging purposes let's print every comments description to logcat
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.getString(Comment.KEY_TEXT) + ", username: " + comment.getUser().getUsername());
                }

                // save received comments to list and notify adapter of new data
                allComments.clear();
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void loadNextDataFromParse(Long offset) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.setSkip(Math.toIntExact(offset));
        query.setLimit(5);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.getString(Comment.KEY_TEXT) + ", username: " + comment.getUser().getUsername());
                }
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
                maxId += offset;
            }
        });
    }

    private void queryComments() {
        // specify what type of data we want to query - Comment.class
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        // include data referred by user key
        query.include(Comment.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(5);
        // order comments by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for comments
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                // for debugging purposes let's print every comments description to logcat
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.getString(Comment.KEY_TEXT) + ", username: " + comment.getUser().getUsername());
                }

                // save received comments to list and notify adapter of new data
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
                maxId = Long.valueOf(comments.size());
            }
        });
    }
}