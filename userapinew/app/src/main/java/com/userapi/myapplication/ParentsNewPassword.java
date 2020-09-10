package com.userapi.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ParentsNewPassword extends AppCompatActivity {
    private EditText pPass, pRepass,addCourseText;
    private Button Update;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_new_password);
        pPass = (EditText) findViewById(R.id.Password);
        pRepass = (EditText) findViewById(R.id.rePassword);
        Update = (Button) findViewById(R.id.ChangeBtn);
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ChangeBtn:
                        updatepassword();
                        break;
                }
            }
        });
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
    public void addCourseFromTextBox(){
        updatepassword();
    }
    public void updatepassword(){
        final String password = pPass.getText().toString().trim();
        final String repassword = pRepass.getText().toString().trim();
        final String contact= getIntent().getStringExtra("contact");
        final String cnic =getIntent().getStringExtra("gaurdianCNIC");
        if (password.isEmpty()) {
            pPass.setError("password Field is empty");
            pPass.requestFocus();
            return;
        }

        if (repassword.equals(password)) {
        } else {
            pRepass.setError("passwords do not match");
            pRepass.requestFocus();
            return;
        }

        if (password.length() < 8) {
            pPass.setError("Invalid password entered");
            pPass.requestFocus();
            return;
        }
        progressDialog.setMessage("In Progress...");
        progressDialog.show();
        final Query query = FirebaseDatabase.getInstance().getReference("Parents")
                .orderByChild("gaurdianCNIC")
                .equalTo(cnic);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        ds.getRef().child("password").setValue(password);
                        Toast.makeText(ParentsNewPassword.this,"Updated, Login with your new password...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ParentsNewPassword.this,ParentsLogin.class));
                        finish();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
