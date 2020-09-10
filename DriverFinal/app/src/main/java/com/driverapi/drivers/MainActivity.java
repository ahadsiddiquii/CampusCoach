package com.driverapi.drivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.driverapi.drivers.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    AnnouncesInfo announce;
    AdminAnnounce adminAnnounce;
    private final String CHANNEL_ID="personal notifications";
    private final int NOTIFICATION_ID = 001;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    DatabaseReference ref, refer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        announcementcheck();

        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
        startActivity(intent);
        MainActivity.this.finish();

    }

    public void announcementcheck(){
        announce=new AnnouncesInfo();
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

                            Intent intent=new Intent(MainActivity.this,Announcements.class);
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
                            Intent intent=new Intent(MainActivity.this,Announcements.class);
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
//    public void announcementcheck(){
//        announce=new announceinfo();
//        database=FirebaseDatabase.getInstance();
//        ref=database.getReference("Announcements");
//        refer = database.getReference("Admin Announce");
//        if(ref==null){
//
//        }
//        else {
//            refer.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot d : dataSnapshot.getChildren()) {
//                         adminAnnounce= d.getValue(AdminAnnounce.class);
//                        final long adminannounceno = adminAnnounce.getAdano();
//                        ref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    announce = ds.getValue(announceinfo.class);
//                                    final long announcementno = announce.getAno();
//                                    if(announcementno==adminannounceno-1){
//                                        long currentannounce = adminannounceno - announcementno;
//                                        if (currentannounce == 1) {
//                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
//                                                    .setSmallIcon(R.drawable.img_456677)
//                                                    .setContentTitle("Announcements:")
//                                                    .setContentText(currentannounce + " new announcement.")
//                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//                                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
//                                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
//                                        } else {
//                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
//                                                    .setSmallIcon(R.drawable.img_456677)
//                                                    .setContentTitle("Announcements:")
//                                                    .setContentText(currentannounce + " new announcements.")
//                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//                                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
//                                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
//                                        }
//
//
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//
//            });
//        }
//    }
@Override
public void onBackPressed(){
//System.exit(0);
    super.onBackPressed();

}
}


