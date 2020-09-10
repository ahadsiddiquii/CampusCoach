package com.user.studentapi;


public class AdminAnnounce {
    public long adano;
    public String intdate;

    public AdminAnnounce(){}

    public AdminAnnounce(long adano,String intdate) {
        this.adano = adano;
        this.intdate=intdate;
    }

    public long getAdano() {
        return adano;
    }

    public void setAdano(long adano) {
        this.adano = adano;
    }

    public String getIntdate() {
        return intdate;
    }

    public void setIntdate(String intdate) {
        this.intdate = intdate;
    }
}

