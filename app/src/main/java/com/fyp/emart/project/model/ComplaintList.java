package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("custid")
    @Expose
    private String custid;
    @SerializedName("custname")
    @Expose
    private String custname;
    @SerializedName("martid")
    @Expose
    private String martid;
    @SerializedName("martname")
    @Expose
    private String martname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getMartid() {
        return martid;
    }

    public void setMartid(String martid) {
        this.martid = martid;
    }

    public String getMartname() {
        return martname;
    }

    public void setMartname(String martname) {
        this.martname = martname;
    }

}