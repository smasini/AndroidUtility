package it.smasini.utility.library;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

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
}
