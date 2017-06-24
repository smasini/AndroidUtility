package it.smasini.utility.library.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class NotificationHelper {

    /**
     * enable notification every x milliseconds
     *
     * @param context context
     * @param delay timeout in milliseconds
     */
    public static void enableNotification(Context context, int delay){
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + 1000*60*60*2; //first check in 2 hours
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, delay, pendingIntent);
    }

    /**
     * enable notification every 24 hours
     *
     * @param context context
     */
    public static void enableNotification(Context context){
        int delay = 1000 * 60 * 60 * 24; //after the first check, check every 24 hours
        enableNotification(context, delay);
    }


    /**
     * disable notification
     * @param context context
     */
    public static void disableNotification(Context context){
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static Notification createNotification(Context context, String title, String message, int smallIcon, boolean vibrate, int color){
        return createNotification(context, title, message, null, 0, smallIcon, vibrate, color, 0);
    }

    public static Notification createNotification(Context context, String title, String message, int smallIcon, Intent intentClick, boolean vibrate, int color){
        return createNotification(context, title, message, intentClick, 0, smallIcon, vibrate, color, 0);
    }

    public static Notification createNotification(Context context, String title, String message, int smallIcon, Intent intentClick, boolean vibrate, int color, int requestCode){
        return createNotification(context, title, message, intentClick, 0, smallIcon, vibrate, color, requestCode);
    }

    public static Notification createNotification(Context context, String title, String message, int largeIcon, int smallIcon, boolean vibrate, int color){
        return createNotification(context, title, message, null, largeIcon, smallIcon, vibrate, color, 0);
    }

    public static Notification createNotification(Context context, String title, String message, Intent intentClick, int largeIcon, int smallIcon, boolean vibrate, int color){
        return createNotification(context, title, message, intentClick, largeIcon, smallIcon, vibrate, color, 0);
    }

    public static Notification createNotification(Context context, String title, String message, Intent intentClick, int largeIcon, int smallIcon, boolean vibrate, int color, int requestCode) {
        PendingIntent pendingIntent = null;
        if(intentClick!=null){
            pendingIntent = PendingIntent.getActivity(context, requestCode, intentClick, PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                //.setStyle(new NotificationCompat.BigTextStyle(notificationBuilder).bigText(message))
                .setSmallIcon(smallIcon)
                .setColor(color)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        if(largeIcon!=0){
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), largeIcon);
            notificationBuilder.setLargeIcon(icon);
        }

        if(pendingIntent!=null){
            notificationBuilder.setContentIntent(pendingIntent);
        }
        if(vibrate){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
        if(color != Color.TRANSPARENT){
            notificationBuilder.setLights(color, 1000, 1000);
        }

        return notificationBuilder.build();
    }

    public static void showNotification(Context context, Notification notification){
        showNotification(context, notification, 0);
    }

    public static void showNotification(Context context, Notification notification, int id){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public static void removeNotification(Context context, int id){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
