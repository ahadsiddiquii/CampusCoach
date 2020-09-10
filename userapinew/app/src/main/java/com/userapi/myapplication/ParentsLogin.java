package com.userapi.myapplication;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ParentsLogin extends AppCompatActivity {
    EditText cnic,code,addCourseText;
    boolean compare=false;
    Button button,forgot;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    Parents parentss;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_login);
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        cnic=(EditText)findViewById(R.id.ID);
        code=(EditText)findViewById(R.id.Password);
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
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if(firebaseUser != null){
//            finish();
//            startActivity(new Intent(ParentsLogin.this,ParentsMapsActivity.class));
//            ParentsLogin.this.finish();
//        }
        button=(Button)findViewById(R.id.SignBtn);
        forgot=(Button)findViewById(R.id.button);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button:
                        startActivity(new Intent(ParentsLogin.this,ParentPassword.class));
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login();
                }
        });
    }
    public void addCourseFromTextBox(){
        login();
    }
    public void login(){
        final String checkcontact,checkpassword,forward;
        checkcontact=cnic.getText().toString().trim();
        checkpassword=code.getText().toString().trim();
        forward=cnic.getText().toString();
        if (checkcontact.isEmpty())
        {
            cnic.setError("Please enter your contact number");
            cnic.requestFocus();
            return;
        }
        if (checkpassword.isEmpty())
        {
            code.setError("please enter the password");
            code.requestFocus();
            return;
        }
        progressDialog.setMessage("In Progress...");
        progressDialog.show();
        Query query= FirebaseDatabase.getInstance().getReference("Parents")
                .orderByChild("contact")
                .equalTo(cnic.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                    {
                        final Parents  parents = postSnapshot.getValue(Parents.class);
                        compare= parents.getpassword().equals(code.getText().toString());
                        if (compare)
                        {
                            Intent intent=new Intent(ParentsLogin.this,ParentsMapsActivity.class);
                            intent.putExtra("contact",cnic.getText().toString());

//                            Query query = FirebaseDatabase.getInstance().getReference("Parents")
//                                    .orderByChild("contact")
//                                    .equalTo(cnic.getText().toString());
//                            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        for(DataSnapshot ds: dataSnapshot.getChildren())
//                                        {
//                                            parentss=ds.getValue(Parents.class);
//                                            id=parents.getId();
//                                            Toast.makeText(ParentsLogin.this, id, Toast.LENGTH_SHORT).show();
//                                        }
////                    id = dataSnapshot.child("id").getValue().toString();
//
//                                    }
//                                    else{
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });

                            startActivity(intent);
                        }
                    }
                    if (!compare)
                    {
                        code.setError("Incorrect Password");
                        code.requestFocus();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(ParentsLogin.this,"Invalid Contact number or Password",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(ParentsLogin.this,"ERROR RETRIEVING DATA",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ParentsLogin.this,SplashScreen.class);
        startActivity(intent);
        finish();
    }
}