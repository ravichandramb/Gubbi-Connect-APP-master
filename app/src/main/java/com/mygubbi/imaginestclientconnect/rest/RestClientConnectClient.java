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
 * @since 3/29/2018
 */
public class RestClientConnectClient {

    private static RestClientConnectClient restGubbiConnectClient;
    private ApiService apiService;

    private RestClientConnectClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient builder = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new ClientConnectRequestInterceptor("game", "Mygubbi"))
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ClientConnectConstants.getClientConnectBaseUrlUat())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static RestClientConnectClient getInstance() {
        if (restGubbiConnectClient == null) {
            restGubbiConnectClient = new RestClientConnectClient();
        }
        return restGubbiConnectClient;
    }

    public ApiService getApiService() {
        return apiService;
    }

}