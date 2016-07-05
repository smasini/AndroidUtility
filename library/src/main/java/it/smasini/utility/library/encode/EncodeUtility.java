package it.smasini.utility.library.encode;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simone Masini on 30/06/2016
 */
public class EncodeUtility {

    /*
         Decode url
     */

    public static String decode(JSONObject obj, String key){
        String text = obj.optString(key);
        return decode(text);
    }

    private static Date decodeDate(String date, String format){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date decodeDate(JSONObject obj, String key){
        String text = obj.optString(key);
        String dateText = decode(text);
        Date date = decodeDate(dateText, "yyyy-MM-dd");
        if(date == null){
            date = decodeDate(dateText, "yyyy-MM-dd HH:mm");
        }
        if(date == null){
            date = decodeDate(dateText, "yyyy/MM/dd HH:mm");
        }
        if(date == null){
            date = decodeDate(dateText, "yyyy/MM/dd");
        }
        return date;
    }

    public static String decode(String text){
        if(text == null){
            return "";
        }
        try {
            text = URLDecoder.decode(text, "cp1252");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }
}
