package it.smasini.utility.library;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by Simone Masini on 30/06/2016
 */
public class ActionHelper {

    public static void intentMail(Activity activity, String subject, String text, String email_address){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email_address, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.send_email_chooser)));
    }

    public static void intentShare(Activity activity, String shareText){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        activity.startActivity(shareIntent);
    }

    public static void openUrl(Activity activity, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void navigateFromCurrentPosition(Activity activity, String address, String comune){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(String.format("http://maps.google.com/maps?daddr=%s", String.format("%s %s", address, comune))));
        activity.startActivity(intent);
    }

    public static void navigateFromCurrentPosition(Activity activity, double latitude, double longitude){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
        activity.startActivity(intent);
    }

    public static void intentPDF(Activity activity, String path){
        intentFile(activity, path, "application/pdf");
    }

    public static void intentPDFUrl(Activity activity, String url){
        openUrl(activity, url);
    }

    public static void intentFile(Activity activity, String path, String mime){
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }
}
