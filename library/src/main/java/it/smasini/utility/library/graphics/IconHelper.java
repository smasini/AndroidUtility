package it.smasini.utility.library.graphics;

import android.content.Context;
import android.content.Intent;

/**
 * Created by smasini on 07/08/16.
 */
public class IconHelper {

    ///REQUIRE
    //<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
   // <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />

    public static void installAppIcon(Context context, Intent myAppLauncherIntent, String appName, int resIcon, boolean install) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myAppLauncherIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        intent.putExtra
                (
                        Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext
                                (
                                        context,
                                        resIcon
                                )
                );
        if(install){
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        }else{
            intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        }
        context.sendBroadcast(intent);
    }

    public static void installAppIcon(Context context, Intent myAppLauncherIntent, int appNameRes, int resIcon, boolean install){
        String appName = context.getString(appNameRes);
        installAppIcon(context, myAppLauncherIntent, appName, resIcon, install);
    }

    public static void installAppIcon(Context context, Class<?> c, String appName, int resIcon, boolean install){
        Intent myAppLauncher = new Intent(context, c);
        myAppLauncher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installAppIcon(context, myAppLauncher, appName, resIcon, install);
    }

    public static void installAppIcon(Context context, Class<?> c, int appNameRes, int resIcon, boolean install){
        Intent myAppLauncher = new Intent(context, c);
        myAppLauncher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String appName = context.getString(appNameRes);
        installAppIcon(context, myAppLauncher, appName, resIcon, install);
    }

    public static void installAppIcon(Context context, Class<?> c, int appNameRes, int resIcon){
        Intent myAppLauncher = new Intent(context, c);
        myAppLauncher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String appName = context.getString(appNameRes);
        installAppIcon(context, myAppLauncher, appName, resIcon, true);
    }

    public static void uninstallAppIcon(Context context, Class<?> c, int appNameRes, int resIcon){
        Intent myAppLauncher = new Intent(context, c);
        myAppLauncher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String appName = context.getString(appNameRes);
        installAppIcon(context, myAppLauncher, appName, resIcon, false);
    }


}
