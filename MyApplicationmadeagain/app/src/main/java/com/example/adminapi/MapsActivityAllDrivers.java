package com.example.adminapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.logging.LogRecord;

public class MapsActivityAllDrivers extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    android.os.Handler mHandler;
    int i=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_all_drivers);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ChildEventListener mChildEventListener;
        mUsers = FirebaseDatabase.getInstance().getReference("Location Drivers");
        mUsers.push().setValue(marker);


    }



    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            final int height = 100;
            int width = 100;
            final int[] j = {0};
            mMap.clear();
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.driver);
            Bitmap b = bitmapdraw.getBitmap();
            final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CoordinatesModel coordinatesModel = new CoordinatesModel();
                        coordinatesModel.setDpointno(s.getValue(CoordinatesModel.class).getDpointno());
                        coordinatesModel.setDriverlatt(s.getValue(CoordinatesModel.class).getDriverlatt());
                        coordinatesModel.setDriverlong(s.getValue(CoordinatesModel.class).getDriverlong());
                        String namel = coordinatesModel.getDpointno();
                        Double latitude1 = coordinatesModel.getDriverlatt();
                        Double longitude1 = coordinatesModel.getDriverlong();
                        LatLng latLng = new LatLng(latitude1, longitude1);
//                        marker=
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Point: " + namel).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//                        marker.showInfoWindow();
//marker.showInfoWindow();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            MapsActivityAllDrivers.this.mHandler.postDelayed(m_Runnable, 3000);

        }
    };


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.isInfoWindowShown()){
            marker.hideInfoWindow();
        }
        else{
            marker.showInfoWindow();
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.driver);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s:dataSnapshot.getChildren()){
                    CoordinatesModel coordinatesModel = new CoordinatesModel();
                    coordinatesModel.setDpointno(s.getValue(CoordinatesModel.class).getDpointno());
                    coordinatesModel.setDriverlatt(s.getValue(CoordinatesModel.class).getDriverlatt());
                    coordinatesModel.setDriverlong(s.getValue(CoordinatesModel.class).getDriverlong());
                    String namel=coordinatesModel.getDpointno();
                    Double latitude1 = coordinatesModel.getDriverlatt();
                    Double longitude1 = coordinatesModel.getDriverlong();
                    LatLng latLng = new LatLng(latitude1, longitude1);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Point: "+ namel).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//                    MarkerOptions TP=new MarkerOptions()
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                    marker.showInfoWindow();
                    mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        i=0;
        this.mHandler= new Handler();
        m_Runnable.run();
    }


    @Override
    protected void onStop() {
        mHandler.removeCallbacks(m_Runnable);
        super.onStop();
//        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("StudentsLocation");
//
//        GeoFire geoFire=new GeoFire(ref);
//        geoFire.removeLocation(userId);
    }
};