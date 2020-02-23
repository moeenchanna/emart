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

    @GET("FypProject/Emart/getproducts.php")
    Call<List<ProductList>> getProducts();

    @GET("FypProject/Emart/getmarts.php")
    Call<List<MartList>> getMarts();
}
