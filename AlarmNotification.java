package com.example.beta;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class AlarmNotification extends BroadcastReceiver {

    @Override   // データを受信した
    public void onReceive(Context context, Intent intent) {

        int requestCode = intent.getIntExtra("RequestCode",0);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, requestCode, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "default";
        // app name
        String title = context.getString(R.string.app_name);

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dataFormat =
                new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);
        String cTime = dataFormat.format(currentTime);

        // メッセージ　+ 11:22:331
        String message = "ごみ出しを忘れてませんか" ;


        // Notification　Channel 設定
        NotificationChannel channel =
                new NotificationChannel(channelId, title,
                        NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(message);



        NotificationManager notificationManager =
//                (NotificationManager)context.getSystemService(NotificationManager.class);
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.btn_star)
                        .setContentTitle("My notification")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat
                = NotificationManagerCompat.from(context);

        // 通知
        notificationManagerCompat.notify(R.string.app_name, builder.build());

    }
}