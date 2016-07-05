package it.smasini.utility.library.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public class FontHelper {

    private static FontHelper instance;

    public static FontHelper getInstance(Context context){
        if(instance == null){
            instance = new FontHelper(context);
        }
        return instance;
    }

    private Typeface bold, regular, thin;

    private FontHelper(Context context){
        bold = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-B.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-R.ttf");
        thin = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-L.ttf");
    }

    public void setTypeface(TextView textView, FontStyle style){
        textView.setTypeface(getTypeface(style));
    }

    public void setTypeface(Button button, FontStyle style){
        button.setTypeface(getTypeface(style));
    }

    public Typeface getTypeface(FontStyle style){
        switch (style){
            case BOLD:
                return bold;
            case THIN:
                return thin;
            case REGULAR:
            default:
                return regular;
        }
    }
}
