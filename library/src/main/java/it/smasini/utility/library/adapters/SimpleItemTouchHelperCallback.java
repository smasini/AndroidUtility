package it.smasini.utility.library.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Simone on 01/09/16.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private boolean swipeEnabled, dragEnabled;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, boolean swipeEnabled, boolean dragEnabled) {
        mAdapter = adapter;
        this.swipeEnabled = swipeEnabled;
        this.dragEnabled = dragEnabled;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return dragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return swipeEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = dragEnabled ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
        int swipeFlags = swipeEnabled ? ItemTouchHelper.START | ItemTouchHelper.END : 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(!dragEnabled)
            return false;
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

}