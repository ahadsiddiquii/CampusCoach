package com.example.adminapi;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteDriver extends AppCompatActivity {
    ListView listViewDriversDelete;
    private EditText PointNo;
    private Button del;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    DriverInfo driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_driver);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Delete Driver");
        del = (Button) findViewById(R.id.DeleteDriverAct);
        PointNo = (EditText) findViewById(R.id.NumberEnter);
            Display();
            deletedriver();
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedriver();
            }
        });

        }

        public void deletedriver(){
            final String pointno = PointNo.getText().toString().trim();
            if (pointno.isEmpty()) {
                PointNo.setError("No Point number entered...");
                PointNo.requestFocus();
                return;
            }
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("Driver Details");

            Query mQuery=ref.orderByChild("dpointno").equalTo(pointno);
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            driver = ds.getValue(DriverInfo.class);
                            ds.getRef().removeValue();
                            Toast.makeText(DeleteDriver.this, "Driver deleted successfully", Toast.LENGTH_SHORT).show();


                        }
                    }
                    else{
                        Toast.makeText(DeleteDriver.this, "Enter a valid point number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }


        public void Display(){
            driver = new DriverInfo();

            listViewDriversDelete = (ListView) findViewById(R.id.listViewDriversDelete);
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("Driver Details");
            list = new ArrayList<>();
            adapter = new ArrayAdapter<String>(this, R.layout.driverinfo, R.id.driver_Info, list);
            ref.addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        driver = ds.getValue(DriverInfo.class);
                        list.add("  Point Number:  " + driver.getDpointno().toString() + System.lineSeparator());
                    }
                    listViewDriversDelete.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

