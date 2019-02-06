package com.mygubbi.imaginestclientconnect.rest;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Hemanth
 * @since 4/02/2018
 * My Gubbi Server header interceptor
 */
public class ClientConnectRequestInterceptor implements Interceptor {

    private static final String TAG = "ClientConnectRequestInt";

    private String credentials;

    ClientConnectRequestInterceptor(String userName, String password) {
        this.credentials = Credentials.basic(userName, password);
        Log.d(TAG, "ClientConnectRequestInterceptor: " + credentials);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization", credentials);
        return chain.proceed(builder.build());
    }
}