package com.user.studentapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        Button Studentbtn=findViewById(R.id.StudentBtn);
        Button Parentsbtn=findViewById(R.id.ParentsBtn);
//        Button proceed= findViewById(R.id.button4);
        Button logo=findViewById(R.id.imageButton);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashScreen.this,AboutUs.class);
                startActivity(intent);
                SplashScreen.this.finish();
            }
        });
        Studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashScreen.this,StudentsLogin.class);
                startActivity(intent);
            }
        });
        Parentsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashScreen.this,ParentsLogin.class);
                startActivity(intent);
            }
        });
//        proceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(SplashScreen.this,Login.class);
//                startActivity(intent);
//                SplashScreen.this.finish();
//            }
//        });
    }


    @Override
    public void onBackPressed(){
//System.exit(0);
        super.onBackPressed();

    }
}



//public class SplashScreen extends AppCompatActivity {
//    private int SLEEP_TIMER=3;
//    @Override
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash_screen);
//        getSupportActionBar().hide();
//        LogoLauncher LogoLauncher = new LogoLauncher();
//        LogoLauncher.start();
//    }
//    private class LogoLauncher extends Thread{
//        public void run(){
//            try{
//                sleep(1000*SLEEP_TIMER);
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//            Intent intent=new Intent(SplashScreen.this,AdminLog.class);
//            startActivity(intent);
//            SplashScreen.this.finish();
//        }
//
//    }
//}
