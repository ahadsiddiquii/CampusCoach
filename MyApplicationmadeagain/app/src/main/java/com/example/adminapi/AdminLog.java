package com.example.adminapi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class AdminLog extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmail, mPassword,addCourseText;
    ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log);
        mEmail = (EditText) findViewById(R.id.ID);
        mPassword = (EditText) findViewById(R.id.Password);
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        progressBar.setVisibility(View.GONE);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null )
        {
            finish();
            startActivity(new Intent(AdminLog.this,Home.class));
            AdminLog.this.finish();
        }
        progressDialog.dismiss();
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
//        findViewById(R.id.registration).setOnClickListener(this);
        findViewById(R.id.SignBtn).setOnClickListener(this);
        addCourseText=(EditText)findViewById(R.id.Password);
        addCourseText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addCourseFromTextBox();
                            return true;

                            default:
                                break;
                    }
                }


                return false;
            }
        });



    }

    private void addCourseFromTextBox(){
        userLogin();
    }


    private void registerUser() {
        String email= mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        if(email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return;
        }
        if(password.length()<6) {
            mPassword.setError("Minimum length of password should be 6");
            mPassword.requestFocus();
            return;
        }
//        progressBar.setVisibility(View.VISIBLE);
        progressDialog.setMessage("In Progress...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"User Registered Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLog.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                    } else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void userLogin() {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Minimum length of password should be 6");
            mPassword.requestFocus();
            return;
        }
//        progressBar.setVisibility(View.VISIBLE);
        progressDialog.setMessage("In Progress...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User Registered Successful", Toast.LENGTH_SHORT).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AdminLog.this, Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.registration:
//                registerUser();
//                break;
            case R.id.SignBtn:
                userLogin();
                break;
        }
    }
    @Override
    public void onBackPressed(){
     Intent intent = new Intent(AdminLog.this, SplashScreen.class);
        startActivity(intent);
        finish();
    }
}



