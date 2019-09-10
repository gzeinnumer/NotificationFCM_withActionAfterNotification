package com.gzeinnumer.notificationfcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gzeinnumer.notificationfcm.network.RetroServer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MyFirebaseMessagingService.onItemClick{

    private static final String TAG = "MainActivity";

    MyFirebaseMessagingService myFirebaseMessagingService;
    public Button btn ;
    public static TextView tv ;
    public static String str = "null";

    //todo 1
    static MainActivity mainActivity;
    //end todo1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);
        //todo 2
        mainActivity = MainActivity.this;
        //end todo2
        initObject();
    }

    private void initObject() {
        myFirebaseMessagingService = new MyFirebaseMessagingService(getApplicationContext(), (MyFirebaseMessagingService.onItemClick) MainActivity.this);
        iniToken();
    }

    private void iniToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

//                        RetroServer.getInstance().regisToken(token).enqueue(new Callback<ResponseInsertToken>() {
//                            @Override
//                            public void onResponse(Call<ResponseInsertToken> call, Response<ResponseInsertToken> response) {
//                                if (response.isSuccessful()){
//                                    Toast.makeText(MainActivity.this, response.body().getResultInsert(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(MainActivity.this, response.body().getResultInsert(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseInsertToken> call, Throwable t) {
//                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("message")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetroServer.getInstance().sendNotifToPasient("dywszWgKxZI:APA91bEBD5kY1fRz_qOuuppHcEFxuRh9YuPvjvSsKZqknFronM3u4MuCwjD1dSB2rNnCSf_bLeqrgk_3Cmqz8nXE64C55DKOy_ZYWOr-obiKlEXQIRjBx_s5UW4BUuGraR8uVQVfEgLr").enqueue(new Callback<ResponsePushNotif>() {
                    @Override
                    public void onResponse(Call<ResponsePushNotif> call, Response<ResponsePushNotif> response) {
                        Toast.makeText(MainActivity.this, "kirim", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponsePushNotif> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //todo 3
    public static void sentCommandToMain(){
        str = "Kamu Dipanggil";
        Log.d(TAG, "sentCommandToMain: "+str);
        mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                tv.setText(str);
                tv.setVisibility(View.VISIBLE);
            }
        });
    }
    //end todo 3

    @Override
    public void onItemClick() {

    }
}
