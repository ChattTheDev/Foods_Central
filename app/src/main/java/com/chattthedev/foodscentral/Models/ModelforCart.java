package com.chattthedev.foodscentral.Models;

public class ModelforCart {

    String menuid, menuname, menuquant, menuprice, menuhalfprice, menufullprice, menuhalf, menufull, userid, resname, menuimage;

    public ModelforCart() {
    }

    public ModelforCart(String menuid, String menuname, String menuquant, String menuprice, String menuhalfprice, String menufullprice, String menuhalf, String menufull, String userid, String resname, String menuimage) {
        this.menuid = menuid;
        this.menuname = menuname;
        this.menuquant = menuquant;
        this.menuprice = menuprice;
        this.menuhalfprice = menuhalfprice;
        this.menufullprice = menufullprice;
        this.menuhalf = menuhalf;
        this.menufull = menufull;
        this.userid = userid;
        this.resname = resname;
        this.menuimage = menuimage;
    }

    public String getMenuimage() {
        return menuimage;
    }

    public void setMenuimage(String menuimage) {
        this.menuimage = menuimage;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getMenuquant() {
        return menuquant;
    }

    public void setMenuquant(String menuquant) {
        this.menuquant = menuquant;
    }

    public String getMenuprice() {
        return menuprice;
    }

    public void setMenuprice(String menuprice) {
        this.menuprice = menuprice;
    }

    public String getMenuhalfprice() {
        return menuhalfprice;
    }

    public void setMenuhalfprice(String menuhalfprice) {
        this.menuhalfprice = menuhalfprice;
    }

    public String getMenufullprice() {
        return menufullprice;
    }

    public void setMenufullprice(String menufullprice) {
        this.menufullprice = menufullprice;
    }

    public String getMenuhalf() {
        return menuhalf;
    }

    public void setMenuhalf(String menuhalf) {
        this.menuhalf = menuhalf;
    }

    public String getMenufull() {
        return menufull;
    }

    public void setMenufull(String menufull) {
        this.menufull = menufull;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
