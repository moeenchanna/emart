package com.fyp.emart.project.Api;

import androidx.annotation.Nullable;

import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("register_customer.php")
    Call<ResponseBody> registerCustomer(
            @Field("name")String name,
            @Field("email")String email,
            @Field("password")String password,
            @Field("phone")String phone,
            @Field("address")String address);

    @FormUrlEncoded
    @POST("register_mart.php")
    Call<ResponseBody> registerMart(
            @Field("name")String name,
            @Field("email")String email,
            @Field("password")String password,
            @Field("phone")String phone,
            @Field("address")String address,
            @Field("latitude")String latitude,
            @Field("longitude")String longitude,
            @Field("logo")String logo,
            @Field("banner")String banner,
            @Field("owner")String owner,
            @Field("ownernumber")String ownernumber,
            @Field("ownerdetail")String ownerdetail,
            @Field("voucher")String voucher);

    @FormUrlEncoded
    @POST("register_user.php")
    Call<ResponseBody> registerUser(
            @Field("email")String email,
            @Field("password")String password,
            @Field("role")String roleid);

    @FormUrlEncoded
    @POST("user_login.php")
    Call<ResponseBody> loginUser(
            @Field("email")String email,
            @Field("password")String password);

    @GET("FypProject/Emart/getproducts.php")
    Call<List<ProductList>> getProducts();

    @GET("FypProject/Emart/getmarts.php")
    Call<List<MartList>> getMarts();

}
