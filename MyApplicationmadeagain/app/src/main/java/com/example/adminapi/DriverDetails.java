package com.example.adminapi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.ArrayList;

public class DriverDetails extends AppCompatActivity {
    ListView listViewDrivers;
    private Button Reg;
    private Button del;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    DriverInfo driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Driver's Details");
        driver=new DriverInfo();
        Reg = (Button)findViewById(R.id.RegisterNewDriver);
        del = (Button)findViewById(R.id.DeleteDriver);
        listViewDrivers = (ListView)findViewById(R.id.listViewDrivers);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Driver Details");
        list = new ArrayList<>();
        adapter= new ArrayAdapter<String>(this,R.layout.driverinfo,R.id.driver_Info,list);
        ref.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    driver=ds.getValue(DriverInfo.class);
                    list.add("  Driver's Name:  "+driver.getDname().toString()+System.lineSeparator()+"  Point Number:  "+driver.getDpointno().toString()+System.lineSeparator()+"  CNIC:  "+driver.getDcnic().toString()+System.lineSeparator()+"  Contact:  "+driver.getDcontact().toString()+ System.lineSeparator()+"  Email:  "+driver.getDemail().toString()+System.lineSeparator()+"  Joined Date:  "+driver.getDjoindate().toString()+System.lineSeparator());
                }
                listViewDrivers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDriverReg();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDriverdel();
            }
        });
    }
    public void openDriverReg(){
        Intent intent = new Intent(this, DriverRegistration.class);
        startActivity(intent);
        finish();
    }
    public void openDriverdel(){
        Intent intent = new Intent(this, DeleteDriver.class);
        startActivity(intent);
        finish();
    }
}
