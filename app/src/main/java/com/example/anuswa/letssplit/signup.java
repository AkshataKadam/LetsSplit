package com.example.anuswa.letssplit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    private EditText namesign, emailsign, passsign;
    private Button signup;
    private FirebaseAuth mAuth;
    private TextView oldAcc;
    private EditText mobileno;
    FirebaseUser firebaseUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;



    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        namesign = findViewById(R.id.name_id);
        emailsign = findViewById(R.id.email_id);
        passsign = findViewById(R.id.password_id);
        signup = findViewById(R.id.signUp_id);
        mobileno = findViewById(R.id.MobSign_id);
        mAuth = FirebaseAuth.getInstance();
         ref = FirebaseDatabase.getInstance().getReference("users");
        oldAcc = findViewById(R.id.oldAcc_id);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser(v);

            }
        });
        oldAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(signup.this, login.class);
                startActivity(in);
            }
        });


    }

    private void registeruser(final View view) {
        String name = namesign.getText().toString().trim();
        final String email = emailsign.getText().toString().trim();
        final String password = passsign.getText().toString().trim();
        String mobile = mobileno.getText().toString().trim();

        user.setName(name);
        user.setEmailid(email);
        user.setPass(password);
        user.setMobileno(mobile);

        if (email.isEmpty()) {
            emailsign.setError("Email is required");
            emailsign.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passsign.setError("Password is required");
            passsign.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailsign.setError("Please enter a valid email_id!");
            emailsign.requestFocus();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Signing in..!!");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                        signin(email,password);
                    Toast.makeText(signup.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(signup.this, crtgrp.class);
                    startActivity(in);
                    } else {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(signup.this, "You are already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                    }
            }
        }
        );

    }
    void signin(String email,String password)
    {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        firebaseUser = mAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        ref.child(uid).setValue(user);
                            Intent intent = new Intent(signup.this,crtgrp.class);
                            startActivity(intent);
                    }
                }
            })  ;

    }

}
