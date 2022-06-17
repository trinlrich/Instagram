package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.ViewHolder> {
    private static final String TAG = "PostGridAdapter";

    private Context context;
    private List<Post> posts;

    public PostGridAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_grid_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PostGridAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Post post = posts.get(position);
        holder.bind(post);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPost = itemView.findViewById(R.id.ivPost);
        }

        public void bind(Post post) {
            if (post != null) {
                ParseFile image = post.getImage();
                if (image != null) {
                    Glide.with(context)
                            .load(image.getUrl())
                            .into(ivPost);
                }
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "ViewHolder clicked!");
            int position = getAdapterPosition();
            Log.i("Check", "onClick " + position);
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }
    }
}
