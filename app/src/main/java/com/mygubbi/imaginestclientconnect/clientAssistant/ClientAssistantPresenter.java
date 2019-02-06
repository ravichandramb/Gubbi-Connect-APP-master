package com.mygubbi.imaginestclientconnect.clientAssistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.AccessToken;
import com.mygubbi.imaginestclientconnect.models.ChatBotMessage;
import com.mygubbi.imaginestclientconnect.models.NotificationList;
import com.mygubbi.imaginestclientconnect.rest.RestGubbiClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@SuppressWarnings("Convert2MethodRef")
class ClientAssistantPresenter {

    private static final String TAG = "AssistantPresenter";

    private ClientAssistantView assistantView;

    ClientAssistantPresenter(ClientAssistantView assistantView) {
        this.assistantView = assistantView;
    }

    public void getOAuthToken(String firebaseToken) {
        RestGubbiClient.getInstance(false).getApiService().getOAuthToken(ClientConnectConstants.STR_BASIC_TOKEN,
                firebaseToken, "welcome", ClientConnectConstants.STR_GRANT_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(accessToken -> accessToken != null)
                .subscribe(new Observer<AccessToken>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
                        Log.d(TAG, "onNext: ");
                        ClientConnectApplication.getInstance().setAccessToken(accessToken);
                        assistantView.loginResult(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        assistantView.loginResult(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void getUserNotifications(String userId, int pageNumber) {
        RestGubbiClient.getInstance(true).getApiService().getUserNotifications(userId, pageNumber, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(notificationList -> notificationList != null)
                .subscribe(new Observer<NotificationList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NotificationList notificationList) {
                        Log.d(TAG, "onNext: " + notificationList.toString());
                        assistantView.showNotifications(notificationList.getUserNotificationList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                getToken();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void queryBot(String query) {
        JsonObject queryObject = new JsonObject();
        queryObject.addProperty("text", query);

        RestGubbiClient.getInstance(true).getApiService().queryBot(queryObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(chatBotMessage -> chatBotMessage != null)
                .subscribe(new Observer<ChatBotMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ChatBotMessage chatBotMessage) {
                        Log.d(TAG, "onNext: ");
                        ChatBotMessage botMessage = new ChatBotMessage(chatBotMessage.getMessage(), false);
                        assistantView.showBotResponse(botMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            if (((HttpException) e).code() == 401) {
                                getToken();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getToken() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUser.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            SharedPreferences sharedPreferences = ClientConnectApplication.getInstance()
                                    .getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString(ClientConnectConstants.FIREBASE_AUTH_TOKEN, idToken).apply();
                            getOAuthToken(idToken);
                        } else {
                            Log.e(TAG, "getToken: Error ", task.getException());
                        }
                    });
        } else {
            Log.e(TAG, "getToken: mUser is null");
        }
    }

}