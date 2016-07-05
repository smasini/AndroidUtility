package it.smasini.utility.library.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public abstract class NotificationReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, createNotification(context));
    }

    public abstract Notification createNotification(Context context);

}
