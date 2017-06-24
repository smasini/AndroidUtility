package it.smasini.utility.library.graphics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import it.smasini.utility.library.R;

/**
 * Created by Simone Masini on 27/07/2016.
 */
public class ColorUtility {


    public static int getThemeAccentColor(Context context) {
        return getThemeColor(context, R.attr.colorAccent);
    }

    public static int getThemePrimaryColor(Context context) {
        return getThemeColor(context, R.attr.colorPrimary);
    }

    public static int getThemePrimaryDarkColor(Context context) {
        return getThemeColor(context, R.attr.colorPrimaryDark);
    }

    private static int getThemeColor(Context context, int res) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(res, value, true);
        return value.data;
    }

    public static int getColor(int res, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(res);
        }else{
            return context.getResources().getColor(res);
        }
    }

    public static ColorStateList getColorSelector(int res, Context context){
        context.getResources().getColorStateList(res);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColorStateList(res);
        }else{
            return context.getResources().getColorStateList(res);
        }
    }

    public static void setStatusBarColor(Activity activity, int color, boolean flag){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if(flag)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public static void setStatusBarColor(Activity activity, int color){
        setStatusBarColor(activity,color, false);
    }

}
