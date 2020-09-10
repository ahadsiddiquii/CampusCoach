package com.userapi.myapplication;

public class AnnouncesInfo {
    public String dannounce;
    public long ano;
    public String intdate,inttime,extension;

    public AnnouncesInfo(){}

    public AnnouncesInfo(String dannounce, long ano, String intdate, String inttime, String extension) {
        this.dannounce = dannounce;
        this.ano=ano;
        this.intdate=intdate;
        this.inttime=inttime;
        this.extension=extension;

    }

    public String getIntdate() {
        return intdate;
    }

    public void setIntdate(String intdate) {
        this.intdate = intdate;
    }

    public String getDannounce() {
        return dannounce;
    }

    public void setDannounce(String dannounce) {
        this.dannounce = dannounce;
    }

    public long getAno() {
        return ano;
    }

    public void setAno(long ano) {
        this.ano = ano;
    }

    public String getInttime() {
        return inttime;
    }

    public void setInttime(String inttime) {
        this.inttime = inttime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
