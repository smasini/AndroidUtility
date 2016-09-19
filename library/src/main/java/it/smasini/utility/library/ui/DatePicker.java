package it.smasini.utility.library.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import it.smasini.utility.library.R;

/**
 * Created by Simone on 19/09/16.
 */
public class DatePicker extends EditText implements DatePickerDialog.OnDateSetListener {

    private Date currentDate;
    private String dateFormat = "dd/MM/yyyy";
    private OnDateChangeListener listener;
    private boolean initOnStart = true;
    private boolean darkStyle;


    public DatePicker(Context context) {
        super(context);
        init(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
            if(a.hasValue(R.styleable.DatePicker_dateFormat)) {
                dateFormat = a.getString(R.styleable.DatePicker_dateFormat);
            }
            initOnStart =a.getBoolean(R.styleable.DatePicker_dataPickerInitOnStart, initOnStart);
            darkStyle = a.getBoolean(R.styleable.DatePicker_dataPickerDarkStyle, false);
            a.recycle();
        }

        if(initOnStart){
            Calendar calendar = Calendar.getInstance();
            currentDate = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat(dateFormat, getResources().getConfiguration().locale);
            setText(format.format(currentDate));
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), getDatePickerStyle(), DatePicker.this, year, month, day);
                dpd.setCancelable(true);
                dpd.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getString(R.string.pulisci_data),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                clearDate();
                                if(listener!=null){
                                    listener.onClearDate();
                                }
                            }
                        });
                dpd.show();
            }
        });
    }

    private int getDatePickerStyle(){
        if(darkStyle)
            return getDatePickerDarkStyle();
        return getDatePickerLightStyle();
    }

    private int getDatePickerDarkStyle(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            return android.R.style.Theme_DeviceDefault_Dialog_Alert;
        }else{
            return AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        }
    }

    private int getDatePickerLightStyle(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            return android.R.style.Theme_DeviceDefault_Light_Dialog_Alert;
        }else{
            return AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        }
    }

    public void clearDate(){
        setText("");
        currentDate = null;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, getResources().getConfiguration().locale);
        setText(format.format(currentDate));
        if(listener!=null){
            listener.onChange(currentDate);
        }
    }

    public void setInitOnStart(boolean initOnStart) {
        this.initOnStart = initOnStart;
    }

    public Date getCurrentDate(){
        return currentDate;
    }

    public String getSelectedDate(){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, getResources().getConfiguration().locale);
        return format.format(currentDate);
    }

    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnDateChangeListener{
        void onChange(Date newDate);
        void onClearDate();
    }
}
