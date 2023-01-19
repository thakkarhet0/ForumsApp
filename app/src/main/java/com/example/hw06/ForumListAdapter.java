package com.example.hw06;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.UViewHolder> {

    ArrayList<Services.Forum> forums;

    IForumAdapter am;

    FirebaseAuth mAuth;

    public ForumListAdapter(ArrayList<Services.Forum> data){
        this.forums = data;
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_item, parent, false);
        am = (IForumAdapter) parent.getContext();
        return new UViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, int position) {
        Services.Forum forum = forums.get(position);
        holder.position = position;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.liked = forum.getLikedBy().contains(user.getUid());
        if(holder.liked){
            holder.like.setImageResource(R.drawable.like_favorite);
        }
        holder.title.setText(forum.getTitle());

        holder.author.setText(forum.getCreatedByName());
        holder.content.setText(forum.getDescription().substring(0, Math.min(forum.getDescription().length(), 200)) + "...");
        SimpleDateFormat dateFormat = new SimpleDateFormat(MainActivity.DATE_FORMAT);
        String deets = dateFormat.format(forum.getCreatedAt());
        int likes = forum.getLikedBy().size();
        if(likes == 1){
            deets = likes + " Like | " + deets;
        }else if(likes > 1){
            deets = likes + " Likes | " + deets;
        }
        holder.details.setText(deets);
        if(!forum.getCreatedById().equals(user.getUid())){
            holder.del.setVisibility(View.GONE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.toggleDialog(true);
                DocumentReference dbc = db.collection(Services.DOC_FORUM).document(forum.getForumId());
                dbc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        am.toggleDialog(false);
                    }
                });
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.liked){
                    forum.unLike(user.getUid());
                    HashMap<String, Object> upd = new HashMap<>();
                    upd.put("likedBy", forum.getLikedBy());
                    am.toggleDialog(true);
                    db.collection(Services.DOC_FORUM).document(forum.getForumId()).update(upd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            am.toggleDialog(false);
                            holder.like.setImageResource(R.drawable.like_not_favorite);
                            holder.liked = false;
                        }
                    });
                }else{
                    forum.addLike(user.getUid());
                    HashMap<String, Object> upd = new HashMap<>();
                    upd.put("likedBy", forum.getLikedBy());
                    am.toggleDialog(true);
                    db.collection(Services.DOC_FORUM).document(forum.getForumId()).update(upd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            am.toggleDialog(false);
                            holder.like.setImageResource(R.drawable.like_favorite);
                            holder.liked = true;
                        }
                    });
                }
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendForumFragment(forum);
            }
        });
    }

    interface IForumAdapter{

        void sendForumFragment(Services.Forum forum);

        void toggleDialog(boolean show);

    }

    @Override
    public int getItemCount() {
        return this.forums.size();
    }

    public static class UViewHolder extends RecyclerView.ViewHolder
    {

        TextView title, author, content, details;
        ImageView del, like;
        View rootView;
        int position;
        boolean liked = false;

        public UViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            title = itemView.findViewById(R.id.for_title);
            author = itemView.findViewById(R.id.for_author);
            content = itemView.findViewById(R.id.for_content);
            details = itemView.findViewById(R.id.for_details);
            del = itemView.findViewById(R.id.imageView);
            like = itemView.findViewById(R.id.imageView3);
        }
    }

}

