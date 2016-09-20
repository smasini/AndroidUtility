package it.smasini.utility.library;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class OtherUtility {

    public static boolean isTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape(Context context){
        return context.getResources().getBoolean(R.bool.is_landscape);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getPathData(Context context, String packageName){
        try {
            ApplicationInfo ai =  context.getPackageManager().getApplicationInfo(packageName, 0);
            return ai.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setOrientationPortraitPhoneLandscapeTablet(Activity activity){
        if(isTablet(activity)){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
