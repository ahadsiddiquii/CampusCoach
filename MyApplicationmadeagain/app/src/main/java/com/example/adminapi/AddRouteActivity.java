package com.example.adminapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class AddRouteActivity extends AppCompatActivity {

    EditText mTitle,mPno,mLocation1,mLocation2,mLocation3,mLocation4,mLocation5,mLocation6,mLocation7,mLocation8,mLocation9,mLocation10,mLocation11,mLocation12,mLocation13,mLocation14,mLocation15,mLocation16,mLocation17;
    Button mAddButton;

    String currenttTitle,dpno,mlLoc1,mlLoc2,mlLoc3,mlLoc4,mlLoc5,mlLoc6,mlLoc7,mlLoc8,mlLoc9,mlLoc10,mlLoc11,mlLoc12,mlLoc13,mlLoc14,mlLoc15,mlLoc16,mlLoc17;

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
        setContentView(R.layout.activity_add_route);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Route");


        mTitle=findViewById(R.id.pTitle);
        mPno=findViewById(R.id.pPno);
        mLocation1=findViewById(R.id.pLocation1);
        mLocation2=findViewById(R.id.pLocation2);
        mLocation3=findViewById(R.id.pLocation3);
        mLocation4=findViewById(R.id.pLocation4);
        mLocation5=findViewById(R.id.pLocation5);
        mLocation6=findViewById(R.id.pLocation6);
        mLocation7=findViewById(R.id.pLocation7);
        mLocation8=findViewById(R.id.pLocation8);
        mLocation9=findViewById(R.id.pLocation9);
        mLocation10=findViewById(R.id.pLocation10);
        mLocation11=findViewById(R.id.pLocation11);
        mLocation12=findViewById(R.id.pLocation12);
        mLocation13=findViewById(R.id.pLocation13);
        mLocation14=findViewById(R.id.pLocation14);
        mLocation15=findViewById(R.id.pLocation15);
        mLocation16=findViewById(R.id.pLocation16);
        mLocation17=findViewById(R.id.pLocation17);
        mAddButton=findViewById(R.id.pAddButton);

        Bundle intent=getIntent().getExtras();
        if (intent!=null){
            currenttTitle=intent.getString("title");
            dpno=intent.getString("dpointno");
            mlLoc1=intent.getString("Location1");
            mlLoc2=intent.getString("Location2");
            mlLoc3=intent.getString("Location3");
            mlLoc4=intent.getString("Location4");
            mlLoc5=intent.getString("Location5");
            mlLoc6=intent.getString("Location6");
            mlLoc7=intent.getString("Location7");
            mlLoc8=intent.getString("Location8");
            mlLoc9=intent.getString("Location9");
            mlLoc10=intent.getString("Location10");
            mlLoc11=intent.getString("Location11");
            mlLoc12=intent.getString("Location12");
            mlLoc13=intent.getString("Location13");
            mlLoc14=intent.getString("Location14");
            mlLoc15=intent.getString("Location15");
            mlLoc16=intent.getString("Location16");
            mlLoc17=intent.getString("Location17");

            mTitle.setText(currenttTitle);
            mPno.setText(dpno);
            mLocation1.setText(mlLoc1);
            mLocation2.setText(mlLoc2);
            mLocation3.setText(mlLoc3);
            mLocation4.setText(mlLoc4);
            mLocation5.setText(mlLoc5);
            mLocation6.setText(mlLoc6);
            mLocation7.setText(mlLoc7);
            mLocation8.setText(mlLoc8);
            mLocation9.setText(mlLoc9);
            mLocation10.setText(mlLoc10);
            mLocation11.setText(mlLoc11);
            mLocation12.setText(mlLoc12);
            mLocation13.setText(mlLoc13);
            mLocation14.setText(mlLoc14);
            mLocation15.setText(mlLoc15);
            mLocation16.setText(mlLoc16);
            mLocation17.setText(mlLoc17);
            actionBar.setTitle("Update Route");
            mAddButton.setText("Update");

        }


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddButton.getText().equals("Add Route")){
                    uploadDataToFirebase();
                    makeannouncement();

                }
                else{
                    beginUpdate(currenttTitle);
                    makeannouncement();
                }
            }
        });

//        mProgressDialog=new ProgressDialog(AddRouteActivity.this);

    }

    private void beginUpdate(String s) {

        final String t=mTitle.getText().toString();
        final String nop=mPno.getText().toString();
        final String l1=mLocation1.getText().toString();
        final String l2=mLocation2.getText().toString();
        final String l3=mLocation3.getText().toString();
        final String l4=mLocation4.getText().toString();
        final String l5=mLocation5.getText().toString();
        final String l6=mLocation6.getText().toString();
        final String l7=mLocation7.getText().toString();
        final String l8=mLocation8.getText().toString();
        final String l9=mLocation9.getText().toString();
        final String l10=mLocation10.getText().toString();
        final String l11=mLocation11.getText().toString();
        final String l12=mLocation12.getText().toString();
        final String l13=mLocation13.getText().toString();
        final String l14=mLocation14.getText().toString();
        final String l15=mLocation15.getText().toString();
        final String l16=mLocation16.getText().toString();
        final String l17=mLocation17.getText().toString();


        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mRef=mFirebaseDatabase.getReference("Routes");
        Query query =mRef.orderByChild("title").equalTo(currenttTitle);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ds.getRef().child("title").setValue(t);
                    ds.getRef().child("search").setValue(t.toLowerCase());
                    ds.getRef().child("dpointno").setValue(nop);
                    ds.getRef().child("location1").setValue(l1);
                    ds.getRef().child("location2").setValue(l2);
                    ds.getRef().child("location3").setValue(l3);
                    ds.getRef().child("location4").setValue(l4);
                    ds.getRef().child("location5").setValue(l5);
                    ds.getRef().child("location6").setValue(l6);
                    ds.getRef().child("location7").setValue(l7);
                    ds.getRef().child("location8").setValue(l8);
                    ds.getRef().child("location9").setValue(l9);
                    ds.getRef().child("location10").setValue(l10);
                    ds.getRef().child("location11").setValue(l11);
                    ds.getRef().child("location12").setValue(l12);
                    ds.getRef().child("location13").setValue(l13);
                    ds.getRef().child("location14").setValue(l14);
                    ds.getRef().child("location15").setValue(l15);
                    ds.getRef().child("location16").setValue(l16);
                    ds.getRef().child("location17").setValue(l17);

                }
                Toast.makeText(AddRouteActivity.this,"Route Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddRouteActivity.this,retrieveroutes.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void makeannouncement(){

        Date date=new Date();
        Date newDate=new Date(date.getTime());
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate=dt.format(newDate);
        final String RouteTitle = mTitle.getText().toString().trim();
        refer = database.getReference("Admin Announce");
        refer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    adminAnnouce =d.getValue(AdminAnnounce.class);
                    long adminannounceno = adminAnnouce.getAdano();
                    long adminadano= adminannounceno+1;
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
                    String title="Routes Changes";
                    String description=RouteTitle + " (Added / Updated)";
                    AnnounceInfo announceInfo = new AnnounceInfo(title, adminadano,stringdate,strTime,description);
                    DatabaseReference newRef = reference.child("Announcements").push();
                    newRef.setValue(announceInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(getApplicationContext(), "Announcement Successful", Toast.LENGTH_SHORT).show();

                            } else {
//                                Toast.makeText(getApplicationContext(), "Announcement Unsuccessful", Toast.LENGTH_SHORT).show();
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





    private void uploadDataToFirebase(){


        String mRouteTitle = mTitle.getText().toString().trim();
        String mSearch=mRouteTitle.toLowerCase();
        String mPointNo = mPno.getText().toString().trim();
        String mLoc1 = mLocation1.getText().toString().trim();
        String mLoc2 = mLocation2.getText().toString().trim();
        String mLoc3 = mLocation3.getText().toString().trim();
        String mLoc4 = mLocation4.getText().toString().trim();
        String mLoc5 = mLocation5.getText().toString().trim();
        String mLoc6 = mLocation6.getText().toString().trim();
        String mLoc7 = mLocation7.getText().toString().trim();
        String mLoc8 = mLocation8.getText().toString().trim();
        String mLoc9 = mLocation9.getText().toString().trim();
        String mLoc10 = mLocation10.getText().toString().trim();
        String mLoc11 = mLocation11.getText().toString().trim();
        String mLoc12 = mLocation12.getText().toString().trim();
        String mLoc13 = mLocation13.getText().toString().trim();
        String mLoc14 = mLocation14.getText().toString().trim();
        String mLoc15 = mLocation15.getText().toString().trim();
        String mLoc16 = mLocation16.getText().toString().trim();
        String mLoc17 = mLocation17.getText().toString().trim();
        if(mRouteTitle.isEmpty())
        {
            mTitle.setError("Route Number required");
            mTitle.requestFocus();
            return;
        }
        if(mPointNo.isEmpty())
        {
            mPno.setError("Point Number required");
            mPno.requestFocus();
            return;
        }

        if(mLoc1.isEmpty())
        {
            mLocation1.setError("At Least 1 Location Required required");
            mLocation1.requestFocus();
            return;
        }
        if(mLoc2.isEmpty())
        {
            mLoc2=" ";

        }
        if(mLoc3.isEmpty())
        {
            mLoc3=" ";

        }
        if(mLoc4.isEmpty())
        {
            mLoc4=" ";

        }
        if(mLoc5.isEmpty())
        {
            mLoc5=" ";

        }
        if(mLoc6.isEmpty())
        {
            mLoc6=" ";

        }
        if(mLoc7.isEmpty())
        {
            mLoc7=" ";

        }
        if(mLoc8.isEmpty())
        {
            mLoc8=" ";

        }
        if(mLoc9.isEmpty())
        {
            mLoc9=" ";

        }
        if(mLoc9.isEmpty())
        {
            mLoc10=" ";

        }
        if(mLoc3.isEmpty())
        {
            mLoc10=" ";

        }
        if(mLoc11.isEmpty())
        {
            mLoc11=" ";

        }
        if(mLoc12.isEmpty())
        {
            mLoc12=" ";

        }
        if(mLoc13.isEmpty())
        {
            mLoc13=" ";

        }
        if(mLoc14.isEmpty())
        {
            mLoc14=" ";

        }
        if(mLoc15.isEmpty())
        {
            mLoc15=" ";

        }
        if(mLoc16.isEmpty())
        {
            mLoc16=" ";

        }
        if(mLoc17.isEmpty())
        {
            mLoc17=" ";

        }
        Locations locate=new Locations(mRouteTitle,mSearch,mPointNo,mLoc1,mLoc2,mLoc3,mLoc4,mLoc5,mLoc6,mLoc7,mLoc8,mLoc9,mLoc10,mLoc11,mLoc12,mLoc13,mLoc14,mLoc15,mLoc16,mLoc17);

        DatabaseReference newRef = ref.child("Routes").push();
        newRef.setValue(locate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Route Successfully Added",Toast.LENGTH_SHORT).show();
                            startActivity( new Intent(AddRouteActivity.this,AddRouteActivity.class));
                            AddRouteActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Route Add Unsuccessful",Toast.LENGTH_SHORT).show();
                        }
            }
        });

//        FirebaseDatabase.getInstance().getReference("Routes")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(locate).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful())
//                        {
//                            Toast.makeText(getApplicationContext(),"Route Successfully Added",Toast.LENGTH_SHORT).show();
//                            startActivity( new Intent(AddRouteActivity.this,AddRouteActivity.class));
//                            AddRouteActivity.this.finish();
//                        }
//                        else
//                        {
//                            Toast.makeText(getApplicationContext(),"Route Add Unsuccessful",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

}
