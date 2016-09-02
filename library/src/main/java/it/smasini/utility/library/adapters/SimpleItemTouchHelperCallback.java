package it.smasini.utility.library.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import it.smasini.utility.library.R;

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
        if(!mAdapter.isMovementAllowed(viewHolder.getAdapterPosition()))
            return 0;
        int dragFlags = dragEnabled ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
        int swipeFlags = swipeEnabled ? ItemTouchHelper.START /*| ItemTouchHelper.END*/ : 0;
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
        int pos = viewHolder.getAdapterPosition();
        mAdapter.onItemDismiss(pos);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        // not sure why, but this method get's called for viewholder that are already swiped away
        if (viewHolder.getAdapterPosition() == -1) {
            // not interested in those
            return;
        }


        Paint p = new Paint();

        if (dX > 0) {
            //per ora mai chiamato
            p.setColor(mAdapter.getSwipedColor());

            // Draw Rect with varying right side, equal to displacement dX
            c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);
        } else {
            // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
            p.setColor(mAdapter.getSwipedColor());
            c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), p);
            p.setColor(Color.WHITE);
            p.setTextSize(20);
            //p.setTextAlign(Paint.Align.LEFT);
            int xPos = (int)dX;
            if(xPos<0)
                xPos = xPos*-1;
            int yPos = (int) ((c.getHeight() / 2) - ((p.descent() + p.ascent()) / 2));
            c.drawText(mAdapter.getSwipeText(), xPos, yPos, p);
        }

        //c.drawText("Elimina", itemView.getRight() + (int) dX, itemView.getTop(), );
        /*
        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);*/



        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }
}