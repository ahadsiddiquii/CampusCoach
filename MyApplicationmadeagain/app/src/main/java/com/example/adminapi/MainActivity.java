package com.example.adminapi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    AnnounceInfo announce;
    AdminAnnounce adminAnnounce;
    DatabaseReference ref,refer;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference();
    private final String CHANNEL_ID="personal_notifications";
    private final int NOTIFICATION_ID= 001;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
//        if(user!=null) {
//            check();
//            remove();
//        }
//        announcementcheck();
       openAdminLogin();
    }




    public void openAdminLogin(){
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
        finish();
    }

    public void opennotification(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.img_456677)
                .setContentTitle("Get Ready...")
                .setContentText("Point arriving in 5 mins")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(MainActivity.this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
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
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        }
                    });
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

                            Intent intent=new Intent(MainActivity.this,AnnouncementsUpdated.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent= PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                                    .setSmallIcon(R.drawable.img_456677)
                                    .setContentTitle("Announcements:")
                                    .setContentText(adminannounceno + " announcement today...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                            builder.setAutoCancel(true);
                            builder.setContentIntent(pendingIntent);


                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                        } else {
                            createNotificationChannel();
                            Intent intent=new Intent(MainActivity.this,AnnouncementsUpdated.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingIntent= PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                                    .setSmallIcon(R.drawable.img_456677)
                                    .setContentTitle("Announcements:")
                                    .setContentText(adminannounceno + " announcements today...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                            builder.setAutoCancel(true);
                            builder.setContentIntent(pendingIntent);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
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
}


