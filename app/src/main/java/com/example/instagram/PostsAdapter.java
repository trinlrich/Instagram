package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagram.fragments.CommentFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{
    private static final String TAG = "PostsAdapter";

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton ivProfileImage;
        private TextView tvUsername;
        private ImageView ivPostImage;
        private TextView tvCaption;
        private ImageButton ibLike;
        private ImageButton ibComment;
        private TextView tvRelativeTimeAgo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvName);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ibLike = itemView.findViewById(R.id.ibLike);
            ibComment = itemView.findViewById(R.id.ibComment);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvRelativeTimeAgo = itemView.findViewById(R.id.tvRelativeTimeAgo);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            Date createdAt = post.getCreatedAt();
            tvRelativeTimeAgo.setText(Post.calculateTimeAgo(createdAt));

            //Profile Image
            ParseFile profileImage = post.getUser().getParseFile(Post.KEY_PROFILE_IMAGE);
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .transform(new CircleCrop())
                        .into(ivProfileImage);
            } else {
                ivProfileImage.setImageResource(R.drawable.empty_profile);
            }
            ivProfileImage.setOnClickListener(this::onProfileClick);

            //Post Image
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivPostImage);
            }
            ivPostImage.setOnClickListener(this::onImageClick);

            //Like and Caption Buttons
            ibLike.setOnClickListener(this::onLikeClick);
            ibComment.setOnClickListener(this::onCommentClick);
        }

        public void onProfileClick(View view) {
            Fragment fragment = new ProfileFragment();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        }

        public void onImageClick(View v) {
            Log.i(TAG, "Image clicked!");
            int position = getAdapterPosition();
            Log.i("Check", "onClick " + position);
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }

        public void onLikeClick(View view) {
            Log.i(TAG, "Like clicked!");
            int position = getAdapterPosition();
            Post post = posts.get(position);

            Log.i(TAG, String.valueOf(post.has("something")));
            Array likes = (Array) post.get("likes");

//            post.add("likes", ParseUser.getCurrentUser());
            Log.i(TAG, "Image liked!");

//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
//            query.whereEqualTo(ParseObject.KEY_OBJECT_ID, post.getObjectId());
//            query.findInBackground(new FindCallback<ParseObject>() {
//                public void done(List<ParseObject> list, ParseException e) {
//                    if (e == null) {
//                        ParseObject person = list.get(0);
//                        person.put("firstName", "Johan");
//                        person.saveInBackground();
//                    } else {
//                        Log.d("score", "Error: " + e.getMessage());
//                    }
//                }
//            });

            ibLike.setImageResource(R.drawable.ufi_heart_active);

        }

        public void onCommentClick(View view) {
            Fragment fragment = new CommentFragment();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
