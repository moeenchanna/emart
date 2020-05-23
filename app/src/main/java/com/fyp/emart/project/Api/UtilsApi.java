package com.fyp.emart.project.Api;

public class UtilsApi {

    // localhost.
    public static final String BASE_URL_API = "http://moeenchanna.com/";

    public static final String SERVER_APP_API_KEY = "AAAAMqH2aBU:APA91bF87fqHhuhg97K2KBP1YqKt847V5A5mcnvw3BZLIpZbZEtEHBUKzQBEUdq7Mx4Oo6De3xtGi9Z7i-KnYZuE3qYacPl0XU1oLr1Kc6zgpY3_bjDovuYZzZwMqMV8ZjCUGg_MZQ5e";


    //  Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
