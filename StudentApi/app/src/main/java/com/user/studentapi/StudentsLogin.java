package com.user.studentapi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentsLogin extends AppCompatActivity {

    private EditText Name,Password,addCourseText;
    private TextView Info;
    private Button Login,Register,button;
    private int counter = 3;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef,ref,refer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    StudentInfo student;
String studentpno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_students_login);
        getSupportActionBar().hide();

        Name = (EditText) findViewById(R.id.ID);
        Name.addTextChangedListener(new MaskWatcher("#######@#########"));
        Password = (EditText) findViewById(R.id.Password);
        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    validate(Name.getText().toString(), Password.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
//        Info = (TextView) findViewById(R.id.Info);
        Login = (Button)findViewById(R.id.SignBtn);
        Register = (Button)findViewById(R.id.RegBtn);
        button=(Button)findViewById(R.id.button);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentsLogin.this,ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            finish();
            startActivity(new Intent(StudentsLogin.this,Home.class));
            StudentsLogin.this.finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentsLogin.this,StudentsRegister.class);
//                String namemove=Name.getText().toString();
//                String passmove= Password.getText().toString();
//                intent.putExtra("emailmove",namemove);
//                intent.putExtra("passwordmove",passmove);
                startActivity(intent);
            }
        });
        FirebaseApp.initializeApp(this);
    }

    public void addCourseFromTextBox(){
        validate(Name.getText().toString(), Password.getText().toString());
    }


    private void validate(String userName, String userPassword){
        final String name = Name.getText().toString().trim();
        final String email = Password.getText().toString().trim();

        progressDialog.setMessage("Verifying ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(name,email )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {



                            if (firebaseAuth.getCurrentUser().isEmailVerified())
                            {
                                Toast.makeText(StudentsLogin.this,"Login successful",Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(StudentsLogin.this,Home.class);
                                updateverify();
                                intent.putExtra("email",name);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(StudentsLogin.this,"YOU ARE NOT VERIFIED! ",Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),Name.getText().toString() , Toast.LENGTH_SHORT).show();
                                notverified(Name.getText().toString(),Password.getText().toString());

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Email or Password" , Toast.LENGTH_SHORT).show();



                        }

                        // ...
                    }
                });
    }

    public void notverified(String email,String password){
        Toast.makeText(StudentsLogin.this,"Please Register Again",Toast.LENGTH_SHORT).show();
//        String email=Name.getText().toString();
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential= EmailAuthProvider
                .getCredential(email,password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           user.delete();
            }
        });
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("New Student Register");
//        Toast.makeText(getApplicationContext(), email , Toast.LENGTH_SHORT).show();
                    Query mQuery=mRef.orderByChild("email").equalTo(email);
                    mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {

//                                studentpno = dataSnapshot.child("gaurdianCNIC").getValue().toString();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    student=ds.getValue(StudentInfo.class);
                                    studentpno=student.getGaurdianCNIC();
//                                    Toast.makeText(getApplicationContext(), studentpno , Toast.LENGTH_SHORT).show();
                                    ds.getRef().removeValue();
                                    ref=mFirebaseDatabase.getReference("Parents");
                                    Query Query=ref.orderByChild("gaurdianCNIC").equalTo(studentpno);
                                    Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                                            if(dataSnap.exists()) {
                                                for (DataSnapshot d : dataSnap.getChildren()) {
                                                    d.getRef().removeValue();
                                                }
                                            }
                                            else{

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                            else{

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }
                    });
                startActivity(new Intent(StudentsLogin.this,StudentsRegister.class));
                }

    public void updateverify(){
        final String name = Name.getText().toString().trim();
        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRef=mFirebaseDatabase.getReference("New Student Register");
        Query query =mRef.orderByChild("email").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ds.getRef().child("account").setValue("Verified");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop() {
//        firebaseAuth = FirebaseAuth.getInstance();
//        validate(Name.getText().toString(), Password.getText().toString());
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if(firebaseUser != null && firebaseUser.isEmailVerified()){
//            finish();
//            startActivity(new Intent(StudentsLogin.this,Home.class));
//            StudentsLogin.this.finish();
//        }
        super.onStop();
    }
}

