package com.user.studentapi;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class retrieverouteyourdriver extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ListView listViewMyroute;
    FirebaseDatabase database;
    DatabaseReference reference,ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Locations locate;
    TextView title, dpointno,Location1,Location2,Location3,Location4,Location5,Location6,Location7,Location8,Location9,Location10,Location11,Location12,Location13,Location14,Location15,Location16,Location17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieverouteyourdriver);
       getSupportActionBar().hide();
        title = (TextView) findViewById(R.id.pTitle);
        dpointno = (TextView) findViewById(R.id.pPno);
        Location1 = (TextView) findViewById(R.id.pLocation1);
        Location2 = (TextView) findViewById(R.id.pLocation2);
        Location3 = (TextView) findViewById(R.id.pLocation3);
        Location4 = (TextView) findViewById(R.id.pLocation4);
        Location5 = (TextView) findViewById(R.id.pLocation5);
        Location6 = (TextView) findViewById(R.id.pLocation6);
        Location7 = (TextView) findViewById(R.id.pLocation7);
        Location8 = (TextView) findViewById(R.id.pLocation8);
        Location9 = (TextView) findViewById(R.id.pLocation9);
        Location10 = (TextView) findViewById(R.id.pLocation10);
        Location11 = (TextView) findViewById(R.id.pLocation11);
        Location12 = (TextView) findViewById(R.id.pLocation12);
        Location13 = (TextView) findViewById(R.id.pLocation13);
        Location14 = (TextView) findViewById(R.id.pLocation14);
        Location15 = (TextView) findViewById(R.id.pLocation15);
        Location16 = (TextView) findViewById(R.id.pLocation16);
        Location17 = (TextView) findViewById(R.id.pLocation17);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        String id = user.getUid();
        locate=new Locations();
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Routes");
        list = new ArrayList<>();
        adapter= new ArrayAdapter<String>(this,R.layout.routeinfo,R.id.route_Info,list);
        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference("New Student Register");
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String studentpointnumber = dataSnapshot.child("pointnumber").getValue().toString();
                Query query = FirebaseDatabase.getInstance().getReference("Routes")
                        .orderByChild("dpointno")
                        .equalTo(studentpointnumber);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                locate=postSnapshot.getValue(Locations.class);
//                                title.setText(locate.getTitle());
//                                dpointno.setText("Point Number: " + locate.getDpointno());
//                                Location1.setText(locate.getLocation1());
//                                Location2.setText(locate.getLocation2());
//                                Location3.setText(locate.getLocation3());
//                                Location4.setText(locate.getLocation4());
//                                Location5.setText(locate.getLocation5());
//                                Location6.setText(locate.getLocation6());
//                                Location7.setText(locate.getLocation7());
//                                Location8.setText(locate.getLocation8());
//                                Location9.setText(locate.getLocation9());
//                                Location10.setText(locate.getLocation10());
//                                Location11.setText(locate.getLocation11());
//                                Location12.setText(locate.getLocation12());
//                                Location13.setText(locate.getLocation13());
//                                Location14.setText(locate.getLocation14());
//                                Location15.setText(locate.getLocation15());
//                                Location16.setText(locate.getLocation16());
//                                Location17.setText(locate.getLocation17());
                                Intent intent =new Intent(retrieverouteyourdriver.this,OpenRoutes.class);
                                intent.putExtra("title",locate.getTitle());
                                intent.putExtra("dpointno","Point Number: " + locate.getDpointno());
                                intent.putExtra("Location1",locate.getLocation1());
                                intent.putExtra("Location2",locate.getLocation2());
                                intent.putExtra("Location3",locate.getLocation3());
                                intent.putExtra("Location4",locate.getLocation4());
                                intent.putExtra("Location5",locate.getLocation5());
                                intent.putExtra("Location6",locate.getLocation6());
                                intent.putExtra("Location7",locate.getLocation7());
                                intent.putExtra("Location8",locate.getLocation8());
                                intent.putExtra("Location9",locate.getLocation9());
                                intent.putExtra("Location10",locate.getLocation10());
                                intent.putExtra("Location11",locate.getLocation11());
                                intent.putExtra("Location12",locate.getLocation12());
                                intent.putExtra("Location13",locate.getLocation13());
                                intent.putExtra("Location14",locate.getLocation14());
                                intent.putExtra("Location15",locate.getLocation15());
                                intent.putExtra("Location16",locate.getLocation16());
                                intent.putExtra("Location17",locate.getLocation17());
                                startActivity(intent);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(retrieverouteyourdriver.this, "ERROR RETRIEVING DATA", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
