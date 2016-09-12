package it.smasini.utility.library.graphics;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Simone on 12/09/16.
 */
public class DrawableUtility {

    public static Drawable getDrawable(Context context, int res){
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(res, context.getTheme());
        }else{
            drawable = context.getResources().getDrawable(res);
        }
        return drawable;
    }


}
