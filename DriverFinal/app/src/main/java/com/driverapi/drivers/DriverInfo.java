package com.driverapi.drivers;

public class DriverInfo {
    public String dname,dcontact,dcnic,demail,djoindate,dpointno;

    public DriverInfo(String dname, String dcontact, String dcnic, String demail, String djoindate,String dpointno) {
        this.dname = dname;
        this.dcontact = dcontact;
        this.dcnic = dcnic;
        this.demail = demail;
        this.djoindate = djoindate;
        this.dpointno=dpointno;
    }
    public DriverInfo(){}
    String getdrivername(){return dname;}

    public String getDcontact() {
        return dcontact;
    }

    public String getDcnic() {
        return dcnic;
    }

    public String getDemail() {
        return demail;
    }

    public String getDjoindate() {
        return djoindate;
    }

    public String getDpoint() {
        return dpointno;
    }
}
