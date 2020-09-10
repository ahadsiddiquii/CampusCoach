package com.example.adminapi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import java.util.Objects;

public class AddStudent extends AppCompatActivity {
    EditText mSname,mScontact,mScnic,mSemail,mSpointno,mSpassword,mSreconfirm,mSid,mSjoindate;
    Button mSRegisterBtn;
    String sName,sContact,sCnic,sEmail,sPointno,sId,sJoindate;
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
        setContentView(R.layout.activity_add_student);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Register Student");

        mSname=findViewById(R.id.Studentname);
        mSpointno=findViewById(R.id.Studentpoint);
        mScnic=findViewById(R.id.Studentcnic);
        mSemail=findViewById(R.id.Studentemail);
        mSjoindate=findViewById(R.id.StudenteditText1);
        mScontact=findViewById(R.id.Studentcontact);
        mSpassword=findViewById(R.id.StudentPassword);
        mSreconfirm=findViewById(R.id.Studentreconfirm);
        mSid=findViewById(R.id.Studentid);
        mSRegisterBtn=findViewById(R.id.SRegisterBtn);
        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        mSjoindate.setInputType(InputType.TYPE_NULL);
        mSid.addTextChangedListener(new MaskWatcher("###-####"));
        mScnic.addTextChangedListener(new MaskWatcher("#####-#######-#"));
        mSemail.addTextChangedListener(new MaskWatcher("#######@#########"));
        mSjoindate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddStudent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mSjoindate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Bundle intent=getIntent().getExtras();
        if (intent!=null){
            sName=intent.getString("studentName");
            sPointno=intent.getString("pointnumber");
            sId=intent.getString("studentID");
            sCnic=intent.getString("gaurdianCNIC");
            sEmail=intent.getString("email");
            sJoindate=intent.getString("joindate");
            sContact=intent.getString("contact");

            mSname.setText(sName);
            mSpointno.setText(sPointno);
            mScnic.setText(sCnic);
            mSemail.setText(sEmail);
            mSid.setText(sId);
            mSjoindate.setText(sJoindate);
            mScontact.setText(sContact);
            mSpassword.setText(null);
            mSreconfirm.setText(null);

            actionBar.setTitle("Update Student's Information");
            mSRegisterBtn.setText("Update");

        }

        mSRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSRegisterBtn.getText().equals("Register")){
                    register();
                }
                else{
                    beginUpdate(sId);
                }
            }
        });
    }

    private void beginUpdate(String s) {
        final String sn=mSname.getText().toString();
        final String sid=mSid.getText().toString();
        final String sp=mSpointno.getText().toString();
        final String scn=mScnic.getText().toString();
        final String se=mSemail.getText().toString();
        final String sj=mSjoindate.getText().toString();
        final String sc=mScontact.getText().toString();
        final String spass=mSpassword.getText().toString();
        final String srec=mSreconfirm.getText().toString();

        try {
            int num = Integer.parseInt(scn);
            Log.i("",num+" is a number");
        } catch (NumberFormatException e) {
            Log.i("",scn+" is not a number");
        }
        if(sn.isEmpty())
        {
            mSname.setError("Name required");
            mSname.requestFocus();
            return;
        }
        if(scn.isEmpty())
        {
            mScnic.setError("CNIC is required");
            mScnic.requestFocus();
            return;
        }
        if(scn.length()!=15)
        {
            mScnic.setError("Invalid cnic");
            mScnic.requestFocus();
            return;
        }
        if(scn!=null)
        {
            mScnic.setError("You cannot change cnic");
            mScnic.requestFocus();
        }
        if(sc.isEmpty())
        {
            mScontact.setError("Contact number required");
            mScontact.requestFocus();
            return;
        }
        if(sc.length()!=11)
        {
            mScontact.setError("Enter a valid contact number");
            mScontact.requestFocus();
            return;
        }
        if(sp.isEmpty())
        {
            mSpointno.setError("Valid Point Number Required");
            mSpointno.requestFocus();
            return;
        }
        char[] chars = se.toCharArray();
        System.out.println(chars.length);

        for(int i = 0; i < chars.length; i++)
        {
            if(chars[i] == '@'){
                if (chars[i+1] == 'n' && chars[i+2] == 'u' && chars[i+3] == '.' && chars[i+4] == 'e' && chars[i+5] == 'd' && chars[i+6] == 'u' && chars[i+7] == '.' && chars[i+8] == 'p' && chars[i+9] == 'k'){
                }else{
                    mSemail.setError("Invalid Domain");
                    mSemail.requestFocus();
                    return;
                }
            }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(se).matches())
        {
            mSemail.setError("Enter a valid email");
            mSemail.requestFocus();
            return;
        }
        if (sid.isEmpty()) {
            mSid.setError("ID Field is empty");
            mSid.requestFocus();
            return;
        }

        if (sid.length() != 8) {
            mSid.setError("Invalid student ID entered");
            mSid.requestFocus();
            return;
        }
        if(spass!=null )
        {
            mSpassword.setError("You cannot update the Password");
            mSpassword.requestFocus();

        }
        if(srec!=null )
        {
            mSreconfirm.setError("You cannot update the Password");
            mSreconfirm.requestFocus();

        }


        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRef=mFirebaseDatabase.getReference("New Student Register");
        Query query =mRef.orderByChild("studentID").equalTo(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ds.getRef().child("studentName").setValue(sn);
                    ds.getRef().child("pointnumber").setValue(sp);
                    ds.getRef().child("studentID").setValue(sid);
//                    ds.getRef().child("gaurdianCNIC").setValue(scn);
                    ds.getRef().child("joindate").setValue(sj);
                    ds.getRef().child("email").setValue(se);
                    ds.getRef().child("contact").setValue(sc);

                }
                Toast.makeText(AddStudent.this,"Student's information updated", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference Ref=mFirebaseDatabase.getReference("Parents");
        Query pquery =Ref.orderByChild("gaurdianCNIC").equalTo(sCnic);
        pquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
//                    ds.getRef().child("gaurdianCNIC").setValue(scn);
                    ds.getRef().child("contact").setValue(sc);

                }
//                Toast.makeText(AddStudent.this,"Student's information updated", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        startActivity(new Intent(AddStudent.this,StudentDetailsUpdated.class));
        finish();
    }

    public void register() {
        final String name = mSname.getText().toString().trim();
        final String email = mSemail.getText().toString().trim();
        final String password = mSpassword.getText().toString().trim();
        final String id = mSid.getText().toString().trim();
        final String gcnic = mScnic.getText().toString().trim();
        final String pointnumber = mSpointno.getText().toString().trim();
        final String reconfirm = mSreconfirm.getText().toString().trim();
        final String contact = mScontact.getText().toString().trim();
        final String joindate=mSjoindate.getText().toString().trim();
        try {
            int num = Integer.parseInt(gcnic);
            Log.i("", num + "is a number");
        } catch (NumberFormatException e) {
            Log.i("", gcnic + "is not a number");
        }

        if (name.isEmpty()) {
            mSname.setError("Name Field is empty");
            mSname.requestFocus();
            return;
        }

        if (name.length() > 20) {
            mSname.setError("Name too Long");
            mSname.requestFocus();
            return;
        }

        if (contact.isEmpty()) {
            mScontact.setError("contact Field is empty");
            mScontact.requestFocus();
            return;
        }

        if (contact.length() != 11) {
            mScontact.setError("invalid number");
            mScontact.requestFocus();
            return;
        }

        char[] chars = email.toCharArray();
        System.out.println(chars.length);

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '@') {
                if (chars[i + 1] == 'n' && chars[i + 2] == 'u' && chars[i + 3] == '.' && chars[i + 4] == 'e' && chars[i + 5] == 'd' && chars[i + 6] == 'u' && chars[i + 7] == '.' && chars[i + 8] == 'p' && chars[i + 9] == 'k') {
                } else {
                    mSemail.setError("Invalid Domain");
                    mSemail.requestFocus();
                    return;
                }
            }
        }

        if (email.isEmpty()) {
            mSemail.setError("Email Field is empty");
            mSemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mSemail.setError("Please enter a valid email");
            mSemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mSpassword.setError("password Field is empty");
            mSpassword.requestFocus();
            return;
        }

        if (reconfirm.equals(password)) {
        } else {
            mSreconfirm.setError("passwords do not match");
            mSreconfirm.requestFocus();
            return;
        }

        if (password.length() != 8) {
            mSpassword.setError("Invalid password entered");
            mSpassword.requestFocus();
            return;
        }

        if (id.isEmpty()) {
            mSid.setError("ID Field is empty");
            mSid.requestFocus();
            return;
        }

        if (id.length() != 8) {
            mSid.setError("Invalid student ID entered");
            mSid.requestFocus();
            return;
        }

        if (gcnic.isEmpty()) {
            mScnic.setError("Email Field is empty");
            mScnic.requestFocus();
            return;
        }

        if (gcnic.length() != 15) {
            mScnic.setError("Invalid CNIC entered");
            mScnic.requestFocus();
            return;
        }

        if (pointnumber.length() > 2) {
            mSpointno.setError("Invalid Point number");
            mSpointno.requestFocus();
            return;
        }
        if (joindate.isEmpty())
        {
            mSjoindate.setError("Date Field is Empty");
            mSjoindate.requestFocus();
            return;
        }
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("New Student Register");
        final int[] flag = {0};
        Query mQuery = ref.orderByChild("gaurdianCNIC").equalTo(gcnic);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Toast.makeText(AddStudent.this, "CNIC already exists...", Toast.LENGTH_SHORT).show();
                        mScnic.setError("CNIC already exists...");
                        mScnic.requestFocus();

                    }
                    return;

                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //store data in firebase
                                        StudentInfo students = new StudentInfo(name, id, gcnic, email, pointnumber,contact,joindate,"Not Verified",password);
                                        Parents parents = new Parents(gcnic, password,contact);
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        FirebaseDatabase.getInstance().getReference("New Student Register")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(students).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(getApplicationContext(),"Registeration Successful, Please check registered email for verification",Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(AddStudent.this,StudentDetailsUpdated.class);
                                                                startActivity(intent);
                                                                AddStudent.this.finish();
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Registeration Unsuccessful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        FirebaseDatabase pdatabase = FirebaseDatabase.getInstance();
                                        FirebaseDatabase.getInstance().getReference("Parents")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(parents).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Registeration successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AddStudent.this, StudentDetailsUpdated.class));
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Registeration Unsuccessful", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(AddStudent.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
