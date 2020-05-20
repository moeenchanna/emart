package com.fyp.emart.project.Api;

public class DataConfig {
    //Address Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    //Login Method
    public static final String SHARED_PREF_NAME = "mysharedpref";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROLE_ID = "roleid";

    //Customer Methods
    public static final String CUSTOMER_EMAIL = "CUSTOMER_EMAIL";
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String CUSTOMER_iD= "CUSTOMERI_D";
    public static final String CUSTOMER_PHONE = "CUSTOMER_PHONE";
    public static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
    public static final String CUSTOMER_FCM_KEY = "CUSTOMER_FCM_KEY";

    //Mart temp id
    public static final String TEMP_MART_iD= "TEMP_PRODUCT_ID";

    //Discount Ammount
    public static final String DISCOUNT_AMOUNT= "DISCOUNT_AMOUNT";

    //Mart Methods
    public static final String MART_EMAIL = "MART_EMAIL";
    public static final String MART_NAME = "MART_NAME";
    public static final String MART_iD= "MART_iD";
    public static final String MART_PHONE = "MART_PHONE";
    public static final String MART_ADDRESS = "MART_ADDRESS";
    public static final String MART_FCM_KEY = "MART_FCM_KEY";
}
