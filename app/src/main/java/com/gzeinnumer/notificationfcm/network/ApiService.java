package com.gzeinnumer.notificationfcm.network;



import com.gzeinnumer.notificationfcm.ResponseInsertToken;
import com.gzeinnumer.notificationfcm.ResponsePushNotif;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseInsertToken> regisToken(@Field("Token") String Token);

    @FormUrlEncoded
    @POST("push_notification.php")
    Call<ResponsePushNotif> sendNotifToPasient(@Field("Token") String Token);

}
