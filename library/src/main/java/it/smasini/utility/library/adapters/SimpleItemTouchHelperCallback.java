package it.smasini.utility.library.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
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
    private boolean swipeRightEnabled, swipeLeftEnabled, dragEnabled;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, boolean swipeRightEnabled, boolean swipeLeftEnabled, boolean dragEnabled) {
        mAdapter = adapter;
        this.swipeRightEnabled = swipeRightEnabled;
        this.swipeLeftEnabled = swipeLeftEnabled;
        this.dragEnabled = dragEnabled;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return dragEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if(!mAdapter.isMovementAllowed(viewHolder.getAdapterPosition()))
            return 0;
        int dragFlags = dragEnabled ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
        int swipeFlags = 0;
        if(swipeRightEnabled){
            swipeFlags = ItemTouchHelper.START;
        }
        if(swipeLeftEnabled){
            swipeFlags = swipeFlags == 0 ? ItemTouchHelper.END : swipeFlags | ItemTouchHelper.END;
        }
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
        if(direction == ItemTouchHelper.START){
            mAdapter.onRightSwipe(pos);
        }else{
            mAdapter.onLeftSwipe(pos);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        // not sure why, but this method get's called for viewholder that are already swiped away
        if (viewHolder.getAdapterPosition() == -1) {
            return;
        }

        Paint p = new Paint();
        Paint p2 = new Paint();

        p2.setTextSize(25);
        p2.setTypeface(Typeface.DEFAULT_BOLD);
        int marginText = 30;

        float height = (float) itemView.getBottom() - (float) itemView.getTop();
        float textSize = p2.getTextSize();
        int yPos = itemView.getTop() + (int)(height/2) + (int)(textSize/2);

        if (dX > 0) {
            p.setColor(mAdapter.getColorSwipeLeft());
            p2.setColor(mAdapter.getColorTextSwipeLeft());
            c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);
            int xPos = (int)dX - marginText - (int)p2.measureText(mAdapter.getTextSwipeLeft());
            c.drawText(mAdapter.getTextSwipeLeft(), xPos, yPos, p2);
        } else {
            p.setColor(mAdapter.getColorSwipeRight());
            p2.setColor(mAdapter.getColorTextSwipeRight());
            c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), p);
            int xPos = (int)(itemView.getRight()+ dX) + marginText;
            c.drawText(mAdapter.getTextSwipeRight(), xPos, yPos, p2);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}