package com.mygubbi.imaginestclientconnect.clientConnectMain;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.AccessToken;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

/**
 * @author Hemanth
 * @since 5/08/2018
 */
public class ClientConnectApplication extends Application {

    private static final String TAG = "ClientConnectApplicatio";

    private static ClientConnectApplication instance;

    private static AccessToken accessToken;
    private static String firebaseToken;
    private static String opportunityId = null;
    private static ClientProfile clientProfile = null;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreferences = this.getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static ClientConnectApplication getInstance() {
        return instance;
    }

    public void setAccessToken(AccessToken accessToken) {
        sharedPreferences.edit().putString(ClientConnectConstants.USER_ACCESS_TOKEN, new Gson().toJson(accessToken)).apply();
    }

    public AccessToken getAccessToken() {
        if (accessToken == null) {
            String userData = sharedPreferences.getString(ClientConnectConstants.USER_ACCESS_TOKEN, "");
            if (!TextUtils.isEmpty(userData)) {
                accessToken = new Gson().fromJson(userData, AccessToken.class);
                return accessToken;
            } else {
                return null;
            }
        } else {
            return accessToken;
        }
    }

    public String getFirebaseToken() {
        if (firebaseToken == null) {
            firebaseToken = sharedPreferences.getString(ClientConnectConstants.FIREBASE_AUTH_TOKEN, "");
            return firebaseToken;
        } else {
            return firebaseToken;
        }
    }

    public void setFirebaseToken(String firebaseToken) {
        sharedPreferences.edit().putString(ClientConnectConstants.FIREBASE_AUTH_TOKEN, firebaseToken).apply();
        ClientConnectApplication.firebaseToken = firebaseToken;
    }

    public String getOpportunityId() {
        if (opportunityId == null) {
            opportunityId = sharedPreferences.getString(ClientConnectConstants.OPPORTUNITY_ID, "");
            return opportunityId;
        } else {
            return opportunityId;
        }
    }

    public void setOpportunityId(String opportunityId) {
        ClientConnectApplication.opportunityId = null;
        sharedPreferences.edit().putString(ClientConnectConstants.OPPORTUNITY_ID, opportunityId).apply();
    }

    public ClientProfile getClientProfile() {
        if (clientProfile == null) {
            String profileData = sharedPreferences.getString(ClientConnectConstants.CLIENT_CONNECT_PROFILE, "");

            if (!TextUtils.isEmpty(profileData)) {
                Gson gson = new Gson();

                ClientProfile clientProfile = gson.fromJson(profileData, ClientProfile.class);

                if (clientProfile != null) {
                    ClientConnectApplication.clientProfile = clientProfile;
                } else {
                    ClientConnectApplication.clientProfile = null;
                }
            } else {
                ClientConnectApplication.clientProfile = null;
            }
            return clientProfile;
        } else {
            return clientProfile;
        }
    }

    public void setClientProfile(ClientProfile clientProfile) {
        sharedPreferences.edit().putString(ClientConnectConstants.CLIENT_CONNECT_PROFILE, new Gson().toJson(clientProfile)).apply();
    }

}