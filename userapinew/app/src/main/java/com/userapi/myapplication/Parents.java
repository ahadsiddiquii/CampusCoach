package com.userapi.myapplication;

public class Parents {

    public String gaurdianCNIC;
    public String password;
    public String contact;

    public Parents(){}

    public Parents(String gaurdianCNIC, String password,String contact) {
        this.gaurdianCNIC = gaurdianCNIC;
        this.password = password;
        this.contact=contact;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGaurdianCNIC() {
        return gaurdianCNIC;
    }

    public void setGaurdianCNIC(String gaurdianCNIC) {
        this.gaurdianCNIC = gaurdianCNIC;
    }

    public String getpassword() {
        return password;
    }




}
