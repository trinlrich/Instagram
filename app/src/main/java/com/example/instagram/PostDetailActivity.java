package com.example.instagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailActivity extends AppCompatActivity {

    public static final String TAG = "PostDetailActivity";

    private Post post;

    private ActionBar actionBar;

    private TextView tvUsername;
    private ImageView ivProfileImage;
    private ImageView ivPostImage;
    private TextView tvCaption;
    private TextView tvRelativeTimeAgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(R.drawable.nav_logo_whiteout);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tvUsername = findViewById(R.id.tvName);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        tvCaption = findViewById(R.id.tvCaption);
        tvRelativeTimeAgo = findViewById(R.id.tvRelativeTimeAgo);

        Intent intent = getIntent();
        post = Parcels.unwrap(intent.getParcelableExtra(Post.class.getSimpleName()));

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getCaption());
        tvRelativeTimeAgo.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        ParseFile image = post.getImage();
        if (image != null) {
            Log.i(TAG, image.getUrl());
            Glide.with(this)
                    .load(image.getUrl())
                    .into(ivPostImage);
        }

        ParseFile profileImage = post.getUser().getParseFile(Comment.KEY_PROFILE_IMAGE);
        if (profileImage != null) {
            Glide.with(this)
                    .load(profileImage.getUrl())
                    .transform(new CircleCrop())
                    .into(ivProfileImage);
        } else {
            ivProfileImage.setImageResource(R.drawable.empty_profile);
        }
    }
}