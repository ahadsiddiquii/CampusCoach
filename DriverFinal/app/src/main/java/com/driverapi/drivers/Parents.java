package com.driverapi.driverfinal;

public class Parents {
    public void setGaurdianCNIC(String gaurdianCNIC) {
        this.gaurdianCNIC = gaurdianCNIC;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    private String gaurdianCNIC,password;
    public Parents(){}
    public Parents(String password,String gaurdianCNIC)
    {
        this.gaurdianCNIC=gaurdianCNIC;
        this.password=password;

    }
    public String GetCnic()
    {
        return gaurdianCNIC;
    }
    public String getPassword()
    {
        return password;
    }
}
