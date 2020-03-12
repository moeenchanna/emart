package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminComplaintModel {

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
    @SerializedName("martid")
    @Expose
    private String martid;

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

    public String getMartid() {
        return martid;
    }

    public void setMartid(String martid) {
        this.martid = martid;
    }

}