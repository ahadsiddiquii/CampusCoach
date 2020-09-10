package com.example.adminapi;

public class DriverInfo {
    public String dname,dcontact,dcnic,demail,djoindate,dpointno;

    public DriverInfo(){}

    public DriverInfo(String dname, String dcontact, String dcnic, String demail, String djoindate,String dpointno) {
        this.dname = dname;
        this.dcontact = dcontact;
        this.dcnic = dcnic;
        this.demail = demail;
        this.djoindate = djoindate;
        this.dpointno=dpointno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDcontact() {
        return dcontact;
    }

    public void setDcontact(String dcontact) {
        this.dcontact = dcontact;
    }

    public String getDcnic() {
        return dcnic;
    }

    public void setDcnic(String dcnic) {
        this.dcnic = dcnic;
    }

    public String getDemail() {
        return demail;
    }

    public void setDemail(String demail) {
        this.demail = demail;
    }

    public String getDjoindate() {
        return djoindate;
    }

    public void setDjoindate(String djoindate) {
        this.djoindate = djoindate;
    }

    public String getDpointno() {
        return dpointno;
    }

    public void setDpointno(String dpointno) {
        this.dpointno = dpointno;
    }
}
