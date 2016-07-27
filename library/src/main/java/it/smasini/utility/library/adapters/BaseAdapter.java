package it.smasini.utility.library.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import it.smasini.utility.library.ColorUtility;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    final protected Context mContext;
    final private int layoutId;
    private boolean multipleSelectionEnabled;
    private OnClickHandler<T> mClickHandler;
    private View mEmptyView;
    protected OnSwapData<T> onSwapData;
    private SparseBooleanArray selectedItems;
    private List<T> viewModels = new ArrayList<>();

    public BaseAdapter(Context context, View emptyView, OnClickHandler<T> clickHandler, int layoutId, boolean multipleSelectionEnabled) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        this.mEmptyView = emptyView;
        this.layoutId = layoutId;
        this.multipleSelectionEnabled = multipleSelectionEnabled;
        this.selectedItems = new SparseBooleanArray();
    }

    public BaseAdapter(Context context, View emptyView, OnClickHandler<T> clickHandler, int layoutId){
        this(context, emptyView, clickHandler, layoutId, false);
    }

    public BaseAdapter(Context context, int layoutId){
        this(context, null, null, layoutId, false);
    }

    public BaseAdapter(Context context, View emptyView, int layoutId){
        this(context, emptyView, null, layoutId, false);
    }

    public BaseAdapter(Context context, OnClickHandler<T> clickHandler, int layoutId){
        this(context, null, clickHandler, layoutId, false);
    }

    public BaseAdapter(Context context, int layoutId, boolean multipleSelectionEnabled){
        this(context, null, null, layoutId, multipleSelectionEnabled);
    }

    public BaseAdapter(Context context, View emptyView, int layoutId, boolean multipleSelectionEnabled){
        this(context, emptyView, null, layoutId, multipleSelectionEnabled);
    }

    public BaseAdapter(Context context, OnClickHandler<T> clickHandler, int layoutId, boolean multipleSelectionEnabled){
        this(context, null, clickHandler, layoutId, multipleSelectionEnabled);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return getViewHolder(view);
        }else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
        T viewModel = viewModels.get(position);
        holder.setIndex(position);
        onBindCustomViewHolder(holder, position, viewModel);
        if(multipleSelectionEnabled){
            if(isPositionSelected(position))
                setSelectedStyle(holder);
            else
                setDeselectedStyle(holder);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setOnClickHandler(OnClickHandler<T> onClickHandler) {
        this.mClickHandler = onClickHandler;
    }

    public void setOnSwapData(OnSwapData<T> onSwapData) {
        this.onSwapData = onSwapData;
    }

    public abstract void onBindCustomViewHolder(ViewHolder viewHolder, int position, T viewModel);
    public abstract ViewHolder getViewHolder(View view);

    //da sovrascrivere se si vuole utilizzare questa funzionalità
    public boolean isEquals(T model, T modelToCompare){
        return false;
    }

    public int getPosition(T model){
        for(int i = 0; i < viewModels.size();i++){
            T tmp = viewModels.get(i);
            if(isEquals(tmp, model)){
                return i;
            }
        }
        return -1;
    }

    protected void setSelectedStyle(BaseAdapter.ViewHolder holder){
        holder.changeBackground(ColorUtility.getThemeAccentColor(mContext));
    }

    protected void setDeselectedStyle(BaseAdapter.ViewHolder holder){
        holder.changeBackground(Color.TRANSPARENT);
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemsCount() {
        return selectedItems.size();
    }

    public List<T> getSelectedItems() {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            int pos = selectedItems.keyAt(i);
            items.add(viewModels.get(pos));
        }
        return items;
    }

    public boolean isPositionSelected(int position){
        return selectedItems.get(position, false);
    }

    public boolean isOneItemSelected(){
        return getSelectedItemsCount() > 0;
    }

    @Override
    public int getItemCount() {
        if(viewModels==null)
            return 0;
        return viewModels.size();
    }

    public void swapData(List<T> newList){
        viewModels = newList;
        notifyDataSetChanged();
        if(mEmptyView!=null)
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if(onSwapData!=null)
            onSwapData.onSwap(newList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public int index;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            if(multipleSelectionEnabled){
                view.setOnLongClickListener(this);
            }
        }

        protected void changeBackground(int color){
            this.itemView.setBackgroundColor(color);
        }

        @Override
        public void onClick(View v) {
            if(isOneItemSelected()){
                toggleSelection(index);
            }
            else if(mClickHandler!=null) {
                int adapterPosition = getAdapterPosition();
                T viewModel = viewModels.get(adapterPosition);
                mClickHandler.onClick(viewModel);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            toggleSelection(index);
            return false;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
    public interface OnClickHandler<T> {
        void onClick(T model);
    }

    public interface OnSwapData<T> {
        void onSwap(List<T> list);
    }
}
