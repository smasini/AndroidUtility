package it.smasini.utility.library.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

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
    final public int VIEW_TYPE_LOAD = 10000;

    protected Interpolator mInterpolator = new LinearInterpolator();
    final protected Context mContext;
    private View mEmptyView;
    protected View rootViewForSnackbar;
    final private int layoutId;

    protected int loadingTypeLayoutId = R.layout.base_adapter_loading_item;

    protected List<T> viewModels = new ArrayList<>();
    private List<T> originalViewModels;

    private boolean multipleSelectionEnabled, infiniteScrollEnable;
    protected boolean loadingMoreRecords = false;
    protected boolean animationScrollEnabled = false;
    protected boolean isOnItemSelection = false;
    protected boolean isOnBind = false;


    private OnClickHandler<T> mClickHandler;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    protected OnSwapData<T> onSwapData;
    private OnMultipleSelectionEvent<T> multipleSelectionEvent;
    private OnGestureEvent<T> gestureEvent;
    private InfinteScrollListener infinteScrollListener;

    private SparseBooleanArray selectedItems;
    protected int highlightedColor, defaultColor, selectedColor, swipedRightColor, swipedLeftColor, swipedRightTextColor, swipedLeftTextColor;
    private int selectedPosition = -1, currentOldSelectedPosition = -1;
    protected int lastAnimatedPosition = -1;

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
        if(infiniteScrollEnable && position == getItemCount()-1) {
            return VIEW_TYPE_LOAD;
        }
        T model = viewModels.get(position);
        return getItemViewType(model);
    }

    public int getItemViewType(T model){
        return VIEW_TYPE_DEFAULT;
    }

    public int getLayoutForType(int viewType){
        if(viewType == VIEW_TYPE_LOAD)
            return loadingTypeLayoutId;
        return layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutForType(viewType), viewGroup, false);
            view.setFocusable(true);
            if(viewType == VIEW_TYPE_LOAD){
                return getViewHolderForLoader(view);
            }
            return getViewHolder(view);
        }else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(BaseAdapter<T>.ViewHolder holder, int position) {
        holder.setIndex(position);
        if(getItemViewType(position) != VIEW_TYPE_LOAD) {
            T viewModel = viewModels.get(position);
            isOnBind = true;
            onBindCustomViewHolder(holder, position, viewModel);
            if (multipleSelectionEnabled && isOneItemSelected()) {
                boolean isSelected = isPositionSelected(position);
                if (isSelected)
                    setSelectedStyle(holder);
                else {
                    setDeselectedStyle(holder, position);
                }
                bindElementSelected(holder, viewModel, position, isSelected);
            } else {
                setDeselectedStyle(holder, position);
                animateViewHolderAtPosition(holder, position);
            }
            isOnBind = false;
        }else{
            onBindCustomViewHolderForLoader(holder, position);
            animateViewHolderAtPosition(holder, position);
        }
    }

    protected void animateViewHolderAtPosition(final ViewHolder viewHolder, final int position){
        boolean selectionShouldHideAnimation = isOnItemSelection && (position == currentOldSelectedPosition || position == selectedPosition);
        if(animationScrollEnabled && !selectionShouldHideAnimation && !loadingMoreRecords) {
            if(position > lastAnimatedPosition) {
                for (Animator anim : getAnimatorsScrollDown(viewHolder.itemView)) {
                    anim.setDuration(400).start();
                    anim.setInterpolator(mInterpolator);
                }
            }else{
                for (Animator anim : getAnimatorsScrollUp(viewHolder.itemView)) {
                    anim.setDuration(400).start();
                    anim.setInterpolator(mInterpolator);
                }
            }
            /*
            Animation animation = AnimationUtils.loadAnimation(mContext, getAnimationResource(position));
            viewHolder.itemView.startAnimation(animation);
            */
            lastAnimatedPosition = position;
        }else{
            viewHolder.clearAnimation();
            //viewHolder.itemView.clearAnimation();
        }
        checkIfIsOnSelectionEnded(position);
    }

    protected Animator[] getAnimatorsScrollDown(View view){
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
        };
    }

    protected Animator[] getAnimatorsScrollUp(View view){
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight(), 0)
        };
    }

    protected int getAnimationResource(int position){
        if(lastAnimatedPosition < 0){
            return R.anim.up_from_bottom;
        }
        if(position >= lastAnimatedPosition)
            return R.anim.up_from_bottom;
        return R.anim.down_from_top;
    }

    public void pauseAnimation(){
        animationScrollEnabled = false;
    }

    public void startAnimation(){
        animationScrollEnabled = true;
    }

    public void resetAnimation(){
        lastAnimatedPosition = -1;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(animationScrollEnabled) {
            holder.itemView.clearAnimation();
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

    public ViewHolder getViewHolderForLoader(View view){
        return new ViewHolder(view);
    }
    protected void onBindCustomViewHolderForLoader(ViewHolder holder, int position){

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

    public void clearSelectedPosition(){
        int pos = selectedPosition;
        selectedPosition = -1;
        notifyItemChanged(pos);
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
        if(infiniteScrollEnable && loadingMoreRecords){
            return;
        }
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
            //inizio della multiselezione
            if(multipleSelectionEvent!=null){
                multipleSelectionEvent.onStartMultipleSelection();
            }
            endInfiniteScroll();
        }else if(started && !isOneItemSelected()){
            //fine della multiselezione
            if(multipleSelectionEvent!=null){
                multipleSelectionEvent.onStopMultipleSelection();
            }
            restartInfiniteScroll(false);
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
        if(mEmptyView!=null) {
            if (getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            }else{
                mEmptyView.setVisibility(View.GONE);
            }
        }
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

    public int getItemCount(boolean excludeLoading) {
        int count = getItemCount();
        if(excludeLoading && infiniteScrollEnable){
            count--;
        }
        return count;
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
        if(infiniteScrollEnable && viewModels.size()> 0){
            viewModels.add(null);
        }
        notifyDataSetChanged();
        if(mEmptyView!=null)
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if(onSwapData!=null)
            onSwapData.onSwap(newList);
        loadingMoreRecords = false;
    }

    public void addData(List<T> newList){
        if(viewModels==null){
            viewModels = newList;
        }else {
            if(infiniteScrollEnable && loadingMoreRecords){
                viewModels.remove(viewModels.size()-1);
                lastAnimatedPosition = viewModels.size()-1;
            }
            viewModels.addAll(newList);
        }
        if(mEmptyView!=null)
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if(onSwapData!=null)
            onSwapData.onSwap(newList);
        if(infiniteScrollEnable && viewModels.size()> 0){
            viewModels.add(null);
        }
        notifyDataSetChanged();
        loadingMoreRecords = false;
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
                if(infiniteScrollEnable && loadingMoreRecords)
                    return false;
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
            if(vm!=null) {
                if (checkFilter(vm, filter))
                    dataFilter.add(vm);
            }
        }
        swapData(dataFilter, true);
    }

    public void clearFilter(){
        filterData(null);
    }

    public void setHighlightedColor(int highlightedColor) {
        this.highlightedColor = highlightedColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSwipedRightColor(int swipedRightColor) {
        this.swipedRightColor = swipedRightColor;
    }

    public void setSwipedLeftColor(int swipedLeftColor) {
        this.swipedLeftColor = swipedLeftColor;
    }

    public void setSwipedRightTextColor(int swipedRightTextColor) {
        this.swipedRightTextColor = swipedRightTextColor;
    }

    public void setSwipedLeftTextColor(int swipedLeftTextColor) {
        this.swipedLeftTextColor = swipedLeftTextColor;
    }

    public void endInfiniteScroll() {
        this.infiniteScrollEnable = false;
        if(viewModels.size() > 0) {
            if (viewModels.get(viewModels.size() - 1) == null) {
                viewModels.remove(viewModels.size() - 1);
            }
        }
    }
    public void restartInfiniteScroll(boolean resetPage) {
        this.infiniteScrollEnable = true;
        if(viewModels.size() > 0) {
            if (viewModels.get(viewModels.size() - 1) != null) {
                viewModels.add(null);
                notifyDataSetChanged();
            }
        }
        if(resetPage)
            resetInfiniteScroll();
    }

    public void resetInfiniteScroll(){
        if(endlessRecyclerViewScrollListener!=null)
            this.endlessRecyclerViewScrollListener.reset();
    }

    public void enableInfiniteScroll(RecyclerView recyclerView, InfinteScrollListener scrollListener){
        this.infiniteScrollEnable = true;
        this.infinteScrollListener = scrollListener;
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(infiniteScrollEnable && !loadingMoreRecords && infinteScrollListener!=null){
                    loadingMoreRecords = true;
                    infinteScrollListener.onLoadMore(page, totalItemsCount);
                }
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    public void selectItem(int adapterPosition){
        isOnItemSelection = true;
        currentOldSelectedPosition = selectedPosition;
        selectedPosition = adapterPosition;
        T viewModel = viewModels.get(adapterPosition);
        if(mClickHandler!=null)
            mClickHandler.onClick(viewModel);
        notifyItemChanged(adapterPosition);
        if(currentOldSelectedPosition!=-1)
            notifyItemChanged(currentOldSelectedPosition);
    }

    private boolean[] selection = new boolean[2];
    private void checkIfIsOnSelectionEnded(int position){
        if(isOnItemSelection){
            if(selectedPosition == position){
                selection[0] = true;
            }
            if(currentOldSelectedPosition == position){
                currentOldSelectedPosition = -1;
                selection[1] = true;
            }
            boolean done = true;
            for (boolean b : selection){
                done &= b;
            }
            if(done){
                isOnItemSelection = false;
                selection[0] = false;
                selection[1] = false;
            }
        }
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
            else {
                selectItem(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            toggleSelection(index);
            return false;
        }

        public void clearAnimation() {
            View v = itemView;
            ViewCompat.setAlpha(v, 1);
            ViewCompat.setScaleY(v, 1);
            ViewCompat.setScaleX(v, 1);
            ViewCompat.setTranslationY(v, 0);
            ViewCompat.setTranslationX(v, 0);
            ViewCompat.setRotation(v, 0);
            ViewCompat.setRotationY(v, 0);
            ViewCompat.setRotationX(v, 0);
            ViewCompat.setPivotY(v, v.getMeasuredHeight() / 2);
            ViewCompat.setPivotX(v, v.getMeasuredWidth() / 2);
            ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
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

    public interface InfinteScrollListener{
        void onLoadMore(int page, int totalItemsCount);
    }

}
