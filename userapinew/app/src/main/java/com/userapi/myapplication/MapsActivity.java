package com.userapi.myapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.userapi.myapplication.CoordinatesModel;
import com.userapi.myapplication.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

  private final String CHANNEL_ID="personal_notifications";
  private final int NOTIFICATION_ID= 001;
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

  FirebaseAuth firebaseAuth;
  FirebaseDatabase firebaseDatabase;
  FirebaseUser user;
  Handler mHandler;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
      checkLocationPermission();
    }

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

  }

  private final Runnable m_Runnable = new Runnable() {

    @Override
    public void run() {
        mMap.clear();

      int height = 100;
      int width = 100;
      BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.user);
      Bitmap b=bitmapdraw.getBitmap();
      final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

      BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.mipmap.driver);
      Bitmap bs=bitmapdraws.getBitmap();
      final Bitmap smallsMarker = Bitmap.createScaledBitmap(bs, width, height, false);
      Location locations =lastLocation;
      lastLocation=locations;
      firebaseAuth = FirebaseAuth.getInstance();
      firebaseDatabase = FirebaseDatabase.getInstance();
      user = firebaseAuth.getCurrentUser();
      String id = user.getUid();
      LatLng latLng = new LatLng(locations.getLatitude(),locations.getLongitude());

      final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
      DatabaseReference ref= FirebaseDatabase.getInstance().getReference("StudentsLocation");
      DatabaseReference reference= FirebaseDatabase.getInstance().getReference("New Student Register");
      GeoFire geoFire=new GeoFire(ref);
      geoFire.setLocation(userId,new GeoLocation(locations.getLatitude(),locations.getLongitude()));
      final double latt=locations.getLatitude();
      final double lngg=locations.getLongitude();
      final Location loc1= new Location("");
      loc1.setLatitude(latt);
      loc1.setLongitude(lngg);
      reference.addValueEventListener(new ValueEventListener() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String uname = dataSnapshot.child(userId).child("studentName").getValue(String.class);
            CoordinatesModel cordinatesModel=new CoordinatesModel(uname,latt,lngg);
            FirebaseDatabase.getInstance().getReference("Location Test")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(cordinatesModel);
            LatLng latLng20=new LatLng(latt,lngg);
              mMap.addMarker(new MarkerOptions().position(latLng20).title("Your Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });

      DatabaseReference databaseReference = firebaseDatabase.getReference("New Student Register");
      databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          String studentpointnumber = dataSnapshot.child("pointnumber").getValue().toString();
          Query query = FirebaseDatabase.getInstance().getReference("Location Drivers")
                  .orderByChild("dpointno")
                  .equalTo(studentpointnumber);
          query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                  CoordinatesModel coordinatesModel = new CoordinatesModel();
                  coordinatesModel.setDpointno(postSnapshot.getValue(CoordinatesModel.class).getDpointno());
                  coordinatesModel.setDriverlatt(postSnapshot.getValue(CoordinatesModel.class).getDriverlatt());
                  coordinatesModel.setDriverlong(postSnapshot.getValue(CoordinatesModel.class).getDriverlong());
                  String namel=coordinatesModel.getDpointno();
                  Double latitude1 = coordinatesModel.getDriverlatt();
                  Double longitude1 = coordinatesModel.getDriverlong();
                  LatLng latLng = new LatLng(latitude1, longitude1);
                  mMap.addMarker(new MarkerOptions().position(latLng).title("Point: "+ namel).icon(BitmapDescriptorFactory.fromBitmap(smallsMarker)));

//                                    addnotifs();
                  Location loc2= new Location("");
                  loc2.setLatitude(latitude1);
                  loc2.setLongitude(longitude1);
                  float distanceInMeters=loc1.distanceTo(loc2);
                  if(distanceInMeters<100){
                    //notify driver has arrived
                  }
                  else if(distanceInMeters>=100 && distanceInMeters<2000){
                    //notify driver 5 mins away
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

        private void addnotifs(){
          NotificationCompat.Builder builder=new NotificationCompat.Builder(MapsActivity.this)
                  .setSmallIcon(R.mipmap.driver)
                  .setContentTitle("Get Ready...")
                  .setContentText("Point arriving in 5 minutes");


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
      MapsActivity.this.mHandler.postDelayed(m_Runnable,3000);
    }
  };


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
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    String id = user.getUid();

    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
    int height = 100;
    int width = 100;
    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.user);
    Bitmap b=bitmapdraw.getBitmap();
    final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

    BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.mipmap.driver);
    Bitmap bs=bitmapdraws.getBitmap();
    final Bitmap smallsMarker = Bitmap.createScaledBitmap(bs, width, height, false);



    if(currentLocationMarker==null) {
      currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

      mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
      mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

    }else{
      currentLocationMarker.setPosition(latLng);
    }
    if(client!=null){
      LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
    }
    final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("StudentsLocation");
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("New Student Register");
    GeoFire geoFire=new GeoFire(ref);
    geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));
    final double latt=location.getLatitude();
    final double lngg=location.getLongitude();
    final Location loc1= new Location("");
    loc1.setLatitude(latt);
    loc1.setLongitude(lngg);
    reference.addValueEventListener(new ValueEventListener() {

      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
          String uname = dataSnapshot.child(userId).child("studentName").getValue(String.class);
          CoordinatesModel cordinatesModel=new CoordinatesModel(uname,latt,lngg);
          FirebaseDatabase.getInstance().getReference("Location Test")
                  .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                  .setValue(cordinatesModel);


        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    DatabaseReference databaseReference = firebaseDatabase.getReference("New Student Register");
    databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String studentpointnumber = dataSnapshot.child("pointnumber").getValue().toString();
        Query query = FirebaseDatabase.getInstance().getReference("Location Drivers")
                .orderByChild("dpointno")
                .equalTo(studentpointnumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
              for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                CoordinatesModel coordinatesModel = new CoordinatesModel();
                coordinatesModel.setDpointno(postSnapshot.getValue(CoordinatesModel.class).getDpointno());
                coordinatesModel.setDriverlatt(postSnapshot.getValue(CoordinatesModel.class).getDriverlatt());
                coordinatesModel.setDriverlong(postSnapshot.getValue(CoordinatesModel.class).getDriverlong());
                String namel=coordinatesModel.getDpointno();
                Double latitude1 = coordinatesModel.getDriverlatt();
                Double longitude1 = coordinatesModel.getDriverlong();
                LatLng latLng = new LatLng(latitude1, longitude1);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Point: "+ namel).icon(BitmapDescriptorFactory.fromBitmap(smallsMarker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
//                                addnotifs();
                Location loc2= new Location("");
                loc2.setLatitude(latitude1);
                loc2.setLongitude(longitude1);
                float distanceInMeters=loc1.distanceTo(loc2);
                if(distanceInMeters<100){
                  //notify driver has arrived
                    createNotificationChannel();
                  NotificationCompat.Builder builder=new NotificationCompat.Builder(MapsActivity.this)
                          .setSmallIcon(R.mipmap.driver)
                          .setContentTitle("Get Ready...")
                          .setContentText("Your Point has arrived")
                          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                          .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                  NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(MapsActivity.this);
                  notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

                }
                else if(distanceInMeters>=100 && distanceInMeters<1000){
                  //notify driver 5 mins away
                    createNotificationChannel();
                  NotificationCompat.Builder builder=new NotificationCompat.Builder(MapsActivity.this)
                          .setSmallIcon(R.mipmap.driver)
                          .setContentTitle("Get Ready...")
                          .setContentText("Point arriving in 5 mins")
                          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                          .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                  NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(MapsActivity.this);
                  notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
                }
                else if(distanceInMeters>=1000 && distanceInMeters<3000){
                    //notify driver 5 mins away
                    createNotificationChannel();
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(MapsActivity.this)
                            .setSmallIcon(R.mipmap.driver)
                            .setContentTitle("Get Ready...")
                            .setContentText("Point arriving in 5 mins")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                    NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(MapsActivity.this);
                    notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
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

      private void addnotifs(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MapsActivity.this)
                .setSmallIcon(R.mipmap.driver)
                .setContentTitle("Get Ready...")
                .setContentText("Point arriving within 5 mins");
//                Intent notificationIntent = new Intent(this,MapsActivity.class);

      }
      private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
          CharSequence name = "Personal Notifications";
          String description = "Include all the personal notifications";
          int importance=NotificationManager.IMPORTANCE_DEFAULT;
          NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,name,importance);
          notificationChannel.setDescription(description);
          NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
          notificationManager.createNotificationChannel(notificationChannel);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }



    });

    this.mHandler= new Handler();
    m_Runnable.run();

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