package it.smasini.utility.library.notifications;

import android.app.AlarmManager;
import android.app.Notification;
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
     * enable notification every 24 hours
     *
     * @param context
     */
    public static void enableNotification(Context context){
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int delay = 1000 * 60 * 60 * 24; //after the first check, check every 24 hours
        long futureInMillis = SystemClock.elapsedRealtime() + 1000*60*60*2; //first check in 2 hours
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, delay, pendingIntent);
    }

    /**
     * disable notification
     * @param context
     */
    public static void disableNotification(Context context){
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    public static Notification createNotification(Context context, String title, String message, Intent intentClick, int largeIcon, int smallIcon, boolean vibrate, int color) {
        PendingIntent pendingIntent = null;
        if(intentClick!=null){
            pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intentClick, PendingIntent.FLAG_ONE_SHOT);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), largeIcon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setSmallIcon(smallIcon)
                .setLargeIcon(icon)
                //.setStyle(new NotificationCompat.BigTextStyle(notificationBuilder).bigText(message))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
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
}
