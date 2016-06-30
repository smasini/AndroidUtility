package it.smasini.utility.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simone Masini on 30/06/2016
 */
public class DateUtility {

    private static Date parseStringDate(String date, String format){
        String data = date.replace("T", " ");
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = formatter.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date convertStringToDate(String data){
        Date date = parseStringDate(data, "yyyy/MM/dd");
        if(date == null){
            date = parseStringDate(data, "yyyy/MM/dd HH:mm:ss");

        }
        if(date == null){
            date = parseStringDate(data, "yyyy-MM-dd");
        }
        if(date == null){
            date = parseStringDate(data, "yyyy-MM-dd HH:mm:ss");
        }

        return date;
    }

    public static String formatStringDate(String date){
        Date d = convertStringToDate(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(d);
    }

    public static String convertDateToQueryString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(date);
    }
}
