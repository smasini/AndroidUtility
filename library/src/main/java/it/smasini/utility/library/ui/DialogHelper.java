package it.smasini.utility.library.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import it.smasini.utility.library.R;

/**
 * Created by Simone Masini on 30/06/2016
 */
public class DialogHelper {

    private static ProgressDialog progressDialog;
    private static ProgressDialog progressDialogDeterminate;

    public static void showDialog(Context context){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context, R.style.DialogStyle);
        }
        progressDialog.setMessage(context.getString(R.string.label_progress_dialog_task));

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showProgress(Context context, int max, final Callback callback){
        if(progressDialogDeterminate == null){
            progressDialogDeterminate = new ProgressDialog(context, R.style.DialogStyle);
        }
        progressDialogDeterminate.setMessage(context.getString(R.string.label_progress_download_task));

        progressDialogDeterminate.setIndeterminate(false);
        progressDialogDeterminate.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialogDeterminate.setMax(max);
        progressDialogDeterminate.setCancelable(false);
        progressDialogDeterminate.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.label_annulla), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callback!=null){
                    callback.onCancel();
                }
            }
        });
        progressDialogDeterminate.show();
    }

    public static void setProgress(int progress){
        if(progressDialogDeterminate!=null){
            progressDialogDeterminate.setProgress(progress);
        }
    }

    public static void hideProgress(){
        if(progressDialogDeterminate != null && progressDialogDeterminate.isShowing()){
            progressDialogDeterminate.dismiss();
            progressDialogDeterminate = null;
        }
    }

    public interface Callback{
        void onCancel();
    }

    public static void alert(Activity activity, String title, String message){
        alert(activity,title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void alert(Activity activity, String title, String message, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogStyle);

        if(title != null)
            builder.setTitle(title);
        if(message!= null)
            builder.setMessage(message);

        builder.setNegativeButton(activity.getString(R.string.label_ok), clickListener);
        builder.show();
    }

    public static void warning(Activity activity, String title, String message, DialogInterface.OnClickListener positiveButtonClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogStyle);

        if(title != null)
            builder.setTitle(title);
        if(message!= null)
            builder.setMessage(message);
        builder.setPositiveButton(activity.getString(R.string.label_ok), positiveButtonClick);
        builder.setNegativeButton(activity.getString(R.string.label_annulla), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
