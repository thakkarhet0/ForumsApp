package com.example.hw06;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterFragment extends Fragment {

    RegInterface am;
    Button sub_btn;
    TextView reg_cancel;
    EditText txt_name, txt_email, txt_pass;
    private FirebaseAuth mAuth;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegInterface){
            am = (RegInterface) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        am.setTitle(R.string.register_account);
        txt_name = view.findViewById(R.id.reg_name);
        txt_email = view.findViewById(R.id.reg_email);
        txt_pass = view.findViewById(R.id.reg_password);
        sub_btn = view.findViewById(R.id.button_submit);
        reg_cancel = view.findViewById(R.id.reg_cancel);

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_name.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || txt_pass.getText().toString().isEmpty()){
                    am.showAlertDialog(getString(R.string.empty_fields));
                    return;
                }
                am.toggleDialog(true);
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(txt_email.getText().toString(), txt_pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                am.toggleDialog(false);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest.Builder upcr = new UserProfileChangeRequest.Builder();
                                    upcr.setDisplayName(txt_name.getText().toString());
                                    user.updateProfile(upcr.build());
                                    am.sendForumsFragment();
                                } else {
                                    am.showAlertDialog(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
        reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.goBack();
            }
        });
        return view;
    }

    interface RegInterface{

        void showAlertDialog(String msg);

        void toggleDialog(boolean show);

        void setTitle(int title_id);

        void sendForumsFragment();

        void sendLoginFragment();

        void goBack();

    }

}