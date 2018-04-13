package com.example.ruireutov.organiser;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerViewItemTouchListener implements RecyclerView.OnItemTouchListener{
    private static final int SWIPE_THRESHOLD = 30;
    private static final int SCROLL_THRESHOLD = 30;

    private static final int UNLOCKED = 0;
    private static final int SCROLL_LOCK = 1;
    private static final int SWIPE_LOCK = 2;
    private static final int LONG_CLICK_LOCK = 3;


    private boolean scrollDisabled;
    private int moveLock;
    private float startX;
    private float startY;
    private RecyclerView.ViewHolder activeItem;
    private OnTouchActionListener onTouchActionListener;
    private GestureDetectorCompat gestureDetector;

    public interface OnTouchActionListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder);
        void onItemLongClick(RecyclerView.ViewHolder viewHolder);
        void onItemSwipe(RecyclerView.ViewHolder viewHolder, float dx);
        void onItemRelease(RecyclerView.ViewHolder viewHolder);
    }

    public RecyclerViewItemTouchListener(Context context, final OnTouchActionListener onTouchActionListener) {
        this.scrollDisabled = false;
        this.moveLock = UNLOCKED;
        this.onTouchActionListener = onTouchActionListener;
        this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d("GestureDetector", "onDown is called");
                startX = e.getRawX();
                startY = e.getRawY();
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {}

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d("GestureDetector", "onSingleTapUp is called");
                onTouchActionListener.onItemClick(activeItem);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("GestureDetector", "onScroll is called distanceY: " + distanceY + " distanceX: " + distanceX);
                float deltaX = e2.getRawX() - startX;
                if(moveLock == UNLOCKED) {
                    if(Math.abs(startY - e2.getRawY())  > SCROLL_THRESHOLD) {
                        moveLock = SCROLL_LOCK;
                    } else if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                        moveLock = SWIPE_LOCK;
                    }
                }

                if(moveLock == SWIPE_LOCK) {
                    Log.d("GestureDetector", "SWIPE_LOCK handled");
                    onTouchActionListener.onItemSwipe(activeItem, deltaX);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d("GestureDetector", "onLongPress is called");
                moveLock = LONG_CLICK_LOCK;
                onTouchActionListener.onItemLongClick(activeItem);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("GestureDetector", "onFling is called");
                return false;
            }
        });
        this.gestureDetector.setIsLongpressEnabled(true);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d("ItemTouchListener", "onInterceptTouchEvent received " + e.getAction());
        if(!this.scrollDisabled && this.moveLock == SCROLL_LOCK) return false; //no action is needed, as recycler view scroll is in progress

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ItemTouchListener", "ACTION_DOWN event");
                if (this.activeItem != null) {
                    return false;
                }
                View activeView = rv.findChildViewUnder(e.getX(), e.getY());
                if(activeView == null) return false; //case if item is not found
                this.activeItem = rv.findViewHolderForAdapterPosition(rv.getChildAdapterPosition(activeView));
                return this.gestureDetector.onTouchEvent(e);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.gestureDetector.onTouchEvent(e);
                this.activeItem = null;
                this.moveLock = UNLOCKED;
                return false;
            default:
                return (this.moveLock == LONG_CLICK_LOCK) || this.gestureDetector.onTouchEvent(e);
        }
//        boolean a = (this.moveLock == LONG_CLICK_LOCK) || this.gestureDetector.onTouchEvent(e);
//        Log.d("ItemTouchListener", "onInterceptTouchEvent returns  " + a);
//        return a;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d("ItemTouchListener", "onTouchEvent received " + e.getAction());
        this.gestureDetector.onTouchEvent(e);
        switch ( e.getAction() ) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("ItemTouchListener", "onTouchEvent ACTION_MOVE||ACTION_CANCEL event");
                this.onTouchActionListener.onItemRelease(this.activeItem);
                this.activeItem = null;
                this.moveLock = UNLOCKED;
                break;
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d("ItemTouchListener", "onRequestDisallowInterceptTouchEvent is called with disallowIntercept val: " + disallowIntercept);
        this.scrollDisabled = disallowIntercept;
    }
}
