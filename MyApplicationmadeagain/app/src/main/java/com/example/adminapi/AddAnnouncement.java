package com.example.adminapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAnnouncement extends AppCompatActivity {
EditText mAnnouncement,mDescription;
    Button mAnnounceButton;
    String newAnnouncement,newDescription;

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
        setContentView(R.layout.activity_add_announcement);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Announcement");

        mAnnouncement = findViewById(R.id.pAnnouncement);
        mAnnouncement.setMovementMethod(new ScrollingMovementMethod());
        mDescription = findViewById(R.id.pDescription);
        mDescription.setMovementMethod(new ScrollingMovementMethod());
        mAnnounceButton = findViewById(R.id.pAnnounceButton);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            newAnnouncement = intent.getString("dannounce");
            mAnnouncement.setText(newAnnouncement);
            newDescription = intent.getString("extension");
            mDescription.setText(newDescription);
            actionBar.setTitle("Update Announcement");
            mAnnounceButton.setText("Update");


        }
        mAnnounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnnounceButton.getText().equals("Announce")) {
                    uploadDataToFirebase();


                } else {
                    beginUpdate(newAnnouncement,newDescription);

                }
            }
        });
    }

    public void uploadDataToFirebase(){
        Date date=new Date();
        Date newDate=new Date(date.getTime());
//        +(604800000L*2)+(24*60*60)
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate=dt.format(newDate);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String strTime =mdformat.format(calendar.getTime());
        final String announce = mAnnouncement.getText().toString().trim();
        final String desc = mDescription.getText().toString().trim();
        if (announce.isEmpty()) {
            mAnnouncement.setError("Title Empty");
            mAnnouncement.requestFocus();
            return;
        }
        if (desc.isEmpty()) {
            mDescription.setError("Announcement Empty");
            mDescription.requestFocus();
            return;
        }
        refer = database.getReference("Admin Announce");
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    adminAnnouce = d.getValue(AdminAnnounce.class);
                    long adminannounceno = adminAnnouce.getAdano();
                    long adminadano=adminannounceno+1;
                    AdminAnnounce adminAnnounce=new AdminAnnounce(adminadano,stringdate);
                    FirebaseDatabase.getInstance().getReference("Admin Announce")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(adminAnnounce).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
//                    Toast.makeText(getApplicationContext(),"Announcement Successful",Toast.LENGTH_SHORT).show();
//                    startActivity( new Intent(Announcements.this,Announcements.class));
//                    Announcements.this.finish();
                            }
                            else
                            {
//                    Toast.makeText(getApplicationContext(),"Announcement Unsuccessful",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AnnounceInfo announceInfo = new AnnounceInfo(announce, adminadano,stringdate,strTime,desc);
                    DatabaseReference newRef = reference.child("Announcements").push();
                    newRef.setValue(announceInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Announcement Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAnnouncement.this, AnnouncementsUpdated.class));
                                AddAnnouncement.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Announcement Unsuccessful", Toast.LENGTH_SHORT).show();
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

    private void beginUpdate(String s,String ss) {
        final String a=mAnnouncement.getText().toString();
        final String d=mDescription.getText().toString();
        mAnnouncement.setText(s);
        mDescription.setText(ss);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String strTime =mdformat.format(calendar.getTime());
        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRef=mFirebaseDatabase.getReference("Announcements");
        Query query =mRef.orderByChild("dannounce").equalTo(newAnnouncement);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ds.getRef().child("dannounce").setValue(a);
                    ds.getRef().child("extension").setValue(d);
                    ds.getRef().child("inttime").setValue(strTime);
                }
                Toast.makeText(AddAnnouncement.this,"Announcement Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddAnnouncement.this,AnnouncementsUpdated.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
