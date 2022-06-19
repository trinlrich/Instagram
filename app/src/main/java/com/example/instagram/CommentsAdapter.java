package com.example.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private static final String TAG = "CommentsAdapter";

    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCommentProfile;
        private TextView tvComment;
        private TextView tvRelativeTimeAgo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentProfile = itemView.findViewById(R.id.ivCommentProfile);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvRelativeTimeAgo = itemView.findViewById(R.id.tvRelativeTimeAgo);
        }

        public void bind(Comment comment) {
            tvComment.setText(comment.getString(Comment.KEY_TEXT));
            tvRelativeTimeAgo.setText(Comment.calculateTimeAgo(comment.getCreatedAt()));
            ParseFile profileImage = comment.getUser().getParseFile(Comment.KEY_PROFILE_IMAGE);
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .transform(new CircleCrop())
                        .into(ivCommentProfile);
            } else {
                ivCommentProfile.setImageResource(R.drawable.empty_profile);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }
}
