package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MartProfileList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("ownernumber")
    @Expose
    private String ownernumber;
    @SerializedName("ownerdetail")
    @Expose
    private String ownerdetail;
    @SerializedName("voucher")
    @Expose
    private String voucher;
    @SerializedName("fcmkey")
    @Expose
    private String fcmkey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnernumber() {
        return ownernumber;
    }

    public void setOwnernumber(String ownernumber) {
        this.ownernumber = ownernumber;
    }

    public String getOwnerdetail() {
        return ownerdetail;
    }

    public void setOwnerdetail(String ownerdetail) {
        this.ownerdetail = ownerdetail;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getFcmkey() {
        return fcmkey;
    }

    public void setFcmkey(String fcmkey) {
        this.fcmkey = fcmkey;
    }

}
