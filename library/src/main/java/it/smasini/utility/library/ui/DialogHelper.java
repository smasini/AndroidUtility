package it.smasini.utility.library.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.List;

import it.smasini.utility.library.R;
import it.smasini.utility.library.SizeUtility;
import it.smasini.utility.library.adapters.SpinnerItem;

/**
 * Created by Simone Masini on 30/06/2016
 */
public class DialogHelper {

    private static ProgressDialog progressDialog;
    private static ProgressDialog progressDialogDeterminate;
    public static int alertDialogStyle = -1;
    public static int dialogStyle = -1;

    public static void showDialog(Context context){
        if(progressDialog == null){
            if(dialogStyle==-1){
                progressDialog = new ProgressDialog(context);
            }else{
                progressDialog = new ProgressDialog(context, dialogStyle);
            }
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
            if(dialogStyle==-1){
                progressDialogDeterminate = new ProgressDialog(context);
            }else{
                progressDialogDeterminate = new ProgressDialog(context, dialogStyle);
            }
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

    public interface EditTextHandler{
        void onEditTextCreated(EditText editText);
    }

    public interface SpinnerHandler{
        void onSpinnerCreated(CustomSpinner spinner);
        List<SpinnerItem> getItems();
        void onPositiveButtonClick(String selectedValue);
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
        AlertDialog.Builder builder;
        if(alertDialogStyle == -1){
            builder = new AlertDialog.Builder(activity);
        }else {
            builder = new AlertDialog.Builder(activity, alertDialogStyle);
        }

        if(title != null)
            builder.setTitle(title);
        if(message!= null)
            builder.setMessage(message);

        builder.setNegativeButton(activity.getString(R.string.label_ok), clickListener);
        builder.show();
    }

    public static void warning(Activity activity, String title, String message, DialogInterface.OnClickListener positiveButtonClick){
        AlertDialog.Builder builder;
        if(alertDialogStyle == -1){
            builder = new AlertDialog.Builder(activity);
        }else {
            builder = new AlertDialog.Builder(activity, alertDialogStyle);
        }

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

    public static void alertInputText(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonClick){
        alertInputText(context, title, message, positiveButtonClick, null);
    }

    public static void alertInputText(Context context, String title, String message, DialogInterface.OnClickListener positiveButtonClick, EditTextHandler editTextHandler){
        AlertDialog.Builder builder;
        if(alertDialogStyle == -1){
            builder = new AlertDialog.Builder(context);
        }else {
            builder = new AlertDialog.Builder(context, alertDialogStyle);
        }
        if(title!=null)
            builder.setTitle(title);
        if(message!=null)
            builder.setMessage(message);

        EditText input = new EditText(context);
        builder.setView(input);
        if(editTextHandler!=null){
            editTextHandler.onEditTextCreated(input);
        }
        builder.setPositiveButton(context.getString(R.string.label_ok),positiveButtonClick);
        builder.setNegativeButton(context.getString(R.string.label_annulla), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();
    }

    public static void alertSpinnerInput(Context context, String title, String message, final SpinnerHandler spinnerHandler){
        AlertDialog.Builder builder;
        if(alertDialogStyle == -1){
            builder = new AlertDialog.Builder(context);
        }else {
            builder = new AlertDialog.Builder(context, alertDialogStyle);
        }
        if(title!=null)
            builder.setTitle(title);
        if(message!=null)
            builder.setMessage(message);

        FrameLayout frameLayout = new FrameLayout(context);

        final CustomSpinner spinner = new CustomSpinner(context);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) SizeUtility.convertDpToPixel(8, context);
        params.setMargins(margin,margin,margin,margin);
        spinner.setLayoutParams(params);
        frameLayout.addView(spinner);
        builder.setView(frameLayout);
        if(spinnerHandler!=null){
            spinnerHandler.onSpinnerCreated(spinner);
            spinner.setItems(spinnerHandler.getItems());
        }
        builder.setPositiveButton(context.getString(R.string.label_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(spinnerHandler!=null){
                    spinnerHandler.onPositiveButtonClick(spinner.getSelectedValue());
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(context.getString(R.string.label_annulla), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();
    }

    public static void warningCustomView(Context context, String title, String message, View customView, DialogInterface.OnClickListener positiveButton){
        AlertDialog.Builder builder;
        if(alertDialogStyle == -1){
            builder = new AlertDialog.Builder(context);
        }else {
            builder = new AlertDialog.Builder(context, alertDialogStyle);
        }
        if(title!=null)
            builder.setTitle(title);
        if(message!=null)
            builder.setMessage(message);

        builder.setView(customView);


        builder.setPositiveButton(context.getString(R.string.label_ok), positiveButton);
        builder.setNegativeButton(context.getString(R.string.label_annulla), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();
    }

}
