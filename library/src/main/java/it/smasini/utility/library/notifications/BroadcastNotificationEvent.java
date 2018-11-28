package it.smasini.utility.library.notifications;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import it.smasini.utility.library.BuildConfig;

/**
 * Created by Simone on 24/01/17.
 */

public class BroadcastNotificationEvent<T> {

    private static BroadcastNotificationEvent instance;
    public static String packageName = BuildConfig.APPLICATION_ID;

    public static BroadcastNotificationEvent getInstance(){
        if(instance==null)
            instance = new BroadcastNotificationEvent();
        return instance;
    }

    private BroadcastNotificationEvent(){
        filter = packageName + ".NOTIFICATION_EVENT";
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lastContextTriggered = context;
                lastIntentTriggered = intent;
                if(broadcastEventListener!=null){
                    eventSendWaiting = false;
                    broadcastEventListener.onEvent(context, intent);
                }
            }
        };
    }

    private Context lastContextTriggered;
    private Intent lastIntentTriggered;
    private Object tag;


    private boolean eventSendWaiting = false;
    private String filter = "";
    private BroadcastReceiver broadcastReceiver;
    private BroadcastEventListener broadcastEventListener;
    private BroadcastEventCreator<T> broadcastEventCreator;

    public void register(Activity activity){
        Log.d("REGISTER_FOR_EVENT", filter);
        activity.registerReceiver(broadcastReceiver, new IntentFilter(filter));
    }

    public void unregister(Activity activity){
        Log.d("UNREGISTER_FOR_EVENT", filter);
        try {
            activity.unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            Log.d("ErrorUnregisterReceiver" ,e.getMessage());
        }
        broadcastEventListener = null;
    }

    public void sendEvent(Context context){
        sendEvent(context, null);
    }

    public void sendEvent(Context context, T obj){
        Intent intent = new Intent();
        if(broadcastEventCreator!=null){
            intent = broadcastEventCreator.onIntentEventCreated(intent, obj);
        }
        intent.setAction(filter);
        eventSendWaiting = true;
        Log.d("EVENT_SEND", filter);
        context.sendBroadcast(intent);
    }

    public boolean isEventSendWaiting() {
        return eventSendWaiting;
    }

    public void receiveEventForced(){
        if(eventSendWaiting && broadcastEventListener!=null){
            eventSendWaiting = false;
            broadcastEventListener.onEvent(lastContextTriggered, lastIntentTriggered);
        }
    }

    public void setBroadcastEventCreator(BroadcastEventCreator<T> broadcastEventCreator) {
        this.broadcastEventCreator = broadcastEventCreator;
    }

    public void setBroadcastEventListener(BroadcastEventListener broadcastEventListener) {
        this.broadcastEventListener = broadcastEventListener;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public interface BroadcastEventListener{
        void onEvent(Context context, Intent intent);
    }
    public interface BroadcastEventCreator<T>{
        Intent onIntentEventCreated(Intent intent, T obj);
    }
}
