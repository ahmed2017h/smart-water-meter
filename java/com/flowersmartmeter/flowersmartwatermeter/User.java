package com.flowersmartmeter.flowersmartwatermeter;

import java.util.Date;

public class User {
    private String logo_url;
    private String username;
    private String phonenumber;
    private String email;
    private String collector_id;
    private String meter_id;
    private String user_id;
    Date sessionExpiryDate;
    public void setLogo_url(String logo_url){this.logo_url =logo_url;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setphonenumber(String phoneNumber) {
        this.phonenumber = phoneNumber;
    }
    public void setEmail (String email){
        this.email= email;
    }
    public void setCollector_id(String collector_id){this.collector_id = collector_id;}
    public void setMeter_id(String meter_id){this.meter_id = meter_id;}
    public void setUser_id(String user_id){this.user_id = user_id;}
    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }
    public String getLogo_url(){return logo_url;}
    public String getPhonenumber(){return  phonenumber;}
    public String getEmail(){return email;}
    public String getCollector_id(){return collector_id;}
    public String getMeter_id(){return meter_id;}
    public String getUser_id(){return user_id;}



    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
