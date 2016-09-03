package it.smasini.utility.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public abstract class AutocompleteBaseAdapter<T> extends android.widget.BaseAdapter implements Filterable {

    protected static final int MAX_RESULTS = 10;
    protected Context mContext;
    protected List<T> resultList = new ArrayList<>();
    protected int resLayout;

    public AutocompleteBaseAdapter(Context mContext, int layout) {
        this.mContext = mContext;
        this.resLayout = layout;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public T getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resLayout, parent, false);
        }
        T model = getItem(position);
        convertView = bindView(convertView, model);
        return convertView;
    }

    public abstract List<T> findResults(Context context, String text);
    public abstract View bindView(View convertView, T model);

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<T> result = findResults(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = result;
                    filterResults.count = result.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<T>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }
}
