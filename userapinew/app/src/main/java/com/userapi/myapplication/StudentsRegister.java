package com.userapi.myapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class StudentsRegister extends AppCompatActivity{

    private long backPressedTime;
    private EditText sname, semail, spassword, sstudentid, sgaurdiancnic, spointnumber, sreconfirm,scontact,eText,addCourseText;
    private Button RegBtn;
    private FirebaseAuth mAuth, nAuth,firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseDatabase database,firebaseDatabase;
    DatabaseReference ref;
    DatePickerDialog picker;
    FirebaseUser user;
String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        spointnumber = (EditText) findViewById(R.id.Pointno);
        sname = (EditText) findViewById(R.id.name);
        spassword = (EditText) findViewById(R.id.password);
        sstudentid = (EditText) findViewById(R.id.id);
        getSupportActionBar().hide();
        sstudentid.addTextChangedListener(new MaskWatcher("###-####"));
        sgaurdiancnic = (EditText) findViewById(R.id.gaurdiancnic);
        sgaurdiancnic.addTextChangedListener(new MaskWatcher("#####-#######-#"));
        semail = (EditText) findViewById(R.id.email);
        semail.addTextChangedListener(new MaskWatcher("#######@#########"));
        sreconfirm = (EditText) findViewById(R.id.reconfirm);
        scontact =  (EditText)findViewById(R.id.contact);
        scontact.setOnEditorActionListener(new DoneOnEditorActionListener());
        scontact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    register();
                    handled = true;
                }
                return handled;
            }
        });
        addCourseText=(EditText)findViewById(R.id.Pointno);
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





        eText = (EditText) findViewById(R.id.date);
        RegBtn = (Button) findViewById(R.id.RegBtn);
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.RegBtn:
                        register();
                        break;
                }
            }
        });
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        nAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        eText = (EditText) findViewById(R.id.date);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(StudentsRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    private void addCourseFromTextBox(){
     register();
    }
    private void register() {

        progressDialog.setMessage("Registering ...");
        progressDialog.show();
        final String name = sname.getText().toString().trim();
        final String email = semail.getText().toString().trim();
        final String password = spassword.getText().toString().trim();
        final String id = sstudentid.getText().toString().trim();
        final String gcnic = sgaurdiancnic.getText().toString().trim();
        final String pointnumber = spointnumber.getText().toString().trim();
        final String joindate=eText.getText().toString().trim();
        final String reconfirm = sreconfirm.getText().toString().trim();
        final String contact = scontact.getText().toString().trim();
        try {
            int num = Integer.parseInt(gcnic);
            Log.i("", num + "is a number");
        } catch (NumberFormatException e) {
            Log.i("", gcnic + "is not a number");
        }

        if (name.isEmpty()) {
            sname.setError("Name Field is empty");
            sname.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (name.length() > 40) {
            sname.setError("Name too Long");
            sname.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (contact.isEmpty()) {
            scontact.setError("contact Field is empty");
            scontact.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (contact.length() != 11) {
            scontact.setError("invalid number");
            scontact.requestFocus();
            progressDialog.dismiss();
            return;
        }

        char[] chars = email.toCharArray();
        System.out.println(chars.length);

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '@') {
                if (chars[i + 1] == 'n' && chars[i + 2] == 'u' && chars[i + 3] == '.' && chars[i + 4] == 'e' && chars[i + 5] == 'd' && chars[i + 6] == 'u' && chars[i + 7] == '.' && chars[i + 8] == 'p' && chars[i + 9] == 'k') {
                } else {
                    semail.setError("Invalid Domain");
                    semail.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
            }
        }

        int amount = Integer.parseInt(spointnumber.getText().toString());
        if (amount < 1 || amount > 23)
        {
            spointnumber.setError("Invalid Point number");
            spointnumber.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (email.isEmpty()) {
            semail.setError("Email Field is empty");
            semail.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            semail.setError("Please enter a valid email");
            semail.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (password.isEmpty()) {
            spassword.setError("password Field is empty");
            spassword.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (reconfirm.equals(password)) {
        } else {
            sreconfirm.setError("passwords do not match");
            sreconfirm.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (password.length() < 8) {
            spassword.setError("Invalid password entered");
            spassword.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (id.isEmpty()) {
            sstudentid.setError("ID Field is empty");
            sstudentid.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (id.length() != 8) {
            sstudentid.setError("Invalid student ID entered");
            sstudentid.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (gcnic.isEmpty()) {
            sgaurdiancnic.setError("Email Field is empty");
            sgaurdiancnic.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (gcnic.length() != 15) {
            sgaurdiancnic.setError("Invalid Parent CNIC entered");
            sgaurdiancnic.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (pointnumber.length() > 2) {
            spointnumber.setError("Invalid Point number");
            spointnumber.requestFocus();
            progressDialog.dismiss();
            return;
        }


        if (joindate.isEmpty())
        {
            eText.setError("Date Field is Empty");
            eText.requestFocus();
            progressDialog.dismiss();
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
                        Toast.makeText(StudentsRegister.this, "CNIC already exists...", Toast.LENGTH_SHORT).show();
                        sgaurdiancnic.setError("CNIC already exists...");
                        sgaurdiancnic.requestFocus();
                        progressDialog.dismiss();

                    }
                    return;

                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        //store data in firebase
//                                        firebaseAuth = FirebaseAuth.getInstance();
//                                        firebaseDatabase = FirebaseDatabase.getInstance();
//                                        if (firebaseAuth.getCurrentUser() != null) {
//                                            user = firebaseAuth.getCurrentUser();
//                                            userid = user.getUid();
//                                        }

                                        Students students = new Students(name, id, gcnic, email, pointnumber,contact,joindate,"Not Verified",password);
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
                                                                Toast.makeText(getApplicationContext(),"Registeration Successful, Please check your email for verification",Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(StudentsRegister.this,StudentsLogin.class);
                                                                startActivity(intent);
                                                                StudentsRegister.this.finish();
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Registeration Unsuccessful", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
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
//                                                    Toast.makeText(getApplicationContext(), "Registeration Successful", Toast.LENGTH_SHORT).show();
//                                                    startActivity(new Intent(StudentsRegister.this, StudentsLogin.class));
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Registeration Unsuccessful", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(StudentsRegister.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

