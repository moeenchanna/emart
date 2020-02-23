package com.fyp.emart.project.Api;

import androidx.annotation.Nullable;

import com.fyp.emart.project.model.ProductList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApiService {

    // This function is to call Login Api
    @FormUrlEncoded
    @POST("account/login")
    Call<ResponseBody> loginRequest(@Field("email") String usernmae,
                                    @Field("password") String password);

    // This function is to call API Customers
    @FormUrlEncoded
    @POST("reference/getproducts")
    Call<ProductList> getCustomers(@Field("IdentityToken") String token,
                                   @Nullable @Field("Name") String name);
}
