package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("orderno")
    @Expose
    private String orderno;
    @SerializedName("orderdetail")
    @Expose
    private String orderdetail;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusid")
    @Expose
    private String statusid;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("custemail")
    @Expose
    private String custemail;
    @SerializedName("custid")
    @Expose
    private String custid;
    @SerializedName("martid")
    @Expose
    private String martid;


    @SerializedName("fcm")
    @Expose
    private String fcm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(String orderdetail) {
        this.orderdetail = orderdetail;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusid() {
        return statusid;
    }

    public void setStatusid(String statusid) {
        this.statusid = statusid;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getCustemail() {
        return custemail;
    }

    public void setCustemail(String custemail) {
        this.custemail = custemail;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getMartid() {
        return martid;
    }

    public void setMartid(String martid) {
        this.martid = martid;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }
}