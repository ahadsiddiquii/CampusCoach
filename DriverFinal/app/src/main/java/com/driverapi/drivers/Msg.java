package com.driverapi.drivers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.driverapi.drivers.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Msg extends AppCompatActivity {
    Button send;
    TextView text;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("New Student Register");
    DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("Driver Details");

    final int SEND_SMS_PERMISSION_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        send=(Button)findViewById(R.id.send);
        text=(TextView)findViewById(R.id
        .text) ;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String contact = ds.child("contact").getValue(String.class);
//                            msg code

                            SmsManager smgr = SmsManager.getDefault();
                if (checkpermission(Manifest.permission.SEND_SMS)) {
                                smgr.sendTextMessage(contact, "FRD", "An Announcement has been added,kindly check it.", null, null);
                            } else {
                                ActivityCompat.requestPermissions(Msg.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                            }

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
             }
               };
                rootRef.addListenerForSingleValueEvent(eventListener);
                ValueEventListener eventListeners = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String contact = ds.child("dcontact").getValue(String.class);
//                            msg code

                            SmsManager smgr = SmsManager.getDefault();
                            if (checkpermission(Manifest.permission.SEND_SMS)) {
                                smgr.sendTextMessage(contact, "FRD", "An Announcement has been added,kindly check it.", null, null);
                            } else {
                                ActivityCompat.requestPermissions(Msg.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                            }

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                Ref.addListenerForSingleValueEvent(eventListeners);


            }


        });

    }
    public boolean checkpermission(String pemission)
    {
        int check= ContextCompat.checkSelfPermission(this,pemission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }
}
