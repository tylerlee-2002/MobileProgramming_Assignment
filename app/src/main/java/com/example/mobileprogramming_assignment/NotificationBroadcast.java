package com.example.mobileprogramming_assignment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent repeating_Intent = new Intent(context, MainActivity.class);
        repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeating_Intent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                .setContentIntent(pendingIntent)
                .setContentTitle("PASTICCINO")
                .setContentText("This is a daily notification")
                .setSmallIcon(R.drawable.app_logo)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }


}

