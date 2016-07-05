package it.smasini.utility.library;

import android.content.Context;
import android.os.Build;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class DrawableUtility {
    public static int getColor(int res, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(res);
        }else{
            return context.getResources().getColor(res);
        }
    }
}
