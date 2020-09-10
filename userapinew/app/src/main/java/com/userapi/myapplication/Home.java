package com.userapi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email=getIntent().getStringExtra("email");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_TrackYourPoint) {
            openMapActivity();
        } else if (id == R.id.nav_Announcement) {
            openAnnouncements();
        } else if (id == R.id.nav_Myinfo) {
            openmyinfo();
        } else if (id == R.id.nav_DriverDetails) {
openDriverDetails();
        } else if (id == R.id.nav_Route) {
            OpenRoute();
        } else if (id == R.id.nav_Feedback) {
openfeedback();
        }else if (id == R.id.nav_Logout) {
            openlogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openMapActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void OpenRoute(){
        Intent intent = new Intent(this, retrieverouteyourdriver.class);
        startActivity(intent);
    }

    public void openDriverDetails(){
        Intent intent = new Intent(this, DriverDetailsUpdated.class);
        intent.putExtra("emails",email);
        startActivity(intent);
    }
    public void openAnnouncements(){
        Intent intent = new Intent(this, AnnouncementsUpdated.class);
        startActivity(intent);
    }
    public void openmyinfo(){

        Intent intent = new Intent(this, OpenStudentDetails.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void openlogout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
        finish();
    }
    public void openfeedback(){
        Intent intent = new Intent(this,Feedback.class);
        startActivity(intent);
    }
}
