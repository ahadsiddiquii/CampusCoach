package com.user.studentapi;

public class Students{

    public String studentName;
    public String pointnumber;
    public String studentID;
    public String gaurdianCNIC;
    public String email;
    public String contact;
    public String joindate;
    public String account;
    public String password;



    public Students(){}
    public Students(String studentName, String studentID,String gaurdianCNIC,String email, String pointnumber,String contact,String joindate,String account,String password){
        this.studentName = studentName;
        this.studentID = studentID;
        this.gaurdianCNIC = gaurdianCNIC;
        this.email=email;
        this.pointnumber = pointnumber;
        this.contact=contact;
        this.joindate=joindate;
        this.account=account;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setPointnumber(String pointnumber) {
        this.pointnumber = pointnumber;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setGaurdianCNIC(String gaurdianCNIC) {
        this.gaurdianCNIC = gaurdianCNIC;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStudentName() { return studentName; }
    public  String getEmail(){return email;}
    public String getStudentID() { return studentID; }
    public String getGaurdianCNIC() {return gaurdianCNIC; }
    public String getPointnumber() {return pointnumber; }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }
}
