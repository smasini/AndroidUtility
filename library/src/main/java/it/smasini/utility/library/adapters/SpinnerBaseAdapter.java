package it.smasini.utility.library.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public class SpinnerBaseAdapter extends ArrayAdapter<SpinnerItem> {

    private int itemResource, dropdownResource;
    private Activity context;
    private List<SpinnerItem> items;
    private CustomSpinnerBinding customSpinnerBinding;

    public SpinnerBaseAdapter(Activity context, int itemResource, int dropdownResource) {
        super(context, itemResource);
        this.context = context;
        this.itemResource = itemResource;
        this.dropdownResource = dropdownResource;
        this.items = new ArrayList<>();
        setDropDownViewResource(dropdownResource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(itemResource, parent, false);

        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        SpinnerItem item = getItem(position);
        if(item!=null)
        textView.setText(item.getLabel());

        if(customSpinnerBinding!=null){
            row = customSpinnerBinding.bindView(row, item);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(dropdownResource, parent, false);

        CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(android.R.id.text1);
        SpinnerItem item = getItem(position);
        if(item!=null)
        checkedTextView.setText(item.getLabel());
        if(customSpinnerBinding!=null){
            row = customSpinnerBinding.bindDropdownView(row, item);
        }
        return row;
    }

    @Override
    public void addAll(Collection<? extends SpinnerItem> collection) {
        super.addAll(collection);
        items.clear();
        for (SpinnerItem item : collection) {
            items.add(item);
        }
    }

    @Override
    public void add(SpinnerItem object) {
        super.add(object);
        items.add(object);
    }

    @Override
    public void clear() {
        super.clear();
        items.clear();
    }

    public void setDropdownResource(int dropdownResource) {
        this.dropdownResource = dropdownResource;
    }

    public void setItemResource(int itemResource) {
        this.itemResource = itemResource;
    }

    public void setCustomSpinnerBinding(CustomSpinnerBinding customSpinnerBinding) {
        this.customSpinnerBinding = customSpinnerBinding;
    }

    public List<SpinnerItem> getItems() {
        return items;
    }

    public interface CustomSpinnerBinding{
        View bindView(View rootView, SpinnerItem item);
        View bindDropdownView(View rootView, SpinnerItem item);
    }
}