package it.smasini.utility.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static int differenceDays(Calendar start, Calendar end) {
        Date startDate = start.getTime();
        Date endDate = end.getTime();
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        return (int)diffDays;
    }

    public static long differenceSeconds(Date startDate, Date endDate){
        long diffInMs = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(diffInMs);
    }

    public static Calendar convertDate(Date date){
        Calendar calendar = Calendar.getInstance();
        if(date == null)
            return calendar;
        calendar.setTime(date);
        return calendar;
    }

    public static Date convertCalendar(Calendar calendar){
        if(calendar == null)
            return new Date();
        return calendar.getTime();
    }

}
