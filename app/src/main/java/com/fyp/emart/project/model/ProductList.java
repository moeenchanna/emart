package com.fyp.emart.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {
    @SerializedName("idProduct")
    @Expose
    private String idProduct;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductDescription")
    @Expose
    private String productDescription;
    @SerializedName("Productimage")
    @Expose
    private String productimage;
    @SerializedName("ProductBrand")
    @Expose
    private String productBrand;
    @SerializedName("ProductPrice")
    @Expose
    private String productPrice;
    @SerializedName("ProductQty")
    @Expose
    private String productQty;
    @SerializedName("Martid")
    @Expose
    private String martid;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getMartid() {
        return martid;
    }

    public void setMartid(String martid) {
        this.martid = martid;
    }

}