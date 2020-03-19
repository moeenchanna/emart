package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("comment")
    @Expose
    private String comment;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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