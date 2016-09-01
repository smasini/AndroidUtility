package it.smasini.utility.library.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.smasini.utility.library.R;
import it.smasini.utility.library.graphics.ColorUtility;

/**
 * Created by Simone Masini on 05/07/2016.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>  {

    final protected Context mContext;
    final private int layoutId;
    private boolean multipleSelectionEnabled;
    private OnClickHandler<T> mClickHandler;
    private View mEmptyView;
    protected OnSwapData<T> onSwapData;
    private SparseBooleanArray selectedItems;
    private List<T> viewModels = new ArrayList<>();
    private OnMultipleSelectionEvent<T> multipleSelectionEvent;
    private OnGestureEvent<T> gestureEvent;
    protected View rootViewForSnackbar;
    protected int highlightedColor, defaultColor;

    public BaseAdapter(Context context, View emptyView, OnClickHandler<T> clickHandler, int layoutId, boolean multipleSelectionEnabled) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        this.mEmptyView = emptyView;
        this.layoutId = layoutId;
        this.multipleSelectionEnabled = multipleSelectionEnabled;
        this.selectedItems = new SparseBooleanArray();
        this.highlightedColor = ColorUtility.getThemeAccentColor(mContext);
        this.defaultColor = Color.TRANSPARENT;
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
    public void onBindViewHolder(BaseAdapter<T>.ViewHolder holder, int position) {
        T viewModel = viewModels.get(position);
        holder.setIndex(position);
        onBindCustomViewHolder(holder, position, viewModel);
        if(multipleSelectionEnabled){
            boolean isSelected =isPositionSelected(position);
            if(isSelected)
                setSelectedStyle(holder);
            else
                setDeselectedStyle(holder);
            bindElementSelected(holder, viewModel, position, isSelected);
        }
    }

    public void setMultipleSelectionEvent(OnMultipleSelectionEvent<T> multipleSelectionEvent) {
        this.multipleSelectionEvent = multipleSelectionEvent;
    }

    public void setGestureEvent(OnGestureEvent<T> gestureEvent) {
        this.gestureEvent = gestureEvent;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setOnClickHandler(OnClickHandler<T> onClickHandler) {
        this.mClickHandler = onClickHandler;
    }

    public void setRootViewForSnackbar(View rootViewForSnackbar) {
        this.rootViewForSnackbar = rootViewForSnackbar;
    }

    public void setOnSwapData(OnSwapData<T> onSwapData) {
        this.onSwapData = onSwapData;
    }

    public abstract void onBindCustomViewHolder(ViewHolder viewHolder, int position, T viewModel);
    public abstract ViewHolder getViewHolder(View view);
    protected void bindElementSelected(BaseAdapter<T>.ViewHolder holder, T viewModel, int position, boolean isSelected){
        return;
    }

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

    private void setSelectedStyle(BaseAdapter.ViewHolder holder){
        holder.changeBackground(highlightedColor);
    }

    private void setDeselectedStyle(BaseAdapter.ViewHolder holder){
        holder.changeBackground(defaultColor);
    }

    public void toggleSelection(int pos) {
        boolean started = isOneItemSelected();
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            if(multipleSelectionEvent!=null){
                multipleSelectionEvent.onElementDeselected(viewModels.get(pos), pos);
            }
        }
        else {
            selectedItems.put(pos, true);
            if(multipleSelectionEvent!=null){
                multipleSelectionEvent.onElementSelected(viewModels.get(pos), pos);
            }
        }
        if(!started && isOneItemSelected()){
            if(multipleSelectionEvent!=null){
                multipleSelectionEvent.onStartMultipleSelection();
            }
        }else if(started && !isOneItemSelected()){
            if(multipleSelectionEvent!=null){
                multipleSelectionEvent.onStopMultipleSelection();
            }
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
        if(multipleSelectionEvent!=null){
            multipleSelectionEvent.onStopMultipleSelection();
        }
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

    protected String getCancelButtontext(){
        return mContext.getString(R.string.recyclerview_delete_item_cancel);
    }

    protected String getCancelText(){
        return mContext.getString(R.string.recyclerview_delete_item_msg);
    }

    public void enableGesture(RecyclerView recyclerView, boolean dragMove, boolean swipeDelete){
        if(multipleSelectionEnabled){
            dragMove = false;
        }
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                T vm = viewModels.get(fromPosition);
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(viewModels, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(viewModels, i, i - 1);
                    }
                }
                notifyItemMoved(fromPosition, toPosition);
                if(gestureEvent!=null){
                    gestureEvent.moved(vm, toPosition, fromPosition);
                }
            }

            @Override
            public void onItemDismiss(final int position) {
                final T vm = viewModels.remove(position);
                notifyItemRemoved(position);
                Snackbar snackbar = Snackbar.make(rootViewForSnackbar, getCancelText(), Snackbar.LENGTH_LONG).setAction(getCancelButtontext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModels.add(position, vm);
                        notifyItemInserted(position);
                    }
                });
                snackbar.setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if(event!= Snackbar.Callback.DISMISS_EVENT_ACTION){
                            if(gestureEvent!=null){
                                gestureEvent.deleted(vm, position);
                            }
                        }
                    }
                });
                snackbar.show();
            }
        }, swipeDelete, dragMove);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
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

        public void changeBackground(int color){
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

    //interfacce di callback
    public interface OnClickHandler<T> {
        void onClick(T model);
    }

    public interface OnSwapData<T> {
        void onSwap(List<T> list);
    }

    public interface OnMultipleSelectionEvent<T>{
        void onElementSelected(T viewModel, int position);
        void onElementDeselected(T viewModel, int position);
        void onStartMultipleSelection();
        void onStopMultipleSelection();
    }

    public interface OnGestureEvent<T>{
        void deleted(T viewModel, int position);
        void moved(T viewModel, int newPosition, int oldPosition);
    }

}
