package com.example.myapplication.swipeGesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.myapplication.swipeGesture.SwipeActions;

public class SwipeGestureDetector implements GestureDetector.OnGestureListener {

    private final int MIN_X_SWIPE_DISTANCE = 180;
    private final int MIN_Y_SWIPE_DISTANCE = 180;

    private SwipeActions swipeActions;


    public SwipeGestureDetector(SwipeActions actions) {
        swipeActions = actions;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //TypeCast the difference of co-ordinates to int and store in another variable

        int distanceSwipedInY = (int) (e1.getY() - e2.getY());

        // Make Check For Horizontal Swipe
        if (Math.abs(distanceSwipedInY) > MIN_Y_SWIPE_DISTANCE) {
            // Now Check Which Side Swipe Happened
            if (distanceSwipedInY < 0) {
                swipeActions.onSwipeDown();
            }
        }
        return false;
    }
}
