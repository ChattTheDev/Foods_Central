package com.chattthedev.foodscentral.Models;

public class RatingModel {

    String userphone, foodid, rateValue, comment, foodname;

    public RatingModel() {
    }

    public RatingModel(String userphone, String foodid, String rateValue, String comment, String foodname) {
        this.userphone = userphone;
        this.foodid = foodid;
        this.rateValue = rateValue;
        this.comment = comment;
        this.foodname = foodname;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
