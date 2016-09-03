package it.smasini.utility.library.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import it.smasini.utility.library.R;
import it.smasini.utility.library.SizeUtility;
import it.smasini.utility.library.adapters.AutocompleteBaseAdapter;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public class DelayAutoCompleteTextView extends FrameLayout {

    private CustomAutoCompleteTextView autoCompleteTextView;
    private String hint = "";
    private int threshold = 1;

    public DelayAutoCompleteTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DelayAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        if(attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DelayAutoCompleteTextView);
            hint = a.getString(R.styleable.DelayAutoCompleteTextView_hintText);
            threshold = a.getInt(R.styleable.DelayAutoCompleteTextView_threshold, threshold);
            a.recycle();
        }
        autoCompleteTextView = new CustomAutoCompleteTextView(context);
        autoCompleteTextView.setHint(hint);
        autoCompleteTextView.setThreshold(threshold);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        autoCompleteTextView.setLayoutParams(lp);

        this.addView(autoCompleteTextView);

        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0, 0, (int)SizeUtility.convertDpToPixel(8, context), 0);
        lp2.gravity = Gravity.CENTER_VERTICAL|Gravity.END;
        progressBar.setLayoutParams(lp2);
        progressBar.setVisibility(GONE);

        this.addView(progressBar);

        autoCompleteTextView.setLoadingIndicator(progressBar);
    }

    public AutoCompleteTextView getAutoCompleteTextView() {
        return autoCompleteTextView;
    }

    public void setAdapter(AutocompleteBaseAdapter adapter){
        autoCompleteTextView.setAdapter(adapter);
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        autoCompleteTextView.setAutoCompleteDelay(autoCompleteDelay);
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
