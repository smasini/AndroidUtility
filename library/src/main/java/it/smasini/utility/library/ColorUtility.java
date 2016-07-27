package it.smasini.utility.library;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

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
}
