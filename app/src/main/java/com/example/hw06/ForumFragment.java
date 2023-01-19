package com.example.hw06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ForumFragment extends Fragment {

    private static final String FORUM = "forum";

    TextView title, author, desc, com_count;

    EditText comm;

    Button post;
    RecyclerView comments;

    LinearLayoutManager llm;

    FirebaseAuth mAuth;

    private Services.Forum forum;

    CommentListAdapter cla;

    IForum am;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IForum){
            am = (IForum) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    public static ForumFragment newInstance(Services.Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forum = (Services.Forum) getArguments().getSerializable(FORUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        am.setTitle(R.string.forum);
        title = view.findViewById(R.id.textView2);
        author = view.findViewById(R.id.textView3);
        desc = view.findViewById(R.id.textView4);
        com_count = view.findViewById(R.id.textView5);
        comments = view.findViewById(R.id.comments_list);
        comm = view.findViewById(R.id.editTextTextPersonName3);
        post = view.findViewById(R.id.button3);
        mAuth = FirebaseAuth.getInstance();
        author.setText(forum.getCreatedByName());
        title.setText(forum.getTitle());
        desc.setText(forum.getDescription());
        comments.setHasFixedSize(true);
        cla = new CommentListAdapter(forum.getComments(), forum.getForumId());
        comments.setAdapter(cla);
        llm = new LinearLayoutManager(getContext());
        comments.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(comments.getContext(),
                llm.getOrientation());
        comments.addItemDecoration(dividerItemDecoration);

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String com = comm.getText().toString();
                if(com.isEmpty()){
                    return;
                }

                comm.setText("");
                HashMap<String, Object> comment = new HashMap<>();
                comment.put("text", com);
                comment.put("createdById", user.getUid());
                comment.put("createdByName", user.getDisplayName());
                comment.put("createdAt", new Date());
                am.toggleDialog(true);
                db.collection(Services.DOC_FORUM).document(forum.getForumId())
                        .collection(Services.DOC_COM)
                        .add(comment)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                am.toggleDialog(false);
                                if(!task.isSuccessful()) {
                                    am.showAlertDialog(task.getException().getMessage());
                                }
                            }
                        });
            }
        });

        db.collection(Services.DOC_FORUM).document(forum.getForumId()).collection(Services.DOC_COM).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(value == null){
                    return;
                }
                forum.clearComments();
                for(QueryDocumentSnapshot doc : value){
                    Services.Comment comment = doc.toObject(Services.Comment.class);
                    comment.setCommentId(doc.getId());
                    forum.addComment(comment);
                }
                cla.notifyDataSetChanged();
                com_count.setText(forum.getComments().size() + " Comments");
            }
        });

        return view;
    }

    interface IForum{

        void showAlertDialog(String msg);

        void toggleDialog(boolean show);

        void setTitle(int title_id);

    }

}