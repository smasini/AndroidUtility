package it.smasini.utility.library.graphics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
    }
}
