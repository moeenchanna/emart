package com.fyp.emart.project.Api;

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
}
