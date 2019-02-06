package com.mygubbi.imaginestclientconnect.helpers;

/**
 * @author Hemanth
 * @since 4/9/2018
 */
public final class ClientConnectConstants {


    private static final String CLIENT_CONNECT_BASE_URL_UAT_1 = "https://gameuat.mygubbi.com:1446/";

    private static final String CLIENT_CONNECT_BASE_URL_UAT = "http://202.83.18.99:1445/";
    private static final String CLIENT_CONNECT_BASE_URL_PROD = "https://game.mygubbi.com:1446/";
    private static final String GUBBI_BASE_URL = "https://imaginestback.mygubbi.com";

    public static final String FILE_PROVIDER_AUTHORITY = "com.mygubbi.imaginestclientconnect.fileprovider";

    public static String getClientConnectBaseUrlUat() {
       return CLIENT_CONNECT_BASE_URL_PROD;
     //   return CLIENT_CONNECT_BASE_URL_UAT_1;
    }

    public static String getGubbiBaseUrl() {
        return GUBBI_BASE_URL;
    }

    public static final String CLIENT_CONNECT_PROFILE = "client_connect_profile";
    public static final String CLIENT_CONNECT_PREFS = "client_connect_prefs";
    public static final String CLIENT_ISSUE = "client_issue";
    public static final String CLIENT_SIGNATURE = "client_signature";
    public static final String GUBBI_ACCESS_TOKEN = "gubbi_access_token";
    public static final String USER_ACCESS_TOKEN = "user_access_token";
    public static final String USER_DATA = "user_data";
    public static final String USER_LOGGED_IN = "user_logged_in";
    public static final String IS_NEW_USER = "is_new_user";
    public static final String FIREBASE_USER_ID = "firebase_user_id";
    public static final String FIREBASE_AUTH_TOKEN = "firebase_auth_token";
    public static final String FIREBASE_USER_NAME = "firebase_user_name";
    public static final String FIREBASE_USER_PHOTO = "firebase_user_photo";

    public static final String STR_AUTHORIZATION = "Authorization";
    public static final String STR_BEARER = "Bearer";
    public static final String STR_BASIC_TOKEN = "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=";
    public static final String STR_GRANT_TYPE = "password";

    public static final int VIEW_TYPE_QUESTION_HEADER = 0;
    public static final int VIEW_TYPE_QUESTION = 1;
    public static final int VIEW_TYPE_SUBMIT = 2;

    public static final String OPPORTUNITY_ID = "opportunity_id";
    public static final String ERROR_SOMETHING_WRONG = "Something went wrong, please try again.";
    public static String STR_BLANK = "";

    public static String STR_NULL = "null";
}