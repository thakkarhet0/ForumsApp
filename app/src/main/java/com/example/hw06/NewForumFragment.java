package com.example.hw06;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class NewForumFragment extends Fragment {

    INewForum am;
    EditText title, desc;
    Button submit;
    TextView cancel;
    FirebaseAuth mAuth;

    private FirebaseFirestore db;

    public static NewForumFragment newInstance() {
        return new NewForumFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof INewForum){
            am = (INewForum) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        am.setTitle(R.string.new_forum);
        title = view.findViewById(R.id.editTextTextPersonName);
        desc = view.findViewById(R.id.editTextTextPersonName2);
        submit = view.findViewById(R.id.subbbb);
        cancel = view.findViewById(R.id.textView);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ftitle = title.getText().toString();
                String fdesc = desc.getText().toString();
                if(ftitle.isEmpty() || fdesc.isEmpty()){
                    Toast.makeText(getContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                    return;
                }
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                HashMap<String, Object> forum = new HashMap<>();
                forum.put("title", ftitle);
                forum.put("createdById", user.getUid());
                forum.put("createdByName", user.getDisplayName());
                forum.put("createdAt", new Date());
                forum.put("description", fdesc);
                forum.put("likedBy", new ArrayList<>());
                am.toggleDialog(true);
                db.collection(Services.DOC_FORUM).add(forum)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                am.toggleDialog(false);
                                am.sendForumsFragment();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        am.toggleDialog(false);
                        am.showAlertDialog(e.getMessage());
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.goBack();
            }
        });
        return view;
    }

    interface INewForum{

        void toggleDialog(boolean show);

        void sendForumsFragment();

        void setTitle(int title_id);

        void showAlertDialog(String alert);

        void goBack();

    }

}