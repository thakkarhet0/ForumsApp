package com.example.hw06;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ForumsFragment extends Fragment {

    IForums am;
    Button logout, new_forum;
    RecyclerView forums_list;
    LinearLayoutManager llm;
    ArrayList<Services.Forum> forums = new ArrayList<>();
    private FirebaseAuth mAuth;
    ForumListAdapter fla;

    public static ForumsFragment newInstance() {
        return new ForumsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IForums){
            am = (IForums) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        am.setTitle(R.string.forums);
        forums_list = view.findViewById(R.id.forums_list);
        logout = view.findViewById(R.id.button);
        new_forum = view.findViewById(R.id.button2);
        forums_list.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        forums_list.setLayoutManager(llm);
        fla = new ForumListAdapter(forums);
        forums_list.setAdapter(fla);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(forums_list.getContext(),
                llm.getOrientation());
        forums_list.addItemDecoration(dividerItemDecoration);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                am.sendLoginFragment();
            }
        });
        new_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.sendNewForumFragment();
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Services.DOC_FORUM).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(value == null){
                    return;
                }
                forums.clear();
                for(QueryDocumentSnapshot doc : value){
                    Services.Forum forum = doc.toObject(Services.Forum.class);
                    forum.setForumId(doc.getId());
                    forums.add(forum);
                }
                fla.notifyDataSetChanged();
            }
        });
        return view;
    }


    interface IForums{

        void sendLoginFragment();

        void sendNewForumFragment();

        void setTitle(int title_id);

    }

}