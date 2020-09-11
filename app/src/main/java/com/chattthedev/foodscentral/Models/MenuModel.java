package com.chattthedev.foodscentral.Models;

public class MenuModel {
    String finalprice, itemcat, itemlargesizename, itemname, itemsmallsizename, largesizeprice, originalprice, productimage, resid, smallsizeprice, status, menuid, resname;

    public MenuModel() {
    }

    public MenuModel(String finalprice, String itemcat, String itemlargesizename, String itemname, String itemsmallsizename, String largesizeprice, String originalprice, String productimage, String resid, String smallsizeprice, String status, String menuid, String resname) {
        this.finalprice = finalprice;
        this.itemcat = itemcat;
        this.itemlargesizename = itemlargesizename;
        this.itemname = itemname;
        this.itemsmallsizename = itemsmallsizename;
        this.largesizeprice = largesizeprice;
        this.originalprice = originalprice;
        this.productimage = productimage;
        this.resid = resid;
        this.smallsizeprice = smallsizeprice;
        this.status = status;
        this.menuid = menuid;
        this.resname = resname;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resnamecat) {
        this.resname = resnamecat;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFinalprice() {
        return finalprice;
    }

    public void setFinalprice(String finalprice) {
        this.finalprice = finalprice;
    }

    public String getItemcat() {
        return itemcat;
    }

    public void setItemcat(String itemcat) {
        this.itemcat = itemcat;
    }

    public String getItemlargesizename() {
        return itemlargesizename;
    }

    public void setItemlargesizename(String itemlargesizename) {
        this.itemlargesizename = itemlargesizename;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemsmallsizename() {
        return itemsmallsizename;
    }

    public void setItemsmallsizename(String itemsmallsizename) {
        this.itemsmallsizename = itemsmallsizename;
    }

    public String getLargesizeprice() {
        return largesizeprice;
    }

    public void setLargesizeprice(String largesizeprice) {
        this.largesizeprice = largesizeprice;
    }

    public String getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(String originalprice) {
        this.originalprice = originalprice;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getSmallsizeprice() {
        return smallsizeprice;
    }

    public void setSmallsizeprice(String smallsizeprice) {
        this.smallsizeprice = smallsizeprice;
    }
}
