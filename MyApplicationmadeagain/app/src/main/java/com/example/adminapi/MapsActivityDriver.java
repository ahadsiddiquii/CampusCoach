package com.example.adminapi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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

import static android.telephony.CellLocation.requestLocationUpdate;

public class MapsActivityDriver extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    Location nLastLocation;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_UPDATE_TIME = 1000;
    private final long MIN_UPDATES_DISTANCE = 5;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_driver);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //permission is granted
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{//permission denied
                    Toast.makeText(this,"Permission Denied!",Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
    }

    protected synchronized void buildGoogleApiClient(){
        client= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        final int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.driver);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        if(currentLocationMarker==null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Location Test");
            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapm : dataSnapshot.getChildren()) {
                        CoordinatesModel coordinatesModel = new CoordinatesModel();
                        coordinatesModel.setDpointno(snapm.getValue(CoordinatesModel.class).getDpointno());
                        coordinatesModel.setDriverlatt(snapm.getValue(CoordinatesModel.class).getDriverlatt());
                        coordinatesModel.setDriverlong(snapm.getValue(CoordinatesModel.class).getDriverlong());
                        String namel=coordinatesModel.getDpointno();
                        Double latitude1 = coordinatesModel.getDriverlatt();
                        Double longitude1 = coordinatesModel.getDriverlong();
                        LatLng latLng = new LatLng(latitude1, longitude1);
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Point: "+ namel).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }
//        else{
////            currentLocationMarker.setPosition(latLng);
//        }
//        if (marker == null) {
//            MarkerOptions options = new MarkerOptions().position(latLong)
//                    .title("Marker Title");
//            marker = mMap.addMarker(options);
//        }
//        else {
//            marker.setPosition(latLong);
//        }

//        public void animateMarker(final Marker marker, final LatLng toPosition,
//        final boolean hideMarker) {
//            final Handler handler = new Handler();
//            final long start = SystemClock.uptimeMillis();
//            Projection proj = mGoogleMapObject.getProjection();
//            Point startPoint = proj.toScreenLocation(marker.getPosition());
//            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//            final long duration = 500;
//
//            final Interpolator interpolator = new LinearInterpolator();
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    long elapsed = SystemClock.uptimeMillis() - start;
//                    float t = interpolator.getInterpolation((float) elapsed
//                            / duration);
//                    double lng = t * toPosition.longitude + (1 - t)
//                            * startLatLng.longitude;
//                    double lat = t * toPosition.latitude + (1 - t)
//                            * startLatLng.latitude;
//                    marker.setPosition(new LatLng(lat, lng));
//
//                    if (t < 1.0) {
//                        // Post again 16ms later.
//                        handler.postDelayed(this, 16);
//                    } else {
//                        if (hideMarker) {
//                            marker.setVisible(false);
//                        } else {
//                            marker.setVisible(true);
//                        }
//                    }
//                }
//            });
//        }
//        map.addMarker(new MarkerOptions()
//                .position(POSITION)
//                .title("Your title")
//                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
//        );

        if(client!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
//        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("StudentsLocation");
//        GeoFire geoFire=new GeoFire(ref);
//        geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));
//        final double latt=location.getLatitude();
//        final double lngg=location.getLongitude();
//        CoordinatesModel cordinatesModel=new CoordinatesModel(latt,lngg);
//        FirebaseDatabase.getInstance().getReference("Location Test")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(cordinatesModel);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return false;
        }
        else
            return true;
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        final int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.driver);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        if (currentLocationMarker == null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Location Test");
            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapm : dataSnapshot.getChildren()) {
                        CoordinatesModel coordinatesModel = new CoordinatesModel();
                        coordinatesModel.setDpointno(snapm.getValue(CoordinatesModel.class).getDpointno());
                        coordinatesModel.setDriverlatt(snapm.getValue(CoordinatesModel.class).getDriverlatt());
                        coordinatesModel.setDriverlong(snapm.getValue(CoordinatesModel.class).getDriverlong());
                        String namel=coordinatesModel.getDpointno();
                        Double latitude1 = coordinatesModel.getDriverlatt();
                        Double longitude1 = coordinatesModel.getDriverlong();
                        LatLng latLng = new LatLng(latitude1, longitude1);
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Point: "+ namel).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
//        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("StudentsLocation");
//
//        GeoFire geoFire=new GeoFire(ref);
//        geoFire.removeLocation(userId);
    }
};