package com.example.mobileprogramming_assignment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint({"MissingPermission", "UnsafeProtectedBroadcastReceiver"})
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent repeating_Intent = new Intent(context, MainActivity.class);
        repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeating_Intent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                .setContentIntent(pendingIntent)
                .setContentTitle("Dementia")
                .setContentText("Hey! Back to study on Dementia!")
                .setSmallIcon(R.drawable.app_logo)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }


}

