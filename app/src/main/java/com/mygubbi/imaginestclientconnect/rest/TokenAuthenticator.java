package com.mygubbi.imaginestclientconnect.rest;

import android.support.annotation.NonNull;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.AccessToken;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

/**
 * @author Hemanth
 * @since 5/08/2018
 */
public class TokenAuthenticator implements Authenticator {

    private static final String TAG = "TokenAuthenticator";

    private static final int RESPONSE_UNAUTHORIZED_401 = 401;

    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) {
        // If unauthorized (Token expired)...
        if (response.code() == RESPONSE_UNAUTHORIZED_401) {
            // Gets all 401 in sync blocks,
            // to avoid multiply token updates...
            synchronized (this) {
                Call<AccessToken> refreshTokenRequest = RestGubbiClient.getInstance(false)
                        .getApiService().synchronousTokenRefresh("Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=",
                                ClientConnectApplication.getInstance().getFirebaseToken(),
                                "welcome", "password");

                try {
                    AccessToken accessToken = refreshTokenRequest.execute().body();

                    if (accessToken != null) {
                        ClientConnectApplication.getInstance().setAccessToken(accessToken);
                        return response.request().newBuilder()
                                .header(ClientConnectConstants.STR_AUTHORIZATION, ClientConnectConstants.STR_BEARER + " " + accessToken.getAccessToken())
                                .build();
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            AccessToken accessToken = ClientConnectApplication.getInstance().getAccessToken();

            return response.request().newBuilder()
                    .header(ClientConnectConstants.STR_AUTHORIZATION, ClientConnectConstants.STR_BEARER + " " + accessToken.getAccessToken())
                    .build();
        }
    }
}