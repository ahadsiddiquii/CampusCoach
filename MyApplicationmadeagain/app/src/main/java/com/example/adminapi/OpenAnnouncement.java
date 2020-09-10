package com.example.adminapi;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class OpenAnnouncement extends AppCompatActivity {
    TextView mAno, mAnnounce, mDescript, mDate, mTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_announcement);
        ActionBar actionBar=getSupportActionBar();


        mAno=findViewById(R.id.rAno);
        mAnnounce=findViewById(R.id.rAnnounce);
        mAnnounce.setMovementMethod(new ScrollingMovementMethod());
        mDescript=findViewById(R.id.rDescript);
        mDescript.setMovementMethod(new ScrollingMovementMethod());
        mDate=findViewById(R.id.rdate);
        mTime=findViewById(R.id.rtime);
//        Bundle intent = getIntent().getExtras();
//        if (intent != null) {
//            newAnnouncement = intent.getString("dannounce");
//            mAnnouncement.setText(newAnnouncement);
//            newDescription = intent.getString("extension");
//            mDescription.setText(newDescription);
//
//
//
//        }
//        intent.putExtra("ano",Announceno);
//        intent.putExtra("dannounce",Announcement);
//        intent.putExtra("extension",Description);
//        intent.putExtra("inttime",Time);
//        intent.putExtra("intdate",Date);

        String Ano= getIntent().getStringExtra("ano");
        String Dannounce =getIntent().getStringExtra("dannounce");
        String Extension=getIntent().getStringExtra("extension");
        String Intdate=getIntent().getStringExtra("intdate");
        String Inttime=getIntent().getStringExtra("inttime");

        actionBar.setTitle("Announcement "+ Ano);

        mAno.setText(" "+Ano+".");
        mAnnounce.setText(" " +Dannounce);
        mDescript.setText(Extension);
        mDate.setText(Intdate);
        mTime.setText(Inttime);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
