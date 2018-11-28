package it.smasini.utility.library.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import java.util.List;
import it.smasini.utility.library.R;
import it.smasini.utility.library.adapters.SpinnerBaseAdapter;
import it.smasini.utility.library.adapters.SpinnerItem;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public class CustomSpinner extends AppCompatSpinner {

    private SpinnerBaseAdapter adapter;

    public CustomSpinner(Context context) {
        super(context);
        init(null);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
        init(null);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(!isInEditMode()) {
            int dropdownLayout = android.R.layout.simple_spinner_dropdown_item;
            int itemLayout = android.R.layout.simple_spinner_item;
            if (attrs != null) {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
                if (a.hasValue(R.styleable.CustomSpinner_spinner_item_resource)) {
                    itemLayout = a.getResourceId(R.styleable.CustomSpinner_spinner_item_resource, itemLayout);
                }
                if (a.hasValue(R.styleable.CustomSpinner_spinner_dropdown_item_resource)) {
                    dropdownLayout = a.getResourceId(R.styleable.CustomSpinner_spinner_dropdown_item_resource, dropdownLayout);
                }
                a.recycle();
            }
            Activity activity;
            if(getContext() instanceof Activity) {
                activity = (Activity) getContext();
            }else{
                ContextThemeWrapper ctw = (ContextThemeWrapper) getContext();
                activity = (Activity) ctw.getBaseContext();
            }
            adapter = new SpinnerBaseAdapter(activity, itemLayout, dropdownLayout);
            this.setAdapter(adapter);
        }
    }

    public void setItems(List<SpinnerItem> items){
        setItems(items, false);
    }

    public void setItems(List<SpinnerItem> items, boolean clear){
        if(adapter!=null){
            if(clear){
                adapter.clear();
            }
            adapter.addAll(items);
        }
    }

    public void setSpinnerArrowColor(int color){
        getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void setDropdownResource(int dropdownResource) {
        adapter.setDropdownResource(dropdownResource);
    }

    public void setItemResource(int itemResource) {
        adapter.setItemResource(itemResource);
    }

    @Override
    public SpinnerBaseAdapter getAdapter() {
        return adapter;
    }

    public SpinnerItem getSelectedSpinnerItem(){
        SpinnerItem item = (SpinnerItem)getSelectedItem();
        if(item!=null){
            return  item;
        }
        return  null;
    }

    public String getSelectedValue(){
        SpinnerItem item = (SpinnerItem)getSelectedItem();
        if(item!=null){
            return  item.getValue();
        }
        return  "";
    }

    public void setSelectedItem(String value){
        for(int i = 0; i < adapter.getItems().size();i++){
            SpinnerItem item = adapter.getItem(i);
            if(item.getValue().equals(value)){
                setSelection(i);
                return;
            }
        }
        setSelection(-1);
    }

    public void setCustomSpinnerBinding(SpinnerBaseAdapter.CustomSpinnerBinding customSpinnerBinding) {
        if(adapter!=null){
            adapter.setCustomSpinnerBinding(customSpinnerBinding);
        }
    }

}