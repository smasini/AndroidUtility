package it.smasini.utility.library.adapters;

/**
 * Created by Simone on 01/09/16.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);
    void onRightSwipe(int position);
    void onLeftSwipe(int position);
    boolean isMovementAllowed(int position);

    int getColorSwipeRight();
    int getColorSwipeLeft();
    int getColorTextSwipeRight();
    int getColorTextSwipeLeft();
    String getTextSwipeRight();
    String getTextSwipeLeft();

}
