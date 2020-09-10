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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ParentPassword extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref,refer;
    Parents link;
    Students students;
    EditText p1,p2,addCourseText;
    Button reset;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_password);
        p1 = (EditText) findViewById(R.id.cnic);
        p2 = (EditText)findViewById(R.id.contact);
        reset = (Button) findViewById(R.id.verify);
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        p1.addTextChangedListener(new MaskWatcher("#####-#######-#"));
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.verify:
                        check();
                        break;
                }
            }
        });
        addCourseText=(EditText)findViewById(R.id.cnic);
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
        check();
    }
    private void check() {
        final String cnic = p1.getText().toString().trim();
        final String contact = p2.getText().toString().trim();
        if (cnic.isEmpty()) {
            p1.setError("CNIC Field is mandatory");
            p1.requestFocus();
            return;
        }
        if (cnic.length() != 15) {
            p1.setError("Invalid CNIC entered");
            p1.requestFocus();
            return;
        }
        if (contact.isEmpty()) {
            p2.setError("Contact Field is mandatory");
            p2.requestFocus();
            return;
        }
        if (contact.length() != 11) {
            p2.setError("Invalid Contact entered");
            p2.requestFocus();

            return;
        }
        progressDialog.setMessage("In Progress...");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("Parents")
                .orderByChild("gaurdianCNIC")
                .equalTo(cnic);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Query query = FirebaseDatabase.getInstance().getReference("Parents")
                            .orderByChild("contact")
                            .equalTo(contact);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                            progressDialog.dismiss();
                            if (dataSnap.exists()) {
                                Intent intent=new Intent(ParentPassword.this,ParentsNewPassword.class);
                                intent.putExtra("contact",contact);
                                intent.putExtra("gaurdianCNIC",cnic);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                    }
                    else{
                    Toast.makeText(ParentPassword.this, "Please Enter your existing Contact and CNIC...", Toast.LENGTH_SHORT).show();
                }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(ParentPassword.this, "ERROR RETRIEVING DATA", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
