package com.example.adminapi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddDriver extends AppCompatActivity {
    EditText mDname,mDcontact,mDcnic,mDemail,mDjoindate,mDpointno,mDpassword;
    Button mRegisterBtn;
    String dName,dContact,dCnic,dEmail,dJoindate,dPointno;
    private FirebaseAuth mAuth;

    DatePickerDialog picker;
    ProgressDialog mProgressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    AnnounceInfo announce;
    AdminAnnounce adminAnnouce;
    DatabaseReference  refer;

    DatabaseReference reference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Register Driver");

        mDname=findViewById(R.id.Drivername);
        mDpointno=findViewById(R.id.Driverpoint);
        mDcnic=findViewById(R.id.Drivercnic);
        mDemail=findViewById(R.id.Driveremail);
        mDjoindate=findViewById(R.id.DrivereditText1);
        mDcontact=findViewById(R.id.Drivercontact);
        mDpassword=findViewById(R.id.DriverPassword);
        mRegisterBtn=findViewById(R.id.RegisterBtn);
        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        mDjoindate.setInputType(InputType.TYPE_NULL);
        mDjoindate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddDriver.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mDjoindate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Bundle intent=getIntent().getExtras();
        if (intent!=null){
            dName=intent.getString("dname");
            dPointno=intent.getString("dpointno");
            dCnic=intent.getString("dcnic");
            dEmail=intent.getString("demail");
            dJoindate=intent.getString("djoindate");
            dContact=intent.getString("dcontact");

            mDname.setText(dName);
            mDpointno.setText(dPointno);
            mDcnic.setText(dCnic);
            mDemail.setText(dEmail);
            mDjoindate.setText(dJoindate);
            mDcontact.setText(dContact);
            mDpassword.setText(null);

            actionBar.setTitle("Update Driver's Information");
            mRegisterBtn.setText("Update");

        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRegisterBtn.getText().equals("Register")){
                    register();
                }
                else{
                    beginUpdate(dCnic);
                }
            }
        });
    }

    private void beginUpdate(String s) {
        final String dn=mDname.getText().toString();
        final String dp=mDpointno.getText().toString();
        final String dcn=mDcnic.getText().toString();
        final String de=mDemail.getText().toString();
        final String dj=mDjoindate.getText().toString();
        final String dc=mDcontact.getText().toString();
        final String dpass=mDpassword.getText().toString();

        try {
            int num = Integer.parseInt(dcn);
            Log.i("",num+" is a number");
        } catch (NumberFormatException e) {
            Log.i("",dcn+" is not a number");
        }
        if(dn.isEmpty())
        {
            mDname.setError("Name required");
            mDname.requestFocus();
            return;
        }
        if(dcn.isEmpty())
        {
            mDcnic.setError("CNIC is required");
            mDcnic.requestFocus();
            return;
        }
        if(dcn.length()!=13)
        {
            mDcnic.setError("Invalid cnic");
            mDcnic.requestFocus();
            return;
        }
        if(dc.isEmpty())
        {
            mDcontact.setError("Contact number required");
            mDcontact.requestFocus();
            return;
        }
        if(dc.length()!=11)
        {
            mDcontact.setError("Enter a valid contact number");
            mDcontact.requestFocus();
            return;
        }
        if(dp.isEmpty())
        {
            mDpointno.setError("Valid Point Number Required");
            mDpointno.requestFocus();
            return;
        }
        char[] chars = de.toCharArray();
        System.out.println(chars.length);

        for(int i = 0; i < chars.length; i++)
        {
            if(chars[i] == '@'){
                if (chars[i+1] == 'n' && chars[i+2] == 'u' && chars[i+3] == '.' && chars[i+4] == 'e' && chars[i+5] == 'd' && chars[i+6] == 'u' && chars[i+7] == '.' && chars[i+8] == 'p' && chars[i+9] == 'k'){
                }else{
                    mDemail.setError("Invalid Domain");
                    mDemail.requestFocus();
                    return;
                }
            }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(de).matches())
        {
            mDemail.setError("Enter a valid email");
            mDemail.requestFocus();
            return;
        }
        if(dpass!=null)
        {
            mDpassword.setError("You cannot update the Password");
            mDpassword.requestFocus();

        }


        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRef=mFirebaseDatabase.getReference("Driver Details");
        Query query =mRef.orderByChild("dcnic").equalTo(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ds.getRef().child("dname").setValue(dn);
                    ds.getRef().child("dpointno").setValue(dp);
                    ds.getRef().child("dcnic").setValue(dcn);
                    ds.getRef().child("demail").setValue(de);
                    ds.getRef().child("djoindate").setValue(dj);
                    ds.getRef().child("dcontact").setValue(dc);

                }
                Toast.makeText(AddDriver.this,"Driver's information updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddDriver.this,DriverDetailsUpdated.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void register() {
        final String name=mDname.getText().toString().trim();
        final String cnic=mDcnic.getText().toString().trim();
        final String pointno=mDpointno.getText().toString().trim();
        final String joindate=mDjoindate.getText().toString().trim();
        final String contact=mDcontact.getText().toString().trim();
        final String email=mDemail.getText().toString().trim();
        String password=mDpassword.getText().toString().trim();
        try {
            int num = Integer.parseInt(cnic);
            Log.i("",num+" is a number");
        } catch (NumberFormatException e) {
            Log.i("",cnic+" is not a number");
        }
        if(name.isEmpty())
        {
            mDname.setError("Name required");
            mDname.requestFocus();
            return;
        }
        if(cnic.isEmpty())
        {
            mDcnic.setError("CNIC is required");
            mDcnic.requestFocus();
            return;
        }
        if(cnic.length()!=13)
        {
            mDcnic.setError("Invalid cnic");
            mDcnic.requestFocus();
            return;
        }
        if(contact.isEmpty())
        {
            mDcontact.setError("Contact number required");
            mDcontact.requestFocus();
            return;
        }
        if(contact.length()!=11)
        {
            mDcontact.setError("Enter a valid contact number");
            mDcontact.requestFocus();
            return;
        }
        if(pointno.isEmpty())
        {
            mDpointno.setError("Valid Point Number Required");
            mDpointno.requestFocus();
            return;
        }
        char[] chars = email.toCharArray();
        System.out.println(chars.length);

        for(int i = 0; i < chars.length; i++)
        {
            if(chars[i] == '@'){
                if (chars[i+1] == 'n' && chars[i+2] == 'u' && chars[i+3] == '.' && chars[i+4] == 'e' && chars[i+5] == 'd' && chars[i+6] == 'u' && chars[i+7] == '.' && chars[i+8] == 'p' && chars[i+9] == 'k'){
                }else{
                    mDemail.setError("Invalid Domain");
                    mDemail.requestFocus();
                    return;
                }
            }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mDemail.setError("Enter a valid email");
            mDemail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            mDpassword.setError("Create Password");
            mDpassword.requestFocus();
            return;
        }
        if(password.length()<8)
        {
            mDpassword.setError("Password should be atleast 8 characters long");
            mDpassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //store data in firebase
                            DriverInfo driverInfo=new DriverInfo(name,contact,cnic,email,joindate,pointno);
                            FirebaseDatabase.getInstance().getReference("Driver Details")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(driverInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Registeration Successful",Toast.LENGTH_SHORT).show();
                                        startActivity( new Intent(AddDriver.this,DriverDetailsUpdated.class));
                                        AddDriver.this.finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Registeration Unsuccessful",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(AddDriver.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

