package com.fyp.emart.project.Api;

import androidx.annotation.Nullable;

import com.fyp.emart.project.model.AdminOrder;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;

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
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("address") String address);

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
            @Field("voucher") String voucher);

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

    // Get mart
    @GET("FypProject/Emart/getorders.php")
    Call<List<AdminOrder>> getAdminorder();



}
