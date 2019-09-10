package com.gzeinnumer.notificationfcm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gzeinnumer.notificationfcm.network.RetroServer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";

    private Button btn;
    private TextView tv;
    private Context context;

    private onItemClick click;

    public interface onItemClick {
        void onItemClick();
    }

    public MyFirebaseMessagingService() {

    }


    public MyFirebaseMessagingService(Context context, onItemClick onItemClick) {
        this.context=context;
        this.click=onItemClick;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

//        sendNotification(remoteMessage.getFrom());
        sendNotification(remoteMessage.getData().get("message"));

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                scheduleJob();
            } else {
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }



    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    @SuppressLint("LongLogTag")
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        RetroServer.getInstance().regisToken(token).enqueue(new Callback<ResponseInsertToken>() {
            @Override
            public void onResponse(Call<ResponseInsertToken> call, Response<ResponseInsertToken> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MyFirebaseMessagingService.this, response.body().getResultInsert(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyFirebaseMessagingService.this, response.body().getResultInsert(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsertToken> call, Throwable t) {
                Toast.makeText(MyFirebaseMessagingService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendNotification(String messageBody) {
        MainActivity.sentCommandToMain();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.fcm_message))
//                        .setContentText(messageBody)
                        .setContentText("Pasien dipanggil")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
//        click.onItemClick();

    }
}
