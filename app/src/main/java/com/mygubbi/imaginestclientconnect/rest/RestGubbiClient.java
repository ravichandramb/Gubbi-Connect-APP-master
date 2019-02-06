package com.mygubbi.imaginestclientconnect.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Hemanth
 * @since 5/08/2018
 */
public class RestGubbiClient {

    private static RestGubbiClient restGubbiClient;
    private ApiService apiService;

    private RestGubbiClient(boolean isAuthTrue) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(logging)
                .addInterceptor(new GubbiRequestInterceptor())
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS);

        if (isAuthTrue) {
            builder.authenticator(new TokenAuthenticator());
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ClientConnectConstants.getGubbiBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static RestGubbiClient getInstance(boolean isAuthTrue) {
        if (restGubbiClient == null) {
            restGubbiClient = new RestGubbiClient(isAuthTrue);
        }
        return restGubbiClient;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
