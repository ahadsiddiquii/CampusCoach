package com.userapi.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ParentsMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref,refer,reference;
    FirebaseUser user;
    android.os.Handler mHandler;
    Parents parents;
    CoordinatesModel coordinatesModel;
    String contact,studentpointnumber;
    int i=0;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    StudentInfo student,s;
    String sid,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Your Child's Location");
        mUsers = FirebaseDatabase.getInstance().getReference("New Student Register");mUsers.push().setValue(marker);
//        contact =getIntent().getStringExtra("contact");
//        String c =getIntent().getStringExtra("contact");
//            Toast.makeText(ParentsMapsActivity.this, c, Toast.LENGTH_SHORT).show();
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        final String id = user.getUid();
//        Query mquery = FirebaseDatabase.getInstance().getReference("Parents")
//                .orderByChild("contact")
//                .equalTo(c);
//        mquery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for(DataSnapshot ds: dataSnapshot.getChildren())
//                    {
//                        parents=ds.getValue(Parents.class);
//                        sid=parents.getId();
////                        Toast.makeText(ParentsMapsActivity.this, sid, Toast.LENGTH_SHORT).show();
//
//                        if(sid.equals(id)){
////                            Toast.makeText(ParentsMapsActivity.this, id+" == "+sid, Toast.LENGTH_SHORT).show();
//                        }
//                        else{
////                            Toast.makeText(ParentsMapsActivity.this, id+ "  Not equal  "+sid, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//                else{
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {

            final int height = 100;
            int width = 100;
//
//            firebaseAuth = FirebaseAuth.getInstance();
//            firebaseDatabase = FirebaseDatabase.getInstance();
//            user = firebaseAuth.getCurrentUser();
//            String id = user.getUid();

            String c =getIntent().getStringExtra("contact");
//            Toast.makeText(ParentsMapsActivity.this, c, Toast.LENGTH_SHORT).show();
//            Query mquery = FirebaseDatabase.getInstance().getReference("Parents")
//                    .orderByChild("contact")
//                    .equalTo(c);
//            mquery.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        for(DataSnapshot ds: dataSnapshot.getChildren())
//                        {
//                            parents=ds.getValue(Parents.class);
//                            id=parents.getId();
////                            Toast.makeText(ParentsMapsActivity.this, id, Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                    else{
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.driver);
            Bitmap b = bitmapdraw.getBitmap();
            final Bitmap smallsMarker = Bitmap.createScaledBitmap(b, width, height, false);
//            if (id == null) {
//
//            }
//            else {
//                DatabaseReference databaseReference = firebaseDatabase.getReference("New Student Register");
                Query bquery = FirebaseDatabase.getInstance().getReference("New Student Register")
                        .orderByChild("contact")
                        .equalTo(c);
                bquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                s = ds.getValue(StudentInfo.class);
                                studentpointnumber = s.getPointnumber();
                                sid=studentpointnumber;
                            }
                        } else {

                        }
                        if (studentpointnumber == null) {

                        } else {
//                        studentpointnumber = dataSnapshot.child("pointnumber").getValue().toString();
                            Query query = FirebaseDatabase.getInstance().getReference("Location Drivers")
                                    .orderByChild("dpointno")
                                    .equalTo(sid);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            CoordinatesModel coordinatesModel = new CoordinatesModel();
                                            coordinatesModel.setDpointno(postSnapshot.getValue(CoordinatesModel.class).getDpointno());
                                            coordinatesModel.setDriverlatt(postSnapshot.getValue(CoordinatesModel.class).getDriverlatt());
                                            coordinatesModel.setDriverlong(postSnapshot.getValue(CoordinatesModel.class).getDriverlong());
                                            String namel = coordinatesModel.getDpointno();
                                            Double latitude1 = coordinatesModel.getDriverlatt();
                                            Double longitude1 = coordinatesModel.getDriverlong();
                                            LatLng latLng = new LatLng(latitude1, longitude1);
                                            mMap.addMarker(new MarkerOptions().position(latLng).title("Point: " + namel).icon(BitmapDescriptorFactory.fromBitmap(smallsMarker)));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                                    addnotifs();
                                            if (i == 0) {
                                                mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
                                                i++;
                                            }


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(Home.this, "ERROR RETRIEVING DATA", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        }

                        @Override
                        public void onCancelled (@NonNull DatabaseError databaseError){

                        }


                });

//            ParentsMapsActivity.this.mHandler.removeCallbacks(m_Runnable);
            ParentsMapsActivity.this.mHandler.postDelayed(m_Runnable, 3000);
            mMap.clear();

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final int height = 100;
        int width = 100;
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        String id = user.getUid();
        String c =getIntent().getStringExtra("contact");
//        Toast.makeText(ParentsMapsActivity.this, c, Toast.LENGTH_SHORT).show();
//        Query mquery = FirebaseDatabase.getInstance().getReference("Parents")
//                .orderByChild("contact")
//                .equalTo(c);
//        mquery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for(DataSnapshot ds: dataSnapshot.getChildren())
//                    {
//                        parents=ds.getValue(Parents.class);
//                        id=parents.getGaurdianCNIC();
//
//                    }
//
//                }
//                else{
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.driver);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallsMarker = Bitmap.createScaledBitmap(b, width, height, false);

        googleMap.setOnMarkerClickListener(ParentsMapsActivity.this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//if(id==null){}
//else {

//    databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
    Query bquery = FirebaseDatabase.getInstance().getReference("New Student Register")
            .orderByChild("contact")
            .equalTo(c);
    bquery.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                s = ds.getValue(StudentInfo.class);
                studentpointnumber = s.getPointnumber();
sid=studentpointnumber;
            }
        } else {

        }
        if (sid == null) {

        } else {
//            studentpointnumber = dataSnapshot.child("pointnumber").getValue().toString();
            Query query = FirebaseDatabase.getInstance().getReference("Location Drivers")
                    .orderByChild("dpointno")
                    .equalTo(sid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            CoordinatesModel coordinatesModel = new CoordinatesModel();
                            coordinatesModel.setDpointno(postSnapshot.getValue(CoordinatesModel.class).getDpointno());
                            coordinatesModel.setDriverlatt(postSnapshot.getValue(CoordinatesModel.class).getDriverlatt());
                            coordinatesModel.setDriverlong(postSnapshot.getValue(CoordinatesModel.class).getDriverlong());
                            String namel = coordinatesModel.getDpointno();
                            Double latitude1 = coordinatesModel.getDriverlatt();
                            Double longitude1 = coordinatesModel.getDriverlong();
                            LatLng latLng = new LatLng(latitude1, longitude1);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Point: " + namel).icon(BitmapDescriptorFactory.fromBitmap(smallsMarker)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
//                                    addnotifs();


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(Home.this, "ERROR RETRIEVING DATA", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });

        this.mHandler = new Handler();
        m_Runnable.run();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map,menu);
//        MenuItem item=menu.findItem(R.id.action_search);
//        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                firebaseSearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                firebaseSearch(newText);
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_announce){
            Intent intent = new Intent(ParentsMapsActivity.this, AnnouncementsUpdated.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.action_logout){
            mHandler.removeCallbacks(m_Runnable);
            finish();
            openlogout();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
//System.exit(0);
        super.onBackPressed();

    }
    public void openlogout(){

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, ParentsLogin.class);
//        Toast.makeText(ParentsMapsActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }


}