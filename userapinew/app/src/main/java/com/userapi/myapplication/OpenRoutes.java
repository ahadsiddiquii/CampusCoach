package com.userapi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class OpenRoutes extends AppCompatActivity {

    TextView mTitleTv,mLocation1,mLocation2,mLocation3,mLocation4,mLocation5,mLocation6,mLocation7,mLocation8,mLocation9,mLocation10,mLocation11,mLocation12,mLocation13,mLocation14,mLocation15,mLocation16,mLocation17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_routes);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Your Route");
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshRoutes);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });

showData();




    }
    public void refreshData(){
        showData();
    }
    public void showData(){
        mTitleTv=findViewById(R.id.TitleTv);
        mLocation1=findViewById(R.id.rLocation1);
        mLocation2=findViewById(R.id.rLocation2);
        mLocation3=findViewById(R.id.rLocation3);
        mLocation4=findViewById(R.id.rLocation4);
        mLocation5=findViewById(R.id.rLocation5);
        mLocation6=findViewById(R.id.rLocation6);
        mLocation7=findViewById(R.id.rLocation7);
        mLocation8=findViewById(R.id.rLocation8);
        mLocation9=findViewById(R.id.rLocation9);
        mLocation10=findViewById(R.id.rLocation10);
        mLocation11=findViewById(R.id.rLocation11);
        mLocation12=findViewById(R.id.rLocation12);
        mLocation13=findViewById(R.id.rLocation13);
        mLocation14=findViewById(R.id.rLocation14);
        mLocation15=findViewById(R.id.rLocation15);
        mLocation16=findViewById(R.id.rLocation16);
        mLocation17=findViewById(R.id.rLocation17);

        String mTitle=getIntent().getStringExtra("title");
        String mLoc1 =getIntent().getStringExtra("Location1");
        String mLoc2=getIntent().getStringExtra("Location2");
        String mLoc3=getIntent().getStringExtra("Location3");
        String mLoc4=getIntent().getStringExtra("Location4");
        String mLoc5=getIntent().getStringExtra("Location5");
        String mLoc6=getIntent().getStringExtra("Location6");
        String mLoc7=getIntent().getStringExtra("Location7");
        String mLoc8=getIntent().getStringExtra("Location8");
        String mLoc9=getIntent().getStringExtra("Location9");
        String mLoc10=getIntent().getStringExtra("Location10");
        String mLoc11=getIntent().getStringExtra("Location11");
        String mLoc12=getIntent().getStringExtra("Location12");
        String mLoc13=getIntent().getStringExtra("Location13");
        String mLoc14=getIntent().getStringExtra("Location14");
        String mLoc15=getIntent().getStringExtra("Location15");
        String mLoc16=getIntent().getStringExtra("Location16");
        String mLoc17=getIntent().getStringExtra("Location17");



        mTitleTv.setText(mTitle);
        mLocation1.setText(mLoc1);
        mLocation2.setText(mLoc2);
        mLocation3.setText(mLoc3);
        mLocation4.setText(mLoc4);
        mLocation5.setText(mLoc5);
        mLocation6.setText(mLoc6);
        mLocation7.setText(mLoc7);
        mLocation8.setText(mLoc8);
        mLocation9.setText(mLoc9);
        mLocation10.setText(mLoc10);
        mLocation11.setText(mLoc11);
        mLocation12.setText(mLoc12);
        mLocation13.setText(mLoc13);
        mLocation14.setText(mLoc14);
        mLocation15.setText(mLoc15);
        mLocation16.setText(mLoc16);
        mLocation17.setText(mLoc17);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(OpenRoutes.this,Home.class);
        startActivity(intent);
        finish();
    }
}
