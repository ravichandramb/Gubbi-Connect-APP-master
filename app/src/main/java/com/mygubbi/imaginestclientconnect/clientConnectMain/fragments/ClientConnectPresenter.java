package com.mygubbi.imaginestclientconnect.clientConnectMain.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;
import com.mygubbi.imaginestclientconnect.rest.RestClientConnectClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Hemanth
 * @since 4/6/2018
 */
@SuppressLint("LogNotTimber")
public class ClientConnectPresenter {

    private static final String TAG = "ClientConnectPresenter";

    private Context context;
    private ClientConnectView clientConnectView;

    ClientConnectPresenter(Context context, ClientConnectView clientConnectView) {
        this.context = context;
        this.clientConnectView = clientConnectView;
    }

    public void getClientProfile() {
        clientConnectView.showProgress("Loading...");

        // CLIENT_CONNECT_PROFILE
        SharedPreferences sharedPreferences = context.getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, Context.MODE_PRIVATE);
        String profileData = sharedPreferences.getString(ClientConnectConstants.CLIENT_CONNECT_PROFILE, "");

        if (!TextUtils.isEmpty(profileData)) {
            Gson gson = new Gson();

            ClientProfile clientProfile = gson.fromJson(profileData, ClientProfile.class);

            if (clientProfile != null) {
                clientConnectView.showProfileData(clientProfile);
            } else {
                fetchProfileDataFromServer();
            }
        } else {
            fetchProfileDataFromServer();
        }
    }

    private void fetchProfileDataFromServer() {
        RestClientConnectClient.getInstance().getApiService()
                .getClientProfile(ClientConnectApplication.getInstance().getOpportunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ClientProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(ClientProfile clientProfile) {
                        Log.d(TAG, "onNext: ");

                        SharedPreferences preferences = context.getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, Context.MODE_PRIVATE);
                        preferences.edit().putString(ClientConnectConstants.CLIENT_CONNECT_PROFILE,
                                new Gson().toJson(clientProfile)).apply();

                        clientConnectView.showProfileData(clientProfile);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                        clientConnectView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

}