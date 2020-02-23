package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MartList {



    @SerializedName("IdMart")
    @Expose
    private String idMart;
    @SerializedName("MartName")
    @Expose
    private String martName;
    @SerializedName("Martlogo")
    @Expose
    private String martlogo;
    @SerializedName("MartBanner")
    @Expose
    private String martBanner;
    @SerializedName("MartAddress")
    @Expose
    private String martAddress;

    public String getIdMart() {
        return idMart;
    }

    public void setIdMart(String idMart) {
        this.idMart = idMart;
    }

    public String getMartName() {
        return martName;
    }

    public void setMartName(String martName) {
        this.martName = martName;
    }

    public String getMartlogo() {
        return martlogo;
    }

    public void setMartlogo(String martlogo) {
        this.martlogo = martlogo;
    }

    public String getMartBanner() {
        return martBanner;
    }

    public void setMartBanner(String martBanner) {
        this.martBanner = martBanner;
    }

    public String getMartAddress() {
        return martAddress;
    }

    public void setMartAddress(String martAddress) {
        this.martAddress = martAddress;
    }

}