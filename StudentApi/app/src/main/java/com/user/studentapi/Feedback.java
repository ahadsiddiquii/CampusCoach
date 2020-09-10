package com.user.studentapi;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.media.MediaCas;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.AsyncTask;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import javax.mail.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;


public class Feedback extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private TextView name;
    private Button sendfeedback;
    String username;
    String msg;
    String em;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        firebaseAuth=FirebaseAuth.getInstance();
        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Feedback");
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.feedbacktypelist,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        feedbackSpinner.setAdapter(adapter);
        feedbackSpinner.setOnItemSelectedListener(this);
        final String feedbackType = feedbackSpinner.getSelectedItem().toString();
        final CheckBox responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
        final boolean bRequiresResponse = responseCheckbox.isChecked();
        final EditText feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedbackField.setMovementMethod(new ScrollingMovementMethod());
        final String feedback = feedbackField.getText().toString();
        name=(TextView)findViewById(R.id.Name);
        sendfeedback=(Button)findViewById(R.id.ButtonSendFeedback);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=firebaseAuth.getCurrentUser();
        String id=user.getUid();
        final String driveremail=user.getEmail();
        msg=feedbackField.getText().toString();
        DatabaseReference databaseReference=firebaseDatabase.getReference("New Student Register");
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=dataSnapshot.child("studentName").getValue().toString();
                em=dataSnapshot.child("email").getValue().toString();
                name.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(Intent.ACTION_SEND);
//                i.setData(Uri.parse("mailfrom:"+driveremail));
                boolean check;
                String ftype;
                ftype=feedbackSpinner.getSelectedItem().toString();
                String myresponse;
                check=responseCheckbox.isChecked();
                String myfeedback=feedbackField.getText().toString();
                if(myfeedback.isEmpty())
                {
                    feedbackField.setError("Feedback Required");
                    feedbackField.requestFocus();
                    return;
                }
                if (check)
                {
                    myresponse="YES";
                }
                else
                {
                    myresponse="NO";
                }
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, ftype);
                intent.putExtra(Intent.EXTRA_TEXT,"Name: "+username+"\n\nEmail: "+em+"\n\nFeedback: "+ feedbackField.getText().toString()+"\n\n Email Response required? : "+myresponse);
                intent.setData(Uri.parse("mailto:firebasefrt@gmail.com"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
                finish();
//                Toast.makeText(Feedback.this,"Feedback Sent.",Toast.LENGTH_SHORT).show();
//                i.setType("message/rfc822");
//                try
//                {
//                    startActivity(Intent.createChooser(i,"Please select email"));
//                }
//                catch (android.content.ActivityNotFoundException ex)
//                {
//                    Toast.makeText(FeedBack.this,"There are no email clients",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

