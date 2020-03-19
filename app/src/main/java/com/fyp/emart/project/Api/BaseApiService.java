package com.fyp.emart.project.Api;

import com.fyp.emart.project.model.ComplaintList;
import com.fyp.emart.project.model.CustomerProfileList;
import com.fyp.emart.project.model.MartLocationList;
import com.fyp.emart.project.model.MartProfileList;
import com.fyp.emart.project.model.OrderList;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;
import com.fyp.emart.project.model.ReviewList;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaseApiService {

    // Customer Signup
    @FormUrlEncoded
    @POST("FypProject/Emart/register_customer.php")
    Call<ResponseBody> registerCustomer(
            @Field("cname") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("fcmkey") String token);

    // Mart Signup
    @FormUrlEncoded
    @POST("FypProject/Emart/register_mart.php")
    Call<ResponseBody> registerMart(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("logo") String logo,
            @Field("banner") String banner,
            @Field("owner") String owner,
            @Field("ownernumber") String ownernumber,
            @Field("ownerdetail") String ownerdetail,
            @Field("voucher") String voucher,
            @Field("fcmkey") String token);


    // User Signup
    @FormUrlEncoded
    @POST("FypProject/Emart/register_user.php")
    Call<ResponseBody> registerUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") String roleid);


    // User Authentication
    @FormUrlEncoded
    @POST("FypProject/Emart/user_login.php")
    Call<ResponseBody> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    // Get products by mart id
    @GET("FypProject/Emart/getproducts.php")
    Call<List<ProductList>> getProducts(@Query("mart") String martid);


    // Get mart
    @GET("FypProject/Emart/getmarts.php")
    Call<List<MartList>> getMarts();

    // Get all orders for admin
    @GET("FypProject/Emart/getorders.php")
    Call<List<OrderList>> getAdminorder();

    // Get all orders for customer
    @POST("FypProject/Emart//getorderhistory.php")
    Call<List<OrderList>> getOrderHistory(@Query("custid") String custid);

    // Get all orders for mart
    @GET("FypProject/Emart/getmartorders.php")
    Call<List<OrderList>> getMartOrders(@Query("martid") String martid);


    // Punch Order
    @FormUrlEncoded
    @POST("FypProject/Emart/punchorder.php")
    Call<ResponseBody> OrderPunch(
            @Field("orderno") String orderno,
            @Field("orderdetail") String orderdetail,
            @Field("datetime") String datetime,
            @Field("status") String status,
            @Field("statusid") String statusid,
            @Field("subtotal") String subtotal,
            @Field("custemail") String custemai,
            @Field("custid") String custid,
            @Field("martid") String martid);

    // Get mart
    @GET("FypProject/Emart/location.php")
    Call<JSONObject> getMartsLocation();

    // Get Mart Profile
    @POST("FypProject/Emart/getmartrsid.php")
    Call<List<MartProfileList>> getMartProfile(@Query("email") String email);

    // Get Customer Profile
    @POST("FypProject/Emart/getcustomersid.php")
    Call<List<CustomerProfileList>> getCustomerProfile(@Query("email") String email);

    // Get all complaint for admin
    @GET("FypProject/Emart/getallcomplaint.php")
    Call<List<ComplaintList>> getAdminComplaint();

    // Get all complaint for customer
    @POST("FypProject/Emart/getcustomercomplaint.php")
    Call<List<ComplaintList>> getCustomerComplaint(@Query("custid") String custid);

    // Get all complaint for mart
    @GET("FypProject/Emart/getmartcomplaint.php")
    Call<List<ComplaintList>> getMartComplaint(@Query("martid") String martid);

    // Get all reviews for admin
    @GET("FypProject/Emart/getallreview.php")
    Call<List<ReviewList>> getAdminReviews();

    // Get all reviews for customer
    @POST("FypProject/Emart//getcustomerreviews.php")
    Call<List<ReviewList>> getCustomerReviews(@Query("custid") String custid);

    // Get all reviews for mart
    @GET("FypProject/Emart/getmartreviews.php")
    Call<List<ReviewList>> getMartReviews(@Query("martid") String martid);

    //Add Review
    @FormUrlEncoded
    @POST("FypProject/Emart/addreview.php")
    Call<ResponseBody> AddReview(
            @Field("datetime") String datetime,
            @Field("comment") String comment,
            @Field("rate") String rate,
            @Field("custid") String custid,
            @Field("custname") String custname,
            @Field("martid") String martid,
            @Field("martname") String martname);

    //Add Complaints
    @FormUrlEncoded
    @POST("FypProject/Emart/addcomplaint.php")
    Call<ResponseBody> AddComplaints(
            @Field("detail") String detail,
            @Field("datetime") String datetime,
            @Field("custid") String custid,
            @Field("custname") String custname,
            @Field("martid") String martid,
            @Field("martname") String martname,
            @Field("statusid") String statusid,
            @Field("dispatchid") String dispatchid);


    //Add Products by Mart
    @FormUrlEncoded
    @POST("FypProject/Emart/addproducts.php")
    Call<ResponseBody> AddProducts(
            @Field("ProductName") String ProductName,
            @Field("ProductDescription") String ProductDescription,
            @Field("Productimage") String Productimage,
            @Field("ProductBrand") String ProductBrand,
            @Field("ProductPrice") String ProductPrice,
            @Field("ProductQty") String ProductQty,
            @Field("martid") String martid);

    // Customer Profile Update
    @FormUrlEncoded
    @POST("FypProject/Emart/")
    Call<ResponseBody> updateCustomerProfile(
            @Field("cname") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address);

    // Mart Signup
    @FormUrlEncoded
    @POST("FypProject/Emart/.php")
    Call<ResponseBody> updateMartProfile(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);
}
