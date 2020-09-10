package com.user.studentapi;

public class CoordinatesModel {
    public String dpointno;
    public double driverlatt,driverlong;

    public CoordinatesModel(){}

    public CoordinatesModel(String dpointno,double driverlatt, double driverlong) {
        this.dpointno=dpointno;
        this.driverlatt = driverlatt;
        this.driverlong = driverlong;
    }

    public String getDpointno() {
        return dpointno;
    }

    public void setDpointno(String dpointno) {
        this.dpointno = dpointno;
    }

    public double getDriverlatt() {
        return driverlatt;
    }

    public void setDriverlatt(double driverlatt) {
        this.driverlatt = driverlatt;
    }

    public double getDriverlong() {
        return driverlong;
    }

    public void setDriverlong(double driverlong) {
        this.driverlong = driverlong;
    }
}
