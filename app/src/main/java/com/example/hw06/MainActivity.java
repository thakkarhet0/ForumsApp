package com.example.hw06;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity.java
 * Assignment HW06
 * Sneh Jain
 * Ivy Pham
 */

public class MainActivity extends AppCompatActivity implements CommentListAdapter.ICommentAdapter, ForumListAdapter.IForumAdapter, LoginFragment.LoginInterface, ForumFragment.IForum, RegisterFragment.RegInterface, ForumsFragment.IForums, NewForumFragment.INewForum {

    public static final String DATE_FORMAT = "MM/dd/yyyy h:ma";

    ProgressDialog dialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            sendLoginFragment();
        }else{
            sendForumsFragment();
        }
    }

    public void sendLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, LoginFragment.newInstance())
                .commit();
    }

    public void sendForumFragment(Services.Forum forum){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, ForumFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }

    public void toggleDialog(boolean show){
        if(show) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();
        }else{
            dialog.dismiss();
        }
    }

    public void showAlertDialog(String alert){
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(alert)
                .setPositiveButton(R.string.okay, null)
                .show();
    }

    @Override
    public void sendNewForumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, NewForumFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void sendRegisterFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, RegisterFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void sendForumsFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, ForumsFragment.newInstance())
                .commit();
    }

    public void goBack(){
        getSupportFragmentManager().popBackStack();
    }

}