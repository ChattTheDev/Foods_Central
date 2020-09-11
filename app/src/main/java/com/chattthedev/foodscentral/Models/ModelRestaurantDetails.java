package com.chattthedev.foodscentral.Models;

public class ModelRestaurantDetails {
    String resendtime,resimage,resowner,resownerphone,resservicedays,resstarttime,restaurantaddress,restaurantname;

    public ModelRestaurantDetails(String resendtime, String resimage, String resowner, String resownerphone, String resservicedays, String resstarttime, String restaurantaddress, String restaurantname) {
        this.resendtime = resendtime;
        this.resimage = resimage;
        this.resowner = resowner;
        this.resownerphone = resownerphone;
        this.resservicedays = resservicedays;
        this.resstarttime = resstarttime;
        this.restaurantaddress = restaurantaddress;
        this.restaurantname = restaurantname;
    }

    public ModelRestaurantDetails() {
    }

    public String getResendtime() {
        return resendtime;
    }

    public void setResendtime(String resendtime) {
        this.resendtime = resendtime;
    }

    public String getResimage() {
        return resimage;
    }

    public void setResimage(String resimage) {
        this.resimage = resimage;
    }

    public String getResowner() {
        return resowner;
    }

    public void setResowner(String resowner) {
        this.resowner = resowner;
    }

    public String getResownerphone() {
        return resownerphone;
    }

    public void setResownerphone(String resownerphone) {
        this.resownerphone = resownerphone;
    }

    public String getResservicedays() {
        return resservicedays;
    }

    public void setResservicedays(String resservicedays) {
        this.resservicedays = resservicedays;
    }

    public String getResstarttime() {
        return resstarttime;
    }

    public void setResstarttime(String resstarttime) {
        this.resstarttime = resstarttime;
    }

    public String getRestaurantaddress() {
        return restaurantaddress;
    }

    public void setRestaurantaddress(String restaurantaddress) {
        this.restaurantaddress = restaurantaddress;
    }

    public String getRestaurantname() {
        return restaurantname;
    }

    public void setRestaurantname(String restaurantname) {
        this.restaurantname = restaurantname;
    }
}
