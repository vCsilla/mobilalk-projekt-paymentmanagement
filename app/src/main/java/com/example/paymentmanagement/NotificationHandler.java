package com.example.paymentmanagement;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "paymentmanagement_noti_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mNotificationManager;
    private Context mContext;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }
    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }else{
            NotificationChannel channel = new NotificationChannel(
                    "paymentmanagement_noti_channel", "Payment Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {500, 500});
            channel.setDescription("Noti from Payment Management");
            this.mNotificationManager.createNotificationChannel(channel);
        }

    }
    public void send(String message){
        Intent intent = new Intent(mContext, PaymentManagingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Payment Manager")
                .setContentText(message)
                .setSmallIcon(R.drawable.expense)
                .setContentIntent(pendingIntent);
        this.mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
