package com.mygubbi.imaginestclientconnect.rest;

import android.support.annotation.NonNull;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Hemanth
 * @since 5/08/2018
 * Assistant API header interceptor
 */
public class GubbiRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        /*Request originalRequest = chain.request();

        // Nothing to add to intercepted request if:
        // a) Authorization value is empty because user is not logged in yet
        // b) There is already a header with updated Authorization value
        if (authorizationTokenIsEmpty() || alreadyHasAuthorizationHeader(originalRequest)) {
            return chain.proceed(originalRequest);
        }*/

        AccessToken accessToken = ClientConnectApplication.getInstance().getAccessToken();

        String token;

        if (accessToken == null) {
            token = "";
        } else {
            token = accessToken.getAccessToken();
        }

        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader(ClientConnectConstants.STR_AUTHORIZATION, ClientConnectConstants.STR_BEARER + " " + token);
        return chain.proceed(builder.build());
    }

}