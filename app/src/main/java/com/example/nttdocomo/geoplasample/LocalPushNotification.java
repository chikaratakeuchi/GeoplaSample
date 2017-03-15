package com.example.nttdocomo.geoplasample;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.nttdocomo.geoplasample.ui.GeopopMessageActivity;
import com.example.nttdocomo.geoplasample.ui.GeopopPopupActivity;
import com.geopla.geopop.sdk.GeopopUtil;
import com.geopla.geopop.sdk.callback.NotificationCallback;
import com.geopla.geopop.sdk.model.UserNotification;
import com.geopla.geopop.sdk.ui.BaseGeopopMessageActivity;

public class LocalPushNotification implements NotificationCallback {

    @Override
    public void send(Context context, UserNotification userNotification) {
        if(GeopopUtil.getPopinfoPopupEnabled(context)) {
            showPopup(context, userNotification);
        }

        showNotification(context, userNotification);
    }

    public static void showPopup(Context context, UserNotification userNotification) {
        GeopopPopupActivity.startActivity(context, userNotification);
    }

    public static void showNotification(Context context, UserNotification userNotification) {
        Intent intent = BaseGeopopMessageActivity.createInent(context, GeopopMessageActivity.class, userNotification);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(context.getApplicationInfo().icon);

        builder.setContentTitle(userNotification.getTitle());
        builder.setContentText(userNotification.getMessage());
        builder.setWhen(System.currentTimeMillis());

        if(GeopopUtil.getPopinfoSoundEnabled(context)) {
            builder.setSound(GeopopUtil.getPopinfoSound(context));
        }

        if(GeopopUtil.getPopinfoVibrationEnabled(context)) {
            builder.setVibrate(GeopopUtil.getPopinfoVibration(context));
        }

        builder.setTicker(userNotification.getTitle());
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify((int)userNotification.getId(), builder.build());
    }
}
