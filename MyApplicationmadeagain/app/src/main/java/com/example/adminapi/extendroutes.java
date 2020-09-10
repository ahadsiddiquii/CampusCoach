package com.example.adminapi;

import com.google.android.gms.maps.model.LatLng;

public class extendroutes {
    String Location;

    public extendroutes(){
    }
    public extendroutes(String Location){
        this.Location=Location;

    }
    public String getLocation(){
        return Location;
    }
    public void setLocation(String Location){
        this.Location=Location;
    }
}
