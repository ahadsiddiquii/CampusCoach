package com.user.studentapi;

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

import com.user.studentapi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    AnnounceInfo announce;
    AdminAnnounce adminAnnounce;
    DatabaseReference ref,refer;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference();
    private final String CHANNEL_ID="personal_notifications";
    private final int NOTIFICATION_ID= 001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        announcementcheck();
       openAdminLogin();
    }
    public void openAdminLogin(){
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
        finish();
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

