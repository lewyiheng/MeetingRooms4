package com.example.meetingrooms4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    int reqCode = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String room = intent.getStringExtra("room");
        String startTime = intent.getStringExtra("startTime");
        String user_id = Integer.toString(intent.getIntExtra("userID",0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is for default notification");
            nm.createNotificationChannel(channel);
        }

        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra("userID",user_id);
        i.putExtra("startTime",startTime);
        i.putExtra("room",room);
        PendingIntent pIntent = PendingIntent.getActivity(context, reqCode, i, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setContentTitle(room + " at " + startTime);
        builder.setContentText("You have an incoming reservation");
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        //builder.addAction(R.drawable.ic_magnify, "No", pIntent);
        Notification n = builder.build();
        nm.notify(reqCode, n);
    }
}
