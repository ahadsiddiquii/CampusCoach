package com.example.adminapi;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Announcements extends AppCompatActivity {
    private long backPressedTime;
    private EditText eannounce;
    private EditText eText;
    private Button AnnounceBtn;
    private Button MessageBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    ListView listViewAnnouncements;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    AnnounceInfo announce;
    AdminAnnounce adminAnnouce;
    DatabaseReference ref, refer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    android.os.Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Announcements");
        eannounce = (EditText) findViewById(R.id.announce);
        AnnounceBtn = (Button) findViewById(R.id.AnnounceBtn);
        AnnounceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        MessageBtn = (Button) findViewById(R.id.MessageBtn);
        MessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open message activity
                openMessage();
            }
        });
        FirebaseApp.initializeApp(this);
//        mAuth = FirebaseAuth.getInstance();
        announce = new AnnounceInfo();
        listViewAnnouncements = (ListView) findViewById(R.id.listViewAnnouncements);
        database = FirebaseDatabase.getInstance();
//
//        this.mHandler= new Handler();
//        m_Runnable.run();
check();
remove();
display();


    }
//
//    private final Runnable m_Runnable = new Runnable() {
//        @Override
//        public void run() {
//            check();
//            remove();
//            display();
//            Announcements.this.mHandler.postDelayed(m_Runnable,3000);
//        }
//
//    };
    public void display(){
        Date date=new Date();
        Date newDate=new Date(date.getTime());
//        +(604800000L*2)+(24*60*60)
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String todaysdate=dt.format(newDate);
        ref = database.getReference("Announcements");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.announceinfo, R.id.announce_Info, list);
                        Query mQuery=ref.orderByChild("intdate").equalTo(todaysdate);
                        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i=1;
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
//                                    ds.getRef().removeValue();
                                    announce = ds.getValue(AnnounceInfo.class);
                                    list.add(i + "-> " + announce.getDannounce().toString() + System.lineSeparator());
                                    i++;
                                }
                                listViewAnnouncements.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
    }

    public void check() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
//        + (604800000L * 2) + (24 * 60 * 60)
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        ref = database.getReference("Admin Announce");
        Query mQuery=ref.orderByChild("intdate").equalTo(stringdate);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        break;
                    }
                }
                else{
                    AdminAnnounce adminAnnounce=new AdminAnnounce(0,stringdate);
                    FirebaseDatabase.getInstance().getReference("Admin Announce")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(adminAnnounce).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(),"Announcement Successful",Toast.LENGTH_SHORT).show();
//                    startActivity( new Intent(Announcements.this,Announcements.class));
//                    Announcements.this.finish();
                            } else {
//                    Toast.makeText(getApplicationContext(),"Announcement Unsuccessful",Toast.LENGTH_SHORT).show();
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

    public void register() {

        Date date=new Date();
        Date newDate=new Date(date.getTime());
//        +(604800000L*2)+(24*60*60)
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate=dt.format(newDate);

        final String announce = eannounce.getText().toString().trim();
        if (announce.isEmpty()) {
            eannounce.setError("Announcement Empty");
            eannounce.requestFocus();
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
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                    final String strTime =mdformat.format(calendar.getTime());
                    String desc="...";
                    AnnounceInfo announceInfo = new AnnounceInfo(announce, adminannounceno,stringdate,strTime,desc);
                    DatabaseReference newRef = reference.child("Announcements").push();
                    newRef.setValue(announceInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Announcement Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Announcements.this, Announcements.class));
                                Announcements.this.finish();
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

    public void remove(){
        Date date = new Date();
        Date newDate = new Date(date.getTime() );
//        + (604800000L * 2) + (24 * 60 * 60)
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        ref = database.getReference("Admin Announce");
        Query mQuery=ref.orderByChild("adano").equalTo(0);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    refer = database.getReference("Announcements");
                    refer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                d.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void openMessage(){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}









