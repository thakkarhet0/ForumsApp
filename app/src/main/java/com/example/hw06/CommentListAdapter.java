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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.UViewHolder> {

    ArrayList<Services.Comment> comments;

    FirebaseAuth mAuth;

    ICommentAdapter am;

    String forum_id;

    public CommentListAdapter(ArrayList<Services.Comment> data, String forumId){
        this.comments = data;
        this.forum_id = forumId;
    }

    interface ICommentAdapter{

        void toggleDialog(boolean show);

    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        am = (ICommentAdapter) parent.getContext();
        return new UViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, int position) {
        Services.Comment comment = comments.get(position);
        holder.position = position;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.author.setText(comment.getCreatedByName());
        holder.comment.setText(comment.getText());
        if(!comment.getCreatedById().equals(user.getUid())){
            holder.del.setVisibility(View.GONE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.toggleDialog(true);
                db.collection(Services.DOC_FORUM).document(forum_id).collection(Services.DOC_COM).document(comment.getCommentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        am.toggleDialog(false);
                    }
                });
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat(MainActivity.DATE_FORMAT);
        holder.details.setText(dateFormat.format(comment.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public static class UViewHolder extends RecyclerView.ViewHolder
    {

        TextView author, comment, details;
        View rootView;
        ImageView del;
        int position;

        public UViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            author = itemView.findViewById(R.id.textView6);
            comment = itemView.findViewById(R.id.textView7);
            details = itemView.findViewById(R.id.textView8);
            del = itemView.findViewById(R.id.imageView2);
        }
    }

}

