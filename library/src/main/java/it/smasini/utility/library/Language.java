package it.smasini.utility.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.Locale;

/**
 *Created by Simone Masini on 30/06/2016
 */
public class Language {

    private static final String LANGUAGE = "language_code";
    private static final String TAG = "Language";
    public static String currentLanguage = "";

    public static void loadLocale(Context context){
        changeAndSaveLocale(context, restoreLocale(context));
    }

    public static void changeLocale(Context context, String lang){
        if(lang != null && !lang.equals("")) {
            Locale myLocale = new Locale(lang);
            Locale.setDefault(myLocale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = myLocale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            currentLanguage = lang;
            Log.d(TAG, "Language changed: " + lang);
        }
    }

    public static void changeAndSaveLocale(Context context, String lang) {
        changeLocale(context, lang);
        saveLocale(context, lang);
    }

    public static void saveLocale(Context context, String lang){
        if(lang != null && !lang.equals("")) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit()
                    .putString(LANGUAGE, lang)
                    .apply();
            Log.d(TAG, "Language saved: " + lang);
        }
    }

    public static String restoreLocale(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String language = prefs.getString(LANGUAGE, context.getResources().getConfiguration().locale.getLanguage());
        currentLanguage = language;
        Log.d(TAG, "Language loaded: " + language);
        return language;
    }

    public static void setDefaultLocale(Context context){
        String lang = context.getResources().getConfiguration().locale.getLanguage();
        changeLocale(context, lang);
        saveLocale(context, lang);
    }

}
