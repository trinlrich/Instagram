package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcel;
import org.parceler.Parcels;

public class PostDetailActivity extends AppCompatActivity {

    private Post post;

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvCaption;
    private TextView tvRelativeTimeAgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvCaption = findViewById(R.id.tvCaption);
        tvRelativeTimeAgo = findViewById(R.id.tvRelativeTimeAgo);

        Intent intent = getIntent();
        post = Parcels.unwrap(intent.getParcelableExtra(Post.class.getSimpleName()));

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getCaption());
        tvRelativeTimeAgo.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this)
                    .load(image.getUrl())
                    .into(ivImage);
        }
    }
}