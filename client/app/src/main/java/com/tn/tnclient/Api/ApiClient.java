package com.tn.tnclient.Api;

/**
 * Created by Suhail on 8/7/2017.
 */

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "http://192.168.1.3:8088/TNServer/"; //put base url here
    public static final String FURTHER_REQUEST = "http://192.168.1.3:8088/TNServer/user/users";
    public static final String USER_SIZE = "http://192.168.1.3:8088/TNServer/user/size";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofit.create(ApiInterface.class);
        }
        return retrofit;
    }
}
