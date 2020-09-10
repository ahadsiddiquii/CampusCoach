package com.driverapi.drivers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.driverapi.drivers.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Announcements extends AppCompatActivity {
    ListView listViewAnnouncements;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    AnnouncesInfo announce;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        setTitle("اعلان");
        announce=new AnnouncesInfo();
        listViewAnnouncements = (ListView)findViewById(R.id.listViewAnnouncements);
        database=FirebaseDatabase.getInstance();
        Date date=new Date();
        Date newDate=new Date(date.getTime()+(604800000L*2)+(24*60*60));
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String todaysdate=dt.format(newDate);
        ref=database.getReference("Announcements");
        final SwipeRefreshLayout pullToRefreshAnnounce=findViewById(R.id.pullToRefreshAnnounce);
        pullToRefreshAnnounce.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshdData();
                pullToRefreshAnnounce.setRefreshing(false);
            }
        });
        list = new ArrayList<>();
        adapter= new ArrayAdapter<String>(this,R.layout.activity_announceinfo,R.id.announce_Info,list);
        Query mQuery=ref.orderByChild("intdate").equalTo(todaysdate);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    announce = ds.getValue(AnnouncesInfo.class);
                    list.add(i + "->" + announce.getDannounce().toString() + System.lineSeparator());
                    i++;
                }
                listViewAnnouncements.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void refreshdData(){
        Date date=new Date();
        Date newDate=new Date(date.getTime()+(604800000L*2)+(24*60*60));
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String todaysdate=dt.format(newDate);
        Query mQuery=ref.orderByChild("intdate").equalTo(todaysdate);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    announce = ds.getValue(AnnouncesInfo.class);
                    list.add(i + "->" + announce.getDannounce().toString() + System.lineSeparator());
                    i++;
                }
                listViewAnnouncements.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
