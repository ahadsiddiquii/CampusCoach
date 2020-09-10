package com.example.adminapi;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentDetailsNew extends AppCompatActivity {

    ListView listViewStudents;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    StudentInfo student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_new);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Student's Details");


        student=new StudentInfo();
        listViewStudents = (ListView)findViewById(R.id.listViewStudents);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("New Student Register");
        list = new ArrayList<>();
        adapter= new ArrayAdapter<String>(this,R.layout.studentinfo,R.id.student_Info,list);
        ref.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    student=ds.getValue(StudentInfo.class);
                    list.add("  Student's Name:  "+student.getStudentName().toString()+System.lineSeparator()+"  Student's ID:  "+student.getStudentID().toString()+System.lineSeparator()+"  Point Number:  "+student.getPointnumber().toString()+System.lineSeparator()+"  CNIC:  "+student.getGaurdianCNIC().toString()+System.lineSeparator()+"  Email:  "+student.getEmail().toString()+System.lineSeparator()+"  Contact:  "+student.getContact().toString()+System.lineSeparator());
                }
                listViewStudents.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

}
