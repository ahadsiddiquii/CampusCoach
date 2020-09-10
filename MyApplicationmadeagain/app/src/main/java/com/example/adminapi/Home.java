package com.example.adminapi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AnnounceInfo announce;
    AdminAnnounce adminAnnounce;
    DatabaseReference ref,refer,mRef;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference reference=database.getReference();
    private final String CHANNEL_ID="personal_notifications";
    private final int NOTIFICATION_ID= 001;
    FirebaseAuth firebaseAuth;
    StudentInfo student,stu;
    String studentpno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null) {
            check();
            remove();
        }
        wipeout();
        announcementcheck();

    }

    public void wipeout(){

                    mRef = database.getReference("New Student Register");
                    Query mQuery=mRef.orderByChild("account").equalTo("Not Verified");
                    mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    stu = ds.getValue(StudentInfo.class);
                                    String mail = stu.getEmail();
                                    String pass = stu.getPassword();
                                    notverified(mail,pass);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

    public void notverified(String email,String password){
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential= EmailAuthProvider
                .getCredential(email,password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.delete();
            }
        });
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("New Student Register");
//        Toast.makeText(getApplicationContext(), email , Toast.LENGTH_SHORT).show();
        Query mQuery=mRef.orderByChild("email").equalTo(email);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

//                                studentpno = dataSnapshot.child("gaurdianCNIC").getValue().toString();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        student=ds.getValue(StudentInfo.class);
                        studentpno=student.getGaurdianCNIC();
//                                    Toast.makeText(getApplicationContext(), studentpno , Toast.LENGTH_SHORT).show();
                        ds.getRef().removeValue();
                        ref=mFirebaseDatabase.getReference("Parents");
                        Query Query=ref.orderByChild("gaurdianCNIC").equalTo(studentpno);
                        Query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                                if(dataSnap.exists()) {
                                    for (DataSnapshot d : dataSnap.getChildren()) {
                                        d.getRef().removeValue();
                                    }
                                }
                                else{

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    public void remove(){
        Date date = new Date();
        Date newDate = new Date(date.getTime() );
//        + (604800000L * 2) + (24 * 60 * 60)
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        ref = database.getReference("Admin Announce");
        Query mQuery=ref.orderByChild("adano").equalTo(0);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    refer = database.getReference("Announcements");
                    refer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                d.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void check() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
//        + (604800000L * 2) + (24 * 60 * 60)
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        ref = database.getReference("Admin Announce");
        Query mQuery=ref.orderByChild("intdate").equalTo(stringdate);
        mQuery.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        break;
                    }
                }
                else{

                    AdminAnnounce adminAnnounce=new AdminAnnounce(0,stringdate);
                    FirebaseDatabase.getInstance().getReference("Admin Announce")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(adminAnnounce).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(),"Announcement Successful",Toast.LENGTH_SHORT).show();
//                    startActivity( new Intent(Announcements.this,Announcements.class));
//                    Announcements.this.finish();
                            } else {
//                    Toast.makeText(getApplicationContext(),"Announcement Unsuccessful",Toast.LENGTH_SHORT).show();
                            }
                            wipeout();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void erase(){
        ref = database.getReference("Admin Announce");

                            ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds: dataSnapshot.getChildren())
                            {
                            ds.getRef().removeValue();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
    }


    public void announcementcheck(){
        announce=new AnnounceInfo();
        database=FirebaseDatabase.getInstance();
        refer = database.getReference("Admin Announce");
        if(refer==null){

        }
        else {
            refer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        adminAnnounce = d.getValue(AdminAnnounce.class);
                        long adminannounceno = adminAnnounce.getAdano();
                        if (adminannounceno == 1) {
                            createNotificationChannel();

                            Intent intent=new Intent(Home.this,AnnouncementsUpdated.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent= PendingIntent.getActivity(Home.this,0,intent,PendingIntent.FLAG_ONE_SHOT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this,CHANNEL_ID)
                                    .setSmallIcon(R.drawable.img_456677)
                                    .setContentTitle("Announcements:")
                                    .setContentText(adminannounceno + " announcement today...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                            builder.setAutoCancel(true);
                            builder.setContentIntent(pendingIntent);


                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Home.this);
                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                        } else {
                            createNotificationChannel();
                            Intent intent=new Intent(Home.this,AnnouncementsUpdated.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent= PendingIntent.getActivity(Home.this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this,CHANNEL_ID)
                                    .setSmallIcon(R.drawable.img_456677)
                                    .setContentTitle("Announcements:")
                                    .setContentText(adminannounceno + " announcements today...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                            builder.setAutoCancel(true);
                            builder.setContentIntent(pendingIntent);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Home.this);
                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Personal Notifications";
            String description = "Include all the personal notifications";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_TrackDrivers) {
            openMapsActivity();
        } else if (id == R.id.nav_Announcement) {
            openAnnouncements();
        } else if (id == R.id.nav_Studentinfo) {
            openStudentDetails();
        } else if (id == R.id.nav_DriverDetails) {
            openDriverDetails();
        } else if (id == R.id.nav_Routes) {
            openRoute1();
        }else if (id == R.id.nav_Logout) {
            openlogout();
        }
//        else if (id == R.id.check) {
//            opencheck();
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openRoute1(){
        Intent intent = new Intent(this, retrieveroutes.class);
        startActivity(intent);
    }
    public void openDriverDetails(){
        Intent intent = new Intent(this, DriverDetailsUpdated.class);
        startActivity(intent);
    }
    public void openAnnouncements(){
        Intent intent = new Intent(this, AnnouncementsUpdated.class);
        startActivity(intent);
    }
    public void openStudentDetails(){
        Intent intent = new Intent(this, StudentDetailsUpdated.class);
        startActivity(intent);
    }
    public void openMapsActivity(){
        Intent intent = new Intent(this, MapsActivityAllDrivers.class);
        startActivity(intent);
    }
    public void openlogout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AdminLog.class);
        Toast.makeText(Home.this, "Logging Out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
    public void opencheck(){

        Intent intent = new Intent(this, DriverDetailsUpdated.class);
        startActivity(intent);
    }


}
