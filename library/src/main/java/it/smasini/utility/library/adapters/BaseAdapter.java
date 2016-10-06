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

    final protected int VIEW_TYPE_DEFAULT = 1;

    final protected Context mContext;
    final private int layoutId;
    private boolean multipleSelectionEnabled;
    private OnClickHandler<T> mClickHandler;
    private View mEmptyView;
    protected OnSwapData<T> onSwapData;
    private SparseBooleanArray selectedItems;
    protected List<T> viewModels = new ArrayList<>();
    private List<T> originalViewModels;
    private OnMultipleSelectionEvent<T> multipleSelectionEvent;
    private OnGestureEvent<T> gestureEvent;
    protected View rootViewForSnackbar;
    protected int highlightedColor, defaultColor, selectedColor, swipedRightColor, swipedLeftColor, swipedRightTextColor, swipedLeftTextColor;
    private int selectedPosition = -1;
    protected boolean isOnBind = false;

    public BaseAdapter(Context context, View emptyView, OnClickHandler<T> clickHandler, int layoutId, boolean multipleSelectionEnabled) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        this.mEmptyView = emptyView;
        this.layoutId = layoutId;
        this.multipleSelectionEnabled = multipleSelectionEnabled;
        this.selectedItems = new SparseBooleanArray();
        this.highlightedColor = ColorUtility.getThemeAccentColor(mContext);
        this.defaultColor = Color.TRANSPARENT;
        this.selectedColor = Color.TRANSPARENT;
        this.swipedRightColor = defaultColor;
        this.swipedLeftColor = defaultColor;
        this.swipedRightTextColor = defaultColor;
        this.swipedLeftTextColor = defaultColor;
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
    public int getItemViewType(int position) {
        T model = viewModels.get(position);
        return getItemViewType(model);
    }

    public int getItemViewType(T model){
        return VIEW_TYPE_DEFAULT;
    }

    public int getLayoutForType(int viewType){
        return layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutForType(viewType), viewGroup, false);
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
        isOnBind = true;
        onBindCustomViewHolder(holder, position, viewModel);
        if(multipleSelectionEnabled && isOneItemSelected()){
            boolean isSelected = isPositionSelected(position);
            if(isSelected)
                setSelectedStyle(holder);
            else {
                setDeselectedStyle(holder, position);
            }
            bindElementSelected(holder, viewModel, position, isSelected);
        }else{
            setDeselectedStyle(holder, position);
        }
        isOnBind = false;
    }

    public void setMultipleSelectionEvent(OnMultipleSelectionEvent<T> multipleSelectionEvent) {
        this.multipleSelectionEvent = multipleSelectionEvent;
    }

    public void setGestureEvent(OnGestureEvent<T> gestureEvent) {
        this.gestureEvent = gestureEvent;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        if(getItemCount()==0){
            mEmptyView.setVisibility(View.VISIBLE);
        }else{
            mEmptyView.setVisibility(View.GONE);
        }
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

    public T getItem(int position){
        return viewModels.get(position);
    }

    private void setSelectedStyle(BaseAdapter.ViewHolder holder){
        holder.changeBackground(highlightedColor);
    }

    private void setDeselectedStyle(BaseAdapter.ViewHolder holder, int position){
        if(position == selectedPosition){
            holder.changeBackground(selectedColor);
        }else {
            holder.changeBackground(defaultColor);
        }
    }

    public void toggleSelection(int pos){
        toggleSelection(pos, true);
    }

    public void toggleSelection(int pos, boolean notify) {
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
        if(notify && !isOnBind)
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

    public void deleteSelected(){
        List<T> vms = getSelectedItems();
        for (T vm : vms) {
            int pos = getPosition(vm);
            viewModels.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void deleteItem(T vm){
        int position = getPosition(vm);
        deleteItem(position);
    }

    public void deleteItem(int position){
        viewModels.remove(position);
        if(!isOnBind)
            notifyItemRemoved(position);
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
     swapData(newList, false);
    }

    public void swapData(List<T> newList, boolean saveOriginal){
        if(saveOriginal){
            if(originalViewModels==null) {
                originalViewModels = new ArrayList<>(viewModels);
            }
        }
        viewModels = newList;
        notifyDataSetChanged();
        if(mEmptyView!=null)
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if(onSwapData!=null)
            onSwapData.onSwap(newList);
    }

    protected String getCancelButtonText(){
        return mContext.getString(R.string.recyclerview_delete_item_cancel);
    }

    protected String getMessageAfterSwipeRightItem(){
        return mContext.getString(R.string.recyclerview_delete_item_msg);
    }

    protected String getMessageAfterSwipeLeftItem(){
        return mContext.getString(R.string.recyclerview_delete_item_msg);
    }

    protected String getSwipeRightText(){
        return mContext.getString(R.string.recyclerview_delete_text).toUpperCase();
    }

    protected String getSwipeLeftText(){
        return mContext.getString(R.string.recyclerview_delete_text).toUpperCase();
    }


    public void enableGesture(RecyclerView recyclerView, boolean dragMove, boolean swipeRight, boolean swipeLeft){
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
            public void onRightSwipe(final int position) {
                onSwipe(position, false);
            }

            @Override
            public void onLeftSwipe(int position) {
                onSwipe(position, true);
            }

            @Override
            public boolean isMovementAllowed(int position) {
                if(multipleSelectionEnabled)
                    return !isOneItemSelected();
                return true;
            }

            @Override
            public int getColorSwipeRight() {
                return swipedRightColor;
            }

            @Override
            public int getColorSwipeLeft() {
                return swipedLeftColor;
            }

            @Override
            public int getColorTextSwipeRight() {
                return swipedRightTextColor;
            }

            @Override
            public int getColorTextSwipeLeft() {
                return swipedLeftTextColor;
            }

            @Override
            public String getTextSwipeRight() {
                return getSwipeRightText();
            }

            @Override
            public String getTextSwipeLeft() {
                return getSwipeLeftText();
            }

        }, swipeRight, swipeLeft, dragMove);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    protected void onSwipe(final int position, final boolean left){
        final T vm = viewModels.get(position);
        viewModels.remove(position);
        notifyItemRemoved(position);
        Snackbar snackbar = Snackbar.make(rootViewForSnackbar, left ? getMessageAfterSwipeLeftItem() : getMessageAfterSwipeRightItem(), Snackbar.LENGTH_LONG).setAction(getCancelButtonText(), new View.OnClickListener() {
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
                        if(left){
                            gestureEvent.onLeftSwipe(vm, position);
                        }else{
                            gestureEvent.onRightSwipe(vm, position);
                        }
                    }
                }
            }
        });
        snackbar.show();
    }

    public boolean checkFilter(T vm, String text){
        return false;
    }

    public void filterData(String filter){
        if(filter==null || filter.equals("")) {
            if(originalViewModels==null){
                swapData(viewModels);
            }else {
                swapData(originalViewModels);
            }
            return;
        }
        List<T> dataFilter = new ArrayList<>();
        List<T> datTemp = originalViewModels==null ? viewModels : originalViewModels;
        for(T vm : datTemp){
            if(checkFilter(vm, filter))
                dataFilter.add(vm);
        }
        swapData(dataFilter, true);
    }

    public void clearFilter(){
        filterData(null);
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
            if(multipleSelectionEnabled && isOneItemSelected()){
                toggleSelection(index);
            }
            else if(mClickHandler!=null) {
                int oldSelected = selectedPosition;
                int adapterPosition = getAdapterPosition();
                selectedPosition = adapterPosition;
                T viewModel = viewModels.get(adapterPosition);
                mClickHandler.onClick(viewModel);
                notifyItemChanged(adapterPosition);
                if(oldSelected!=-1)
                    notifyItemChanged(oldSelected);
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
        void onRightSwipe(T viewModel, int position);
        void onLeftSwipe(T viewModel, int position);
        void moved(T viewModel, int newPosition, int oldPosition);
    }

}
