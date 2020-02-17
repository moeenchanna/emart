package com.fyp.emart.project.Api;

public class UtilsApi {

    // localhost.
    public static final String BASE_URL_API = "http://moeenchanna.com/api/";

    //  Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
