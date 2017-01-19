package it.smasini.utility.library;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import it.smasini.utility.library.ui.DialogHelper;

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

    public static void openStorePage(Activity activity, String appPackageName){
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
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

    public static void intentFile(Activity activity, String path){
        String extension = FileUtility.getExtension(path);
        String mime = FileUtility.getMimeTypeFromExtension(extension);
        try {
            intentFile(activity, path, mime);
        }catch (Exception e){
            DialogHelper.alert(activity, activity.getString(R.string.open_file_error_title), activity.getString(R.string.open_file_error_msg) + extension);
        }
    }

    public static void intentFile(Activity activity, String path, String mime){
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    public static void intentChoiceFile(Activity activity, int requestCode) {
        intentChoiceFile(activity, "*/*", requestCode);
    }

    public static void intentChoiceFile(Activity activity, String minmeType, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(minmeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", minmeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (activity.getPackageManager().resolveActivity(sIntent, 0) != null){
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
        }
        else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }
        try {
            activity.startActivityForResult(chooserIntent, requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFilePathFromResult(Intent data){
        if(data!=null){
            return data.getDataString();
        }
        return "";
    }

    public static void openDBInSqliteDubugger(Activity activity,String databasepath, String directory){
        openDBInSqliteDubugger(activity, databasepath, directory, null);
    }

    public static void openDBInSqliteDubugger(final Activity activity, String databasepath, String directory, String sqlCommand){
        String filename = "copy.db";
        if(FileUtility.copyFile(databasepath, directory, filename)){
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setData(Uri.parse("sqlite:" + directory + "/" + filename));
            if(sqlCommand!=null && !sqlCommand.equals("")){
                intent.putExtra("sql", sqlCommand);
            }
            try {
                activity.startActivity(intent);
            }
            catch (ActivityNotFoundException e){
                DialogHelper.warning(activity, "SQLite Debugger non trovata", "Sembra che SQLite Debugger non sia installata, vuoi accedere alla scheda dello store?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openStorePage(activity, "oliver.ehrenmueller.dbadmin");
                    }
                });
            }
        }else{
            Log.e("Error", "Error");
        }
    }

}
