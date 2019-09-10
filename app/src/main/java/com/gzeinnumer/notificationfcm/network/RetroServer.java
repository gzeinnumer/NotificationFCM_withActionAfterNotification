package com.gzeinnumer.notificationfcm.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
    public static Retrofit setInit(){
        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.15/fcm_notification/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getInstance(){
        return setInit().create(ApiService.class);
    }
}
